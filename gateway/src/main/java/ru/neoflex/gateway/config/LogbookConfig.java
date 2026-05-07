package ru.neoflex.gateway.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.*;
import org.zalando.logbook.spring.webflux.LogbookWebFilter;

import static java.util.regex.Pattern.compile;
import static org.zalando.logbook.core.Conditions.exclude;
import static org.zalando.logbook.core.Conditions.requestTo;
import static org.zalando.logbook.json.JsonPathBodyFilters.jsonPath;

@Configuration
@EnableAutoConfiguration(exclude = {
        org.zalando.logbook.autoconfigure.webflux.LogbookWebFluxAutoConfiguration.class
})
public class LogbookConfig {

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .condition(exclude(
                        requestTo("/actuator/**"),
                        requestTo("/v3/api-docs/**"),
                        requestTo("/swagger-ui/**")
                ))
                .headerFilter(HeaderFilters.authorization())
                .bodyFilter(jsonPath("$.passportSeries").replace("XXXX"))
                .bodyFilter(jsonPath("$.passportNumber").replace("XXXXXX"))
                .bodyFilter(jsonPath("$.firstName").replace(compile("^(\\w).+"), "$1."))
                .bodyFilter(jsonPath("$.middleName").replace(compile("^(\\w).+"), "$1."))
                .sink(new DefaultSink(
                        new DefaultHttpLogFormatter(),
                        new DefaultHttpLogWriter()
                ))
                .build();
    }

    @Bean
    @Order(-100)
    public WebFilter logbookWebFilter(Logbook logbook) {
        return new LogbookWebFilter(logbook);
    }
}
