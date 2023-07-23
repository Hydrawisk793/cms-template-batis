package kaphein.template.cmstemplatebatis.web;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface PageController
{
    @AliasFor(annotation = Controller.class)
    String value() default "";
}
