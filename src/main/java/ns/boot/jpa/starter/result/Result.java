package ns.boot.jpa.starter.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nanshen
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
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

	public static Result success(Object data) {
		return new Result(data, null, ResultEnum.SUCCESS);
	}

	public static Result failure(Object data, String msg) {
		return new Result(data, msg, ResultEnum.FAIL);
	}
}
