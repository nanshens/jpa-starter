package ns.boot.jpa.starter.constant;

/**
 * @author ns
 */

public class QueryConstant {
	public final static String LIMIT_INFO_REGEX = "\blimit\\s*[0-9]\\d*\b";
	public final static String OFFSET_INFO_REGEX = "\boffset\\s*[0-9]\\d*\b";

	private final static String SPECIAL_SYMBOL_REGEX = "[\\!\\=\\<\\>\\&\\|\\~]+";

	private final static String EQ= "=";
	private final static String NOT = "!";
	private final static String LIKE = "~";
	private final static String GT = ">";
	private final static String GE = ">=";
	private final static String LT = "<";
	private final static String LE = "<=";
	private final static String AND = "&";


}
