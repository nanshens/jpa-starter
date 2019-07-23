package ns.boot.jpa.starter.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ns
 */
@Data
@ConfigurationProperties(prefix = "jpa-utils")
public class JpaStarterProperties {

	private String baseUrl = "";
}
