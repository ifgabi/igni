package gg.igni.igniserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IgniServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgniServerApplication.class, args);
	}

}
