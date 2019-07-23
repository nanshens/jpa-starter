package ns.boot.jpa.starter.configure;

import ns.boot.jpa.starter.controller.FindController;
import ns.boot.jpa.starter.property.JpaStarterProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(FindController.class)
@ConditionalOnWebApplication
@EnableConfigurationProperties(JpaStarterProperties.class)
public class AutoConfigure {

	@Bean
	@ConditionalOnMissingBean
	FindController findController() {
		return new FindController();
	}
}