package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;

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

	public void buildSpecification(){

	}

	public void parser() {

	}

	private List<T> query() {

	}

	@Override
	public JSON resultJson() {
		return null;
	}

	@Override
	public List<T> resultList() {
		return null;
	}

	@Override
	public Page<T> resultPage() {
		return null;
	}
}
