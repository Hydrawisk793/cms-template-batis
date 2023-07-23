package kaphein.template.cmstemplatebatis.auth.batis;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectTemplateAuthMapperConfig
{
    @Bean
    public static MapperScannerConfigurer projectTemplateAuthMapperScannerConfigurer()
    {
        final MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setAnnotationClass(Mapper.class);
        configurer.setBasePackage(ProjectTemplateAuthMapperConfig.class.getPackage().getName());

        return configurer;
    }
}
