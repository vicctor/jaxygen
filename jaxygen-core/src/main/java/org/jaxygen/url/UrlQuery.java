package org.jaxygen.url;


import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.jaxygen.exceptions.UrlParseException;

/**Class represents a URL query.
 * @author Artur Keska
 *
 */
public class UrlQuery {

    private Map<String, String> parameters = new HashMap<String, String>();
    static char[] reservedChars = {':', '/', '?', '#', '[', ']',
        '@', '!', '$', '&', '\'', '(', ')',
        '+', ',', ';', '=', '~',
        '\\', '{', '}', '|', '<', '>', '`', '^', '.',
        ' '};

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the parameters
     */
    public Map<String, String> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        String rc = "";
        for (String key : parameters.keySet()) {
            String value = parameters.get(key);
            if(value == null) {
                value = "";
            }
            value = value.replace("%", "%" + Integer.toHexString('%'));
            for (char c : reservedChars) {
                key = key.replace("" + c, "%" + Integer.toHexString(c));
                value = value.replace("" + c, "%" + Integer.toHexString(c));
            }
            rc += key + "=" + value + "&";
        }
        return rc;
    }

    public void add(String name, String value) {
        parameters.put(name, value);
    }

    // Parse URL Query string part
    public static UrlQuery parse(final String query) throws UrlParseException {
        Map<String, String> params = new HashMap<String, String>();
        if (!query.isEmpty()) {
            try {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    if (pair != null && pair.length > 0) {
                        String key = URLDecoder.decode(pair[0], "UTF-8");
                        String value = null;
                        if (pair.length > 1) {
                            value = URLDecoder.decode(pair[1], "UTF-8");
                        }
                        params.put(key, value);
                    }
                }
            } catch (Exception ex) {
                throw new UrlParseException("Could not parse URL Query: " + query, ex);
            }
        }
        UrlQuery q = new UrlQuery();
        q.setParameters(params);
        return q;
    }
}
