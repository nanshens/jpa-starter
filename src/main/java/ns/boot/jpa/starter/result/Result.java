package ns.boot.jpa.starter.result;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author nanshen
 */

@Data
public class Result {
	enum ResultEnum{SUCCESS, FAIL}

	private Object data;
	private String msg;
	private ResultEnum code;

	public void successResult(Object data){
		this.data = data;
		code = ResultEnum.SUCCESS;
	}

	public void failResult(String msg){
		this.msg = msg;
		code = ResultEnum.FAIL;
	}

}
