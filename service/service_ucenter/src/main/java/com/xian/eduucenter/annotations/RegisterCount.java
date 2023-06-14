package com.xian.eduucenter.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegisterCount {
    String description()  default "";
}
