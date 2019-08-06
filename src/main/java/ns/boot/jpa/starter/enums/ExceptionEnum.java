package ns.boot.jpa.starter.enums;

/**
 * @author ns
 */

public enum ExceptionEnum {

	ENTITY_ERROR("查询实体有误"),
	VALUE_ERROR("查询实体的值有误: "),
	NAME_ERROR("查询实体的属性名有误: "),
	PAGE_ERROR("查询实体的page参数有误: "),
	SORT_ERROR("查询实体的sort参数有误: "),
	COLUMN_ERROR("查询实体的column参数有误: ");

	private String msg;

	public String getMsg() {
		return msg;
	}

	ExceptionEnum(String msg) {
		this.msg = msg;
	}
}
