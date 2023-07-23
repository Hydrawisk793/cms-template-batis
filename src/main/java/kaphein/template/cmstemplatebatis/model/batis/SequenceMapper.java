package kaphein.template.cmstemplatebatis.model.batis;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface SequenceMapper
{
    int insert(Map<String, Object> paramInOut);

    int update(Map<String, Object> param);

    Map<String, Object> findOneBySequenceName(Map<String, Object> param);
}
