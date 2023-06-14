package com.xian.eduservice.annotations;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CoursePublish {
    String description()  default "";
}
