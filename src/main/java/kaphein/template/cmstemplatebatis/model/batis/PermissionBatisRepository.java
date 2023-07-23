package kaphein.template.cmstemplatebatis.model.batis;

import kaphein.template.cmstemplatebatis.persistence.PaginationParameter;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;
import kaphein.template.cmstemplatebatis.persistence.batis.AbstractBatisRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import kaphein.template.cmstemplatebatis.model.Permission;
import kaphein.template.cmstemplatebatis.model.PermissionRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class PermissionBatisRepository extends AbstractBatisRepository<Permission>
    implements PermissionRepository
{
    public PermissionBatisRepository(SqlSessionTemplate sqlSessionTemplate)
    {
        super(sqlSessionTemplate, Permission.class, Permission::new);
    }

    @Override
    public void delete(Long permissionUid, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("permissionUid", permissionUid);

        getMapper(PermissionMapper.class).delete(param);
    }

    @Override
    public List<Permission> findByPrimaryKeyIn(Collection<Object> primaryKeys, TransactionStatus txStatus)
    {
        return findByPermissionUidIn(
            primaryKeys
                .stream()
                .map(Long.class::cast)
                .collect(Collectors.toList()),
            txStatus
        );
    }

    @Override
    public List<Permission> findAll(TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();

        return decorateRows(getMapper(PermissionMapper.class).findAll(param));
    }

    @Override
    public PaginationResult<Permission> findAll(PaginationParameter paginationParameter, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        if(null != paginationParameter)
        {
            param.put("pagination", paginationParameter);
        }

        return (PaginationResult<Permission>)decorateRows(getMapper(PermissionMapper.class).findAll(param));
    }

    @Override
    public Permission findOneByPermissionUid(Long permissionUid, TransactionStatus txStatus)
    {
        return findByPermissionUidIn(Collections.singletonList(permissionUid), txStatus)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Permission> findByPermissionUidIn(Collection<Long> values, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", values);

        return decorateRows(getMapper(PermissionMapper.class).findByPermissionUidIn(param));
    }

    @Override
    public Permission findOneByPermissionName(String permissionName, TransactionStatus txStatus)
    {
        return findByPermissionNameIn(Collections.singletonList(permissionName), txStatus)
            .stream()
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Permission> findByPermissionNameIn(Collection<String> values, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", values);

        return decorateRows(getMapper(PermissionMapper.class).findByPermissionNameIn(param));
    }

    @Override
    protected void executeInsert(Map<String, Object> insertParam, TransactionStatus txStatus)
    {
        getMapper(PermissionMapper.class).insert(insertParam);
    }

    @Override
    protected void executeUpdate(Map<String, Object> updateParam, TransactionStatus txStatus)
    {
        getMapper(PermissionMapper.class).update(updateParam);
    }
}
