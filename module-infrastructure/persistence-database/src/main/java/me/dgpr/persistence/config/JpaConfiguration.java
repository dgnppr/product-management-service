package me.dgpr.persistence.config;

import me.dgpr.persistence.Persistence;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackageClasses = {Persistence.class}
)
public class JpaConfiguration {

}
