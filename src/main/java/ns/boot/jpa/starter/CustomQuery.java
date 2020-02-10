package ns.boot.jpa.starter;

import ns.boot.jpa.starter.entity.QueryFilter;
import ns.boot.jpa.starter.entity.QueryOrder;
import ns.boot.jpa.starter.util.QueryUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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
	private int subQueryNumber = 1;

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
			}
		}
		return this;
	}

	public CustomQuery<T> or(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (!QueryUtils.isNull(queryFilter.getValue())) {
				queryFilter.setSubQueryNumber(0);
				queryFilter.setConditionEnum(Predicate.BooleanOperator.OR);
				filters.add(queryFilter);
			}
		}
		return this;
	}

	public CustomQuery<T> subAnd(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (!QueryUtils.isNull(queryFilter.getValue())) {
				queryFilter.setSubQueryNumber(subQueryNumber);
				queryFilter.setConditionEnum(Predicate.BooleanOperator.AND);
				filters.add(queryFilter);
				subQueryNumber = subQueryNumber + 1;
			}
		}
		return this;
	}

	public CustomQuery<T> subOr(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (!QueryUtils.isNull(queryFilter.getValue())) {
				queryFilter.setSubQueryNumber(subQueryNumber);
				queryFilter.setConditionEnum(Predicate.BooleanOperator.OR);
				filters.add(queryFilter);
				subQueryNumber = subQueryNumber + 1;
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
		return buildPredicate(root, cb);
	}

	private Predicate buildPredicate(Root<T> root, CriteriaBuilder cb) {
		Predicate predicate = null;
		for (int i = 0; i < filters.size(); i++) {
			QueryFilter qf = filters.get(i);
			if (i == 0){
				predicate = buildPredicate(qf, root, cb);
			} else {
				predicate = selectCondition(predicate, buildPredicate(qf, root, cb), cb, qf.getConditionEnum());
			}
		}
		return predicate;
	}
	private Predicate selectCondition(Predicate basicPredicate, Predicate newPredicate, CriteriaBuilder cb, Predicate.BooleanOperator conditionEnum) {
		return conditionEnum == Predicate.BooleanOperator.AND ? cb.and(basicPredicate, newPredicate) : cb.or(basicPredicate, newPredicate);
	}

	private Predicate buildPredicate(QueryFilter queryFilter, Root<T> root, CriteriaBuilder cb) {
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
	/*-----------------------------------------*/

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
