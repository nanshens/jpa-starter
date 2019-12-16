package ns.boot.jpa.starter;

import ns.boot.jpa.starter.entity.QueryFilter;
import ns.boot.jpa.starter.enums.Condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author ns
 */

public class CustomQuery<T>{
	private List<QueryFilter> filters = new ArrayList<>();

	public CustomQuery<T> and(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (Objects.isNull(queryFilter.getValue())) {
				continue;
			}
			queryFilter.setChildQuery(false);
			queryFilter.setCondition(Condition.And);
			filters.add(queryFilter);
		}
		return this;
	}

	public CustomQuery<T> or(QueryFilter... queryFilters) {
		for (QueryFilter queryFilter : queryFilters) {
			if (Objects.isNull(queryFilter.getValue())) {
				continue;
			}
			queryFilter.setChildQuery(false);
			queryFilter.setCondition(Condition.Or);
			filters.add(queryFilter);
		}
		return this;
	}
}
