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
		JpaSqlQuery jpaSqlQuery = new JpaSqlQuery();
		jpaSqlQuery.setEm(entityManager);
		return jpaSqlQuery;
	}

	public <T> JpaSqlQuery<T> createSqlQuery(Class<T> tClass) {
		JpaSqlQuery<T> jpaSqlQuery = new JpaSqlQuery<>(tClass);
		jpaSqlQuery.setEm(entityManager);
		return jpaSqlQuery;
	}

	public <T> JpaJsonQuery<T> createJsonQuery(Class<T> tClass) {
		JpaJsonQuery<T> jpaJsonQuery = new JpaJsonQuery<>(tClass);
		jpaJsonQuery.setEm(entityManager);
		return jpaJsonQuery;
	}

	public <T> JpaCustomQuery<T> createCustomQuery(Class<T> tClass) {
		JpaCustomQuery<T> jpaCustomQuery = new JpaCustomQuery<>(tClass);
		jpaCustomQuery.setEm(entityManager);
		return jpaCustomQuery;
	}

}
