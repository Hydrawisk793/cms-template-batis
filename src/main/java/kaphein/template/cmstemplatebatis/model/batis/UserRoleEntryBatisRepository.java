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

import kaphein.template.cmstemplatebatis.model.UserRoleEntry;
import kaphein.template.cmstemplatebatis.model.UserRoleEntryRepository;

@Repository
public class UserRoleEntryBatisRepository
    extends AbstractBatisRepository<UserRoleEntry>
    implements UserRoleEntryRepository
{
    public UserRoleEntryBatisRepository(SqlSessionTemplate sqlSessionTemplate)
    {
        super(sqlSessionTemplate, UserRoleEntry.class, UserRoleEntry::new);
    }

    @Override
    public List<Map<String, Object>> reset(Long userUid, Collection<String> roleNames, TransactionStatus txStatus)
    {
        boolean shouldFlush = false;
        final Set<String> newRoleNames = new HashSet<>(roleNames);
        final Set<String> oldRoleNames = findByUserUidIn(Collections.singletonList(userUid), txStatus)
            .stream()
            .map(UserRoleEntry::getRoleName)
            .collect(Collectors.toSet());

        // TODO : insertParams 리턴 값 재정의
        List<Map<String, Object>> insertParams = null;
        if(newRoleNames.isEmpty())
        {
            insertParams = Collections.emptyList();
        }
        else
        {
            addTo(
                userUid,
                newRoleNames
                    .stream()
                    .filter((newRoleName) -> !oldRoleNames.contains(newRoleName))
                    .collect(Collectors.toList()),
                txStatus
            );

            shouldFlush = true;
        }

        if(!oldRoleNames.isEmpty())
        {
            removeFrom(
                userUid,
                oldRoleNames
                    .stream()
                    .filter((oldRoleName) -> !newRoleNames.contains(oldRoleName))
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
    public List<Map<String, Object>> addTo(Long userUid, Collection<String> roleNames, TransactionStatus txStatus)
    {
        return insert(
            roleNames
                .stream()
                .map((roleName) ->
                {
                    final Map<String, Object> param = new HashMap<>();
                    param.put("userUid", userUid);
                    param.put("roleName", roleName);

                    return param;
                })
                .collect(Collectors.toList()),
            txStatus
        );
    }

    @Override
    public void removeFrom(Long userUid, Collection<String> roleNames, TransactionStatus txStatus)
    {
        for(String roleName : roleNames)
        {
            deleteByCompositeKey(userUid, roleName, txStatus);
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
                MapUtils.getLong(compositeKey, "userUid", null),
                MapUtils.getString(compositeKey, "roleName", null),
                txStatus
            );
        }
    }

    @Override
    public void deleteByCompositeKey(Long userUid, String roleName, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("userUid", userUid);
        param.put("roleName", roleName);

        executeDelete(param, txStatus);
    }

    @Override
    public List<UserRoleEntry> findByUserUidIn(Collection<Long> userUids, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", userUids);

        return decorateRows(getMapper(UserRoleEntryMapper.class).findByUserUidIn(param));
    }

    @Override
    public List<UserRoleEntry> findByRoleNameIn(Collection<String> roleNames, TransactionStatus txStatus)
    {
        final Map<String, Object> param = new HashMap<>();
        param.put("values", roleNames);

        return decorateRows(getMapper(UserRoleEntryMapper.class).findByRoleNameIn(param));
    }

    @Override
    protected void executeInsert(Map<String, Object> insertParam, TransactionStatus txStatus)
    {
        getMapper(UserRoleEntryMapper.class).insert(insertParam);
    }

    @Override
    protected void executeDelete(Map<String, Object> deleteParam, TransactionStatus txStatus)
    {
        getMapper(UserRoleEntryMapper.class).delete(deleteParam);
    }
}
