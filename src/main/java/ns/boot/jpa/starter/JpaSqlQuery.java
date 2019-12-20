package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.Query;
import java.util.List;

/**
 * @author ns
 */

public class JpaSqlQuery<T> extends JpaQuery<T>{
	private String sqlQuery;

	public JpaSqlQuery() {
		super();
	}

	public JpaSqlQuery(Class<T> tClass) {
		super(tClass);
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
		Query query = entityClass == null ? getEm().createNativeQuery(sqlQuery) :
				getEm().createNativeQuery(sqlQuery, entityClass);
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
