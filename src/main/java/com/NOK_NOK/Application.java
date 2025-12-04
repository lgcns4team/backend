package com.NOK_NOK;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
        // ================================================
        // 애플리케이션 실행 시 env 파일에서 자동으로 환경변수 셋팅
        Dotenv env = Dotenv.configure().ignoreIfMissing().load();
        env.entries().forEach((entry) -> {
            System.setProperty(entry.getKey(), entry.getValue());
        });
        // ================================================

		SpringApplication.run(Application.class, args);
	}

}
