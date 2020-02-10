package ns.boot.jpa.starter.enums;

import java.util.EnumSet;

/**
 * @author zn
 */

public enum JoinEnum {
	Default("join"),
	List("joinList"),
	Set("joinSet"),
	Map("joinMap");

	private String rootName;

	JoinEnum(String rootName) {
		this.rootName = rootName;
	}

	public String getRootName() {
		return rootName;
	}

	public static EnumSet<JoinEnum> getAllJoinParams() {
		return EnumSet.allOf(JoinEnum.class);
	}
}
