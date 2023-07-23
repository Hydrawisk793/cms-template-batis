package kaphein.template.cmstemplatebatis.model;

import java.util.Collection;
import java.util.List;

import kaphein.template.cmstemplatebatis.persistence.PaginationParameter;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;
import org.springframework.transaction.TransactionStatus;

public interface AppUserRepository
{
    void delete(Long userUid, TransactionStatus txStatus);

    List<AppUser> findAll(TransactionStatus txStatus);

    PaginationResult<AppUser> findAll(PaginationParameter paginationParameter, TransactionStatus txStatus);

    AppUser findOneByUserUid(Long value, TransactionStatus txStatus);

    List<AppUser> findByUserUidIn(Collection<Long> values, TransactionStatus txStatus);

    AppUser findOneByLoginName(String loginName, TransactionStatus txStatus);
}
