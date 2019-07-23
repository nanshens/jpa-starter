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
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author ns
 */

@Repository
public class FindRepo {
	@Autowired
	private EntityManager entityManager;

	@SneakyThrows
	public void find(JSONObject jso, String url) {

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

		System.out.println(targetCls);
		System.out.println(entityManager == null);
//
//		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//		CriteriaQuery<Tuple> cq = cb.createTupleQuery();
//
//
//
//		Root<?> root1 = cq.from(targetCls.get(0));
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
