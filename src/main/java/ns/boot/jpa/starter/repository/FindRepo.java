package ns.boot.jpa.starter.repository;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.SneakyThrows;
import ns.boot.jpa.starter.entity.QueryFilter;
import ns.boot.jpa.starter.utils.QueryUtils;
import org.apache.logging.log4j.util.Strings;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @author ns
 */

@Repository
public class FindRepo {

	@SneakyThrows
	public List find(JSONObject jso, String url, EntityManager entityManager) {
		List result = new ArrayList();
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
//			CriteriaQuery<?> cq = cb.createTupleQuery();
			CriteriaQuery<?> cq = cb.createQuery(targetCls.get(entity));
			Root<?> root = cq.from(targetCls.get(entity));

//			cq.multiselect(root.get("code"), root.get("name"));
			cq.where(buildPredicate(fieldMap, cb, root, null));
			List<?> resultList = entityManager.createQuery(cq).getResultList();
			System.out.println(resultList);
			result.add(resultList);
		}
		return result;
//
//
//		Root<Object> root = cq.from(Object.class);
//		root.alias("a");
//		cq.multiselect(root.get("status").alias("status"));
//		Predicate where = cb.conjunction();
//		cq.where(where).groupBy(root.get("status"));
//		List<Tuple> tuples = entityManager.createQuery(cq).getResultList();
	}

	public Predicate buildPredicate(Map fieldMap, CriteriaBuilder cb, Root<?> root, Predicate predicate) {
		Stack<String> fields = new Stack<>();
		fields.addAll(fieldMap.keySet());

		while (!fields.empty()) {
			String field = fields.pop();
			String[] fs = field.split("\\.");
			Object value = getObject(fieldMap, fs, 0);

			if (value instanceof LinkedHashMap) {
				for (Object o : ((Map) value).keySet()) {
					fields.push(field + "." + o.toString());
				}
			} else {
				predicate = predicate == null ?
						getPredicate(fs, value, cb, root) :
						cb.and(predicate, getPredicate(fs, value, cb, root));
			}
		}
		return predicate;
	}

	public Object getObject(Map objectMap, String[] fs, int i) {
		return i < fs.length - 1 ? getObject((Map) objectMap.get(fs[i]), fs, i + 1) : objectMap.get(fs[i]);
	}

	public Path getPath(Root root, Path path, String[] fs, int i) {
		return i < fs.length ? getPath(root, path == null ? root.get(fs[i]) : path.get(fs[i]), fs, i + 1) : path;
	}


	public Predicate getPredicate(String[] fs, Object o, CriteriaBuilder cb, Root<?> root) {
		String f = fs[fs.length - 1];
		if (f.contains("&")) {
			fs[fs.length - 1] = f.replace("&", "");
			return getPredicate(fs, o, cb, root);
		} else if (f.contains("!")) {
//			not in,not like :not realize
			fs[fs.length - 1] = f.replace("!", "");
			Path finPath = getPath(root, null, fs, 0);
			if (o == null) {
				return cb.isNotNull(finPath);
			}else {
				return cb.notEqual(finPath, o);
			}
		} else if (f.contains("~")) {
			fs[fs.length - 1] = f.replace("~", "");
			Path finPath = getPath(root, null, fs, 0);
			return cb.like(finPath, o.toString());
		} else {
			Path finPath = getPath(root, null, fs, 0);
			if (o instanceof ArrayList) {
				return finPath.in((ArrayList) o);
			} else {
				if (o == null) {
					return cb.isNull(finPath);
				} else {
					String v = o.toString();
					if (v.contains("<=")) {
						return cb.lessThanOrEqualTo(finPath, v.replace("<=", ""));
					} else if (v.contains(">=")) {
						return cb.greaterThanOrEqualTo(finPath, v.replace(">=", ""));
					} else if (v.contains("<")) {
						return cb.lessThan(finPath, v.replace("<", ""));
					} else if (v.contains(">")) {
						return cb.greaterThan(finPath, v.replace(">", ""));
					} else {
						return cb.equal(finPath, o);
					}
				}
			}
		}
	}
}
