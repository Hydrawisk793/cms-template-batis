package kaphein.template.cmstemplatebatis.persistence;

import java.util.Objects;

/**
 *  리스트에 대한 페이지네이션 정보
 */
public class Pagination
{
    public Pagination(Pagination src)
    {
        this.itemTotalCount = src.itemTotalCount;
        this.itemCountPerPage = src.itemCountPerPage;
        this.currentPageIndex = src.currentPageIndex;
        this.displayedPageCount = src.displayedPageCount;
        this.pageTotalCount = src.pageTotalCount;
        this.pageIndexStart = src.pageIndexStart;
        this.pageIndexEnd = src.pageIndexEnd;
    }

    public Pagination(long itemTotalCount, long itemCountPerPage, long currentPageIndex, long displayedPageCount)
    {
        if(itemTotalCount < 0)
        {
            throw new IllegalArgumentException("'itemTotalCount' cannot be negative");
        }

        if(itemCountPerPage < 1)
        {
            throw new IllegalArgumentException("'itemCountPerPage' must be greater than or equal to 1");
        }

        if(currentPageIndex < 0)
        {
            throw new IllegalArgumentException("'currentPageIndex' cannot be negative");
        }

        if(displayedPageCount < 0)
        {
            throw new IllegalArgumentException("'displayedPageCount' cannot be negative");
        }

        this.itemTotalCount = itemTotalCount;
        this.itemCountPerPage = itemCountPerPage;
        this.currentPageIndex = currentPageIndex;
        this.displayedPageCount = displayedPageCount;

        final long pageTotalCount = (itemTotalCount / itemCountPerPage) + (itemTotalCount % itemCountPerPage > 0 ? 1 : 0);
        displayedPageCount = Math.min(Math.max(displayedPageCount, 1), pageTotalCount);
        final long pageIndexStart = (displayedPageCount > 0 ? (currentPageIndex / displayedPageCount) * displayedPageCount : 0);
        final long pageIndexEnd = (displayedPageCount > 0 ? pageIndexStart + displayedPageCount - 1 : 0);

        this.pageTotalCount = pageTotalCount;
        this.pageIndexStart = pageIndexStart;
        this.pageIndexEnd = pageIndexEnd;
    }

    public long getItemTotalCount()
    {
        return itemTotalCount;
    }

    public long getItemCountPerPage()
    {
        return itemCountPerPage;
    }

    public long getCurrentPageIndex()
    {
        return currentPageIndex;
    }

    public long getDisplayedPageCount()
    {
        return displayedPageCount;
    }

    public long getPageTotalCount()
    {
        return pageTotalCount;
    }

    public long getPageIndexStart()
    {
        return pageIndexStart;
    }

    public long getPageIndexEnd()
    {
        return pageIndexEnd;
    }

    @Override
    public boolean equals(Object o)
    {
        boolean result = this == o;

        if(!result && (o instanceof Pagination))
        {
            final Pagination other = (Pagination)o;

            result = getItemTotalCount() == other.getItemTotalCount()
                && getItemCountPerPage() == other.getItemCountPerPage()
                && getCurrentPageIndex() == other.getCurrentPageIndex()
                && getDisplayedPageCount() == other.getDisplayedPageCount()
                && getPageTotalCount() == other.getPageTotalCount()
                && getPageIndexStart() == other.getPageIndexStart()
                && getPageIndexEnd() == other.getPageIndexEnd();
        }

        return result;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(
            getItemTotalCount(),
            getItemCountPerPage(),
            getCurrentPageIndex(),
            getDisplayedPageCount(),
            getPageTotalCount(),
            getPageIndexStart(),
            getPageIndexEnd()
        );
    }

    /**
     *  총 아이템 개수
     */
    private final long itemTotalCount;

    /**
     * 페이지 별 아이템 개수
     */
    private final long itemCountPerPage;

    /**
     *  현재 조회중인 페이지의 인덱스
     */
    private final long currentPageIndex;

    /**
     *  UI에 표시되는 페이지 인덱스의 개수
     */
    private final long displayedPageCount;

    /**
     *  총 페이지 개수
     */
    private final long pageTotalCount;

    /**
     *  시작 페이지의 인덱스
     */
    private final long pageIndexStart;

    /**
     *  마지막 페이지의 인덱스
     */
    private final long pageIndexEnd;
}
