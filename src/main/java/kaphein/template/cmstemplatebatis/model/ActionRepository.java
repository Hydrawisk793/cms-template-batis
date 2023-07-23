package kaphein.template.cmstemplatebatis.model;

import java.util.Collection;
import java.util.List;

import kaphein.template.cmstemplatebatis.persistence.PaginationParameter;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;
import org.springframework.transaction.TransactionStatus;

public interface ActionRepository
{
    List<Action> findAll(TransactionStatus txStatus);

    PaginationResult<Action> findAll(PaginationParameter paginationParameter, TransactionStatus txStatus);

    Action findOneByActionUid(Long actionUid, TransactionStatus txStatus);

    List<Action> findByActionUidIn(Collection<Long> values, TransactionStatus txStatus);

    Action findOneByActionUrl(String actionUrl, TransactionStatus txStatus);
}
