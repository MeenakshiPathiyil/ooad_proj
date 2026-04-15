package unisync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "api",
                "controller",
                "service",
                "dao",
                "model",
                "factory",
                "observer",
                "singleton",
                "strategy",
                "ui",
                "unisync"
        }
)
public class UniSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniSyncApplication.class, args);
    }
}

