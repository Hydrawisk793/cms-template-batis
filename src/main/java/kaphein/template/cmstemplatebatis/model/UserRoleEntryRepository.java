package kaphein.template.cmstemplatebatis.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;

public interface UserRoleEntryRepository
{
    List<Map<String, Object>> reset(Long userUid, Collection<String> roleNames, TransactionStatus txStatus);

    List<Map<String, Object>> addTo(Long userUid, Collection<String> roleNames, TransactionStatus txStatus);

    void removeFrom(Long userUid, Collection<String> roleNames, TransactionStatus txStatus);

    void deleteByEntryUidIn(Collection<Long> entryUids, TransactionStatus txStatus);

    void deleteByEntryUid(Long entryUid, TransactionStatus txStatus);

    void deleteByCompositeKeyIn(Collection<Map<String, Object>> compositeKeys, TransactionStatus txStatus);

    void deleteByCompositeKey(Long userUid, String roleName, TransactionStatus txStatus);

    List<UserRoleEntry> findByUserUidIn(Collection<Long> userUids, TransactionStatus txStatus);

    List<UserRoleEntry> findByRoleNameIn(Collection<String> roleNames, TransactionStatus txStatus);
}
