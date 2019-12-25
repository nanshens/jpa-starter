package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author ns
 */
public abstract class JpaQuery<T> {
	protected EntityManager em;
	protected Class<T> entityClass;
	protected boolean isPaged;

	public JpaQuery(Class<T> tClass) {
		entityClass = tClass;
	}

	public JpaQuery() {
	}

	protected EntityManager getEm() {
		return em;
	}

	protected void setEm(EntityManager em) {
		this.em = em;
	}

	private void cache() {

	}

	private void parser() {

	}

	private void query() {

	}

	public abstract JSON resultJson();
	public abstract List<T> resultList();
	public abstract Page<T> resultPage();
}
