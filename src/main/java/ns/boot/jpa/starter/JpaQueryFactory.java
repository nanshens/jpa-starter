package ns.boot.jpa.starter;

import org.springframework.stereotype.Component;

/**
 * @author ns
 */

@Component
public class JpaQueryFactory {

	public JpaSqlQuery createSqlQuery() {
		return new JpaSqlQuery();
	}

	public <T> JpaSqlQuery<T> createSqlQuery(Class<T> tClass) {
		return new JpaSqlQuery<>(tClass);
	}

	public <T> JpaJsonQuery<T> createJsonQuery(Class<T> tClass) {
		return new JpaJsonQuery<>(tClass);
	}

	public <T> JpaCustomQuery<T> createCustomQuery(Class<T> tClass) {
		return new JpaCustomQuery<>(tClass);
	}

}
