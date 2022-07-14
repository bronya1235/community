package com.nowcoder.app.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: Bao
 * @Date: 2022/7/14-07-14-20:52
 * @Description com.nowcoder.app.community.annotation
 * @Function 作为一个标识符，只有登录才能访问该页面和资源
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
