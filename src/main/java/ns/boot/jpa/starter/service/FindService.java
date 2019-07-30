package ns.boot.jpa.starter.service;

import com.alibaba.fastjson.JSONObject;
import ns.boot.jpa.starter.repository.FindRepo;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

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
	 * 5. exception handle
	 */
	public List find(JSONObject jso, String baseUrl, EntityManager entityManager) {
		return findRepo.find(jso, baseUrl, entityManager);
	}

}

