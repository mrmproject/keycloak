package org.keycloak.federation.ldap.idm.query.internal;

import java.util.Date;

import org.keycloak.federation.ldap.idm.store.ldap.LDAPUtil;
import org.keycloak.models.LDAPConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Pedro Igor
 */
public class EqualCondition extends NamedParameterCondition {

    private static Map<Character, String> ESCAPE_TABLE;
    // ldap query escaped characters
    static {
        ESCAPE_TABLE = new HashMap<>();
        ESCAPE_TABLE.put('\\', "\\5c");
        ESCAPE_TABLE.put('*', "\\2a");
        ESCAPE_TABLE.put('(', "\\28");
        ESCAPE_TABLE.put(')', "\\29");
    }

    private final Object value;

    public EqualCondition(String name, Object value) {
        super(name);
        this.value = escpaedValue(value);;
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

    public Object getValue() {
        return this.value;
    }

    @Override
    public void applyCondition(StringBuilder filter) {
        Object parameterValue = value;
        if (Date.class.isInstance(value)) {
            parameterValue = LDAPUtil.formatDate((Date) parameterValue);
        }

        filter.append("(").append(getParameterName()).append(LDAPConstants.EQUAL).append(parameterValue).append(")");
    }

    @Override
    public String toString() {
        return "EqualCondition{" +
                "paramName=" + getParameterName() +
                ", value=" + value +
                '}';
    }
}
