package com.software.software_development;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import com.software.software_development.core.setup.EntityInitializer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class SoftwareDevelopmentApplication implements CommandLineRunner{

	private final EntityInitializer initializer;

	public static void main(String[] args) {
		SpringApplication.run(SoftwareDevelopmentApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (args.length > 0 && Arrays.asList(args).contains("--populate")) {
			initializer.initializeAll();
		}
	}
}
