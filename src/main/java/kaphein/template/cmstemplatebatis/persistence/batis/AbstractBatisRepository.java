package kaphein.template.cmstemplatebatis.persistence.batis;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.TransactionStatus;

import kaphein.template.cmstemplatebatis.persistence.AbstractEntity;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;

/**
 * MyBatis 기반 레포지터리 클래스의 기본 클래스.
 * insert, update, upsert 및 자주 사용되는 SQL 쿼리에 대한 기본적인 프레임워크를 제공한다.
 *
 * @param <E> 엔티티 클래스 타입
 */
public abstract class AbstractBatisRepository<E extends AbstractEntity>
{
    /**
     * 새로운 레포지터리를 생성한다.
     * 이 클래스를 상속하는 모든 자손 클래스들의 생성자는 이 생성자를 기반으로 작성되어야한다.
     *
     * @param sqlSessionTemplate MyBatis `SqlSessionTemplate` 인스턴스.
     * @param entityClass        엔티티 클래스.
     * @param entitySupplier     엔티티 클래스의 인스턴스를 만드는 함수.
     */
    protected AbstractBatisRepository(
        SqlSessionTemplate sqlSessionTemplate, Class<E> entityClass, Supplier<E> entitySupplier
    )
    {
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.entityClass = entityClass;
        this.entitySupplier = entitySupplier;
    }

    /**
     * 새로운 엔티티를 데이터베이스에 insert 한다.
     * <p>
     * 이 메소드를 사용하려면 {@link #executeInsert} 메소드가 반드시 구현되어야 한다.
     *
     * @param param
     * @param txStatus
     * @return
     * @see #executeInsert
     */
    public final Map<String, Object> insert(Map<String, Object> param, TransactionStatus txStatus)
    {
        return insert(Collections.singletonList(param), txStatus).stream().findFirst().orElse(null);
    }

    /**
     * 새로운 엔티티 다수를 데이터베이스에 insert 한다.
     * <p>
     * 이 메소드를 사용하려면 {@link #executeInsert} 메소드가 반드시 구현되어야 한다.
     *
     * @param params
     * @param txStatus
     * @return
     * @see #executeInsert
     */
    public List<Map<String, Object>> insert(Collection<Map<String, Object>> params, TransactionStatus txStatus)
    {
        return createRowsToInsert(params, txStatus).stream().map((param) ->
        {
            executeInsert(param, txStatus);

            return param;
        }).collect(Collectors.toList());
    }

    /**
     * 엔티티에 대한 update를 수행한다.
     * 업데이토되는 row는 `param`에 지정된 기본키를 통해 지정된다.
     * <p>
     * 이 메소드를 사용하려면 {@link #executeUpdate} 메소드가 반드시 구현되어야 한다.
     *
     * @param param
     * @param txStatus
     * @see #executeUpdate
     */
    public final void update(Map<String, Object> param, TransactionStatus txStatus)
    {
        update(Collections.singletonList(param), txStatus);
    }

    /**
     * 엔티티에 다수 대한 update를 수행한다.
     * 업데이토되는 row는 `params`에 지정된 기본키를 통해 지정된다.
     * <p>
     * 이 메소드를 사용하려면 {@link #executeUpdate} 메소드가 반드시 구현되어야 한다.
     *
     * @param params
     * @param txStatus
     * @see #executeUpdate
     */
    public void update(Collection<Map<String, Object>> params, TransactionStatus txStatus)
    {
        createRowsToUpdate(params, txStatus).forEach((param) -> executeUpdate(param, txStatus));
    }

    /**
     * 기존 엔티티를 update하거나 없는 경우 새로 insert 한다.
     * 업데이토되는 row는 `upsertParam`에 지정된 기본키를 통해 지정된다.
     * <p>
     * 이 메소드를 사용하려면 {@link #executeInsert} 메소드, {@link #executeUpdate} 메소드, {@link #findByPrimaryKeyIn} 메소드가 반드시 구현되어야 한다.
     *
     * @param upsertParam
     * @param txStatus
     * @see #executeInsert
     * @see #executeUpdate
     * @see #findByPrimaryKeyIn
     */
    public final void upsert(Map<String, Object> upsertParam, TransactionStatus txStatus)
    {
        upsert(Collections.singletonList(upsertParam), txStatus);
    }

