package com.kumiq.identity.scim.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public LocaleResolver localResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new ScimMessageConverter());
    }

    public static class ScimMessageConverter extends MappingJackson2HttpMessageConverter {
        public ScimMessageConverter() {
            super(Jackson2ObjectMapperBuilder
                    .json()
                    .serializationInclusion(JsonInclude.Include.NON_EMPTY)
                    .build());
            super.setSupportedMediaTypes(Arrays.asList(
                    new MediaType("application", "json+scim", DEFAULT_CHARSET)
            ));
        }
    }
}
