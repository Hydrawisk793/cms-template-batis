package kaphein.template.cmstemplatebatis.persistence;

import java.util.Objects;
import java.util.function.Consumer;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

/**
 *  {@link PlatformTransactionManager}로부터 {@link org.springframework.transaction.support.TransactionTemplate}를 만드는 과정을 간략화 하여 데이터베이스 트랜잭션 사용을 쉽게 한다.
 */
public class TransactionService
{
    public TransactionService(PlatformTransactionManager platformTransactionManager)
    {
        this.platformTransactionManager = Objects.requireNonNull(platformTransactionManager);
    }

    /**
     *  결과를 반환하는 트랜잭션을 수행한다.
     *
     *  @param isolationLevel 트랜잭션 격리 수준
     *  @param action 트랜잭션 내에서 수행할 로직
     */
    public <R> R doTransactionWithResult(int isolationLevel, TransactionCallback<R> action)
    {
        return new TransactionTemplateBuilder()
            .platformTransactionManager(platformTransactionManager)
            .isolationLevel(isolationLevel)
            .build()
            .execute(action);
    }

    /**
     *  결과를 반환하지 않는 트랜잭션을 수행한다.
     *
     *  @param isolationLevel 트랜잭션 격리 수준
     *  @param action 트랜잭션 내에서 수행할 로직
     */
    public void doTransaction(int isolationLevel, Consumer<TransactionStatus> action)
    {
        new TransactionTemplateBuilder()
            .platformTransactionManager(platformTransactionManager)
            .isolationLevel(isolationLevel)
            .build()
            .executeWithoutResult(action);
    }

    public <R> R doReadUncommittedWithResult(TransactionCallback<R> action)
    {
        return doTransactionWithResult(TransactionDefinition.ISOLATION_READ_UNCOMMITTED, action);
    }

    public void doReadUncommitted(Consumer<TransactionStatus> action)
    {
        doTransaction(TransactionDefinition.ISOLATION_READ_UNCOMMITTED, action);
    }

    public <R> R doReadCommittedWithResult(TransactionCallback<R> action)
    {
        return doTransactionWithResult(TransactionDefinition.ISOLATION_READ_COMMITTED, action);
    }

    public void doReadCommitted(Consumer<TransactionStatus> action)
    {
        doTransaction(TransactionDefinition.ISOLATION_READ_COMMITTED, action);
    }

    public <R> R doRepeatableReadWithResult(TransactionCallback<R> action)
    {
        return doTransactionWithResult(TransactionDefinition.ISOLATION_REPEATABLE_READ, action);
    }

    public void doRepeatableRead(Consumer<TransactionStatus> action)
    {
        doTransaction(TransactionDefinition.ISOLATION_REPEATABLE_READ, action);
    }

    public <R> R doSerializableWithResult(TransactionCallback<R> action)
    {
        return doTransactionWithResult(TransactionDefinition.ISOLATION_SERIALIZABLE, action);
    }

    public void doSerializable(Consumer<TransactionStatus> action)
    {
        doTransaction(TransactionDefinition.ISOLATION_SERIALIZABLE, action);
    }

    public PlatformTransactionManager getPlatformTransactionManager()
    {
        return platformTransactionManager;
    }

    private final PlatformTransactionManager platformTransactionManager;
}
