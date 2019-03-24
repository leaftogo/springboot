package top.leaftogo.tanmu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("top.leaftogo.tanmu.Mapper")
@EnableCaching
@EnableAsync
@EnableScheduling



@SpringBootApplication
public class TanmuApplication {
	public static void main(String[] args) {

		SpringApplication.run(TanmuApplication.class, args);
	}
}
