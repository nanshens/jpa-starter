package ns.boot.jpa.starter.repository;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import ns.boot.jpa.starter.entity.QueryFilter;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ns
 */

@Repository
public class FindRepo {

	@SneakyThrows
	public void find(JSONObject jso, String url, EntityManager entityManager) {

		Map jsoMap = jso.toJavaObject(Map.class);

		Map<String, Class<?>> targetCls = new HashMap<>();

		Reflections reflections = new Reflections(url);
		Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Entity.class);

		for (Class<?> aClass : classSet) {
			jso.forEach((k, v) -> {
				if (aClass.getSimpleName().equals(k)){
					targetCls.put(k, aClass);
				}
			});
		}

		Iterator it = jsoMap.keySet().iterator();

		while (it.hasNext()) {
			String entity = it.next().toString();
			Map fieldMap = ((Map)jsoMap.get(entity));
			Iterator fields = fieldMap.keySet().iterator();

			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<?> cq = cb.createQuery(targetCls.get(entity));
			Root<?> root = cq.from(targetCls.get(entity));
			Predicate predicate = null;

			while (fields.hasNext()) {
				String field = fields.next().toString();
				if (predicate == null) {
					predicate = cb.equal(root.get(field), fieldMap.get(field));
				}else {
					predicate = cb.and(predicate, cb.equal(root.get(field), fieldMap.get(field)));
				}
			}

			cq.where(predicate);
			List<?> result = entityManager.createQuery(cq).getResultList();
			System.out.println(result);
		}



//
//
//		Root<Object> root = cq.from(Object.class);
//		root.alias("a");
//		cq.multiselect(root.get("status").alias("status"));
//		Predicate where = cb.conjunction();
//		cq.where(where).groupBy(root.get("status"));
//		List<Tuple> tuples = entityManager.createQuery(cq).getResultList();
	}

	public Predicate getType(String f, Object o, CriteriaBuilder cb, Root<?> root) {
		if (f.contains("!")) {
//			not in , not equal, not like , not null
			return cb.notEqual(root.get(f), o);
		} else if (f.contains("~")) {
//			like %aa, aa%, %aaa%, ~~
			return cb.like(root.get(f), o.toString());
		} else {
//			equal > < && in null
		}
	}
}
