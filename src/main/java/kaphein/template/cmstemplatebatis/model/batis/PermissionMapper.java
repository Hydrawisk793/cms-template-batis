package kaphein.template.cmstemplatebatis.model.batis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper
{
    int insert(Map<String, Object> paramInOut);

    int update(Map<String, Object> param);

    int delete(Map<String, Object> param);

    List<Map<String, Object>> findAll(Map<String, Object> param);

    List<Map<String, Object>> findByPermissionUidIn(Map<String, Object> param);

    List<Map<String, Object>> findByPermissionNameIn(Map<String, Object> param);
}
