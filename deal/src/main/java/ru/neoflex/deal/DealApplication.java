package ru.neoflex.deal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.neoflex.deal.config.DealProperties;

@SpringBootApplication
@EnableConfigurationProperties(DealProperties.class)
public class DealApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealApplication.class, args);
	}

}
