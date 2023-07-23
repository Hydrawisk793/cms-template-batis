package kaphein.template.cmstemplatebatis.persistence.batis;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class BatisConfig
{
    @Bean
    public SqlSessionTemplate sqlSessionTemplate() throws Exception
    {
        final org.apache.ibatis.session.Configuration batisConfig = new org.apache.ibatis.session.Configuration();
        batisConfig.setMapUnderscoreToCamelCase(true);
        batisConfig.setJdbcTypeForNull(JdbcType.NULL);
        batisConfig.setCallSettersOnNulls(true);
        batisConfig.setReturnInstanceForEmptyRow(true);
        batisConfig.setDefaultExecutorType(ExecutorType.BATCH);

        batisConfig.addInterceptor(new PaginationInterceptor());

        final SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setConfiguration(batisConfig);

        final SqlSessionFactory sqlSessionFactory = Objects.requireNonNull(sessionFactoryBean.getObject());

        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Resource(type = DataSource.class)
    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;
}
