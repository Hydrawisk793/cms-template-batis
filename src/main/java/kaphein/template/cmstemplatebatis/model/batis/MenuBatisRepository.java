package kaphein.template.cmstemplatebatis.model.batis;

import kaphein.template.cmstemplatebatis.persistence.batis.AbstractBatisRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import kaphein.template.cmstemplatebatis.model.Menu;
import kaphein.template.cmstemplatebatis.model.MenuNode;
import kaphein.template.cmstemplatebatis.model.MenuRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MenuBatisRepository extends AbstractBatisRepository<Menu>
    implements MenuRepository
{
    public MenuBatisRepository(SqlSessionTemplate sqlSessionTemplate)
    {
        super(sqlSessionTemplate, Menu.class, Menu::new);
    }

    @Override
    public void upsert(
        Collection<Map<String, Object>> insertParams,
        Collection<Map<String, Object>> updateParams,
        Long adminUserUid,
        TransactionStatus txStatus
    )
    {
        final List<Map<String, Object>> finalUpdateParams = updateParams
            .stream()
            .map((param) ->
            {
                final Map<String, Object> finalUpdateParam = new HashMap<>(param);
                finalUpdateParam.replace("typeDiscriminator", 1);
                finalUpdateParam.replace("uptUserUid", adminUserUid);

                return finalUpdateParam;
            })
            .collect(Collectors.toCollection(LinkedList::new));

        final Map<Object, Long> issuedUidMap = new HashMap<>();
        final List<Pair<Map<String, Object>, Map<String, Object>>> insertedRowPairs = new LinkedList<>();
        insertParams.forEach((param) ->
        {
            final Map<String, Object> finalInsertParam = new HashMap<>(param);
            finalInsertParam.remove("menuUid");
            finalInsertParam.remove("parentMenuUid");
            finalInsertParam.replace("typeDiscriminator", 1);
            finalInsertParam.replace("regUserUid", adminUserUid);

            final Map<String, Object> insertedRow = insert(finalInsertParam, txStatus);
            insertedRowPairs.add(new ImmutablePair<>(param, insertedRow));
        });
        txStatus.flush();
        insertedRowPairs.forEach((insertedRowPair) ->
        {
            final Map<String, Object> param = insertedRowPair.getLeft();
            final Map<String, Object> insertedRow = insertedRowPair.getRight();

            final Long issuedUid = Long.valueOf(insertedRow.get("menuUid").toString());
            issuedUidMap.put(param.get("menuUid"), issuedUid);

            final Map<String, Object> insertedRowToUpdate = new HashMap<>(param);
            insertedRowToUpdate.replace("menuUid", issuedUid);
            finalUpdateParams.add(insertedRowToUpdate);
        });

        finalUpdateParams
            .stream()
            .map((param) ->
            {
                final Object menuUid = param.get("menuUid");
                if(issuedUidMap.containsKey(menuUid))
                {
                    param.replace("menuUid", issuedUidMap.get(menuUid));
                }

                final Object parentMenuUid = param.get("parentMenuUid");
                if(issuedUidMap.containsKey(parentMenuUid))
                {
                    param.replace("parentMenuUid", issuedUidMap.get(parentMenuUid));
                }

                return param;
            })
            .forEach((param) -> update(param, txStatus));
    }

    @Override
    public void delete(Long menuUid, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("menuUid", menuUid);

        getMapper(MenuMapper.class).delete(param);
    }

    @Override
    public List<Menu> findByPrimaryKeyIn(Collection<Object> primaryKeys, TransactionStatus txStatus)
    {
        return findByMenuUidIn(
            primaryKeys
                .stream()
                .map(Long.class::cast)
                .collect(Collectors.toList()),
            txStatus
        );
    }

    @Override
    public List<Menu> findByMenuUidIn(Collection<Long> values, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", values);

        return decorateRows(getMapper(MenuMapper.class).findByMenuUidIn(param));
    }

    @Override
    public List<MenuNode> findSubtreeNodes(TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();

        return decorateRows(getMapper(MenuMapper.class).findSubtreeNodes(param), MenuNode::new);
    }

    @Override
    public List<MenuNode> findSubtreeNodesOf(Long menuUid, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("menuUid", menuUid);

        return decorateRows(getMapper(MenuMapper.class).findSubtreeNodes(param), MenuNode::new);
    }

    @Override
    public List<MenuNode> findSubtreeNodesByMenuNamePathPrefix(String menuNamePathPrefix, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("menuNamePathPrefix", menuNamePathPrefix);

        return decorateRows(getMapper(MenuMapper.class).findSubtreeNodes(param), MenuNode::new);
    }

    @Override
    protected void executeInsert(Map<String, Object> insertParam, TransactionStatus txStatus)
    {
        getMapper(MenuMapper.class).insert(insertParam);
    }

    @Override
    protected void executeUpdate(Map<String, Object> updateParam, TransactionStatus txStatus)
    {
        getMapper(MenuMapper.class).update(updateParam);
    }
}
