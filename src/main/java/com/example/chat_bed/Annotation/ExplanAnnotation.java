package com.example.chat_bed.Annotation;

import java.lang.annotation.*;
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExplanAnnotation {
    String name();
    String explain();
}
