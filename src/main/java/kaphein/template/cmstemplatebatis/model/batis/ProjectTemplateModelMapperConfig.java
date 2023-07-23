package kaphein.template.cmstemplatebatis.model.batis;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectTemplateModelMapperConfig
{
    @Bean
    public static MapperScannerConfigurer projectTemplateModelMapperScannerConfigurer()
    {
        final MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setAnnotationClass(Mapper.class);
        configurer.setBasePackage(ProjectTemplateModelMapperConfig.class.getPackage().getName());

        return configurer;
    }
}
