package ns.boot.jpa.starter.entity;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author zn
 */
public class QueryPageable extends PageRequest {

	public QueryPageable(int page, int size) {
		super(page, size, Sort.unsorted());
	}

	public QueryPageable(int page, int size, Sort.Direction direction, String... name) {
		super(page, size, direction, name);
	}

	public QueryPageable(int page, int size, Sort sort) {
		super(page, size, sort);
	}

	public static QueryPageable page10(int page) {
		return new QueryPageable(page, 10);
	}

	public static QueryPageable page10(int page, Sort sort) {
		return new QueryPageable(page, 10, sort);
	}

	public static QueryPageable page10(int page, Sort.Direction direction, String... name) {
		return new QueryPageable(page, 10, direction, name);
	}

	public static QueryPageable page(int page, int size) {
		return new QueryPageable(page, size);
	}

	public static QueryPageable page(int page, int size, Sort.Direction direction, String... name) {
		return new QueryPageable(page, size, direction, name);
	}

	public static QueryPageable page(int page, Sort sort) {
		return new QueryPageable(page, 10, sort);
	}
}
