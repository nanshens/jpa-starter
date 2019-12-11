package ns.boot.jpa.starter;

/**
 * @author ns
 */

public class Design {
	public static void main(String[] args) {
		/* create
		* need many factories to support different input
		* need generic<T> to limit result type
		* JpaQueryFactory factory = new JpaQueryFactory();
		* */

		/* input
		* 1. factory.input(json)--------自定义json结构体
		* 2. factory.input(jpaquery)----自定义jpaquery
		* 3. factory.input(sql)---------自定义sql
		*
		* */

		/*
		* cache
		*
		* user custom cache method
		*
		* factory.cache()
		*
		*
		* */

		/* parser implicit
		*
		*
		*
		*
		* */

		/*
		 * cache implicit
		 *
		 * default cache method
		 *
		 * */

		/*
		* query implicit
		*
		*
		*
		*
		* */

		/*
		* result
		* list? page? json?
		*
		* factory.resultList();
		* factory.resultPage();
		* factory.resultJson();
		*
		*
		* */

	}
}
