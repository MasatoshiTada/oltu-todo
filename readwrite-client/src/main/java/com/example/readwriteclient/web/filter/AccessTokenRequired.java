package com.example.readwriteclient.web.filter;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * リソースサーバーへのアクセス時にアクセストークンが必要であるコントローラーメソッドに付加する。
 * そのコントローラーメソッド実行前に、AccessTokenFilterが実行される。
 * @see AccessTokenFilter
 */
@NameBinding
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AccessTokenRequired {
}
