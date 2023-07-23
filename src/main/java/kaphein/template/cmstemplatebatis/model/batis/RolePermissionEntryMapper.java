package kaphein.template.cmstemplatebatis.model.batis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolePermissionEntryMapper
{
    int insert(Map<String, Object> paramInOut);

    int delete(Map<String, Object> param);

    List<Map<String, Object>> findByRoleNameIn(Map<String, Object> param);

    List<Map<String, Object>> findByPermissionNameIn(Map<String, Object> param);
}
