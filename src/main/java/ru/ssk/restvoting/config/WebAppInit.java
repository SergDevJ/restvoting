package ru.ssk.restvoting.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Properties;


public class WebAppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    private final String ACTIVE_PROFILE_DB_KEY = "profiles.db";
    private final String ACTIVE_PROFILE_DB_DEFAULT_VALUE = "hsqldb";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setInitParameter("spring.profiles.default", ACTIVE_PROFILE_DB_DEFAULT_VALUE);
        String dbProfile;
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));
            dbProfile = properties.getProperty(ACTIVE_PROFILE_DB_KEY, ACTIVE_PROFILE_DB_DEFAULT_VALUE);
            servletContext.setInitParameter("spring.profiles.active", dbProfile);
            log.info("Set active profile to '{}'", dbProfile);
        } catch (IOException e) {
            servletContext.setInitParameter("spring.profiles.active", ACTIVE_PROFILE_DB_DEFAULT_VALUE);
            log.error("Error reading 'application.properties' file. Active profile set to default");
        }
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{DataJpaConfig.class, CacheConfig.class, SystemConfig.class, WebSecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setForceEncoding(true);
        encodingFilter.setEncoding("UTF-8");
        return new Filter[] {encodingFilter};
    }

    //https://www.logicbig.com/how-to/spring-mvc/spring-customizing-default-error-resolver.html
    @Override
    protected FrameworkServlet createDispatcherServlet (WebApplicationContext wac) {
        DispatcherServlet ds = new DispatcherServlet(wac);
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }
}