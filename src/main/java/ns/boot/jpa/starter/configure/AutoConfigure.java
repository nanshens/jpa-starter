package ns.boot.jpa.starter.configure;

import ns.boot.jpa.starter.JpaQueryFactory;
import ns.boot.jpa.starter.property.JpaStarterProperties;
import ns.boot.jpa.starter.service.FindService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nanshen
 */
@Configuration
@ConditionalOnClass(JpaQueryFactory.class)
@ConditionalOnWebApplication
@EnableConfigurationProperties(JpaStarterProperties.class)
public class AutoConfigure {

	@Bean
	@ConditionalOnMissingBean
	JpaQueryFactory getJpaQueryFactory() {
		return new JpaQueryFactory();
	}
}