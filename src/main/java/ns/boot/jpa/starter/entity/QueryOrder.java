package ns.boot.jpa.starter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

/**
 * @author zn
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryOrder {

    private String name;
    private Sort.Direction direction = Sort.Direction.DESC;
//    private Sort.Direction d= Sort.Direction.ASC;



    public static QueryOrder desc(String name) {
        return new QueryOrder(name, Sort.Direction.DESC);
    }

    public static QueryOrder asc(String name) {
        return new QueryOrder(name, Sort.Direction.ASC);
    }

}