    /**
     * 기존의 여러 엔티티를 update하거나 없는 경우 새로 insert 한다.
     * 업데이토되는 row는 `upsertParams`에 지정된 기본키를 통해 지정된다.
     * <p>
     * 이 메소드를 사용하려면 {@link #executeInsert} 메소드, {@link #executeUpdate} 메소드, {@link #findByPrimaryKeyIn} 메소드가 반드시 구현되어야 한다.
     *
     * @param upsertParams
     * @param txStatus
     * @see #executeInsert
     * @see #executeUpdate
     * @see #findByPrimaryKeyIn
     */
    public void upsert(Collection<Map<String, Object>> upsertParams, TransactionStatus txStatus)
    {
        if(null == upsertParams)
        {
            throw new IllegalArgumentException("'upsertParams' cannot be null");
        }

        final Map<Object, Map<String, Object>> upsertParamMap = upsertParams.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(this::getPrimaryKeyFromUpsertParam, UnaryOperator.identity()));
        final Set<Object> primaryKeys = upsertParamMap.keySet();

        final List<Map<String, Object>> insertParams = new LinkedList<>();
        final List<Map<String, Object>> updateParams = new LinkedList<>();
        final List<E> foundEntities = findByPrimaryKeyIn(primaryKeys, txStatus);
        for(E foundEntity : foundEntities)
        {
            final Object foundPrimaryKey = foundEntity.getPrimaryKey();

            if(primaryKeys.contains(foundPrimaryKey))
            {
                updateParams.add(upsertParamMap.get(foundPrimaryKey));
            }
            else
            {
                insertParams.add(upsertParamMap.get(foundPrimaryKey));
            }
        }

        if(!insertParams.isEmpty())
        {
            for(Map<String, Object> insertParam : insertParams)
            {
                executeInsert(insertParam, txStatus);
            }

            txStatus.flush();
        }

