package kaphein.template.cmstemplatebatis.web;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import kaphein.template.cmstemplatebatis.ParameterInvalidException;
import kaphein.template.cmstemplatebatis.persistence.EntityFieldValueInvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.Method;
import java.sql.SQLDataException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ResponseBody
@ControllerAdvice(annotations = {RestController.class})
public class RestControllerAdvice
{
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Throwable.class)
    public ApiErrorResponse<Object> handleThrowable(Throwable e)
    {
        return handleThrowableWithStackTrace(e, ApiErrorCode.INTERNAL_ERROR);
    }

    @ExceptionHandler(value = NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundExceptionForPage(NoHandlerFoundException e)
    {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse<>(
                ApiErrorCode.METHOD_NOT_FOUND,
                String.format("%s %s does not exist", e.getHttpMethod(), e.getRequestURL())
            ));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    public ApiErrorResponse<Object> handleAccessDeniedException(AccessDeniedException e)
    {
        return handleThrowableWithData(e, ApiErrorCode.UNAUTHORIZED, null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ApiErrorResponse<Object> handleHttpMessageConversionException(HttpMessageConversionException e)
    {
        return handleThrowableAsInvalidParams(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ApiErrorResponse<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e)
    {
        ApiErrorResponse<Object> res = null;

        final Throwable rootCause = e.getRootCause();
        final Throwable cause = (null == rootCause ? e : rootCause);

        if(cause instanceof MismatchedInputException)
        {
            res = handleMismatchedInputException((MismatchedInputException)cause);
        }
        else
        {
            res = handleThrowable(cause);
        }

        return res;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ApiErrorResponse<Object> handleIllegalArgumentException(IllegalArgumentException e)
    {
        return handleThrowableAsInvalidParams(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MismatchedInputException.class)
    public ApiErrorResponse<Object> handleMismatchedInputException(MismatchedInputException e)
    {
        ApiErrorResponse<Object> res = null;

        final List<JsonMappingException.Reference> path = e.getPath();

        if(path.isEmpty())
        {
            res = handleThrowableAsInvalidParams(e);
        }
        else
        {
            res = handleParameterInvalidException(new ParameterInvalidException(path.get(0).getFieldName()));
        }

        return res;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = SQLDataException.class)
    public ApiErrorResponse<Object> handleSQLDataException(SQLDataException e)
    {
        return handleThrowableAsInvalidParams(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ApiErrorResponse<Object> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e)
    {
        return handleThrowableAsInvalidParams(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = ParameterInvalidException.class)
    public ApiErrorResponse<Object> handleParameterInvalidException(ParameterInvalidException e)
    {
        return handleThrowableAsInvalidParams(e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = EntityFieldValueInvalidException.class)
    public ApiErrorResponse<Object> handleEntityFieldValueInvalidException(EntityFieldValueInvalidException e)
    {
        return handleThrowableAsInvalidParams(e);
    }

    @SuppressWarnings("unchecked")
    private ApiErrorResponse<Object> handleThrowableAsInvalidParams(Throwable cause)
    {
        ApiErrorResponse<Object> res = null;

        try
        {
            final Method toMapMethod = cause.getClass().getMethod("toMap");
            final Map<String, Object> data = (Map<String, Object>)toMapMethod.invoke(cause);
            data.replace("type", cause.getClass().getSimpleName());

            res = handleThrowableWithData(cause, ApiErrorCode.INVALID_PARAMS, data);
        }
        catch(NoSuchMethodException nsme)
        {
            res = handleThrowableWithoutData(cause, ApiErrorCode.INVALID_PARAMS);
        }
        catch(Exception e)
        {
            res = handleThrowableWithStackTrace(cause, ApiErrorCode.INVALID_PARAMS);
        }

        return res;
    }

    private ApiErrorResponse<Object> handleThrowableWithStackTrace(Throwable cause, ApiErrorCode code)
    {
        final Object data = Stream
            .of(
                new AbstractMap.SimpleImmutableEntry<>("className", cause.getClass().getName()),
                new AbstractMap.SimpleImmutableEntry<>("stackTrace", cause.getStackTrace())
            )
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return handleThrowableWithData(cause, code, data);
    }

    private ApiErrorResponse<Object> handleThrowableWithData(Throwable cause, ApiErrorCode code, Object data)
    {
        return new ApiErrorResponse<>(code, cause.getMessage(), data);
    }

    private ApiErrorResponse<Object> handleThrowableWithoutData(Throwable cause, ApiErrorCode code)
    {
        return new ApiErrorResponse<>(code, cause.getMessage());
    }
}
