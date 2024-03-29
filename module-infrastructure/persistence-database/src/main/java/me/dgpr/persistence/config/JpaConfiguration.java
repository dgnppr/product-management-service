package me.dgpr.persistence.config;

import me.dgpr.persistence.Persistence;
import me.dgpr.persistence.entity.Entity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackageClasses = Persistence.class)
@EntityScan(basePackageClasses = Entity.class)
public class JpaConfiguration {

}
