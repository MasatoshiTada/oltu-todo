package com.example.readclient.web.engine;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mvc.engine.ViewEngine;
import javax.mvc.engine.ViewEngineContext;
import javax.mvc.engine.ViewEngineException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Thymeleaf 3用のViewEngineクラス。
 */
@ApplicationScoped
public class Thymeleaf3ViewEngine implements ViewEngine {

    @Inject
    private ServletContext servletContext;

    private TemplateEngine templateEngine;

    @PostConstruct
    public void init() {
        ServletContextTemplateResolver resolver =
                new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix(DEFAULT_VIEW_FOLDER);
        resolver.setTemplateMode(TemplateMode.HTML);
        templateEngine = new TemplateEngine();
        templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.setTemplateResolver(resolver);
    }

    @Override
    public boolean supports(String view) {
        return view.endsWith(".html");
    }

    @Override
    public void processView(ViewEngineContext context) throws ViewEngineException {
        try {
            HttpServletRequest httpServletRequest = context.getRequest();
            HttpServletResponse httpServletResponse = context.getResponse();
            WebContext webContext = new WebContext(httpServletRequest, httpServletResponse,
                    servletContext, httpServletRequest.getLocale());
            webContext.setVariables(context.getModels());
            templateEngine.process(context.getView(), webContext, httpServletResponse.getWriter());
        } catch (IOException e) {
            throw new ViewEngineException(e);
        }
    }
}