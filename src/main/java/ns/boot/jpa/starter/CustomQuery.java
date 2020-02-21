package ns.boot.jpa.starter;

import ns.boot.jpa.starter.entity.QueryFilter;
import ns.boot.jpa.starter.entity.QueryOrder;
import ns.boot.jpa.starter.util.QueryUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ns
 */

public class CustomQuery<T> implements Specification<T> {
	private List<QueryFilter> filters = new ArrayList<>();
	private List<QueryOrder> orders = new ArrayList<>();
	private Integer page;
	private Integer limit;
	private int subQueryNumber = 0;
	private boolean existOr = false;
	private boolean existAnd = false;

	private boolean existSub() {
		return subQueryNumber > 0;
	}

	protected int getPage() {
		return page;
	}

	protected int getLimit() {
		return limit;
	}

	protected void setPageInfo(int page, int limit) {
		this.page = page;
		this.limit = limit;
	}

	public CustomQuery<T> and(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (!QueryUtils.isNull(queryFilter.getValue())) {
				queryFilter.setSubQueryNumber(0);
				queryFilter.setConditionEnum(Predicate.BooleanOperator.AND);
				filters.add(queryFilter);
				existAnd = true;
			}
		}
		return this;
	}

	public CustomQuery<T> and() {
		QueryFilter queryFilter = QueryFilter.eq(null, null);
		queryFilter.setConditionEnum(Predicate.BooleanOperator.AND);
		filters.add(queryFilter);
		existAnd = true;
		return this;
	}

	public CustomQuery<T> or() {
		QueryFilter queryFilter = QueryFilter.eq(null, null);
		queryFilter.setConditionEnum(Predicate.BooleanOperator.OR);
		filters.add(queryFilter);
		existOr = true;
		return this;
	}

	public CustomQuery<T> or(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (!QueryUtils.isNull(queryFilter.getValue())) {
				queryFilter.setSubQueryNumber(0);
				queryFilter.setConditionEnum(Predicate.BooleanOperator.OR);
				filters.add(queryFilter);
				existOr = true;
			}
		}
		return this;
	}

//	public CustomQuery<T> forceAnd(QueryFilter... queryFilters) {
//		for (QueryFilter queryFilter : queryFilters) {
//			if (!QueryUtils.isNull(queryFilter.getValue())) {
//				queryFilter.setSubQueryNumber(subQueryNumber);
//				queryFilter.setConditionEnum(Predicate.BooleanOperator.AND);
//				filters.add(queryFilter);
//				subQueryNumber = subQueryNumber + 1;
//				existAnd = true;
//			}
//		}
//		return this;
//	}

	public CustomQuery<T> forceOr(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (!QueryUtils.isNull(queryFilter.getValue())) {
				subQueryNumber = subQueryNumber + 1;
				queryFilter.setSubQueryNumber(subQueryNumber);
				queryFilter.setConditionEnum(Predicate.BooleanOperator.OR);
				filters.add(queryFilter);
//				existOr = true;
			}
		}
		return this;
	}

	public CustomQuery<T> order(QueryOrder... queryOrders) {
		orders.addAll(Arrays.asList(queryOrders));
		return this;
	}

	private void buildSort(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		orders.forEach(order -> {
			if (order.getDirection().equals(Sort.Direction.ASC)) {
				cq.orderBy(cb.asc(buildPath(order.getName(), root)));
			} else {
				cq.orderBy(cb.desc(buildPath(order.getName(), root)));
			}
		});
	}

	private Path<T> buildPath(String paramsName, Root<T> root) {
		String[] params = paramsName.split("\\.");
		return build(params, 1, root.get(params[0]));
	}

	private Path<T> build(String[] params, int i, Path<T> path) {
		return params.length > i ? build(params, i + 1, path.get(params[i])) : path;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		buildSort(root, cq, cb);
		return filters.isEmpty() ? cb.conjunction() : buildPredicate(root, cb);
	}

	private Predicate buildPredicate(Root<T> root, CriteriaBuilder cb) {
		Predicate mainPredicate;
		if (existSub()) {
			mainPredicate = buildSubPredicates(existOr ? cb.disjunction() : cb.conjunction(), root, cb);
		} else {
			if (existOr) {
				mainPredicate = existAnd ? buildOrAndPredicates(cb.disjunction(), root, cb) :
						buildPredicates(cb.disjunction(), root, cb);
			} else {
				mainPredicate = buildPredicates(cb.conjunction(), root, cb);
			}
		}
		return mainPredicate;
	}

	private Predicate buildSubPredicates(Predicate mainPredicate, Root<T> root, CriteriaBuilder cb) {
		List<Predicate> subPredicates = new ArrayList<>(filters.size());
		Predicate subPredicate = cb.disjunction();
		for (QueryFilter filter : filters) {
			if (filter.getSubQueryNumber() > 0) {
				subPredicate.getExpressions().add(selectPredicate(filter, buildPath(filter.getName(), root), cb));
			} else {
				if (subPredicate.getExpressions().size() > 0){
					subPredicates.add(subPredicate);
					subPredicate = cb.disjunction();
				}
				if (filter.getName() != null && filter.getValue() != null) {
					subPredicates.add(selectPredicate(filter, buildPath(filter.getName(), root), cb));
				}
			}
		}
		if (subPredicate.getExpressions().size() > 0){
			subPredicates.add(subPredicate);
		}
		mainPredicate.getExpressions().addAll(subPredicates);
		return mainPredicate;
	}

	private Predicate buildOrAndPredicates(Predicate mainPredicate, Root<T> root, CriteriaBuilder cb) {
		List<Predicate> subPredicates = new ArrayList<>(filters.size());
		Predicate subPredicate = cb.conjunction();
		for (QueryFilter filter : filters) {
			if (filter.getConditionEnum() == Predicate.BooleanOperator.AND) {
				subPredicate.getExpressions().add(selectPredicate(filter, buildPath(filter.getName(), root), cb));
			} else {
				subPredicates.add(subPredicate);
				subPredicate = cb.conjunction();
				if (filter.getName() != null && filter.getValue() != null) {
					subPredicates.add(selectPredicate(filter, buildPath(filter.getName(), root), cb));
				}
			}
		}
		if (subPredicate.getExpressions().size() > 0){
			subPredicates.add(subPredicate);
		}
		mainPredicate.getExpressions().addAll(subPredicates);
		return mainPredicate;
	}

	private Predicate buildPredicates(Predicate mainPredicate, Root<T> root, CriteriaBuilder cb) {
		for (QueryFilter filter : filters) {
			mainPredicate.getExpressions().add(selectPredicate(filter, buildPath(filter.getName(), root), cb));
		}
		return mainPredicate;
	}

	private Predicate selectPredicate(QueryFilter queryFilter, Path path, CriteriaBuilder cb) {
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
				List<Comparable> vs = (ArrayList<Comparable>) queryFilter.getValue();
				return cb.between(path, vs.get(0), vs.get(1));
			default:
				return cb.conjunction();
		}
	}
}
