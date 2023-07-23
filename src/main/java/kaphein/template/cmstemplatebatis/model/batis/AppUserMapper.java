package kaphein.template.cmstemplatebatis.model.batis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppUserMapper
{
    int insert(Map<String, Object> paramInOut);

    int update(Map<String, Object> param);

    int delete(Map<String, Object> param);

    int addPermission(Map<String, Object> param);

    int removePermission(Map<String, Object> param);

    List<Map<String, Object>> findAll(Map<String, Object> param);

    List<Map<String, Object>> findByUserUidIn(Map<String, Object> param);

    Map<String, Object> findOneByLoginName(Map<String, Object> param);
}
