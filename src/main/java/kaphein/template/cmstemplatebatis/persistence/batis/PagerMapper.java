package kaphein.template.cmstemplatebatis.persistence.batis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 페이징 쿼리에 관한 SQL 매핑을 정의한다.
 * 이 쿼리들은 직접적으로 실행되지 않으며, {@link PaginationInterceptor}에 의해 간접적으로 실행된다.
 */
@Mapper
interface PagerMapper
{
    /**
     * 페이징 쿼리를 수행한다.
     *
     * @param param
     * @return 검색된 row의 리스트
     */
    List<Map<String, Object>> findByPage(Map<String, Object> param);

    /**
     * 조건에 해당하는 모든 row의 개수를 찾는다.
     *
     * @param param
     * @return 조건에 해당되는 모든 row의 개수
     */
    Long findPageItemTotalCount(Map<String, Object> param);
}
