package ns.boot.jpa.starter;

import lombok.SneakyThrows;
import ns.boot.jpa.starter.entity.QueryFilter;
import ns.boot.jpa.starter.entity.QueryJoin;
import ns.boot.jpa.starter.entity.QueryOrder;
import ns.boot.jpa.starter.util.QueryUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author acer
 * @date 2018/7/30
 */
public class CustomQueryTemp<T> implements Specification<T> {

	private List<QueryFilter> andFilters = new ArrayList<>();
	private List<QueryFilter> orFilters = new ArrayList<>();
	private List<QueryFilter> whereFilters = new ArrayList<>();
	private List<QueryJoin> joinFilters = new ArrayList<>();
	private List<QueryOrder> queryOrders = new ArrayList<>();
	private Map<String, Join> joinMap = new HashMap<>();
//	private static Map<Enum, Method> parseJoinMap = new HashMap<>();
	private Map<String, Object> queryInfo;

	public CustomQueryTemp<T> and(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (Objects.isNull(queryFilter.getValue())) {
				continue;
			}
//			queryFilter.setChildQuery(false);
			queryFilter.setConditionEnum(Predicate.BooleanOperator.AND);
			whereFilters.add(queryFilter);
		}

		andFilters.addAll(Arrays.asList(queryFilters));
		return this;
	}

	public CustomQueryTemp<T> or(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (Objects.isNull(queryFilter.getValue())) {
				continue;
			}
//			queryFilter.setChildQuery(false);
			queryFilter.setConditionEnum(Predicate.BooleanOperator.OR);
			whereFilters.add(queryFilter);
		}
		orFilters.addAll(Arrays.asList(queryFilters));
		return this;
	}

	public CustomQueryTemp<T> childAnd(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
//			queryFilter.setChildQuery(true);
			queryFilter.setConditionEnum(Predicate.BooleanOperator.AND);
			whereFilters.add(queryFilter);
		}
		return this;
	}

	public CustomQueryTemp<T> childOr(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
//			queryFilter.setChildQuery(true);
			queryFilter.setConditionEnum(Predicate.BooleanOperator.OR);
			whereFilters.add(queryFilter);
		}
		return this;
	}

	public CustomQueryTemp<T> leftJoin(String tableName) {
		joinFilters.add(QueryJoin.leftJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> leftListJoin(String tableName) {
		joinFilters.add(QueryJoin.leftListJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> leftSetJoin(String tableName) {
		joinFilters.add(QueryJoin.leftSetJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> leftMapJoin(String tableName) {
		joinFilters.add(QueryJoin.leftMapJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> rightJoin(String tableName) {
		joinFilters.add(QueryJoin.rightJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> rightListJoin(String tableName) {
		joinFilters.add(QueryJoin.rightListJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> rightSetJoin(String tableName) {
		joinFilters.add(QueryJoin.rightSetJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> rightMapJoin(String tableName) {
		joinFilters.add(QueryJoin.rightMapJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> innerJoin(String tableName) {
		joinFilters.add(QueryJoin.innerJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> innerListJoin(String tableName) {
		joinFilters.add(QueryJoin.innerListJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> innerSetJoin(String tableName) {
		joinFilters.add(QueryJoin.innerSetJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> innerMapJoin(String tableName) {
		joinFilters.add(QueryJoin.innerMapJoin(tableName));
		return this;
	}

	public CustomQueryTemp<T> order(QueryOrder... orders) {
		queryOrders.addAll(Arrays.asList(orders));
		return this;
	}

	public CustomQueryTemp<T> clearAllFilters() {
		clearAndFilters();
		clearOrFilters();
		return this;
	}

	public CustomQueryTemp<T> clearAndFilters() {
		andFilters.clear();
		return this;
	}

	public CustomQueryTemp<T> clearOrFilters() {
		orFilters.clear();
		return this;
	}

	public CustomQueryTemp<T> clearOrders() {
		queryOrders.clear();
		return this;
	}

	public CustomQueryTemp<T> addQueryInfo(Object o) {
		return this;
	}

//	public JpaQuery<T> removeFilters(String... names) {
//		for (String name : names) {
//			andFilters.removeIf(af -> af.getName().equals(name));
//			orFilters.removeIf(of -> of.getName().equals(name));
//		}
//		return this;
//	}

	public CustomQueryTemp<T> removeFiltersIn(String... names) {
		for (String name : names) {
			whereFilters.removeIf(af -> af.getName().equals(name));
		}
		return this;
	}

	public CustomQueryTemp<T> removeFiltersEx(String... names) {
		for (String name : names) {
			whereFilters.removeIf(af -> !af.getName().equals(name));
		}
		return this;
	}

	public CustomQueryTemp<T> rmAndFilter(String name, int index) {
		List<Integer> ids = new ArrayList<>();
		for (int i = 0; i < andFilters.size(); i++) {
			if (andFilters.get(i).getName().equals(name)) {
				ids.add(i);
			}
		}
		andFilters.remove(ids.get(index));
		return this;
	}

	public CustomQueryTemp<T> rmOrFilter(String name, int index) {
		List<Integer> ids = new ArrayList<>();
		for (int i = 0; i < orFilters.size(); i++) {
			if (orFilters.get(i).getName().equals(name)) {
				ids.add(i);
			}
		}
		orFilters.remove(ids.get(index));
		return this;
	}

	private CustomQueryTemp<T> removeNullFilters() {
//		andFilters.removeIf(af -> af.getValue().equals(null) || af.getValue().equals(""));
//		orFilters.removeIf(of -> of.getValue().equals(null) || of.getValue().equals(""));
		whereFilters.removeIf(wf -> wf.getValue().equals(null) || wf.getValue().equals("") || wf.getValue().equals("%%"));
		return this;
	}

	public CustomQueryTemp(Object object) {
//		queryInfoObject = object;
//		getPageInfo(object);
//		buildQueryParams(object);
//		buildFilterValue(object);

		queryInfo = QueryUtils.objectMap(object);
	}

	public CustomQueryTemp() {
	}

//	@SneakyThrows
//	private static void initParseMap() {
//
//		for (JoinParams joinParams : JoinParams.getAllJoinParams()) {
//			parseJoinMap.put(joinParams, Root.class.getMethod(joinParams.getRootName(), String.class, JoinType.class));
//		}
//	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		Predicate predicate = null;
//		buildQueryFilter();
//		addJoin(joinFilters, root);
		removeNullFilters();

//		criteriaQuery.multiselect(root.get("status"));
//		criteriaQuery.groupBy(root.get("status"));

		buildSort(root, cq, cb);
//		if (andFilters.size() > 0 && orFilters.size() == 0) {
//			return parseFilters(andFilters, criteriaBuilder, root, predicate, Condition.And);
//		} else if (andFilters.size() == 0 && orFilters.size() > 0) {
//			return parseFilters(orFilters, criteriaBuilder, root, predicate, Condition.Or);
//		} else if (andFilters.size() > 0){
//			return parseFilters(andFilters, criteriaBuilder, root, parseFilters(orFilters, criteriaBuilder, root, predicate, Condition.Or), Condition.And);
//		}else {
//			return predicate;
//		}

//		Predicate child1 = cb.and(cb.equal(root.get("name"), "3"),
//				cb.equal(root.get("code"), "3"));
//		Predicate child2 = cb.and(cb.equal(root.get("name"), "3"),
//				cb.equal(root.get("code"), "3"));

//		predicate = cb.or(child1, child2);

//		return predicate;
		return buildPredicate(root, cb);
	}

	/* new build predicate logic
	 * 1. base on last filter to select on and or
	 * 2. childor and childand
	 * 3. bug no bracket in and, or
	 *
	 * */
	private Predicate buildPredicate(Root<T> root, CriteriaBuilder cb) {
		Predicate predicate = null;
//		Predicate childPredicate = null;
//		Predicate.BooleanOperator childConditionEnum = null;
//		for (int i = 0; i < whereFilters.size(); i++) {
//			QueryFilter qf = whereFilters.get(i);
//			if (i == 0) {
//				predicate = buildPredicate(qf, root, cb);
//			} else {
//				QueryFilter lastqf = whereFilters.get(i - 1);
//				if (qf.isChildQuery()) {
//					if (!lastqf.isChildQuery()) {
//						childConditionEnum = lastqf.getConditionEnum();
//						childPredicate = buildPredicate(qf, root, cb);
//					} else {
//						if (lastqf.getConditionEnum() == qf.getConditionEnum()) {
//							childPredicate = selectCondition(childPredicate, buildPredicate(qf, root, cb), cb, qf.getConditionEnum());
//						}else {
////							childand -childor
//						}
//						if (i == whereFilters.size() - 1) {
//							predicate = selectCondition(predicate, childPredicate, cb, childConditionEnum);
//						}
//					}
//				}else{
//					if (lastqf.isChildQuery()) {
//						predicate = selectCondition(predicate, childPredicate, cb, childConditionEnum);
//						predicate = selectCondition(predicate, buildPredicate(qf, root, cb), cb, qf.getConditionEnum());
//						childPredicate = null;
//					}else {
//						predicate = selectCondition(predicate, buildPredicate(qf, root, cb), cb, qf.getConditionEnum());
//					}
//				}
//			}
//		}
		return predicate;
	}

	public Predicate selectCondition(Predicate basicPredicate, Predicate newPredicate, CriteriaBuilder cb, Predicate.BooleanOperator conditionEnum) {
		return conditionEnum == Predicate.BooleanOperator.AND ? cb.and(basicPredicate, newPredicate) : cb.or(basicPredicate, newPredicate);
	}

	private Predicate chooseOrAnd(Predicate basicPredicate, Predicate newPredicate, CriteriaBuilder cb, Enum type) {
		return basicPredicate == null ?
				newPredicate :
				type == Predicate.BooleanOperator.AND ?
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
				return cb.equal(path, queryFilter.getValue());
			case EQ_IG_CASE:
				return cb.equal(cb.lower(path), ((String)queryFilter.getValue()).toLowerCase());
			case NE:
				return cb.notEqual(path, queryFilter.getValue());
			case GT:
				return cb.greaterThan(path, (Comparable) queryFilter.getValue());
			case GE:
				return cb.greaterThanOrEqualTo(path, (Comparable) queryFilter.getValue());
			case LT:
				return cb.lessThan(path, (Comparable) queryFilter.getValue());
			case LE:
				return cb.lessThanOrEqualTo(path, (Comparable) queryFilter.getValue());
			case LIKE:
				return cb.like(path, (String) queryFilter.getValue());
			case LIKE_IG_CASE:
				return cb.like(cb.lower(path), ((String)queryFilter.getValue()).toLowerCase());
			case NOT_LIKE:
				return cb.notLike(path, (String) queryFilter.getValue());
			case NOT_LIKE_IG_CASE:
				return cb.notLike(cb.lower(path), ((String)queryFilter.getValue()).toLowerCase());
			case IN:
				return path.in(queryFilter.getValue());
			case NOT_IN:
				return path.in(queryFilter.getValue()).not();
			case IS_NULL:
				return cb.isNull(path);
			case IS_NOT_NULL:
				return cb.isNotNull(path);
			case BETWEEN:
			default:
				List<Comparable> vs = (ArrayList) queryFilter.getValue();
				return cb.between(path, vs.get(0), vs.get(1));
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
