jpa add json parser

todo:

1. url config(delete)
2. json parser(pass)
3. jpa entitymanager(pass)
4. enums and localdate localtime(pass)
5. n + 1 problem
6. select groupby having ...
example

group by

get:

request json

{
    "Entity": {
        "f1": "13",
        "f2!": "sdasdasd",
        "f3>": "123",
        "f3&<": "155",
        "f4!": ["21", "22", "23"],
        "f5": ["21", "22", "23"],
        "f6": "NULL",
        "f7!": "NULL",
        "f8~": "%21321%",
        "f9~": "%21321",
        "f9&": "%21321",

        ...,

        "@column": {
            "except": [],
            "include": ["max(id):maxid"]
        },
        "@page": {
            "page": 1,
            "limit": 10
        },
        "@sort": {
            "f1": "asc",
            "f2": "desc",
            ...
        }
    }
}

response json

{
    "data": {
        "Entity": {
            "f1": "value"
        }
    },
    "msg": "SUCCESS OR FAIL"
}

update:

request json

response json


delete:

request json

response json


add:

request json

response json



jpaquery

//		1.select * from customer
//		where code > '3' or name='2' or address_id ='1';

		JpaQuery query1 = new JpaQuery<Customer>();
		query1.or(QueryFilter.ge("code", "3"),
				QueryFilter.eq("name", "2"),
				QueryFilter.eq("address.id", "1"));

//		2.select * from customer
//		where code > '3' and name='2' and address_id ='1';

		JpaQuery query2 = new JpaQuery<Customer>();
		query2.and(QueryFilter.ge("code", "3"),
				QueryFilter.eq("name", "2"),
				QueryFilter.eq("address.id", "1"));

//		3.select * from customer
//		where code > '3' or (name='2' and address_id ='1');

		JpaQuery query3 = new JpaQuery<Customer>();
		query3.or(QueryFilter.ge("code", "3")
				.childAnd(QueryFilter.eq("name", "2"),
						QueryFilter.eq("address.id", "1"))
				.or(QueryFilter.ge("code", "3"));

//		4.select * from customer
//		where code < '4' and (name='2' or address_id ='1');

		JpaQuery query4 = new JpaQuery<Customer>();
		query4.and(QueryFilter.le("code", "4"))
				.childOr(QueryFilter.eq("name", "2"),
						QueryFilter.eq("address.id", "1"));

//		5.select * from customer
//		where code > '3' or (name='2' and address_id ='1') or (f1 = 'v' and f2 = 'v') or f3 ='v';
		JpaQuery query5 = new JpaQuery<Customer>();
		query4.or(QueryFilter.gt("code", "3"))
				.childAnd(QueryFilter.eq("name", "2"),
						QueryFilter.eq("address.id", "1"))
				.or(QueryFilter.gt("code", "3"))
				.childAnd(QueryFilter.eq("name", "2"),
						QueryFilter.eq("address.id", "1"));


//		6.select * from customer
//		where code > '3' or (name='2' and address_id ='1' and (f1= 'v' or f2= 'v'))

		JpaQuery query6 = new JpaQuery<Customer>();
		query4.or(QueryFilter.gt("code", "3"))
				.childAnd(QueryFilter.eq("name", "2"),
						QueryFilter.eq("address.id", "1"))
				.childOr(QueryFilter.eq("name", "2"),
						QueryFilter.eq("address.id", "1"));


    @Test
	public void method() {
		Specification<SalesOrder> specification4 = (Specification<SalesOrder>) (root, query, cb) -> {
			Predicate parentPredicate;
			Predicate conPredicate = cb.conjunction();
			conPredicate.getExpressions().add(cb.equal(root.get("name"), "12312"));

			Predicate disPredicate = cb.disjunction();
			disPredicate.getExpressions().add(cb.equal(root.get("id"), "12312"));
			disPredicate.getExpressions().add(cb.equal(root.get("code"), "12312"));

			parentPredicate = cb.and(conPredicate, disPredicate);
			return parentPredicate;
		};

		Specification<SalesOrder> specification5 = (Specification<SalesOrder>) (root, query, cb) -> {
			Predicate parentPredicate;
			Predicate conPredicate = cb.conjunction();
			conPredicate.getExpressions().add(cb.equal(root.get("id"), "12312"));
			conPredicate.getExpressions().add(cb.equal(root.get("code"), "12312"));

			Predicate disPredicate = cb.conjunction();
			disPredicate.getExpressions().add(cb.equal(root.get("createBy"), "12312"));
			disPredicate.getExpressions().add(cb.equal(root.get("modifyBy"), "12312"));

			parentPredicate = cb.or(cb.equal(root.get("id"), "12312"), conPredicate, disPredicate, cb.equal(root.get("code"), "12312"));
			return parentPredicate;
		};

		Specification<SalesOrder> specification6 = (Specification<SalesOrder>) (root, query, cb) -> {
			Predicate parentPredicate;
			Predicate conPredicate = cb.disjunction();
			conPredicate.getExpressions().add(cb.equal(root.get("id"), "12312"));
			conPredicate.getExpressions().add(cb.equal(root.get("code"), "12312"));

			Predicate disPredicate = cb.conjunction();
			disPredicate.getExpressions().add(cb.equal(root.get("createBy"), "12312"));
			disPredicate.getExpressions().add(cb.equal(root.get("modifyBy"), "12312"));

			disPredicate = cb.and(disPredicate, conPredicate);

			parentPredicate = cb.or(cb.equal(root.get("code"), "12312"), disPredicate);
			return parentPredicate;
		};
		List<SalesOrder> salesOrders = salesOrderRepo.findAll(specification6);
		System.out.println(salesOrders.size());
	}