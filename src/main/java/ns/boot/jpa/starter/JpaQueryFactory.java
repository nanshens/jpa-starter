package ns.boot.jpa.starter;

import org.springframework.stereotype.Component;

/**
 * @author ns
 */

@Component
public class JpaQueryFactory {

	public JpaQuery query() {
		return new JpaQuery();
	}

}
