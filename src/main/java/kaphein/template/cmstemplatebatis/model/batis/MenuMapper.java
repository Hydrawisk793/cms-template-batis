package kaphein.template.cmstemplatebatis.model.batis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper
{
    int insert(Map<String, Object> paramInOut);

    int update(Map<String, Object> param);

    int delete(Map<String, Object> param);

    List<Map<String, Object>> findByMenuUidIn(Map<String, Object> param);

    List<Map<String, Object>> findSubtreeNodes(Map<String, Object> param);
}
