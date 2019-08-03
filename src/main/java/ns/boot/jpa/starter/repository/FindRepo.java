package ns.boot.jpa.starter.repository;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import ns.boot.jpa.starter.utils.QueryUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author ns
 */

@Repository
public class FindRepo {

	private final static String EQ= "=";
	private final static String NOT = "!";
	private final static String LIKE = "~";
	private final static String GT = ">";
	private final static String GE = ">=";
	private final static String LT = "<";
	private final static String LE = "<=";
	private final static String AND = "&";

	public List find(JSONObject jso, String url, EntityManager entityManager) {

//	************************ get entity class *****************************
		List result = new ArrayList();
		Map jsoMap = jso.toJavaObject(Map.class);

		Map<String, Class<?>> targetCls = new HashMap<>(1);

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
			Map<String, Field> cfs = QueryUtils.getClassField(targetCls.get(entity));
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();

			CriteriaQuery<?> cq = cb.createQuery(targetCls.get(entity));
			Root<?> root = cq.from(targetCls.get(entity));
//			tuple query
//			CriteriaQuery<?> cq = cb.createTupleQuery();
//			cq.multiselect(root.get("code"), root.get("name"));

//			get  column
//			"@column": {
//				"except": [],
//				"include": ["max(id):maxid"]
//			},

//	************************ build page sort *****************************
			Map column = (Map) fieldMap.get("@column");
			Map pageable = (Map) fieldMap.get("@page");
			Map sort = (LinkedHashMap) fieldMap.get("@sort");

			fieldMap.remove("@page");
			fieldMap.remove("@sort");
			fieldMap.remove("@column");

//	************************ build Predicate *****************************

			List<Predicate> predicates = buildPredicate(fieldMap, cb, root, null, cfs);
			cq.where(predicates.toArray(new Predicate[predicates.size()]));
//			cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

//	************************ build sort and page *****************************

			if (sort != null) {
				sort.forEach((k, v) -> {
					String[] ks = k.toString().split("\\.");
					if (v.toString().equals("desc")){
						cq.orderBy(cb.desc(getPath(root, null, ks, 0)));
					}else {
						cq.orderBy(cb.asc(getPath(root, null, ks, 0)));
					}
				});

			}

			Query query = entityManager.createQuery(cq);

			if (pageable != null) {
				int page = (int)pageable.get("page");
				int limit = (int)pageable.get("limit");
				query.setFirstResult((page - 1) * limit)
				.setMaxResults(limit);
			}

//	************************ get result list *****************************

			List<?> resultList = query.getResultList();

//	************************ build json *****************************
			result.add(resultList);
		}
		return result;

//		Root<Object> root = cq.from(Object.class);
//		root.alias("a");
//		cq.multiselect(root.get("status").alias("status"));
//		Predicate where = cb.conjunction();
//		cq.where(where).groupBy(root.get("status"));
//		List<Tuple> tuples = entityManager.createQuery(cq).getResultList();
	}

	public List<Predicate> buildPredicate(Map fieldMap, CriteriaBuilder cb, Root<?> root, Predicate predicate, Map<String, Field> cfs) {
		Stack<String> fields = new Stack<>();
		fields.addAll(fieldMap.keySet());

		List<Predicate> predicateList = new ArrayList<>();

		while (!fields.empty()) {
			String field = fields.pop();
			String[] fs = field.split("\\.");
			Object value = getObject(fieldMap, fs, 0);

			if (value instanceof LinkedHashMap) {
				for (Object o : ((Map) value).keySet()) {
					fields.push(field + "." + o.toString());
				}
			} else {
				value = getValue(cfs.get(clearChar(field)).getType(), value);
//				predicate = predicate == null ?
////						getPredicate(fs, value, cb, root) :
////						cb.and(predicate, getPredicate(fs, value, cb, root));
				predicateList.add(getPredicate(fs, value, cb, root));
			}
		}

		return predicateList;
	}

	public Object getObject(Map objectMap, String[] fs, int i) {
		return i < fs.length - 1 ? getObject((Map) objectMap.get(fs[i]), fs, i + 1) : objectMap.get(fs[i]);
	}

	public Path getPath(Root root, Path path, String[] fs, int i) {
		fs[fs.length - 1] = clearChar(fs[fs.length - 1]);
		return i < fs.length ? getPath(root, path == null ? root.get(fs[i]) : path.get(fs[i]), fs, i + 1) : path;
	}

	public String clearChar(String str) {
		str = str.replace(LT, "")
				.replace(GT, "")
				.replace(EQ, "")
				.replace(LIKE, "")
				.replace(NOT, "")
				.replace(AND, "");
		return str;
	}

	@SneakyThrows
	public Predicate getPredicate(String[] fs, Object o, CriteriaBuilder cb, Root<?> root) {
		String f = fs[fs.length - 1];
		Path path = getPath(root, null, fs, 0);
		if (f.contains(AND)) {
			fs[fs.length - 1] = f.replace(AND, "");
			return getPredicate(fs, o, cb, root);
		} else if (f.contains(NOT)) {
			if (o == null) {
				return cb.isNotNull(path);
			} else if (o instanceof ArrayList) {
				return path.in(o).not();
			} else if (f.contains(LIKE)) {
				return cb.notLike(path, (String) o);
			} else {
				return cb.notEqual(path, o);
			}
		} else if (o instanceof ArrayList) {
			return path.in(o);
		} else if (f.contains(LIKE)) {
			return cb.like(path, (String) o);
		} else if (f.contains(LE)) {
			return cb.lessThanOrEqualTo(path, (Comparable) o);
		} else if (f.contains(GE)) {
			return cb.greaterThanOrEqualTo(path, (Comparable) o);
		} else if (f.contains(LT)) {
			return cb.lessThan(path, (Comparable) o);
		} else if (f.contains(GT)) {
			return cb.greaterThan(path, (Comparable) o);
		} else {
			if (o == null) {
				return cb.isNull(path);
			}else {
				return cb.equal(path, o);
			}
		}
	}

	@SneakyThrows
	public Object getValue(Class fClass, Object o) {
		if (fClass.isEnum()) {
			if (o instanceof ArrayList) {
				o = ((ArrayList<String>) o).stream().map(e -> Enum.valueOf(fClass, e)).collect(Collectors.toList());
			} else {
				o = Enum.valueOf(fClass, (String)o);
			}
		}

		if(fClass == LocalDate.class || fClass == LocalTime.class || fClass == LocalDateTime.class) {
			String v = (String)o;
			if (v.length() < 9) {
				o = LocalTime.parse(v);
			} else if (v.length() < 11) {
				o = LocalDate.parse(v);
			} else {
				o = LocalDateTime.parse(v);
			}
		}

		if (fClass == Date.class) {
			String v = (String)o;
			DateFormat format;
			if (v.length() < 9){
				format = new SimpleDateFormat("HH:mm:ss");
			} else if (v.length() < 11) {
				format = new SimpleDateFormat("yyyy-MM-dd");
			} else {
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			}
			o = format.parse(v);
		}
		return o;
	}
}
