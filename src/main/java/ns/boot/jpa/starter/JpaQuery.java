package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author ns
 */

public abstract class JpaQuery<T> {
	protected Class<T> entityClass;
	@Autowired private EntityManager em;

	public JpaQuery(Class<T> tClass) {
		entityClass = tClass;
	}

	public JpaQuery() {
	}

	protected EntityManager getEm() {
		return em;
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
