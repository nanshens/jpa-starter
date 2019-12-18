package ns.boot.jpa.starter;

import org.springframework.stereotype.Component;

/**
 * @author ns
 */

@Component
public class JpaQueryFactory {

//	public JpaQuery jsonQuery() {
//		return new JpaJsonQuery();
//	}
	public JpaQuery sqlQuery() {
		return new JpaSqlQuery();
	}

//	public JpaQuery customQuery() {
//		return new CustomQuery();
//	}

}
