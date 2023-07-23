package kaphein.template.cmstemplatebatis.persistence;

import java.util.Objects;

import kaphein.template.cmstemplatebatis.LetterCaseUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 데이터베이스 쿼리의 ORDER BY절 조건.
 * 불변 객체.
 */
public class OrderByTerm
{
    /**
     * 지정된 인스턴스를 deep-copy한다.
     *
     * @param src 복사하고자하는 인스턴스
     */
    public OrderByTerm(OrderByTerm src)
    {
        this(src.sortKey, src.descending);
    }

    /**
     * 새로운 인스턴스를 생성한다.
     *
     * 차순은 오름차순으로 지정된다.
     *
     * @param sortKey 엔티티 필드 이름
     */
    public OrderByTerm(String sortKey)
    {
        this(sortKey, false);
    }

    /**
     * 새로운 인스턴스를 생성한다.
     *
     * @param sortKey 엔티티 필드 이름
     * @param descending 내림차순 여부. `false`인 경우 오름차순.
     */
    @JsonCreator
    public OrderByTerm(
        @JsonProperty(value = "sortKey", required = true) String sortKey,
        @JsonProperty(value = "descending") boolean descending
    )
    {
        if(StringUtils.isBlank(sortKey))
        {
            throw new IllegalArgumentException("'sortKey' cannot be blank");
        }
        this.sortKey = sortKey;

        this.columnName = LetterCaseUtils.toSnakeCase(sortKey);

        this.descending = descending;
    }

    public String getSortKey()
    {
        return sortKey;
    }

    /**
     * 엔티티 필드 이름이 변경된 새로운 인스턴스를 생성한다.
     *
     * @param sortKey 새로운 엔티티 필드 이름
     * @return 새로운 인스턴스
     */
    public OrderByTerm withSortKey(String sortKey)
    {
        return new OrderByTerm(sortKey, this.descending);
    }

    /**
     * 엔티티 필드 이름에 해당되는 데이터베이스 컬럼 이름을 반환한다.
     *
     * @return 데이터베이스 컬럼 이름
     */
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * 내림차순 여부를 반환한다.
     * 
     * @return 내림차순 여부
     */
    public boolean isDescending()
    {
        return descending;
    }

    /**
     * 내림차순 여부가 변경된 새로운 인스턴스를 생성한다.
     * 
     * @param descending 내림차순 여부. `false`인 경우 오름차순.
     * @return 새로운 인스턴스
     */
    public OrderByTerm withDescending(boolean descending)
    {
        return new OrderByTerm(this.sortKey, descending);
    }

    @Override
    public boolean equals(Object o)
    {
        boolean result = o == this;

        if(!result && (o instanceof OrderByTerm))
        {
            final OrderByTerm other = (OrderByTerm) o;

            result = Objects.equals(sortKey, other.sortKey) && descending == other.descending;
        }

        return result;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(sortKey, descending);
    }

    /**
     * 엔티티 필드 이름
     */
    private final String sortKey;

    /**
     * 엔티티 필드에 해당하는 컬럼 이름
     */
    private final transient String columnName;

    /**
     * 내림차순 여부. `false`인 경우 오름차순
     */
    private final boolean descending;
}
