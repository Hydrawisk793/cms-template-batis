package kaphein.template.cmstemplatebatis.model;

import java.util.Collection;
import java.util.List;

import kaphein.template.cmstemplatebatis.persistence.PaginationParameter;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;
import org.springframework.transaction.TransactionStatus;

public interface PermissionRepository
{
    void delete(Long permissionUid, TransactionStatus txStatus);

    List<Permission> findAll(TransactionStatus txStatus);

    PaginationResult<Permission> findAll(PaginationParameter paginationParameter, TransactionStatus txStatus);

    Permission findOneByPermissionUid(Long permissionUid, TransactionStatus txStatus);

    List<Permission> findByPermissionUidIn(Collection<Long> values, TransactionStatus txStatus);

    Permission findOneByPermissionName(String permissionName, TransactionStatus txStatus);

    List<Permission> findByPermissionNameIn(Collection<String> values, TransactionStatus txStatus);
}
