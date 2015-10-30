package org.keycloak.federation.ldap.idm.query.internal;

import org.keycloak.federation.ldap.idm.query.Condition;
import org.keycloak.federation.ldap.idm.query.QueryParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pedro Igor
 */
public class EqualCondition implements Condition {

    private static Map<Character, String> ESCAPE_TABLE;
    // ldap query escaped characters
    static {
        ESCAPE_TABLE = new HashMap<>();
        ESCAPE_TABLE.put('\\', "\\5c");
        ESCAPE_TABLE.put('*', "\\2a");
        ESCAPE_TABLE.put('(', "\\28");
        ESCAPE_TABLE.put(')', "\\29");
    }

    private final QueryParameter parameter;
    private final Object value;


    public EqualCondition(QueryParameter parameter, Object value) {
        this.parameter = parameter;
        this.value = escpaedValue(value);
    }

    protected Object escpaedValue(Object value) {
        if (value instanceof String) {
            String stringValue = (String) value;
            StringBuilder stringBuilder = new StringBuilder();

            for (char c : stringValue.toCharArray()) {
                if (ESCAPE_TABLE.containsKey(c)) {
                    stringBuilder.append(ESCAPE_TABLE.get(c));
                } else {
                    stringBuilder.append(c);
                }
            }
            return stringBuilder.toString();
        }
        return value;
    }

    @Override
    public QueryParameter getParameter() {
        return this.parameter;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "EqualCondition{" +
                "parameter=" + parameter.getName() +
                ", value=" + value +
                '}';
    }
}
