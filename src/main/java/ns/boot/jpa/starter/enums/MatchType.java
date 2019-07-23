package ns.boot.jpa.starter.enums;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import java.util.Collection;
import java.util.EnumSet;

/**
 * @author zn
 */

public enum MatchType {
    EQ("equal", CriteriaBuilder.class, Expression.class, Object.class),
    NE("notEqual", CriteriaBuilder.class, Expression.class, Object.class),
    GT("greaterThan", CriteriaBuilder.class, Expression.class, Comparable.class),
    LT("lessThan", CriteriaBuilder.class, Expression.class, Comparable.class),
    GE("greaterThanOrEqualTo", CriteriaBuilder.class, Expression.class, Comparable.class),
    LE("lessThanOrEqualTo", CriteriaBuilder.class, Expression.class, Comparable.class),
    LIKE("like", CriteriaBuilder.class, Expression.class, String.class),
    IN("in", Path.class, Collection.class),
    ISNULL("isNull", CriteriaBuilder.class, Expression.class),
    ISNOTNULL("isNotNull", CriteriaBuilder.class, Expression.class),
    BETWEEN("between", CriteriaBuilder.class, Expression.class, Comparable.class, Comparable.class);

    private Class pathClass;
    private Class targetClass;
    private Class[] paramTypes;
    private String cbName;

    MatchType(String cbName, Class targetClass, Class pathClass, Class ...paramTypes){
        this.targetClass = targetClass;
        this.pathClass = pathClass;
        this.cbName = cbName;
        this.paramTypes = paramTypes;
    }

    public Class[] getParamTypes(){
        return paramTypes;
    }

    public String getCbName() {
        return cbName;
    }

    public Class getPathClass() {
        return pathClass;
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public static EnumSet<MatchType> getAllTypes(){
        return EnumSet.allOf(MatchType.class);
    }
}
