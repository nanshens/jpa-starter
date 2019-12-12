package ns.boot.jpa.starter;

/**
 * @author ns
 */

public class JpaSqlQuery extends JpaQuery{

	public void input(String query) {
		super.sqlQuery = query;
	}
}
