package com.example.authorizationserver.web.filter;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * リソースサーバーのBASIC認証が必要な箇所に付加する
 * このアノテーションを付加したリソースメソッドに、ResourceServerBasicAuthenticationFilterが適用される。
 * @see ResourceServerBasicAuthenticationFilter
 */
@NameBinding
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceServerAuthenticationRequired {
}
