package ns.boot.jpa.starter;

import com.alibaba.fastjson.JSONObject;

/**
 * @author ns
 */

public class JpaJsonQuery extends JpaQuery{

	public void input(JSONObject query){
		super.jsonQuery = query;
	}
}
