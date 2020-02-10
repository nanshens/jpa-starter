package ns.boot.jpa.starter.constant;

/**
 * @author ns
 */

public class QueryConstant {
	public final static String LIMIT_INFO_REGEX = "\blimit\\s*[0-9]\\d*\b";
	public final static String OFFSET_INFO_REGEX = "\boffset\\s*[0-9]\\d*\b";
	public final static String SPECIAL_SYMBOL_REGEX = "[\\!\\=\\<\\>\\&\\|\\~]+";

	public final static String EQ= "=";
	public final static String NOT = "!";
	public final static String LIKE = "~";
	public final static String GT = ">";
	public final static String GE = ">=";
	public final static String LT = "<";
	public final static String LE = "<=";
	public final static String AND = "&";


}
