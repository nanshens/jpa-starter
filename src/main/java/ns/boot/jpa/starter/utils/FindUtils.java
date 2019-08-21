package ns.boot.jpa.starter.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import ns.boot.jpa.starter.enums.ExceptionEnum;
import ns.boot.jpa.starter.exception.JpaException;
import org.reflections.Reflections;

import javax.persistence.Entity;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * @author ns
 */

public class FindUtils {

	private final static String EQ= "=";
	private final static String NOT = "!";
	private final static String LIKE = "~";
	private final static String GT = ">";
	private final static String GE = ">=";
	private final static String LT = "<";
	private final static String LE = "<=";
	private final static String AND = "&";

	private final static String reg = "[\\!\\=\\<\\>\\&\\|\\~]+";

	public static List<Predicate> buildPredicate(Map fieldMap, CriteriaBuilder cb, Root<?> root, Map<String, Field> queryFields) throws JpaException {
		Stack<String> fields = new Stack<>();
		fields.addAll(fieldMap.keySet());

		List<Predicate> predicateList = new ArrayList<>();

		while (!fields.empty()) {
			String field = fields.pop();
			String[] fs = field.split("\\.");
			Object value = getObject(fieldMap, fs, 0);

			if (value instanceof LinkedHashMap) {
				for (Object o : ((Map) value).keySet()) {
					fields.push( field + "." + o.toString());
				}
			} else {
				try {
					value = buildValue(queryFields.get(clearSpecChar(field)).getType(), value);
				} catch (NullPointerException e) {
					throw new JpaException(ExceptionEnum.NAME_ERROR.getMsg() + field);
				} catch (IllegalArgumentException | ParseException | DateTimeException e) {
					throw new JpaException(ExceptionEnum.VALUE_ERROR.getMsg() + field);
				}
				predicateList.add(selectPredicate(fs, value, cb, root));
			}
		}
		return predicateList;
	}

	public static void getTargetClass(String url, Map<String, Class<?>> targetCls, JSONObject queryJson) {

		Reflections reflections = new Reflections(url);
		Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Entity.class);

		for (Class<?> tClass : classSet) {
			queryJson.forEach((k, v) -> {
				if (tClass.getSimpleName().equals(k)){
					targetCls.put(k, tClass);
				}
			});
		}
	}

	public static void buildSort(Map<String, String> sort, CriteriaQuery cq, CriteriaBuilder cb, Root root) throws JpaException{
		sort.forEach((name, direction) -> {
			String[] names = name.split("\\.");
			try {
				if (direction.equals("desc")) {
					cq.orderBy(cb.desc(buildPath(root, null, names, 0)));
				} else if (direction.equals("asc")) {
					cq.orderBy(cb.asc(buildPath(root, null, names, 0)));
				} else {
					throw new JpaException(ExceptionEnum.SORT_ERROR.getMsg() + name);
				}
			} catch (IllegalArgumentException e) {
				throw new JpaException(ExceptionEnum.SORT_ERROR.getMsg() + name);
			}
		});
	}

	public static void buildPage(Query query, Integer page, Integer limit) throws JpaException{
		if (page == null || limit == null){
			throw new JpaException(ExceptionEnum.PAGE_ERROR.getMsg());
		}
		query.setFirstResult((page - 1) * limit)
				.setMaxResults(limit);
	}

	public static void buildJsonFilter(SimplePropertyPreFilter filter, Map<String, List<String>> column) {
		if (column.get("include") != null) {
			column.get("include").forEach(i -> filter.getIncludes().add(i));
		}
		if (column.get("exclude") != null) {
			column.get("exclude").forEach(e -> filter.getExcludes().add(e));
		}
	}

	private static Predicate selectPredicate(String[] fs, Object o, CriteriaBuilder cb, Root<?> root) {
		String f = fs[fs.length - 1];
		Path path = buildPath(root, null, fs, 0);
		if (f.contains(AND)) {
			fs[fs.length - 1] = f.replace(AND, "");
			return selectPredicate(fs, o, cb, root);
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

	private static String clearSpecChar(String str) {
		return str.replaceAll(reg, str);
	}

	private static Object buildValue(Class fClass, Object o) throws ParseException {
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

	private static Object getObject(Map objectMap, String[] fs, int i) {
		return i < fs.length - 1 ? getObject((Map) objectMap.get(fs[i]), fs, i + 1) : objectMap.get(fs[i]);
	}

	private static Path buildPath(Root root, Path path, String[] fs, int i) {
		fs[fs.length - 1] = FindUtils.clearSpecChar(fs[fs.length - 1]);
		return i < fs.length ? buildPath(root, path == null ? root.get(fs[i]) : path.get(fs[i]), fs, i + 1) : path;
	}
}
