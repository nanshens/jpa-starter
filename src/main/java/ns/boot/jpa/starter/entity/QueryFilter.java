package ns.boot.jpa.starter.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ns.boot.jpa.starter.enums.ConditionEnum;
import ns.boot.jpa.starter.enums.MatchType;
import ns.boot.jpa.starter.util.QueryUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * @author acer
 * @date 2018/7/30
 */
@Data
@NoArgsConstructor
public  class QueryFilter {

    private String name;
    private Object value;
    private MatchType type;
    private boolean childQuery;
    private ConditionEnum conditionEnum;

//    public QueryFilter(String name, Object value) {
//        this.name = name;
//        this.value = value;
//    }

    public QueryFilter(String name, Object value, MatchType type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public static QueryFilter eq(String name, Object value){
        return new QueryFilter(name, value, MatchType.EQ);
    }

    public static QueryFilter eqIgnoreCase(String name, Object value){
        return new QueryFilter(name, value, MatchType.EQ_IG_CASE);
    }

    public static QueryFilter ne(String name, Object value){
        return new QueryFilter(name, value, MatchType.NE);
    }

    public static QueryFilter gt(String name, Object value){
        return new QueryFilter(name, value, MatchType.GT);
    }

    public static QueryFilter lt(String name, Object value){
        return new QueryFilter(name, value, MatchType.LT);
    }

    public static QueryFilter ge(String name, Object value){
        return new QueryFilter(name, value, MatchType.GE);
    }

    public static QueryFilter le(String name, Object value){
        return new QueryFilter(name, value, MatchType.LE);
    }

    public static QueryFilter startLike(String name, String value){
        QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : "%" + value, MatchType.LIKE);
    }

    public static QueryFilter startLikeIgnoreCase(String name, String value){
        QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : "%" + value.toLowerCase(), MatchType.LIKE_IG_CASE);
    }
    public static QueryFilter endLike(String name, String value){
        QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : value + "%", MatchType.LIKE);
    }

    public static QueryFilter endLikeIgnoreCase(String name, String value){
        QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : value.toLowerCase() + "%", MatchType.LIKE_IG_CASE);
    }
    public static QueryFilter allLike(String name, String value){
        QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : "%" + value + "%", MatchType.LIKE);
    }

    public static QueryFilter allLikeIgnoreCase(String name, String value){
        QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : "%" + value.toLowerCase() + "%", MatchType.LIKE_IG_CASE);
    }

    public static QueryFilter in(String name, Object... valueList){
        List values = new ArrayList();

        if (valueList.length > 1) {
            values.add(Arrays.asList(valueList));
        } else if (valueList.length == 1) {
            if (valueList[0] instanceof Collection){
                values = (List) valueList[0];
            } else if (valueList[0] instanceof Object[]) {
                values.add(Arrays.asList(valueList[0]));
            }else {
                values.add(valueList[0]);
            }
        }
        return new QueryFilter(name, values, MatchType.IN);
    }

    public static QueryFilter isNull(String name){
        return new QueryFilter(name,null, MatchType.IS_NULL);
    }

    public static QueryFilter isNotNull(String name){
        return new QueryFilter(name, null, MatchType.IS_NOT_NULL);
    }

    public static <T extends Comparable> QueryFilter between(String name, T minValue, T maxValue){
        List<Comparable> valueList = new ArrayList<>();
        valueList.add(minValue);
        valueList.add(maxValue);
        return new QueryFilter(name, valueList, MatchType.BETWEEN);
    }

    public static <T extends Comparable> QueryFilter between(String name, Object... valueList){
        List values = new ArrayList();

        if (valueList.length > 1) {
            values.add(Arrays.asList(valueList));
        } else if (valueList.length == 1) {
            if (valueList[0] instanceof Collection){
                values = (List) valueList[0];
            } else if (valueList[0] instanceof Object[]) {
                values.add(Arrays.asList(valueList[0]));
            }else {
                values.add(valueList[0]);
            }
        }
        return new QueryFilter(name, values, MatchType.BETWEEN);
    }
}
