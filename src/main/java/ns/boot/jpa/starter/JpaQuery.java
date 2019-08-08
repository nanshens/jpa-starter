package ns.boot.jpa.starter;

import lombok.SneakyThrows;
import ns.boot.jpa.starter.entity.QueryFilter;
import ns.boot.jpa.starter.entity.QueryJoin;
import ns.boot.jpa.starter.entity.QueryOrder;
import ns.boot.jpa.starter.enums.JoinParams;
import ns.boot.jpa.starter.enums.MatchType;
import ns.boot.jpa.starter.utils.QueryUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author acer
 * @date 2018/7/30
 */
public class JpaQuery<T> implements Specification<T> {

	enum Condition {Or, And}

	private List<QueryFilter> andFilters = new ArrayList<>();
	private List<QueryFilter> orFilters = new ArrayList<>();
	private List<QueryJoin> joinFilters = new ArrayList<>();
	private List<QueryOrder> queryOrders = new ArrayList<>();
	private Map<String, Join> joinMap = new HashMap<>();
//	private static Map<Enum, Method> parseJoinMap = new HashMap<>();
	private Map<String, Object> queryInfo;

//	static {
//		initParseMap();
//	}

	public JpaQuery<T> and(QueryFilter... queryFilters) {
		andFilters.addAll(Arrays.asList(queryFilters));
		return this;
	}

	public JpaQuery<T> or(QueryFilter... queryFilters) {
		orFilters.addAll(Arrays.asList(queryFilters));
		return this;
	}

	public JpaQuery<T> leftJoin(String tableName) {
		joinFilters.add(QueryJoin.leftJoin(tableName));
		return this;
	}

	public JpaQuery<T> leftListJoin(String tableName) {
		joinFilters.add(QueryJoin.leftListJoin(tableName));
		return this;
	}

	public JpaQuery<T> leftSetJoin(String tableName) {
		joinFilters.add(QueryJoin.leftSetJoin(tableName));
		return this;
	}

	public JpaQuery<T> leftMapJoin(String tableName) {
		joinFilters.add(QueryJoin.leftMapJoin(tableName));
		return this;
	}

	public JpaQuery<T> rightJoin(String tableName) {
		joinFilters.add(QueryJoin.rightJoin(tableName));
		return this;
	}

	public JpaQuery<T> rightListJoin(String tableName) {
		joinFilters.add(QueryJoin.rightListJoin(tableName));
		return this;
	}

	public JpaQuery<T> rightSetJoin(String tableName) {
		joinFilters.add(QueryJoin.rightSetJoin(tableName));
		return this;
	}

	public JpaQuery<T> rightMapJoin(String tableName) {
		joinFilters.add(QueryJoin.rightMapJoin(tableName));
		return this;
	}

	public JpaQuery<T> innerJoin(String tableName) {
		joinFilters.add(QueryJoin.innerJoin(tableName));
		return this;
	}

	public JpaQuery<T> innerListJoin(String tableName) {
		joinFilters.add(QueryJoin.innerListJoin(tableName));
		return this;
	}

	public JpaQuery<T> innerSetJoin(String tableName) {
		joinFilters.add(QueryJoin.innerSetJoin(tableName));
		return this;
	}

	public JpaQuery<T> innerMapJoin(String tableName) {
		joinFilters.add(QueryJoin.innerMapJoin(tableName));
		return this;
	}

	public JpaQuery<T> order(QueryOrder... orders) {
		queryOrders.addAll(Arrays.asList(orders));
		return this;
	}

	public JpaQuery<T> clearAllFilters() {
		clearAndFilters();
		clearOrFilters();
		return this;
	}

	public JpaQuery<T> clearAndFilters() {
		andFilters.clear();
		return this;
	}

	public JpaQuery<T> clearOrFilters() {
		orFilters.clear();
		return this;
	}

	public JpaQuery<T> clearOrders() {
		queryOrders.clear();
		return this;
	}

	public JpaQuery<T> addQueryInfo(Object o) {
		return this;
	}

	public JpaQuery<T> removeFilters(String... names) {
		for (String name : names) {
			andFilters.removeIf(af -> af.getName().equals(name));
			orFilters.removeIf(of -> of.getName().equals(name));
		}
		return this;
	}

	public JpaQuery<T> rmAndFilter(String name, int index) {
		List<Integer> ids = new ArrayList<>();
		for (int i = 0; i < andFilters.size(); i++) {
			if (andFilters.get(i).getName().equals(name)) {
				ids.add(i);
			}
		}
		andFilters.remove(ids.get(index));
		return this;
	}

	public JpaQuery<T> rmOrFilter(String name, int index) {
		List<Integer> ids = new ArrayList<>();
		for (int i = 0; i < orFilters.size(); i++) {
			if (orFilters.get(i).getName().equals(name)) {
				ids.add(i);
			}
		}
		orFilters.remove(ids.get(index));
		return this;
	}

	private JpaQuery<T> removeNullFilters() {
		andFilters.removeIf(af -> af.getName().equals(null) || af.getName().equals(""));
		orFilters.removeIf(of -> of.getName().equals(null) || of.getName().equals(""));
		return this;
	}

	public JpaQuery(Object object) {
//		queryInfoObject = object;
//		getPageInfo(object);
//		buildQueryParams(object);
//		buildFilterValue(object);

		queryInfo = QueryUtils.objectMap(object);
	}

