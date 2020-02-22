package ns.boot.jpa.starter.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ns.boot.jpa.starter.enums.MatchType;
import ns.boot.jpa.starter.util.QueryUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * @author ns
 */
@Data
@NoArgsConstructor
public class QueryFilter {

    private String name;
    private Object value;
    private MatchType type;
    private int subQueryNumber;
    private Predicate.BooleanOperator conditionEnum;

//    public QueryFilter(String name, Object value) {
//        this.name = name;
//        this.value = value;
//    }

    private QueryFilter(String name, Object value, MatchType type) {
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
        value = QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : "%" + value, MatchType.LIKE);
    }

    public static QueryFilter startLikeIgnoreCase(String name, String value){
        value = QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : "%" + value.toLowerCase(), MatchType.LIKE_IG_CASE);
    }
    public static QueryFilter endLike(String name, String value){
        value = QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : value + "%", MatchType.LIKE);
    }

    public static QueryFilter endLikeIgnoreCase(String name, String value){
        value = QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : value.toLowerCase() + "%", MatchType.LIKE_IG_CASE);
    }
    public static QueryFilter allLike(String name, String value){
        value = QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : "%" + value + "%", MatchType.LIKE);
    }

    public static QueryFilter allLikeIgnoreCase(String name, String value){
        value = QueryUtils.format(value);
        return new QueryFilter(name, "".equals(value) ? "" : "%" + value.toLowerCase() + "%", MatchType.LIKE_IG_CASE);
    }

    public static QueryFilter in(String name, Collection<Object> values){
        return new QueryFilter(name, values, MatchType.IN);
    }

    public static QueryFilter in(String name, Object[] values){
        return new QueryFilter(name, Arrays.asList(values), MatchType.IN);
    }

    public static QueryFilter isNull(String name){
        return new QueryFilter(name,null, MatchType.IS_NULL);
    }

    public static QueryFilter isNotNull(String name){
        return new QueryFilter(name, null, MatchType.IS_NOT_NULL);
    }

    public static <T extends Comparable<? super T>> QueryFilter between(String name, T minValue, T maxValue){
        List<T> valueList = new ArrayList<>(2);
        valueList.add(minValue);
        valueList.add(maxValue);
        return new QueryFilter(name, valueList, MatchType.BETWEEN);
    }

    public static <T extends Comparable<? super T>> QueryFilter between(String name,  List<T> valueList){
        return new QueryFilter(name, valueList, MatchType.BETWEEN);
    }
}
