package com.derek_s.hubble_gallery.utils.dagger;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by derek on 15-08-03.
 */


@Scope
@Retention(RUNTIME)
@Documented
public @interface PerApp {
}