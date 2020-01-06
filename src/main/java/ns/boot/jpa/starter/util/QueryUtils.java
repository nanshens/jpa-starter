package ns.boot.jpa.starter.util;

import lombok.SneakyThrows;
import ns.boot.jpa.starter.entity.QueryFilter;
import ns.boot.jpa.starter.entity.QueryOrder;
import org.springframework.data.domain.Sort;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author zn
 */
public class QueryUtils {
    public enum StringEnums{upper,lower}

    public static boolean isNullOrEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if (o instanceof String) {
            return "".equals(o.toString()) || "null".equals(o.toString());
        }
        return false;
    }

    public static String getQueryFilterName(String name) {
        String[] names = name.split("\\.");
        StringBuilder temp = new StringBuilder(names[0]);
        if (names.length > 1) {
            temp.append(Character.toUpperCase(names[1].charAt(0))).append(names[1].substring(1)).toString();
        }
        return temp.toString();
    }

    @SneakyThrows
    public static Object getValue(String field, Object object) {
//        field.setAccessible(true);
        Method getMethod = new PropertyDescriptor(field, object.getClass()).getReadMethod();
        return getMethod.invoke(object);
    }

    public static List<Field> getAllFields(Class clz, List<Field> list) {
        if (clz.getSuperclass() == null) {
            return list;
        } else {
            list.addAll(Arrays.asList(clz.getDeclaredFields()));
            return getAllFields(clz.getSuperclass(), list);
        }
    }

    public static Map<String, Field> getAllFields(Class clz, Map<String, Field> map, String prefix) {
        if (clz.getSuperclass() == null) {
            return map;
        } else {
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
               if (isBaseType(field.getType())){
                   map.put(prefix + field.getName(), field);
               }else {
                   return getAllFields(field.getType(), map, changeFirstChar(field.getType().getSimpleName(), StringEnums.lower) + ".");
               }
            }
            return getAllFields(clz.getSuperclass(), map, changeFirstChar(clz.getSimpleName(), StringEnums.lower) + ".");
        }
    }

    /*
     * need test enums
     *
     * */
    public static Map<String, Object> objectMap(Object o) {
        Map<String, Object> map = new HashMap<>();
        Stack<Field> stack = new Stack<>();
        Stack<Object> object = new Stack<>();
        Stack<String> prefix = new Stack<>();
        List<QueryFilter> afs = new ArrayList<>();
        List<QueryOrder> qo = new ArrayList<>();

        for (Field field : getAllFields(o.getClass(), new ArrayList<>())) {
            stack.push(field);
            object.push(o);
            prefix.push("");
        }

        while (!stack.empty()) {
            Field s = stack.pop();
            Object po = object.pop();
            String pre = prefix.pop();
            if (po == null) continue;

            if (isBaseType(s.getType())) {
                Object v = getValue(s.getName(), po);
                String n = pre + s.getName();
                if (v == null || "page".equals(n) || "limit".equals(n)) continue;

            } else {
                for (Field field : QueryUtils.getAllFields(s.getType(), new ArrayList<>())) {
                    stack.push(field);
                    object.push(QueryUtils.getValue(s.getName(), po));
                    prefix.push(pre + changeFirstChar(QueryUtils.getValue(s.getName(), po).getClass().getSimpleName(), StringEnums.lower) + ".");
                }
            }
        }
        map.put("andFilters", afs);
        map.put("orders", qo);
        return map;
    }

    public static Map<String, Field> getClassField(Class c) {
        Map<String, Field> map = new HashMap<>();
        Stack<Field> stack = new Stack<>();
        Stack<String> prefix = new Stack<>();

        for (Field field : getAllFields(c, new ArrayList<>())) {
            stack.push(field);
            prefix.push("");
        }

        while (!stack.empty()) {
            Field s = stack.pop();
            String pre = prefix.pop();

            if (isBaseType(s.getType())) {
                map.put(pre + s.getName(), s);
            } else {
                for (Field field : QueryUtils.getAllFields(s.getType(), new ArrayList<>())) {
                    stack.push(field);
                    prefix.push(pre + changeFirstChar(s.getType().getSimpleName(), StringEnums.lower) + ".");
                }
            }
        }
        return map;
    }


    public static Object getClassTypeValue(Class<?> typeClass, List<Object> list) {
        Object value = null;
        if (list != null) {
            value = list.get(0);
        }
        if (typeClass == int.class || value instanceof Integer) {
            if (null == value) {
                return null;
            }
            return Integer.parseInt(value.toString());
        } else if (typeClass == short.class) {
            if (null == value) {
                return 0;
            }
            return Short.valueOf(value.toString());
        } else if (typeClass == byte.class) {
            if (null == value) {
                return 0;
            }
            return Byte.valueOf(value.toString());
        } else if (typeClass == double.class) {
            if (null == value) {
                return 0;
            }
            return Double.valueOf(value.toString());
        } else if (typeClass == long.class) {
            if (null == value) {
                return 0;
            }
            return Long.valueOf(value.toString());
        } else if (typeClass == String.class) {
            if (null == value) {
                return "";
            }
            return value.toString();
        } else if (typeClass == boolean.class) {
            if (null == value) {
                return true;
            }
            return value;
        } else if (typeClass == BigDecimal.class) {
            if (null == value) {
                return new BigDecimal(0);
            }
            return new BigDecimal(value + "");
        } else if (typeClass == List.class) {
            return list;
        } else if (typeClass.getSuperclass() == Enum.class) {
            for (int i = 0; i < typeClass.getEnumConstants().length; i++) {
                if (typeClass.getEnumConstants()[i].toString().equals(value)) {
                    return typeClass.getEnumConstants()[i];
                }
            }
            return typeClass.getEnumConstants()[0];
        } else {
            return typeClass.cast(value);
        }
        //enums
    }

    public static boolean isEmpty(Object o) {
        if (o == null){
            return true;
        }else if (o instanceof String && (((String) o).isEmpty() || "".equals(o))) {
            return true;
        }else if (o instanceof Integer && Integer.parseInt((String) o) == 0) {
            return true;
        }else {
            return false;
        }
    }

    public static String changeFirstChar(String str, StringEnums enums) {
        char[] cs=str.toCharArray();
        if ((cs[0] >= 'A' && cs[0] <= 'Z') || (cs[0] >= 'a' && cs[0] <= 'z' )) {
            cs[0] = (char) (enums == StringEnums.upper ? cs[0] - 32 : cs[0] + 32);
        }
        return String.valueOf(cs);
    }

    public static boolean isBaseType(Class c) {
        return c != null && c.getClassLoader() == null || c.isEnum();
    }
}
