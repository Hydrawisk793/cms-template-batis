package kaphein.template.cmstemplatebatis.persistence;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 *  Data source 및 데이터베이스 트랜잭션 등 persistence 관련 설정을 한다.
 */
@EnableTransactionManagement
@Configuration
public class PersistenceConfig implements TransactionManagementConfigurer
{
    @Override
    public TransactionManager annotationDrivenTransactionManager()
    {
        return dataSourceTransactionManager();
    }

    @Bean
    public TransactionService transactionService()
    {
        return new TransactionService(dataSourceTransactionManager());
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager()
    {
        return new DataSourceTransactionManager(dataSource);
    }

    @Resource(type = DataSource.class)
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;
}
