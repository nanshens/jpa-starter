package ns.boot.jpa.starter.service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import ns.boot.jpa.starter.enums.ExceptionEnum;
import ns.boot.jpa.starter.exception.JpaException;
import ns.boot.jpa.starter.property.JpaStarterProperties;
import ns.boot.jpa.starter.result.Result;
import ns.boot.jpa.starter.utils.FindUtils;
import ns.boot.jpa.starter.utils.QueryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

@Service
public class FindService {
	@Autowired
	private JpaStarterProperties properties;
	@Autowired
	private EntityManager entityManager;
	/**
	 *todo:
	 * 1. parser json
	 * 2. create conditions
	 * 3. get result
	 * 4. format result json
	 * 5. exception handle
	 */
	public Result find(JSONObject queryJson) {
		Result result = new Result();
		long s = System.currentTimeMillis();
		JSONObject resultJson = new JSONObject();
		try {

//	************************ get entity class and object ***************
			Map<String, Map> queryJsonMap = queryJson.toJavaObject(Map.class);
			Map<String, Class<?>> targetCls = new HashMap<>(1);
			FindUtils.getTargetClass(properties.getBaseUrl(), targetCls, queryJson);
			if (targetCls.size() == 0) {
				throw new JpaException(ExceptionEnum.ENTITY_ERROR.getMsg());
			}

//	************************ build result ***************
			buildResult(queryJsonMap, targetCls, resultJson);
			result.successResult(resultJson);
		} catch (JpaException e) {
			result.failResult(e.getMessage());
		}
		long e = System.currentTimeMillis();
		System.out.println(e-s);
		return result;
	}

	private void buildResult(Map<String, Map> queryJsonMap, Map<String, Class<?>> targetCls, JSONObject resultJson) throws JpaException{
		queryJsonMap.forEach((qName, qInfo) -> {
//	************************ create base field ****************************
			Class<?> entityClass = targetCls.get(qName);
			Map<String, Field> queryFields = QueryUtils.getClassField(entityClass);

			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<?> cq = cb.createQuery(entityClass);
			Root root = cq.from(entityClass);

//			tuple query
//			CriteriaQuery<?> cq = cb.createTupleQuery();
//			cq.multiselect(root.get("code"), root.get("name"));

//		Root<Object> root = cq.from(Object.class);
//		root.alias("a");
//		cq.multiselect(root.get("status").alias("status"));
//		Predicate where = cb.conjunction();
//		cq.where(where).groupBy(root.get("status"));
//		List<Tuple> tuples = entityManager.createQuery(cq).getResultList();


//			get  column
//			"@column": {
//				"except": [],
//				"include": ["max(id):maxid"]
//			},

//	************************ get page sort column ************************
			Map<String, List<String>> column = (Map) qInfo.get("@column");
			Map<String, Integer> pageable = (Map) qInfo.get("@page");
			Map<String, String> sort = (LinkedHashMap) qInfo.get("@sort");

			qInfo.remove("@page");
			qInfo.remove("@sort");
			qInfo.remove("@column");

//	************************ build Predicate *****************************
			List<Predicate> predicates = FindUtils.buildPredicate(qInfo, cb, root, queryFields);
			cq.where(predicates.toArray(new Predicate[predicates.size()]));

//			cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

//	************** create query, build sort and page *********************
			try {

				if (sort != null) {
					FindUtils.buildSort(sort, cq, cb, root);
				}

				Query query = entityManager.createQuery(cq);

				if (pageable != null) {
					FindUtils.buildPage(query, pageable.get("page"), pageable.get("limit"));
				}

//	************************ get result list *****************************
				List<?> resultList = query.getResultList();

//	************************ build json by column ************************
				SimplePropertyPreFilter filter = new SimplePropertyPreFilter();

				if (column != null) {
					FindUtils.buildJsonFilter(filter, column);
				}

				resultList = resultList
						.stream()
						.map(r -> JSONObject.parse(JSONObject.toJSONString(r, filter)))
						.collect(Collectors.toList());

				resultJson.put(qName, resultList);
			}catch (JpaException e) {
				throw e;
			} catch (Exception e) {
				throw new JpaException(ExceptionEnum.VALUE_ERROR.getMsg());
			}
		});
	}

}

