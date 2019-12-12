package ns.boot.jpa.starter;

/**
 * @author ns
 */

public class JpaCustomQuery extends JpaQuery{

	public void input(CustomQuery query) {
		super.customQuery = query;
	}

}
