package kaphein.template.cmstemplatebatis.model.batis;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LoginLogMapper
{
    int insert(Map<String, Object> paramInOut);

    List<Map<String, Object>> findAll(Map<String, Object> param);
}
