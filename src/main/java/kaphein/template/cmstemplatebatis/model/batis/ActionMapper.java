package kaphein.template.cmstemplatebatis.model.batis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActionMapper
{
    int insert(Map<String, Object> paramInOut);

    int update(Map<String, Object> param);

    int delete(Map<String, Object> param);

    List<Map<String, Object>> findAll(Map<String, Object> param);

    List<Map<String, Object>> findByActionUidIn(Map<String, Object> param);

    Map<String, Object> findOneByActionUrl(Map<String, Object> param);
}
