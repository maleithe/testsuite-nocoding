package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class YAMLBasedURLAction
{
    // dynamic parameters
    public static final String XPATH_GETTER_PREFIX = "xpath";

    public static final String REGEXP_GETTER_PREFIX = "regexp";

    public static final int DYNAMIC_GETTER_COUNT = 10;

    /**
     * Permitted header fields, checked to avoid accidental incorrect spelling
     */
    private final static Set<String> PERMITTEDHEADERFIELDS = new HashSet<String>();

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String TYPE = "Type";

    public static final String TYPE_ACTION = "A";

    public static final String TYPE_STATIC = "S";

    public static final String TYPE_XHR_ACTION = "XA";

    public static final String NAME = "Name";

    public static final String URL = "URL";

    public static final String METHOD = "Method";

    public static final String PARAMETERS = "Parameters";

    public static final String RESPONSECODE = "ResponseCode";

    public static final String XPATH = "XPath";

    public static final String REGEXP = "RegExp";

    public static final String TEXT = "Text";

    public static final String ENCODED = "Encoded";

    static
    {
        PERMITTEDHEADERFIELDS.add(TYPE);
        PERMITTEDHEADERFIELDS.add(NAME);
        PERMITTEDHEADERFIELDS.add(URL);
        PERMITTEDHEADERFIELDS.add(METHOD);
        PERMITTEDHEADERFIELDS.add(PARAMETERS);
        PERMITTEDHEADERFIELDS.add(RESPONSECODE);
        PERMITTEDHEADERFIELDS.add(XPATH);
        PERMITTEDHEADERFIELDS.add(REGEXP);
        PERMITTEDHEADERFIELDS.add(TEXT);
        PERMITTEDHEADERFIELDS.add(ENCODED);

        for (int i = 1; i <= DYNAMIC_GETTER_COUNT; i++)
        {
            PERMITTEDHEADERFIELDS.add(XPATH_GETTER_PREFIX + i);
            PERMITTEDHEADERFIELDS.add(REGEXP_GETTER_PREFIX + i);
        }
    }

    private final String type;

    private final String name;

    private final URL url;

    private final String urlString;

    private final String method;

    // private final List<NameValuePair> parameters;

    // private final HttpResponseCodeValidator httpResponseCodeValidator;

    // private final String xPath;

    // private final String regexpString;

    // private final Pattern regexp;

    // private final String text;

    private final boolean encoded;

    // private final List<String> xpathGetterList = new ArrayList<String>(DYNAMIC_GETTER_COUNT);

    // private final List<String> regexpGetterList = new ArrayList<String>(DYNAMIC_GETTER_COUNT);

    /**
     * Our bean shell
     */
    private final ParamInterpreter interpreter;

    /**
     * Constructor based upon read YAML data
     * 
     * @param interpreter
     *            the bean shell interpreter to use
     * @throws MalformedURLException
     */
    public YAMLBasedURLAction(final Object object, final ParamInterpreter interpreter) throws MalformedURLException
    {
        // no bean shell, so we do not do anything, satisfy final here
        this.interpreter = interpreter;

        // TODO extend the types and build a fall back
        // check the type
        String _type;
        if (((Map) object).containsKey("Action"))
        {
            _type = TYPE_ACTION;
        }
        else
        {
            // FIXME not a fallback
            _type = TYPE_ACTION;
        }

        this.type = _type;

        final Object yamlAction = ((Map) object).get("Action");

        // TODO autonaming if no Name is available
        this.name = ((Map) yamlAction).get(NAME).toString();

        // we need at least an url, stop here of not given
        final Object yamlRequest = ((Map) yamlAction).get("Requests");
        this.urlString = ((Map) yamlRequest).get(URL).toString();
        // TODO IllegalArgumentException if urlString is null

        this.url = interpreter == null ? new URL(this.urlString) : null;

        // TODO extend the methods (GET or POST) and build a fall back if no method
        // check the method
        final String _method;
        _method = ((Map) yamlRequest).get("Type").toString();
        this.method = _method.contains(GET) ? GET : POST;

        final String _encoded = ((Map) yamlRequest).get(ENCODED).toString();
        this.encoded = _encoded.contains("true") ? true : false;

        // System.out.println(interpreter);
        // System.out.println(name);
        // System.out.println(type);
        // System.out.println(urlString);
        // System.out.println(url);
        // System.out.println(method);
        // System.out.println(encoded);

    }
    
    /**
     * Returns true if this is an action to be executed
     * 
     * @return true if this is an action
     */
    public boolean isAction()
    {
        return type.equals(TYPE_ACTION);
    }
    
    /**
     * Returns the name of this line.
     * 
     * @return the name of this line
     */
    public String getName(final AbstractURLTestCase testCase)
    {
        return interpreter != null ? interpreter.processDynamicData(testCase, name) : name;
    }
}
