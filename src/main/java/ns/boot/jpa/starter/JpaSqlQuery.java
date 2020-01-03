package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author ns
 */

public class JpaSqlQuery<T> extends BaseJpaQuery<T>{
	private String sqlQuery;

	protected JpaSqlQuery(EntityManager entityMgr) {
		super(entityMgr);
	}

	protected JpaSqlQuery(Class<T> entityClz, EntityManager entityMgr) {
		super(entityClz, entityMgr);
	}

	public JpaSqlQuery<T> input(String sqlQuery) {
		this.sqlQuery = sqlQuery;
		return this;
	}

	public List<T> cache() {
		return null;
	}

	public void parser() {
	}

	private List<T> query() {
		Query query = entityClz == null ? entityMgr.createNativeQuery(sqlQuery) :
				entityMgr.createNativeQuery(sqlQuery, entityClz);
		return query.getResultList();
	}

	@Override
	public JSON resultJson() {
		return buildJson(query());
	}

	private JSONArray buildJson(List result) {
		return new JSONArray(result);
	}

	@Override
	public List<T> resultList() {
		return query();
	}

	@Override
	public Page<T> resultPage() {
		return new PageImpl<>(query());
	}
}
