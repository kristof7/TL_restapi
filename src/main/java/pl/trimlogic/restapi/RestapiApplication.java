package pl.trimlogic.restapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@Slf4j
@SpringBootApplication
@PropertySources({
        @PropertySource("classpath:restapi.properties")
})
public class RestapiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
    	SpringApplication.run(RestapiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RestapiApplication.class);
    }
}
