package ns.boot.jpastarter.configure;

import ns.boot.jpastarter.controller.FindController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(FindController.class)
public class AutoConfigure {

	@Bean
	@ConditionalOnMissingBean
	FindController findController() {
		return new FindController();
	}
}