package api;

import db.DBApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(DBApplication.class)
public class MiniProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniProjectApplication.class, args);
    }
}
