package kaphein.template.cmstemplatebatis.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.With;

/**
 *  페이지네이션 쿼리를 위한 페이지네이션 파라미터
 */
@Builder
@EqualsAndHashCode
@With
@Getter
public class PaginationParameter
{
    public PaginationParameter(PaginationParameter src)
    {
        this(
            src.itemCountPerPage,
            src.currentPageIndex,
            src.displayedPageCount,
            src.orderByTerms,
            src.rowNumberDescending,
            src.rowNumberColumnName,
            src.subQueryAlias
        );
    }

    public PaginationParameter(
        Long itemCountPerPage,
        Long currentPageIndex,
        Long displayedPageCount,
        Collection<OrderByTerm> orderByTerms
    )
    {
        this(itemCountPerPage, currentPageIndex, displayedPageCount, orderByTerms, null, null, null);
    }

    public PaginationParameter(
        Long itemCountPerPage,
        Long currentPageIndex,
        Long displayedPageCount,
        Collection<OrderByTerm> orderByTerms,
        Boolean rowNumberDescending
    )
    {
        this(itemCountPerPage, currentPageIndex, displayedPageCount, orderByTerms, rowNumberDescending, null, null);
    }

    @JsonCreator
    public PaginationParameter(
        @JsonProperty(value = "rowCountPerPage", required = true) Long itemCountPerPage,
        @JsonProperty(value = "currentPageIndex", required = true) Long currentPageIndex,
        @JsonProperty(value = "displayedPageCount", required = true) Long displayedPageCount,
        @JsonProperty(value = "orderByTerms", required = true) Collection<OrderByTerm> orderByTerms,
        Boolean rowNumberDescending,
        String rowNumberColumnName,
        String subQueryAlias
    )
    {
        if(null == itemCountPerPage)
        {
            this.itemCountPerPage = 1;
        }
        else if(itemCountPerPage < 1)
        {
            throw new IllegalArgumentException("'itemCountPerPage' cannot be less than 1");
        }
        else
        {
            this.itemCountPerPage = itemCountPerPage;
        }

        if(null == currentPageIndex)
        {
            this.currentPageIndex = 0;
        }
        else if(currentPageIndex < 0)
        {
            throw new IllegalArgumentException("'currentPageIndex' cannot be less than 0");
        }
        else
        {
            this.currentPageIndex = currentPageIndex;
        }

        if(null == displayedPageCount)
        {
            this.displayedPageCount = 1;
        }
        else if(displayedPageCount < 0)
        {
            throw new IllegalArgumentException("'displayedPageCount' cannot be less than 0");
        }
        else
        {
            this.displayedPageCount = displayedPageCount;
        }

        if(null == orderByTerms || orderByTerms.isEmpty())
        {
            throw new IllegalArgumentException("At least one order by term must be specified");
        }
        else
        {
            this.orderByTerms = Collections.unmodifiableList(new ArrayList<>(orderByTerms));
        }

        this.rowNumberDescending = null != rowNumberDescending && rowNumberDescending;

        if(StringUtils.isBlank(rowNumberColumnName))
        {
            this.rowNumberColumnName = "rowNumber";
        }
        else
        {
            this.rowNumberColumnName = rowNumberColumnName;
        }

        if(StringUtils.isBlank(subQueryAlias))
        {
            this.subQueryAlias = "subQueryAlias";
        }
        else
        {
            this.subQueryAlias = subQueryAlias;
        }
    }

    public Map<String, Object> toMap()
    {
        return toMap(HashMap::new);
    }

    public Map<String, Object> toMap(Supplier<Map<String, Object>> mapSupplier)
    {
        final Map<String, Object> map = mapSupplier.get();
        map.put("itemCountPerPage", itemCountPerPage);
        map.put("currentPageIndex", currentPageIndex);
        map.put("displayedPageCount", displayedPageCount);
        map.put("orderByTerms", orderByTerms);
        map.put("rowNumberDescending", rowNumberDescending);
        map.put("rowNumberColumnName", rowNumberColumnName);
        map.put("subQueryAlias", subQueryAlias);

        return map;
    }

    /**
     * 페이지 별 아이템 개수
     */
    private final long itemCountPerPage;

    /**
     *  조회하려는 페이지의 인덱스
     */
    private final long currentPageIndex;

    /**
     *  UI에 표시할 페이지 인덱스의 개수
     */
    private final long displayedPageCount;

    /**
     *  리스트 아이템 정렬 조건
     */
    private final List<OrderByTerm> orderByTerms;

    /**
     *  행 번호 내림차순 정렬 여부
     */
    private final boolean rowNumberDescending;

    /**
     *  조회 쿼리 결과에 추가될 행 번호 컬럼의 이름
     */
    private final String rowNumberColumnName;

    /**
     *  페이지네이션 서브 쿼리의 alias
     */
    private final String subQueryAlias;
}
