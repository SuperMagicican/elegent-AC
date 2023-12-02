package cn.elegent.ac.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 主题
 * @author wgl
 */
@Documented
@Component
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Topic {
    String value();

}
