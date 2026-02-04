package fun.hades;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("fun.hades.mapper")
public class HadesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HadesApplication.class, args);
    }

}
