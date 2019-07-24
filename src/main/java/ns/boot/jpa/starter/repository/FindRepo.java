package ns.boot.jpa.starter.repository;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
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

		List<Class<?>> targetCls = new ArrayList<>();


		Reflections reflections = new Reflections(url);
		Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Entity.class);

		for (Class<?> aClass : classSet) {
			jso.forEach((k, v) -> {
				if (aClass.getSimpleName().equals(k)){
					targetCls.add(aClass);
				}
			});
		}
		String s = ((Map)jso.get("Customer")).get("code").toString();

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<?> cq = cb.createQuery(targetCls.get(0));
		Root<?> root = cq.from(targetCls.get(0));
		cq.where(cb.equal(root.get("code"), s));
		List<?> result = entityManager.createQuery(cq).getResultList();

		System.out.println(result);

//
//
//		Root<Object> root = cq.from(Object.class);
//		root.alias("a");
//		cq.multiselect(root.get("status").alias("status"));
//		Predicate where = cb.conjunction();
//		cq.where(where).groupBy(root.get("status"));
//		List<Tuple> tuples = entityManager.createQuery(cq).getResultList();
	}
}
