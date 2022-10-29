package octoveau.sso.admin.util;

import org.apache.commons.lang3.StringUtils;

/**
 * StringHelper
 *
 * @author yifanzheng
 */
public class StringHelper {

    private StringHelper() {

    }

    public static String normalize(String value, String defaultValue) {
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        return value;
    }
}
