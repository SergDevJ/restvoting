package ru.ssk.restvoting.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import ru.ssk.restvoting.system.Settings;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Configuration
@PropertySource("classpath:system.properties")
public class SystemConfig {
    private final String DEFAULT_VOTE_LAST_TIME = "11:00";

    Logger log = LoggerFactory.getLogger(SystemConfig.class);

    @Autowired
    private Environment env;

    @Bean("systemSettings")
    Settings getSystemSettings() {
        Settings settings = new Settings();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
        LocalTime lastVoteTime;
        try {
            lastVoteTime = LocalTime.parse(env.getProperty("system.voteLastTime", DEFAULT_VOTE_LAST_TIME));
        } catch (Exception e) {
            log.warn("Can't parse 'system.voteLastTime' parameter value in 'system.properties' file");
            lastVoteTime = LocalTime.parse(DEFAULT_VOTE_LAST_TIME, formatter);
        }
        settings.setVoteLastTime(lastVoteTime);
        return settings;
    }

    @Bean
    ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("/WEB-INF/messages/app");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(60);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }

    @Bean
    MessageSourceAccessor messageSourceAccessor(@Autowired MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }
}
