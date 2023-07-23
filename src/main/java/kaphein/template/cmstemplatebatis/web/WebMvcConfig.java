package kaphein.template.cmstemplatebatis.web;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.List;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer, InitializingBean
{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        registry
          .addResourceHandler("/public/**")
          .addResourceLocations("classpath:/public/");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        converters.stream()
            .filter(MappingJackson2HttpMessageConverter.class::isInstance)
            .map(MappingJackson2HttpMessageConverter.class::cast)
            .forEach(c -> c.setObjectMapper(objectMapper));
    }

    @Bean
    public ViewResolver viewResolver()
    {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);

        return viewResolver;
    }

    @Override
    public void addFormatters(final FormatterRegistry registry)
    {
        registry.addFormatter(dateFormatter());
    }

    @Bean
    public DateFormatter dateFormatter()
    {
        DateFormatter dateFormatter =  new DateFormatter();
        dateFormatter.setIso(DateTimeFormat.ISO.DATE_TIME);

        return dateFormatter;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }

    @Bean
    public ErrorAttributes errorAttributes()
    {
        return new DefaultErrorAttributes();
    }

    @Resource(type = DispatcherServlet.class)
    public void setDispatcherServlet(DispatcherServlet dispatcherServlet)
    {
        this.dispatcherServlet = dispatcherServlet;
    }

    @Resource(name = "objectMapper")
    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Resource(name = "templateEngine")
    public void setTemplateEngine(ISpringTemplateEngine templateEngine)
    {
        this.templateEngine = templateEngine;
    }

    private DispatcherServlet dispatcherServlet;

    private ObjectMapper objectMapper;

    private ISpringTemplateEngine templateEngine;
}
