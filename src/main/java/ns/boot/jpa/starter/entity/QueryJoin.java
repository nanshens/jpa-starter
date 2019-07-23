package ns.boot.jpa.starter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ns.boot.jpa.starter.enums.JoinParams;

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
    private JoinParams joinParams;

    public static QueryJoin leftJoin(String table){
        return new QueryJoin(table, JoinType.LEFT, JoinParams.Default);
    }

    public static QueryJoin rightJoin(String table){
        return new QueryJoin(table, JoinType.RIGHT, JoinParams.Default);
    }

    public static QueryJoin innerJoin(String table){
        return new QueryJoin(table, JoinType.INNER, JoinParams.Default);
    }

    public static QueryJoin leftListJoin(String table) {
        return new QueryJoin(table, JoinType.LEFT, JoinParams.List);
    }

    public static QueryJoin leftSetJoin(String table) {
        return new QueryJoin(table, JoinType.LEFT, JoinParams.Set);
    }

    public static QueryJoin leftMapJoin(String table) {
        return new QueryJoin(table, JoinType.LEFT, JoinParams.Map);
    }

    public static QueryJoin rightListJoin(String table) {
        return new QueryJoin(table, JoinType.RIGHT, JoinParams.List);
    }

    public static QueryJoin rightSetJoin(String table) {
        return new QueryJoin(table, JoinType.RIGHT, JoinParams.Set);
    }

    public static QueryJoin rightMapJoin(String table) {
        return new QueryJoin(table, JoinType.RIGHT, JoinParams.Map);
    }

    public static QueryJoin innerListJoin(String table) {
        return new QueryJoin(table, JoinType.INNER, JoinParams.List);
    }

    public static QueryJoin innerSetJoin(String table) {
        return new QueryJoin(table, JoinType.INNER, JoinParams.Set);
    }

    public static QueryJoin innerMapJoin(String table) {
        return new QueryJoin(table, JoinType.INNER, JoinParams.Map);
    }
}
