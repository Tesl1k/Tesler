package configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("applications")
@ComponentScan("DAO")
@ComponentScan("resources")
public class MainConfig {
}
