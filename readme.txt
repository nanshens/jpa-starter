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
