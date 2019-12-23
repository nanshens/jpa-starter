package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import ns.boot.jpa.starter.entity.QueryFilter;
import ns.boot.jpa.starter.entity.QueryOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * @author ns
 */

public class JpaCustomQuery<T> extends JpaQuery<T>{
	private CustomQuery<T> customQuery;
	public JpaCustomQuery(Class<T> tClass) {
		super(tClass);
		customQuery = new CustomQuery<>();
	}

	public CustomQuery<T> and(QueryFilter... queryFilters) {
		return customQuery.and(queryFilters);
	}

	public CustomQuery<T> or(QueryFilter... queryFilters) {
		return customQuery.or(queryFilters);
	}

	public CustomQuery<T> order(QueryOrder... orders) {
		return customQuery.order(orders);
	}

	public CustomQuery<T> page(int page, int limit) {
		customQuery.setLimit(limit);
		customQuery.setPage(page);
		isPaged = true;
		return customQuery;
	}

	public JpaCustomQuery<T> input(CustomQuery<T> customQuery){
		this.customQuery = customQuery;
		return this;
	}

//	public CustomQuery<T> buildSpecification(){
//		return customQuery;
//	}

	private TypedQuery<T> parser() {
		CriteriaBuilder builder = getEm().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);

		Predicate predicate = customQuery.toPredicate(root, criteriaQuery, builder);

		if (predicate != null) {
			criteriaQuery.where(predicate);
		}
		criteriaQuery.select(root);
		TypedQuery<T> query = getEm().createQuery(criteriaQuery);

		if (isPaged) {
			query.setFirstResult((customQuery.getPage() - 1) * customQuery.getLimit())
					.setMaxResults(customQuery.getLimit());
		}

		return query;
	}

	private TypedQuery<Long> parserCount() {
		CriteriaBuilder builder = getEm().getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<T> root = criteriaQuery.from(entityClass);

		Predicate predicate = customQuery.toPredicate(root, criteriaQuery, builder);

		if (predicate != null) {
			criteriaQuery.where(predicate);
		}
		criteriaQuery.select(builder.count(root));
		return getEm().createQuery(criteriaQuery);
	}

	private List<T> query() {
		return parser().getResultList();
	}

	private Long queryCount() {
		List<Long> totals = parserCount().getResultList();
		long total = 0L;

		for (Long element : totals) {
			total += element == null ? 0 : element;
		}

		return total;
	}


	@Override
	public JSON resultJson() {
		return new JSONArray((List<Object>) query());
	}

	@Override
	public List<T> resultList() {
		return query();
	}

	@Override
	public Page<T> resultPage() {
		return isPaged ? new PageImpl<>(query()) :
				new PageImpl<>(query(), PageRequest.of(customQuery.getPage(), customQuery.getLimit()), queryCount());
	}
}
