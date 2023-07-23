package kaphein.template.cmstemplatebatis.auth.batis;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RememberMeTokenMapper
{
    int insert(Map<String, Object> paramInOut);

    int update(Map<String, Object> param);

    int delete(Map<String, Object> param);

    Map<String, Object> findOneBySeriesId(Map<String, Object> param);
}