        if(!updateParams.isEmpty())
        {
            for(Map<String, Object> updateParam : updateParams)
            {
                executeUpdate(updateParam, txStatus);
            }

            txStatus.flush();
        }
    }

    protected final Class<E> getEntityClass()
    {
        return entityClass;
    }

    protected final SqlSessionTemplate getSqlSessionTemplate()
    {
        return sqlSessionTemplate;
    }

    /**
     * 현재 진행중인 데이터베이스 트랜잭션과 연결된 MyBatis mapper 인스턴스를 반환한다.
     *
     * @param type MyBatis mapper 클래스
     * @param <T>  MyBatis mapper 타입
     * @return MyBatis mapper 인스턴스
     */
    protected final <T> T getMapper(Class<T> type)
    {
        return sqlSessionTemplate.getMapper(type);
    }

    /**
     * 지정된 기본키에 해당하는 엔티티들을 찾는다.
     * <p>
     * 기본 구현은 미구현이며, 호출시 {@link UnsupportedOperationException} 예외를 던진다.
     *
     * @param primaryKeys 찾으려는 엔티티들의 기본키 리스트
     * @param txStatus    트랜잭션 상태
     * @return 검색된 엔티티 리스트
     * @throws UnsupportedOperationException 메소드가 미구현되었거나 지원되지 않는 경우
     */
    public List<E> findByPrimaryKeyIn(Collection<Object> primaryKeys, TransactionStatus txStatus)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * MyBatis mapper를 사용하여 데이터베이스에 새로운 엔티티를 insert한다.
     * <p>
     * 엔티티에 대한 insert를 수행하려면 이 메소드를 반드시 구현해야 한다.
     * <p>
     * 기본 구현은 미구현이며, 호출시 {@link UnsupportedOperationException} 예외를 던진다.
     *
     * @param insertParam
     * @param txStatus
     * @throws UnsupportedOperationException 메소드가 미구현되었거나 지원되지 않는 경우
     */
    protected void executeInsert(Map<String, Object> insertParam, TransactionStatus txStatus)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * MyBatis mapper를 사용하여 데이터베이스에 엔티티를 update한다.
     * <p>
     * 엔티티에 대한 update를 수행하려면 이 메소드를 반드시 구현해야 한다.
     * <p>
     * 기본 구현은 미구현이며, 호출시 {@link UnsupportedOperationException} 예외를 던진다.
     *
     * @param updateParam
     * @param txStatus
     * @throws UnsupportedOperationException 메소드가 미구현되었거나 지원되지 않는 경우
     */
    protected void executeUpdate(Map<String, Object> updateParam, TransactionStatus txStatus)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * MyBatis mapper를 사용하여 데이터베이스에서 엔티티를 delete한다.
     * <p>
     * 엔티티에 대한 delete를 수행하려면 이 메소드를 반드시 구현해야 한다.
     * <p>
     * 기본 구현은 미구현이며, 호출시 {@link UnsupportedOperationException} 예외를 던진다.
     *
     * @param deleteParam
     * @param txStatus
     * @throws UnsupportedOperationException 메소드가 미구현되었거나 지원되지 않는 경우
     */
    protected void executeDelete(Map<String, Object> deleteParam, TransactionStatus txStatus)
    {
        throw new UnsupportedOperationException();
    }

    protected Object getPrimaryKeyFromUpsertParam(Map<String, Object> upsertParam)
    {
        return entitySupplier.get().assignMap(upsertParam).getPrimaryKey();
    }

    protected void assertInsertParamIsValid(Map<String, Object> insertParam)
    {
        // Does nothing by default.
    }

    protected void assertUpdateParamIsValid(Map<String, Object> updateParam)
    {
        // Does nothing by default.
    }

    protected void assertForeignKeysAreValid(List<Map<String, Object>> params, TransactionStatus txStatus)
    {
        // Does nothing by default.
    }

    protected static <E extends AbstractEntity> List<E> decorateRows(
        Collection<Map<String, Object>> rows,
        Supplier<E> entitySupplier
    )
    {
        final List<E> decoratedRows = rows.stream()
            .filter(Objects::nonNull)
            .map((row) -> decorateRow(row, entitySupplier))
            .collect(Collectors.toList());

        List<E> result = decoratedRows;
        if(rows instanceof PaginationResult)
        {
            result = new PaginationResult<>(
                decoratedRows,
                ((PaginationResult<Map<String, Object>>) rows).getPagination()
            );
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    protected static <E extends AbstractEntity> E decorateRow(Map<String, Object> row, Supplier<E> entitySupplier)
    {
        return (null == row ? null : (E) entitySupplier.get().assignDatabaseTableRow(row));
    }

    protected final List<E> decorateRows(Collection<Map<String, Object>> rows)
    {
        return decorateRows(rows, entitySupplier);
    }

    protected E decorateRow(Map<String, Object> row)
    {
        return decorateRow(row, entitySupplier);
    }

    private List<Map<String, Object>> createRowsToInsert(
        Collection<Map<String, Object>> params,
        TransactionStatus txStatus
    )
    {
        final List<Map<String, Object>> validParams = params.stream()
            .map((param) -> entitySupplier.get().assignMap(param).toMap(param.keySet()))
            .map((param) ->
            {
                assertInsertParamIsValid(param);

                return param;
            })
            .collect(Collectors.toList());

        assertForeignKeysAreValid(validParams, txStatus);

        return validParams.stream()
            .map((param) -> entitySupplier.get().assignMap(param).toDatabaseTableRow(param.keySet()))
            .collect(Collectors.toList());
    }

    private List<Map<String, Object>> createRowsToUpdate(
        Collection<Map<String, Object>> params,
        TransactionStatus txStatus
    )
    {
        final List<Map<String, Object>> validParams = params.stream()
            .map((param) -> entitySupplier.get().assignMap(param).toMap(param.keySet()))
            .map((param) ->
            {
                assertUpdateParamIsValid(param);

                return param;
            })
            .collect(Collectors.toList());

        assertForeignKeysAreValid(validParams, txStatus);

        return validParams.stream()
            .map((param) -> entitySupplier.get().assignMap(param).toDatabaseTableRow(param.keySet()))
            .collect(Collectors.toList());
    }

    private final SqlSessionTemplate sqlSessionTemplate;

    private final Class<E> entityClass;

    private final Supplier<E> entitySupplier;
}
