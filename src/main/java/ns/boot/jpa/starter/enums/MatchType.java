package ns.boot.jpa.starter.enums;

import lombok.SneakyThrows;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EnumSet;

/**
 * @author zn
 */

public enum MatchType {

    EQ,
    EQ_IG_CASE,
    NE,
    GT,
    LT,
    GE,
    LE,
    LIKE,
    LIKE_IG_CASE,
    NOT_LIKE,
    NOT_LIKE_IG_CASE,
    IN,
    NOT_IN,
    IS_NULL,
    IS_NOT_NULL,
    BETWEEN;

//    EQ("equal", CriteriaBuilder.class, Expression.class, Object.class),
//    NE("notEqual", CriteriaBuilder.class, Expression.class, Object.class),
//    GT("greaterThan", CriteriaBuilder.class, Expression.class, Comparable.class),
//    LT("lessThan", CriteriaBuilder.class, Expression.class, Comparable.class),
//    GE("greaterThanOrEqualTo", CriteriaBuilder.class, Expression.class, Comparable.class),
//    LE("lessThanOrEqualTo", CriteriaBuilder.class, Expression.class, Comparable.class),
//    LIKE("like", CriteriaBuilder.class, Expression.class, String.class),
//    NOT_LIKE("notLike", CriteriaBuilder.class, Expression.class, String.class),
//    IN("in", Path.class, Collection.class),
//    NOT_IN("not", CriteriaBuilder.class, Expression.class),
//    IS_NULL("isNull", CriteriaBuilder.class, Expression.class),
//    IS_NOT_NULL("isNotNull", CriteriaBuilder.class, Expression.class),
//    BETWEEN("between", CriteriaBuilder.class, Expression.class, Comparable.class, Comparable.class);

//    private Class pathClass;
//    private Class targetClass;
//    private Class[] paramTypes;
//    private String cbName;
//    private Method method;
//
//    @SneakyThrows
//    MatchType(String cbName, Class targetClass, Class pathClass, Class ...paramTypes){
//        this.targetClass = targetClass;
//        this.pathClass = pathClass;
//        this.cbName = cbName;
//        this.paramTypes = paramTypes;
//        if (paramTypes.length == 0) {
//            this.method = targetClass.getMethod(cbName, pathClass);
//        } else if (paramTypes.length == 1) {
//            this.method = targetClass.getMethod(cbName, pathClass, paramTypes[0]);
//        } else if (paramTypes.length == 2) {
//            this.method = targetClass.getMethod(cbName, pathClass, paramTypes[0], paramTypes[1]);
//        }
//    }
//
//    public Class[] getParamTypes(){
//        return paramTypes;
//    }
//
//    public String getCbName() {
//        return cbName;
//    }
//
//    public Class getPathClass() {
//        return pathClass;
//    }
//
//    public Class getTargetClass() {
//        return targetClass;
//    }
//
//    public Method getMethod() {
//        return method;
//    }

//    public static EnumSet<MatchType> getAllTypes(){
//        return EnumSet.allOf(MatchType.class);
//    }
}
