package kaphein.template.cmstemplatebatis.model;

import kaphein.template.cmstemplatebatis.persistence.PaginationParameter;
import kaphein.template.cmstemplatebatis.persistence.PaginationResult;
import org.springframework.transaction.TransactionStatus;

import java.util.List;

public interface LoginLogRepository
{
    List<LoginLog> findAll(TransactionStatus txStatus);

    PaginationResult<LoginLog> findAll(PaginationParameter paginationParameter, TransactionStatus txStatus);
}
