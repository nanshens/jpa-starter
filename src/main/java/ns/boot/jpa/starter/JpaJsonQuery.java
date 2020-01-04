package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import ns.boot.jpa.starter.enums.ExceptionEnum;
import ns.boot.jpa.starter.exception.JpaException;
import ns.boot.jpa.starter.property.JpaStarterProperties;
import ns.boot.jpa.starter.utils.FindUtils;
import ns.boot.jpa.starter.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.HashMap;
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
	private Map<String, Integer> pageable;
	private Map<String, String> sort;

	protected JpaJsonQuery(Class<T> entityClz, EntityManager entityMgr) {
		super(entityClz, entityMgr);
	}

	public JpaJsonQuery<T> input(JSONObject jsonQuery){
		this.jsonQuery = jsonQuery;
		return this;
	}

	private List<T> query() {

		Map<String, Map> queryJsonMap = jsonQuery.toJavaObject(Map.class);
		Map<String, Field> queryFields = QueryUtils.getClassField(entityClz);

		CriteriaBuilder cb = entityMgr.getCriteriaBuilder();
		CriteriaQuery<?> cq = cb.createQuery(entityClz);
		Root root = cq.from(entityClz);
		column = (Map) jsonQuery.get("@column");
		pageable = (Map) jsonQuery.get("@page");
		sort = (LinkedHashMap) jsonQuery.get("@sort");

		jsonQuery.remove("@page");
		jsonQuery.remove("@sort");
		jsonQuery.remove("@column");
		List<Predicate> predicates = FindUtils.buildPredicate(queryJsonMap, cb, root, queryFields);
		cq.where(predicates.toArray(new Predicate[predicates.size()]));

		if (sort != null) {
			FindUtils.buildSort(sort, cq, cb, root);
		}

		Query query = entityMgr.createQuery(cq);

		if (pageable != null) {
			FindUtils.buildPage(query, pageable.get("page"), pageable.get("limit"));
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
		return new PageImpl<>(query());
	}

}
