package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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
	}

	public JpaCustomQuery<T> input(CustomQuery<T> customQuery){
		this.customQuery = customQuery;
		return this;
	}

	public CustomQuery<T> buildSpecification(){
		return customQuery;
	}

	public void parser() {

	}

	private List<T> query() {
		CriteriaBuilder builder = getEm().getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = builder.createQuery(entityClass);
		Root<T> root = criteriaQuery.from(entityClass);
		Predicate predicate = customQuery.toPredicate(root, criteriaQuery, builder);

		if (predicate != null) {
			criteriaQuery.where(predicate);
		}
		return getEm().createQuery(criteriaQuery).getResultList();
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
		return new PageImpl<>(query());
	}
}
