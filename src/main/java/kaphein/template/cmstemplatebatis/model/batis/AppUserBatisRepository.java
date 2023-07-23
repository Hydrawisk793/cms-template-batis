package kaphein.template.cmstemplatebatis.model.batis;

import kaphein.template.cmstemplatebatis.model.AppUser;
import kaphein.template.cmstemplatebatis.persistence.PaginationParameter;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;
import kaphein.template.cmstemplatebatis.persistence.batis.AbstractBatisRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import kaphein.template.cmstemplatebatis.model.AppUserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class AppUserBatisRepository
    extends AbstractBatisRepository<AppUser>
    implements AppUserRepository
{
    public AppUserBatisRepository(SqlSessionTemplate sqlSessionTemplate)
    {
        super(sqlSessionTemplate, AppUser.class, AppUser::new);
    }

    @Override
    public void delete(Long userUid, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("userUid", userUid);

        getMapper(AppUserMapper.class).delete(param);
    }

    @Override
    public List<AppUser> findByPrimaryKeyIn(Collection<Object> primaryKeys, TransactionStatus txStatus)
    {
        return findByUserUidIn(
            primaryKeys
                .stream()
                .map(Long.class::cast)
                .collect(Collectors.toList()),
            txStatus
        );
    }

    @Override
    public List<AppUser> findAll(TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();

        return decorateRows(getMapper(AppUserMapper.class).findAll(param));
    }

    @Override
    public PaginationResult<AppUser> findAll(PaginationParameter paginationParameter, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        if(null != paginationParameter)
        {
            param.put("pagination", paginationParameter);
        }

        return (PaginationResult<AppUser>)decorateRows(getMapper(AppUserMapper.class).findAll(param));
    }

    @Override
    public AppUser findOneByUserUid(Long value, TransactionStatus txStatus)
    {
        return findByUserUidIn(Collections.singletonList(value), txStatus)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<AppUser> findByUserUidIn(Collection<Long> values, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", values);

        return decorateRows(getMapper(AppUserMapper.class).findByUserUidIn(param));
    }

    @Override
    public AppUser findOneByLoginName(String loginName, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("loginName", loginName);

        return decorateRow(getMapper(AppUserMapper.class).findOneByLoginName(param));
    }

    @Override
    protected void executeInsert(Map<String, Object> insertParam, TransactionStatus txStatus)
    {
        getMapper(AppUserMapper.class).insert(insertParam);
    }

    @Override
    protected void executeUpdate(Map<String, Object> updateParam, TransactionStatus txStatus)
    {
        getMapper(AppUserMapper.class).update(updateParam);
    }
}
