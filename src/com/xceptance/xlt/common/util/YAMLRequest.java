package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class YAMLRequest
{
    public static final String REQUEST = "Request";

    public static final String URL = "Url";

    public static final String GET = "GET";

    public static final String POST = "POST";

    public static final String TYPE = "Type";

    public static final String ENCODED = "Encoded";

    public static final String TRUE = "true";

    public static final String PARAMETERS = "Parameters";

    private final URL url;

    private final String urlString;

    private final String method;

    private final boolean encoded;

    private final YAMLRequestParams yamlRequestParams;
    
    /**
     * encapsulate the YAML Request
     * 
     * @param yamlAction
     *            the record from YAML to process
     * @param interpreter
     *            the bean shell interpreter to use
     * @throws MalformedURLException
     */
    public YAMLRequest(final Map<String, Object> yamlAction, final ParamInterpreter interpreter)
        throws MalformedURLException
    {
        if (yamlAction instanceof Map && yamlAction != null)
        {
            final Map<String, Object> yamlRequest = (Map<String, Object>) yamlAction.get(REQUEST);

            if (yamlRequest.containsKey(URL))
            {
                this.urlString = yamlRequest.get(URL).toString();
                this.url = interpreter == null ? new URL(this.urlString) : null;
            }
            else
            {
                this.urlString = null;
                this.url = null;
            }

            final String _method;
            if (yamlRequest.containsKey(TYPE))
            {
                _method = yamlRequest.get(TYPE).toString();
                this.method = _method.equals(GET) ? GET : POST;
            }
            else
            {
                this.method = null;
            }

            final String _encoded;
            if (yamlRequest.containsKey(ENCODED))
            {
                _encoded = yamlRequest.get(ENCODED).toString();
                this.encoded = _encoded.contains(TRUE) ? true : false;
            }
            else
            {
                this.encoded = false;
            }

            if (yamlRequest.containsKey(PARAMETERS))
            {
                this.yamlRequestParams = new YAMLRequestParams(yamlRequest);
            }
            else
            {
                this.yamlRequestParams = null;
            }
        }
        else
        {
            this.urlString = null;
            this.url = null;
            this.method = null;
            this.encoded = false;
            this.yamlRequestParams = null;
        }
    }

    public String getURLString()
    {
        return this.urlString;
    }

    public URL getURL()
    {
        return this.url;
    }

    public String getMethod()
    {
        return this.method;
    }

    public boolean getEncoded()
    {
        return this.encoded;
    }

    public YAMLRequestParams getYAMLRequestParams()
    {
        return this.yamlRequestParams;
    }

    public boolean YAMLRequestParamsExists()
    {
        return this.yamlRequestParams != null;
    }
    
    public boolean YAMLUrlStringExists()
    {
        return this.urlString != null;
    }
    
    public boolean YAMLMethodExists()
    {
        return this.method != null;
    }
}
