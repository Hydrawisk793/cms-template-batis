package kaphein.template.cmstemplatebatis.model.batis;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import kaphein.template.cmstemplatebatis.model.SequenceRepository;

@Repository
public class SequenceBatisRepository implements SequenceRepository
{
    public SequenceBatisRepository(SqlSessionTemplate sqlSessionTemplate)
    {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public long current(String sequenceName, TransactionStatus txStatus)
    {
        return current(sequenceName, 0, txStatus);
    }

    @Override
    public long current(String sequenceName, long initValue, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("sequenceName", sequenceName);

        Long currentValue = null;

        final Map<String, Object> row = sqlSessionTemplate.getMapper(SequenceMapper.class).findOneBySequenceName(param);
        if(null == row)
        {
            currentValue = initValue;
            param.put("currentValue", currentValue);
            insert(param, txStatus);

            txStatus.flush();
        }
        else
        {
            currentValue = MapUtils.getLongValue(row, "currentValue", initValue);
        }

        return currentValue;
    }

    @Override
    public long next(String sequenceName, TransactionStatus txStatus)
    {
        return next(sequenceName, 0, txStatus);
    }

    @Override
    public long next(String sequenceName, long initValue, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("sequenceName", sequenceName);

        Long nextValue = null;

        final Map<String, Object> row = sqlSessionTemplate.getMapper(SequenceMapper.class).findOneBySequenceName(param);
        if(null == row)
        {
            nextValue = initValue + 1;
            param.put("currentValue", nextValue);
            insert(param, txStatus);
        }
        else
        {
            nextValue = MapUtils.getLongValue(row, "currentValue", initValue) + 1;
            param.put("currentValue", nextValue);
            update(param, txStatus);
        }

        txStatus.flush();

        return nextValue;
    }

    @Override
    public long reset(String sequenceName, TransactionStatus txStatus)
    {
        return reset(sequenceName, 0, txStatus);
    }

    @Override
    public long reset(String sequenceName, long initValue, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("sequenceName", sequenceName);
        param.put("currentValue", initValue);

        final Map<String, Object> row = sqlSessionTemplate.getMapper(SequenceMapper.class).findOneBySequenceName(param);
        if(null == row)
        {
            insert(param, txStatus);
        }
        else
        {
            update(param, txStatus);
        }

        txStatus.flush();

        return initValue;
    }

    private Map<String, Object> insert(Map<String, Object> param, TransactionStatus txStatus)
    {
        final Map<String, Object> insertParam = new HashMap<>(param);

        sqlSessionTemplate.getMapper(SequenceMapper.class).insert(insertParam);

        return insertParam;
    }

    private void update(Map<String, Object> param, TransactionStatus txStatus)
    {
        sqlSessionTemplate.getMapper(SequenceMapper.class).update(param);
    }

    private final SqlSessionTemplate sqlSessionTemplate;
}
