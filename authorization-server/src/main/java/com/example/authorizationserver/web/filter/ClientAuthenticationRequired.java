package com.example.authorizationserver.web.filter;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * クライアントのBASIC認証が必要な箇所に付加する。
 * このアノテーションを付加したリソースメソッドに、ClientBasicAuthenticationFilterが適用される。
 * @see ClientBasicAuthenticationFilter
 */
@NameBinding
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ClientAuthenticationRequired {
}
