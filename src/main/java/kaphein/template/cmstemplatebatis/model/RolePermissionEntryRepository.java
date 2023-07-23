package kaphein.template.cmstemplatebatis.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;

public interface RolePermissionEntryRepository
{
    List<Map<String, Object>> reset(String roleName, Collection<String> permissionNames, TransactionStatus txStatus);

    List<Map<String, Object>> addTo(String roleName, Collection<String> permissionNames, TransactionStatus txStatus);

    void removeFrom(String roleName, Collection<String> permissionNames, TransactionStatus txStatus);

    void deleteByEntryUidIn(Collection<Long> entryUids, TransactionStatus txStatus);

    void deleteByEntryUid(Long entryUid, TransactionStatus txStatus);

    void deleteByCompositeKeyIn(Collection<Map<String, Object>> compositeKeys, TransactionStatus txStatus);

    void deleteByCompositeKey(String roleName, String permissionName, TransactionStatus txStatus);

    List<RolePermissionEntry> findByRoleNameIn(Collection<String> roleNames, TransactionStatus txStatus);

    List<RolePermissionEntry> findByPermissionNameIn(Collection<String> permissionNames, TransactionStatus txStatus);
}
