package kaphein.template.cmstemplatebatis.persistence;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 *  {@link TransactionTemplate} 인스턴스 빌더
 */
public class TransactionTemplateBuilder
{
    public TransactionTemplateBuilder()
    {
        platformTransactionManager(null);
        propagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        propagationBehaviorName(null);
        isolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        isolationLevelName(null);
        name(null);
        timeout(TransactionDefinition.TIMEOUT_DEFAULT);
        readonly(null);
    }

    /**
     *  {@link TransactionTemplate}의 인스턴스를 생성한다.
     *
     *  @return {@link TransactionTemplate}의 인스턴스
     */
    public TransactionTemplate build()
    {
        final TransactionTemplate txTemplate = new TransactionTemplate(platformTransactionManagerValue);

        txTemplate.setPropagationBehavior(propagationBehaviorValue);

        if(null != propagationBehaviorNameValue)
        {
            txTemplate.setPropagationBehaviorName(propagationBehaviorNameValue);
        }

        txTemplate.setIsolationLevel(isolationLevelValue);

        if(null != isolationLevelNameValue)
        {
            txTemplate.setIsolationLevelName(isolationLevelNameValue);
        }

        if(null != nameValue)
        {
            txTemplate.setName(nameValue);
        }

        txTemplate.setTimeout(timeoutValue);

        if(null != readonlyValue)
        {
            txTemplate.setReadOnly(readonlyValue);
        }

        return txTemplate;
    }

    public TransactionTemplateBuilder platformTransactionManager(PlatformTransactionManager value)
    {
        platformTransactionManagerValue = value;

        return this;
    }

    public TransactionTemplateBuilder propagationBehavior(int value)
    {
        propagationBehaviorValue = value;

        return this;
    }

    public TransactionTemplateBuilder propagationBehaviorName(String value)
    {
        propagationBehaviorNameValue = value;

        return this;
    }

    public TransactionTemplateBuilder isolationLevel(int value)
    {
        isolationLevelValue = value;

        return this;
    }

    public TransactionTemplateBuilder isolationLevelName(String value)
    {
        isolationLevelNameValue = value;

        return this;
    }

    public TransactionTemplateBuilder name(String value)
    {
        nameValue = value;

        return this;
    }

    public TransactionTemplateBuilder timeout(int value)
    {
        timeoutValue = value;

        return this;
    }

    public TransactionTemplateBuilder readonly(Boolean value)
    {
        readonlyValue = value;

        return this;
    }

    private PlatformTransactionManager platformTransactionManagerValue;

    private int propagationBehaviorValue;

    private String propagationBehaviorNameValue;

    private int isolationLevelValue;

    private String isolationLevelNameValue;

    private String nameValue;

    private int timeoutValue;

    private Boolean readonlyValue;
}
