package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author ns
 */
public abstract class BaseJpaQuery<T> {
	protected EntityManager entityMgr;
	protected Class<T> entityClz;
	protected boolean isPaged;
	protected Integer page;
	protected Integer limit;
	protected Supplier<List<T>> cacheSupplier;

	protected BaseJpaQuery(Class<T> entityClz, EntityManager entityMgr) {
		this.entityClz = entityClz;
		this.entityMgr = entityMgr;
	}

	public BaseJpaQuery(EntityManager entityMgr) {
		this.entityMgr = entityMgr;
	}

	protected void setPageInfo(int page, int limit) {
		this.page = page;
		this.limit = limit;
		isPaged = true;
	}

	protected List<T> getCacheResult() {
		return cacheSupplier == null ? new ArrayList<>() : cacheSupplier.get();
	}

	public <Q extends BaseJpaQuery<T>> Q cache(Supplier<List<T>> cacheSupplier){
		this.cacheSupplier = cacheSupplier;
		return (Q) this;
	}
	protected abstract <Q extends Query> Q parser();
	protected abstract <Q extends Query> Q parserCount();
	public abstract JSON resultJson();
	public abstract List<T> resultList();
	public abstract Page<T> resultPage();
}
