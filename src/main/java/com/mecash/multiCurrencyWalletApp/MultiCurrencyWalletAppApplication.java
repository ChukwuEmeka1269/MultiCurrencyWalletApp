package com.mecash.multiCurrencyWalletApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MultiCurrencyWalletAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiCurrencyWalletAppApplication.class, args);
	}

}
