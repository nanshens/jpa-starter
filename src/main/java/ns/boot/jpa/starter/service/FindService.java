package ns.boot.jpa.starter.service;

import com.alibaba.fastjson.JSONObject;
import ns.boot.jpa.starter.repository.FindRepo;
import org.springframework.stereotype.Service;

/**
 * @author ns
 */

@Service
public class FindService {

	private FindRepo findRepo = new FindRepo();

	/**
	 *todo:
	 * 1. parser json
	 * 2. create conditions
	 * 3. get result
	 * 4. format result json
	 *
	 */
	public void find(JSONObject jso, String baseUrl) {
		findRepo.find(jso, baseUrl);
	}

}

