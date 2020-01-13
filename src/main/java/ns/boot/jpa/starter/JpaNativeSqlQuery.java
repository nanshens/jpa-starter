package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import ns.boot.jpa.starter.constant.QueryConstant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author ns
 */

public class JpaNativeSqlQuery<T> extends BaseJpaQuery<T>{
	private String nativeSql;
	private String nativeSqlWithoutPageInfo;
	protected JpaNativeSqlQuery(EntityManager entityMgr) {
		super(entityMgr);
	}

	protected JpaNativeSqlQuery(Class<T> entityClz, EntityManager entityMgr) {
		super(entityClz, entityMgr);
	}

	public JpaNativeSqlQuery<T> input(String nativeSql) {
		this.nativeSql = nativeSql;
		this.nativeSqlWithoutPageInfo = nativeSql
				.replaceAll(QueryConstant.LIMIT_INFO, "")
				.replaceAll(QueryConstant.OFFSET_INFO, "");
		if (!nativeSql.isEmpty() && !nativeSql.equals(nativeSqlWithoutPageInfo)) {
			// setPageInfo
		}
		return this;
	}

	public JpaNativeSqlQuery<T> page(int page, int limit) {
		setPageInfo(page, limit);
		if (nativeSql.contains(";")) {
			if (!nativeSql.contains("limit") && !nativeSql.contains("offset")) {
				this.nativeSql = nativeSql.replace(";", " limit " + limit + " offset " + limit * (page - 1));
			} else {
				this.nativeSql = nativeSql
						.replaceAll(QueryConstant.LIMIT_INFO, "limit " + limit)
						.replaceAll(QueryConstant.OFFSET_INFO, "offset " + limit * (page - 1));
			}
		} else {
			this.nativeSql = nativeSql.concat(" limit " + limit + " offset " + limit * (page - 1));
		}
		return this;
	}

	@Override
	protected Query parser() {
		return entityClz == null ? entityMgr.createNativeQuery(nativeSql) :
				entityMgr.createNativeQuery(nativeSql, entityClz);
	}

	@Override
	protected Query parserCount() {
		String countSql = nativeSqlWithoutPageInfo.replaceAll("select.*from", "select count(1) from");
		return entityMgr.createNativeQuery(countSql, Long.class);
	}

	private List<T> query() {
		if (getCacheResult().size() > 0) {
			return getCacheResult();
		}
		Query query = parser();
		return query.getResultList();
	}

	private Long queryCount() {
		Query query = parserCount();
		return (Long) query.getResultList().get(0);
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
		return isPaged ? new PageImpl<>(query()) :
				new PageImpl<>(query(), PageRequest.of(page, limit), queryCount());
	}
}
