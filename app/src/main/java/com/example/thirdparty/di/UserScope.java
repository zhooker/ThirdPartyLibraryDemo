package com.example.thirdparty.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by zhuangsj on 17-6-13.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {
}
