package ns.boot.jpa.starter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ns.boot.jpa.starter.enums.JoinEnum;

import javax.persistence.criteria.JoinType;

/**
 * @author zn
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryJoin {
    private String table;
    private JoinType joinType;
    private JoinEnum joinEnum;

    public static QueryJoin leftJoin(String table){
        return new QueryJoin(table, JoinType.LEFT, JoinEnum.Default);
    }

    public static QueryJoin rightJoin(String table){
        return new QueryJoin(table, JoinType.RIGHT, JoinEnum.Default);
    }

    public static QueryJoin innerJoin(String table){
        return new QueryJoin(table, JoinType.INNER, JoinEnum.Default);
    }

    public static QueryJoin leftListJoin(String table) {
        return new QueryJoin(table, JoinType.LEFT, JoinEnum.List);
    }

    public static QueryJoin leftSetJoin(String table) {
        return new QueryJoin(table, JoinType.LEFT, JoinEnum.Set);
    }

    public static QueryJoin leftMapJoin(String table) {
        return new QueryJoin(table, JoinType.LEFT, JoinEnum.Map);
    }

    public static QueryJoin rightListJoin(String table) {
        return new QueryJoin(table, JoinType.RIGHT, JoinEnum.List);
    }

    public static QueryJoin rightSetJoin(String table) {
        return new QueryJoin(table, JoinType.RIGHT, JoinEnum.Set);
    }

    public static QueryJoin rightMapJoin(String table) {
        return new QueryJoin(table, JoinType.RIGHT, JoinEnum.Map);
    }

    public static QueryJoin innerListJoin(String table) {
        return new QueryJoin(table, JoinType.INNER, JoinEnum.List);
    }

    public static QueryJoin innerSetJoin(String table) {
        return new QueryJoin(table, JoinType.INNER, JoinEnum.Set);
    }

    public static QueryJoin innerMapJoin(String table) {
        return new QueryJoin(table, JoinType.INNER, JoinEnum.Map);
    }
}
