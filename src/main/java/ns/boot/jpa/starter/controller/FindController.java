package ns.boot.jpa.starter.controller;

import com.alibaba.fastjson.JSONObject;
import ns.boot.jpa.starter.property.JpaStarterProperties;
import ns.boot.jpa.starter.service.FindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;

/**
 * @author ns
 */
@RestController
public class FindController {
	private FindService findService = new FindService();
	@Autowired
	private JpaStarterProperties properties;
	@Autowired
	private EntityManager entityManager;
	@GetMapping(value = "/find", produces = "application/json")
	public void find(@RequestBody JSONObject json) {
		findService.find(json, properties.getBaseUrl(), entityManager);
	}
}
