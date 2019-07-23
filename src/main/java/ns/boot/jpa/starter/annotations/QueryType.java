package ns.boot.jpa.starter.annotations;


import ns.boot.jpa.starter.enums.MatchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryType {
	MatchType value() default MatchType.EQ;
}
