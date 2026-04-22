package ru.neoflex.deal.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class DealConfig {

    private final DealProperties dealProperties;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(dealProperties.getCalculatorUrl())
                .build();
    }

    @Bean
    public MeterBinder meterBinder() {
        return registry -> Counter.builder("deal_counter")
                .register(registry);
    }
}