	public JpaQuery() {
	}

//	@SneakyThrows
//	private static void initParseMap() {
//
//		for (JoinParams joinParams : JoinParams.getAllJoinParams()) {
//			parseJoinMap.put(joinParams, Root.class.getMethod(joinParams.getRootName(), String.class, JoinType.class));
//		}
//	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		Predicate predicate = null;
		buildQueryFilter();
//		addJoin(joinFilters, root);
//		removeNullFilters();

//		criteriaQuery.multiselect(root.get("status"));
//		criteriaQuery.groupBy(root.get("status"));


//		1.select * from customer
//		where code > '3' or name='2' or address_id ='1';
//
//		2.select * from customer
//		where code > '3' and name='2' and address_id ='1';
//
//		3.select * from customer
//		where code > '3' or (name='2' and address_id ='1');
//
//		4.select * from customer
//		where code < '4' and (name='2' or address_id ='1');

		buildSort(root, criteriaQuery, criteriaBuilder);
		if (andFilters.size() > 0 && orFilters.size() == 0) {
			return parseFilters(andFilters, criteriaBuilder, root, predicate, Condition.And);
		} else if (andFilters.size() == 0 && orFilters.size() > 0) {
			return parseFilters(orFilters, criteriaBuilder, root, predicate, Condition.Or);
		} else if (andFilters.size() > 0){
			return parseFilters(andFilters, criteriaBuilder, root, parseFilters(orFilters, criteriaBuilder, root, predicate, Condition.Or), Condition.And);
		}else {
			return predicate;
		}
	}

	private Predicate chooseOrAnd(Predicate basicPredicate, Predicate newPredicate, CriteriaBuilder cb, Enum type) {
		return basicPredicate == null ?
				newPredicate :
				type == Condition.And ?
						cb.and(basicPredicate, newPredicate) :
						cb.or(basicPredicate, newPredicate);
	}

//	@SneakyThrows
//	private void addJoin(List<QueryJoin> queryJoins, Root<T> root) {
//		for (QueryJoin queryJoin : queryJoins) {
//			joinMap.put(queryJoin.getTable(), (Join) parseJoinMap.get(queryJoin.getJoinParams()).invoke(root, queryJoin.getTable(), queryJoin.getJoinType()));
//		}
//	}

	private Path buildPath(String paramsName, Root<T> root) {
		String[] params = paramsName.split("\\.");
		return build(params, 1, root.get(params[0]));
	}

	private Path build(String[] params, int i, Path path) {
		return params.length > i ? build(params, i + 1, path.get(params[i])) : path;
	}

	private void buildSort(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		queryOrders.forEach(order -> {
			if (order.getDirection().equals(Sort.Direction.ASC)) {
				cq.orderBy(cb.asc(buildPath(order.getName(), root)));
			} else {
				cq.orderBy(cb.desc(buildPath(order.getName(), root)));
			}
		});
	}

	private Join findJoin(Root root, String joinName) {
		return (Join) root.getJoins().stream().filter(join -> joinName.equals(((Join) join).getAlias()))
				.findAny()
				.orElse(null);
	}

	@SneakyThrows
	public Predicate buildPredicate(QueryFilter queryFilter, Root<T> root, CriteriaBuilder cb) {
		Path path = buildPath(queryFilter.getName(), root);
		switch (queryFilter.getType()) {
			case EQ:
				return cb.equal(path, queryFilter.getName());
			case NE:
				return cb.notEqual(path, queryFilter.getName());
			case GT:
				return cb.greaterThan(path, (Comparable) queryFilter.getValue());
			case GE:
				return cb.greaterThanOrEqualTo(path, (Comparable) queryFilter.getValue());
			case LT:
				return cb.lessThan(path, (Comparable) queryFilter.getValue());
			case LE:
				return cb.lessThanOrEqualTo(path, (Comparable) queryFilter.getValue());
			case BETWEEN:
				List<Comparable> vs = (ArrayList) queryFilter.getValue();
				return cb.between(path, vs.get(0), vs.get(1));
			case LIKE:
				return cb.like(path, (String) queryFilter.getValue());
			case NOT_LIKE:
				return cb.notLike(path, (String) queryFilter.getValue());
			case IN:
				return path.in(queryFilter.getValue());
			case NOT_IN:
				return cb.not(path.in(queryFilter.getValue()));
			case IS_NULL:
				return cb.isNull(path);
			case IS_NOT_NULL:
				return cb.isNotNull(path);
			default:
				return cb.equal(path, queryFilter.getName());
		}
	}

	private Predicate parseFilters(List<QueryFilter> queryParams, CriteriaBuilder cb, Root<T> root, Predicate predicate, Enum type) {
		for (QueryFilter queryFilter : queryParams) {
			predicate = chooseOrAnd(predicate, buildPredicate(queryFilter, root, cb), cb, type);
		}
		return predicate;
	}

	public void autoJoin() {
		//base on ns.boot.jpa.utils.queryinfo field auto join
	}

	public void buildQueryFilter() {
		if (queryInfo != null) {
			andFilters.addAll((List<QueryFilter>) queryInfo.get("andFilters"));
			queryOrders.addAll((List<QueryOrder>) queryInfo.get("orders"));
		}
	}
}
