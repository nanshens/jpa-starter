package ns.boot.jpa.starter.enums;

import java.util.EnumSet;

/**
 * @author zn
 */

public enum JoinParams {
	Default("join"),
	List("joinList"),
	Set("joinSet"),
	Map("joinMap");

	private String rootName;

	JoinParams(String rootName) {
		this.rootName = rootName;
	}

	public String getRootName() {
		return rootName;
	}

	public static EnumSet<JoinParams> getAllJoinParams() {
		return EnumSet.allOf(JoinParams.class);
	}
}
