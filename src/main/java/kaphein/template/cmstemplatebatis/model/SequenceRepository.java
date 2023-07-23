package kaphein.template.cmstemplatebatis.model;

import org.springframework.transaction.TransactionStatus;

public interface SequenceRepository
{
    long current(String sequenceName, TransactionStatus txStatus);

    long current(String sequenceName, long initValue, TransactionStatus txStatus);

    long next(String sequenceName, TransactionStatus txStatus);

    long next(String sequenceName, long initValue, TransactionStatus txStatus);

    long reset(String sequenceName, TransactionStatus txStatus);

    long reset(String sequenceName, long initValue, TransactionStatus txStatus);
}
