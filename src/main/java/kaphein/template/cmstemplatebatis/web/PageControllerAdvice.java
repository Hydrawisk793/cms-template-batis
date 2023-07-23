package kaphein.template.cmstemplatebatis.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.net.URI;
import java.net.URISyntaxException;

@ControllerAdvice(annotations = {PageController.class})
public class PageControllerAdvice
{
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public String handleException(Model model, Exception cause)
    {
        model.addAttribute("message", cause.getMessage());
        model.addAttribute("stackTrace", cause.getStackTrace());

        return "/error/error";
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundExceptionForPage(NoHandlerFoundException e) throws URISyntaxException
    {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI("/error"));

        return ResponseEntity
            .status(HttpStatus.SEE_OTHER)
            .headers(httpHeaders)
            .build();
    }
}
