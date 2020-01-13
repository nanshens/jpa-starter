package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import ns.boot.jpa.starter.util.FindUtils;
import ns.boot.jpa.starter.util.QueryUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ns
 */

public class JpaJsonQuery<T> extends BaseJpaQuery<T>{
	private JSONObject jsonQuery;
	private Map<String, List<String>> column;
	private Map<String, String> sort;

	protected JpaJsonQuery(Class<T> entityClz, EntityManager entityMgr) {
		super(entityClz, entityMgr);
	}

	public JpaJsonQuery<T> input(JSONObject jsonQuery){
		this.jsonQuery = jsonQuery;
		return this;
	}

	@Override
	protected TypedQuery<T> parser() {
		//		Map<String, Map> queryJsonMap = jsonQuery.toJavaObject(Map.class);
		Map<String, Field> queryFields = QueryUtils.getClassField(entityClz);

		CriteriaBuilder cb = entityMgr.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClz);
		Root<T> root = cq.from(entityClz);
		Map<String, Integer> pageable = (Map) jsonQuery.get("@page");

		if (pageable != null) {
			setPageInfo(pageable.get("page"), pageable.get("limit"));
		}

		column = (Map) jsonQuery.get("@column");
		sort = (LinkedHashMap) jsonQuery.get("@sort");

		jsonQuery.remove("@page");
		jsonQuery.remove("@sort");
		jsonQuery.remove("@column");

		List<Predicate> predicates = FindUtils.buildPredicate(jsonQuery, cb, root, queryFields);
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		if (sort != null) {
			FindUtils.buildSort(sort, cq, cb, root);
		}

		return entityMgr.createQuery(cq);
	}

	@Override
	protected TypedQuery<Long> parserCount() {
		Map<String, Field> queryFields = QueryUtils.getClassField(entityClz);
		CriteriaBuilder cb = entityMgr.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<T> root = cq.from(entityClz);

		jsonQuery.remove("@page");
		jsonQuery.remove("@sort");
		jsonQuery.remove("@column");
		List<Predicate> predicates = FindUtils.buildPredicate(jsonQuery, cb, root, queryFields);
		cq.where(predicates.toArray(new Predicate[predicates.size()]));
		cq.select(cb.count(root));

		return entityMgr.createQuery(cq);
	}

	private Long queryCount() {
		List<Long> totals = parserCount().getResultList();
		long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}

	private List<T> query() {
		if (getCacheResult().size() > 0) {
			return getCacheResult();
		}
		TypedQuery<T> query = parser();
		if (isPaged) {
			query.setFirstResult((page - 1) * limit)
					.setMaxResults(limit);
//			FindUtils.buildPage(query, page, limit);
		}
		return query.getResultList();
	}
	@Override
	public JSON resultJson() {
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();

		if (column != null) {
			FindUtils.buildJsonFilter(filter, column);
		}
		return new JSONArray(query()
				.stream()
				.map(r -> JSONObject.parse(JSONObject.toJSONString(r, filter)))
				.collect(Collectors.toList()));
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
