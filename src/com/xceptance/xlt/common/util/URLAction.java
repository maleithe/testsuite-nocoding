package com.xceptance.xlt.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.xceptance.xlt.api.util.XltLogger;
import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class URLAction
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

    public static final String TYPE_ACTION_YAML_NOTATION = "Action";

    public static final String TYPE_STATIC_YAML_NOTATION = "Static";

    public static final String TYPE_XHR_ACTION_YAML_NOTATION = "XHR";

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

    private final List<NameValuePair> parameters;

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
     * Constructor based upon read CSV data
     * 
     * @param record
     *            the record from CSV to process
     * @param object
     *            the record from YAML to process
     * @param interpreter
     *            the bean shell interpreter to use
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     */

    public URLAction(final CSVRecord record, final Map<String, Object> yamlRecord, final ParamInterpreter interpreter)
        throws UnsupportedEncodingException, MalformedURLException
    {
        // check if using YAML or CSV
        final boolean useYaml = XltProperties.getInstance().getProperty("useYAML", false);

        if (useYaml == true)
        {
            // no bean shell, so we do not do anything, satisfy final here
            this.interpreter = interpreter;
            
            //TODO extend for S and XR
            
            final String _type;
            if (yamlRecord instanceof Map && yamlRecord != null)
            {
                // check the type
                if (yamlRecord.containsKey(TYPE_ACTION_YAML_NOTATION))
                {
                    this.type = TYPE_ACTION;
                    //final Map<String, Object> yamlAction = (Map<String, Object>) yamlRecord.get(TYPE_ACTION_YAML_NOTATION);
                }
                else
                {
                    XltLogger.runTimeLogger.warn("Unknown type for YAML, defaulting to 'Action'");
                    this.type = TYPE_ACTION;
                }
            }
            else
            {
                this.type = null;
            }
            
            //TODO extend for S and XR
            final Map<String, Object> yamlAction = (Map<String, Object>) yamlRecord.get(TYPE_ACTION_YAML_NOTATION);

            // TODO autonaming if no Name is available
            this.name = yamlAction.get(NAME).toString();

            // we need at least an url, stop here of not given
            final Map<String, ?> yamlRequest = (Map<String, ?>) yamlAction.get("Requests");
            this.urlString = ((Map<String, ?>) yamlRequest).get(URL).toString();
            // TODO IllegalArgumentException if urlString is null

            this.url = interpreter == null ? new URL(this.urlString) : null;

            // TODO extend the methods (GET or POST) and build a fall back if no method
            // check the method
            final String _method;
            _method = ((Map<String, ?>) yamlRequest).get("Type").toString();
            this.method = _method.contains(GET) ? GET : POST;

            final String _encoded = ((Map<String, ?>) yamlRequest).get(ENCODED).toString();
            this.encoded = _encoded.contains("true") ? true : false;

            final Map<String, ?> yamlParams = (Map<String, ?>) ((Map<String, ?>) yamlRequest).get(PARAMETERS);
            this.parameters = !(yamlParams == null) && !yamlParams.isEmpty() ? setupYAMLParameters(yamlParams) : null;
        }
        else
        {
            // no bean shell, so we do not do anything, satisfy final here
            this.interpreter = interpreter;

            // the header is record 1, so we have to subtract one, for autonaming
            this.name = StringUtils.defaultIfBlank(record.get(NAME), "Action-" + (record.getRecordNumber() - 1));

            // take care of type
            String _type = StringUtils.defaultIfBlank(record.get(TYPE), TYPE_ACTION);
            if (!_type.equals(TYPE_ACTION) && !_type.equals(TYPE_STATIC) && !_type.equals(TYPE_XHR_ACTION))
            {
                XltLogger.runTimeLogger.warn(MessageFormat.format("Unknown type '{0}' in line {1}, defaulting to 'A'",
                                                                  _type, record.getRecordNumber()));
                _type = TYPE_ACTION;
            }
            this.type = _type;

            // we need at least an url, stop here of not given
            this.urlString = record.get(URL);
            if (this.urlString == null)
            {
                throw new IllegalArgumentException(
                                                   MessageFormat.format("No url given in record in line {0}. Need at least that.",
                                                                        record.getRecordNumber()));
            }
            this.url = interpreter == null ? new URL(this.urlString) : null;

            // take care of method
            String _method = StringUtils.defaultIfBlank(record.get(METHOD), GET);
            if (!_method.equals(GET) && !_method.equals(POST))
            {
                XltLogger.runTimeLogger.warn(MessageFormat.format("Unknown method '{0}' in line {1}, defaulting to 'GET'",
                                                                  _method, record.getRecordNumber()));
                _method = GET;
            }
            this.method = _method;

            // get the response code validator
            // this.httpResponseCodeValidator = StringUtils.isNotBlank(record.get(RESPONSECODE)) ? new
            // HttpResponseCodeValidator(
            // Integer.parseInt(record.get(RESPONSECODE)))
            // : HttpResponseCodeValidator.getInstance();
            //
            // // compile pattern only, if no interpreter shall be used
            // this.regexpString = StringUtils.isNotEmpty(record.get(REGEXP)) ? record.get(REGEXP) : null;
            // if (interpreter == null)
            // {
            // this.regexp = StringUtils.isNotEmpty(regexpString) ? RegExUtils.getPattern(regexpString) : null;
            // }
            // else
            // {
            // this.regexp = null;
            // }
            //
            // this.xPath = StringUtils.isNotBlank(record.get(XPATH)) ? record.get(XPATH) : null;
            // this.text = StringUtils.isNotEmpty(record.get(TEXT)) ? record.get(TEXT) : null;
            this.encoded = StringUtils.isNotBlank(record.get(ENCODED)) ? Boolean.parseBoolean(record.get(ENCODED))
                                                                      : false;

            // ok, get all the parameters
            // for (int i = 1; i <= DYNAMIC_GETTER_COUNT; i++)
            // {
            // xpathGetterList.add(StringUtils.isNotBlank(record.get(XPATH_GETTER_PREFIX + i)) ?
            // record.get(XPATH_GETTER_PREFIX
            // + i)
            // : null);
            // regexpGetterList.add(StringUtils.isNotBlank(record.get(REGEXP_GETTER_PREFIX + i)) ?
            // record.get(REGEXP_GETTER_PREFIX
            // + i)
            // : null);
            // }

            // ok, this is the tricky part
            this.parameters = StringUtils.isNotBlank(record.get(PARAMETERS)) ? setupCSVParameters(record.get(PARAMETERS))
                                                                            : null;
        }

    }

    /**
     * Takes the list of parameters and turns it into name value pairs for later usage.
     * 
     * @param paramers
     *            the csv definition string of parameters
     * @return a list with parsed key value pairs
     * @throws UnsupportedEncodingException
     */
    private List<NameValuePair> setupCSVParameters(final String parameters) throws UnsupportedEncodingException
    {
        final List<NameValuePair> list = new ArrayList<NameValuePair>();

        // ok, turn them into & split strings
        final StringTokenizer tokenizer = new StringTokenizer(parameters, "&");
        while (tokenizer.hasMoreTokens())
        {
            final String token = tokenizer.nextToken();

            // the future pair
            String key = null;
            String value = null;

            // split it into key and value at =
            final int pos = token.indexOf("=");
            if (pos >= 0)
            {
                key = token.substring(0, pos);
                if (pos < token.length() - 1)
                {
                    value = token.substring(pos + 1);
                }
            }
            else
            {
                key = token;
            }

            // ok, if this is encoded, we have to decode it again, because the httpclient will encode it
            // on its own later on
            if (encoded)
            {
                key = key != null ? URLDecoder.decode(key, "UTF-8") : null;
                value = value != null ? URLDecoder.decode(value, "UTF-8") : "";
            }
            if (key != null)
            {
                list.add(new NameValuePair(key, value));
            }
        }

        return list;
    }

    /**
     * Takes the map of parameters and turns it into name value pairs for later usage.
     * 
     * @param paramers
     *            the yaml definition string of parameters
     * @return a list with parsed key value pairs
     * @throws UnsupportedEncodingException
     */
    private List<NameValuePair> setupYAMLParameters(final Map<String, ?> parameters)
    {
        final List<NameValuePair> list = new ArrayList<NameValuePair>();

        for (final String parameterKey : parameters.keySet())
        {
            list.add(new NameValuePair(parameterKey.toString(), parameters.get(parameterKey).toString()));
        }

        return list;
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
     * Returns true of header field is value, false otherwise.
     * 
     * @param fieldName
     *            header field to check
     * @return true if valid field, false otherwise TODO test it
     */
    public static boolean isPermittedHeaderField(final String fieldName)
    {
        return PERMITTEDHEADERFIELDS.contains(fieldName);
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

    /**
     * Returns the url of that action. Is required.
     * 
     * @param testCase
     *            for the correct data resulution
     * @return the url with data resolution
     * @throws MalformedURLException
     */
    public URL getURL(final AbstractURLTestCase testCase) throws MalformedURLException
    {
        // process bean shell part
        return interpreter != null ? new URL(interpreter.processDynamicData(testCase, urlString)) : url;
    }

    public HttpMethod getMethod()
    {
        if (this.method.equals(POST))
        {
            return HttpMethod.POST;
        }
        else
        {
            return HttpMethod.GET;
        }
    }

    /**
     * Returns the active interpreter. Important for testing.
     * 
     * @return The active bean interpreter.
     */
    public ParamInterpreter getInterpreter()
    {
        return interpreter;
    }

    public List<NameValuePair> getParameters(final AbstractURLTestCase testCase)
    {
        // process bean shell part
        if (interpreter != null && parameters != null)
        {
            // create new list
            final List<NameValuePair> result = new ArrayList<NameValuePair>(parameters.size());

            // process all
            for (final NameValuePair pair : parameters)
            {
                final String name = interpreter.processDynamicData(testCase, pair.getName());
                String value = pair.getValue();
                value = value != null ? interpreter.processDynamicData(testCase, value) : value;

                result.add(new NameValuePair(name, value));
            }

            return result;
        }
        else
        {
            return parameters;
        }
    }
}
