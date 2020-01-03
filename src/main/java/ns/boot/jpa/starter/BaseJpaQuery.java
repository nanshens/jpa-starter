package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author ns
 */
public abstract class BaseJpaQuery<T> {
	protected EntityManager entityMgr;
	protected Class<T> entityClz;
	protected boolean isPaged;

	protected BaseJpaQuery(Class<T> entityClz, EntityManager entityMgr) {
		this.entityClz = entityClz;
		this.entityMgr = entityMgr;
	}

	public BaseJpaQuery(EntityManager entityMgr) {
		this.entityMgr = entityMgr;
	}

	private void cache() {

	}

	private void parser() {

	}

	private void query() {

	}

	public abstract JSON resultJson();
	public abstract List<T> resultList();
	public abstract Page<T> resultPage();
}
