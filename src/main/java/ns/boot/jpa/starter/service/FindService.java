package ns.boot.jpa.starter.service;

import com.alibaba.fastjson.JSONObject;
import ns.boot.jpa.starter.property.JpaStarterProperties;
import ns.boot.jpa.starter.result.Result;
import ns.boot.jpa.starter.utils.FindUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author ns
 */

@Service
public class FindService {
	@Autowired
	private JpaStarterProperties properties;
	@Autowired
	private EntityManager entityManager;
	/**
	 *todo:
	 * 1. parser json
	 * 2. create conditions
	 * 3. get result
	 * 4. format result json
	 * 5. exception handle
	 */
	public Result find(JSONObject queryJson) {
		Result result = new Result();
		result.setData(new FindUtils().find(queryJson, properties.getBaseUrl(), entityManager));
		return result;
	}

}

