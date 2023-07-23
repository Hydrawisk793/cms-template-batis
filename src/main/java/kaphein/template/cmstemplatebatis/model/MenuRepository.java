package kaphein.template.cmstemplatebatis.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.TransactionStatus;

public interface MenuRepository
{
    void upsert(
        Collection<Map<String, Object>> insertParams,
        Collection<Map<String, Object>> updateParams,
        Long adminUserUid,
        TransactionStatus txStatus
    );

    void delete(Long menuUid, TransactionStatus txStatus);

    List<Menu> findByMenuUidIn(Collection<Long> values, TransactionStatus txStatus);

    List<MenuNode> findSubtreeNodes(TransactionStatus txStatus);

    List<MenuNode> findSubtreeNodesOf(Long menuUid, TransactionStatus txStatus);

    List<MenuNode> findSubtreeNodesByMenuNamePathPrefix(String menuNamePathPrefix, TransactionStatus txStatus);
}
