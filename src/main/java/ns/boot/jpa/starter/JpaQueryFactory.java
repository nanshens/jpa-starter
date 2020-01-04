package ns.boot.jpa.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * @author ns
 */

@Component
public class JpaQueryFactory {
	@Autowired EntityManager entityManager;

	public JpaSqlQuery createSqlQuery() {
		return new JpaSqlQuery(entityManager);
	}

	public <T> JpaSqlQuery<T> createSqlQuery(Class<T> entityClz) {
		return new JpaSqlQuery<>(entityClz, entityManager);
	}

	public <T> JpaJsonQuery<T> createJsonQuery(Class<T> entityClz) {
		return new JpaJsonQuery<>(entityClz, entityManager);
	}

	public <T> JpaCustomQuery<T> createCustomQuery(Class<T> entityClz) {
		return new JpaCustomQuery<>(entityClz, entityManager);
	}

}
