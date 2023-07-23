package kaphein.template.cmstemplatebatis.model.batis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import kaphein.template.cmstemplatebatis.persistence.batis.AbstractBatisRepository;
import org.apache.commons.collections4.MapUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;

import kaphein.template.cmstemplatebatis.model.RolePermissionEntry;
import kaphein.template.cmstemplatebatis.model.RolePermissionEntryRepository;

@Repository
public class RolePermissionEntryBatisRepository extends AbstractBatisRepository<RolePermissionEntry>
    implements RolePermissionEntryRepository
{
    public RolePermissionEntryBatisRepository(SqlSessionTemplate sqlSessionTemplate)
    {
        super(sqlSessionTemplate, RolePermissionEntry.class, RolePermissionEntry::new);
    }

    @Override
    public List<Map<String, Object>> reset(String roleName, Collection<String> permissionNames,
        TransactionStatus txStatus)
    {
        boolean shouldFlush = false;
        final Set<String> newPermissionNames = new HashSet<>(permissionNames);
        final Set<String> oldPermissionNames = findByRoleNameIn(Collections.singletonList(roleName), txStatus)
            .stream()
            .map(RolePermissionEntry::getPermissionName)
            .collect(Collectors.toSet());

        // TODO : insertParams 리턴 값 재정의
        List<Map<String, Object>> insertParams = null;
        if(newPermissionNames.isEmpty())
        {
            insertParams = Collections.emptyList();
        }
        else
        {
            addTo(
                roleName,
                newPermissionNames
                    .stream()
                    .filter((newPermissionName) -> !oldPermissionNames.contains(newPermissionName))
                    .collect(Collectors.toList()),
                txStatus
            );

            shouldFlush = true;
        }

        if(!oldPermissionNames.isEmpty())
        {
            removeFrom(
                roleName,
                oldPermissionNames
                    .stream()
                    .filter((oldPermissionName) -> !newPermissionNames.contains(oldPermissionName))
                    .collect(Collectors.toList()),
                txStatus
            );

            shouldFlush = true;
        }

        if(shouldFlush)
        {
            txStatus.flush();
        }

        return insertParams;
    }

    @Override
    public List<Map<String, Object>> addTo(String roleName, Collection<String> permissionNames,
        TransactionStatus txStatus)
    {
        return insert(
            permissionNames
                .stream()
                .map((permissionName) ->
                {
                    final Map<String, Object> param = new HashMap<>();
                    param.put("roleName", roleName);
                    param.put("permissionName", permissionName);

                    return param;
                })
                .collect(Collectors.toList()),
            txStatus
        );
    }

    @Override
    public void removeFrom(String roleName, Collection<String> permissionNames, TransactionStatus txStatus)
    {
        for(String permissionName : permissionNames)
        {
            deleteByCompositeKey(roleName, permissionName, txStatus);
        }
    }

    @Override
    public void deleteByEntryUidIn(Collection<Long> entryUids, TransactionStatus txStatus)
    {
        for(Long entryUid : entryUids)
        {
            deleteByEntryUid(entryUid, txStatus);
        }
    }

    @Override
    public void deleteByEntryUid(Long entryUid, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("entryUid", entryUid);

        executeDelete(param, txStatus);
    }

    @Override
    public void deleteByCompositeKeyIn(Collection<Map<String, Object>> compositeKeys, TransactionStatus txStatus)
    {
        for(Map<String, Object> compositeKey : compositeKeys)
        {
            deleteByCompositeKey(
                MapUtils.getString(compositeKey, "roleName", null),
                MapUtils.getString(compositeKey, "permissionName", null),
                txStatus
            );
        }
    }

    @Override
    public void deleteByCompositeKey(String roleName, String permissionName, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("roleName", roleName);
        param.put("permissionName", permissionName);

        executeDelete(param, txStatus);
    }

    @Override
    public List<RolePermissionEntry> findByRoleNameIn(Collection<String> roleNames, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", roleNames);

        return decorateRows(getMapper(RolePermissionEntryMapper.class).findByRoleNameIn(param));
    }

    @Override
    public List<RolePermissionEntry> findByPermissionNameIn(Collection<String> permissionNames, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", permissionNames);

        return decorateRows(getMapper(RolePermissionEntryMapper.class).findByPermissionNameIn(param));
    }

    @Override
    protected void executeInsert(Map<String, Object> insertParam, TransactionStatus txStatus)
    {
        getMapper(RolePermissionEntryMapper.class).insert(insertParam);
    }

    @Override
    protected void executeDelete(Map<String, Object> deleteParam, TransactionStatus txStatus)
    {
        getMapper(RolePermissionEntryMapper.class).delete(deleteParam);
    }
}
