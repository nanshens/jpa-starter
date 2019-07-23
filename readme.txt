jpa add json parser

todo:

1. url config
2. json parser
3. jpa entitymanager


example

group by

get:

request json

{
    "Entity": {
        "f1": "13",
        "f2!": "sdasdasd",
        "f3": ">123&&<222",
        "f4!": ["21", "22", "23"],
        "f5": ["21", "22", "23"],
        "f6": "NULL",
        "f7": "NOTNULL",
        "f8~": "%21321%",
        "f9~": "%21321",

        ...,

        "@Column": {
            "except": [],
            "include": [""max(id):maxid""]
        },
        "@Page": {
            "page": 1,
            "limit": 10
        },
        "@Sort": {
            "f1": "asc",
            "f2": "desc",
            ...
        }
    }
}

response json

{
    "data": {
        "f1": "12"
    },
    "code": 200,
    "msg": "1231312"

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
