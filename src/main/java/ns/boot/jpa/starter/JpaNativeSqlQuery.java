package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author ns
 */

public class JpaNativeSqlQuery<T> extends BaseJpaQuery<T>{
	private final static String LIMIT_INFO = "\blimit\\s*[0-9]\\d*\b";
	private final static String OFFSET_INFO = "\boffset\\s*[0-9]\\d*\b";
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
				.replaceAll(LIMIT_INFO, "")
				.replaceAll(OFFSET_INFO, "");
		if (!nativeSql.isEmpty() && !nativeSql.equals(nativeSqlWithoutPageInfo)) {
			// setPageInfo
		}
		return this;
	}

	public JpaNativeSqlQuery<T> page(int page, int limit) {
		setPageInfo(page, limit);
		this.nativeSql = nativeSql
				.replaceAll(LIMIT_INFO, "limit " + limit)
				.replaceAll(OFFSET_INFO, "offset " + limit * (page - 1));
		return this;
	}
	public List<T> cache() {
		return null;
	}

	@Override
	protected Query parser() {
		return entityClz == null ? entityMgr.createNativeQuery(nativeSql) :
				entityMgr.createNativeQuery(nativeSql, entityClz);
	}

	private List<T> query() {
		Query query = parser();
		return query.getResultList();
	}

	private Long queryCount() {
		String countSql = nativeSqlWithoutPageInfo.replaceAll("select.*from", "select count(1) from");
		Query query = entityMgr.createNativeQuery(countSql, Long.class);
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
