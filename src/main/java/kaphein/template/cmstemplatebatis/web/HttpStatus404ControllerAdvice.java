package kaphein.template.cmstemplatebatis.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.Resource;
import java.net.URI;
import java.util.Locale;

/**
 *  HTTP 404로 취급되어야 하는 exception을 처리한다.
 *  API 호출인 경우 JSON으로 응답하며, 그 외 웹 페이지 요청에 대해서는 error 페이지로 응답한다.
 */
@ControllerAdvice
public class HttpStatus404ControllerAdvice
{
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public View handleNoHandlerFoundException(NoHandlerFoundException e, Model model, Locale locale)
        throws Exception
    {
        View view = null;

        final URI requestUri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
        if(requestUri.getPath().startsWith(protocolProperties.getPrefix()) || requestUri.getPath().startsWith(apiProperties.getPrefix()))
        {
            model.mergeAttributes(new ApiErrorResponse<>(
                ApiErrorCode.METHOD_NOT_FOUND,
                String.format("%s %s does not exist", e.getHttpMethod(), e.getRequestURL())
            ).toMap());

            view = new MappingJackson2JsonView(objectMapper);
        }
        else
        {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("stackTrace", e.getStackTrace());

            view = viewResolver.resolveViewName("/error/error", locale);
        }

        return view;
    }

    @Resource(name = "protocolProperties")
    public void setProtocolProperties(ProtocolProperties protocolProperties)
    {
        this.protocolProperties = protocolProperties;
    }

    @Resource(name = "apiProperties")
    public void setApiProperties(ApiProperties apiProperties)
    {
        this.apiProperties = apiProperties;
    }

    @Resource(name = "objectMapper")
    public void setObjectMapper(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Resource(type = ViewResolver.class)
    public void setViewResolver(ViewResolver viewResolver)
    {
        this.viewResolver = viewResolver;
    }

    private ProtocolProperties protocolProperties;

    private ApiProperties apiProperties;

    private ObjectMapper objectMapper;

    private ViewResolver viewResolver;
}
