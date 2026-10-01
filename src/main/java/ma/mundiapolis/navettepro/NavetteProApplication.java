package ma.mundiapolis.navettepro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NavetteProApplication {

    public static void main(String[] args) {
        SpringApplication.run(NavetteProApplication.class, args);
    }
}
