package fr.quentinpigne.springsandboxjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringSandboxJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSandboxJavaApplication.class, args);
	}

}
