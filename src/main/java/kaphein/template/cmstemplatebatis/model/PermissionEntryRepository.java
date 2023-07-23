package kaphein.template.cmstemplatebatis.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;

public interface PermissionEntryRepository
{
    List<Map<String, Object>> reset(Long userUid, Collection<Long> permissionUids, TransactionStatus txStatus);

    List<Map<String, Object>> addTo(Long userUid, Collection<Long> permissionUids, TransactionStatus txStatus);

    void removeFrom(Long userUid, Collection<Long> permissionUids, TransactionStatus txStatus);

    void delete(Collection<Long> permissionEntryUids, TransactionStatus txStatus);

    void delete(Long permissionEntryUid, TransactionStatus txStatus);

    void deleteByCompositeKeys(Collection<Map<String, Object>> compositeKeys, TransactionStatus txStatus);

    void deleteByCompositeKey(Long userUid, Long permissionUid, TransactionStatus txStatus);

    List<RolePermissionEntry> findByPermissionUidIn(Collection<Long> values, TransactionStatus txStatus);

    List<RolePermissionEntry> findByUserUidIn(Collection<Long> values, TransactionStatus txStatus);
}
