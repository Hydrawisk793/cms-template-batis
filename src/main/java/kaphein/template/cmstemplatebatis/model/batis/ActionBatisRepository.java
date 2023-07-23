package kaphein.template.cmstemplatebatis.model.batis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kaphein.template.cmstemplatebatis.persistence.PaginationParameter;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;
import kaphein.template.cmstemplatebatis.persistence.batis.AbstractBatisRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import kaphein.template.cmstemplatebatis.model.Action;
import kaphein.template.cmstemplatebatis.model.ActionRepository;

@Repository
public class ActionBatisRepository extends AbstractBatisRepository<Action>
    implements ActionRepository
{
    public ActionBatisRepository(SqlSessionTemplate sqlSessionTemplate)
    {
        super(sqlSessionTemplate, Action.class, Action::new);
    }

    @Override
    public List<Action> findByPrimaryKeyIn(Collection<Object> primaryKeys, TransactionStatus txStatus)
    {
        return findByActionUidIn(
            primaryKeys
                .stream()
                .map(Long.class::cast)
                .collect(Collectors.toList()),
            txStatus
        );
    }

    @Override
    public List<Action> findAll(TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();

        return decorateRows(getMapper(ActionMapper.class).findAll(param));
    }

    @Override
    public PaginationResult<Action> findAll(PaginationParameter paginationParameter, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        if(null != paginationParameter)
        {
            param.put("pagination", paginationParameter);
        }

        return (PaginationResult<Action>)decorateRows(getMapper(ActionMapper.class).findAll(param));
    }

    @Override
    public Action findOneByActionUid(Long actionUid, TransactionStatus txStatus)
    {
        return findByActionUidIn(Collections.singletonList(actionUid), txStatus)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Action> findByActionUidIn(Collection<Long> values, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", values);

        return decorateRows(getMapper(ActionMapper.class).findByActionUidIn(param));
    }

    @Override
    public Action findOneByActionUrl(String actionUrl, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("actionUrl", actionUrl);

        return decorateRow(getMapper(ActionMapper.class).findOneByActionUrl(param));
    }

    @Override
    protected void executeInsert(Map<String, Object> insertParam, TransactionStatus txStatus)
    {
        getMapper(ActionMapper.class).insert(insertParam);
    }

    @Override
    protected void executeUpdate(Map<String, Object> updateParam, TransactionStatus txStatus)
    {
        getMapper(ActionMapper.class).update(updateParam);
    }
}
