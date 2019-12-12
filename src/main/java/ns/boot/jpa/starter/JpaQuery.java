package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author ns
 */

public abstract class JpaQuery {

	protected JSONObject jsonQuery;
	protected String sqlQuery;
	protected CustomQuery customQuery;

	public JSONObject getQueryJson() {
		return jsonQuery;
	}

	public String getQuerySql() {
		return sqlQuery;
	}

//	public void input(JSONObject query){
//		this.jsonQuery = query;
//	}
//
//	public void input(String query) {
//		this.sqlQuery = query;
//	}
//
//	public void input(CustomQuery query) {
//
//	}

	public void cache() {

	}

	private void parser() {

	}


	private void query() {

	}

	private void buildSpecification(){

	}

	private JSONObject resultJson() {
		return null;
	}

	private List resultList() {
		return null;
	}

	private Page resultPage() {
		return null;
	}

	private boolean isInputed() {
		return sqlQuery != null || jsonQuery != null;
	}
}
