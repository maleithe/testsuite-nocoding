package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.util.Map;

import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class YAMLResponse
{
    public static final String RESPONSE = "Response";

    public static final String HTTPRESPONSECODE = "Httpcode";
    
    public static final String RESPONSEVALIDATE = "Validate";

    private final int httpResponseCode;
    
    private final YAMLResponseValidation yamlResponseValidation;

    /**
     * encapsulate the YAML Response
     * 
     * @param yamlAction
     *            the record from YAML to process
     * @param interpreter
     *            the bean shell interpreter to use
     * @throws MalformedURLException
     */
    public YAMLResponse(final Map<String, Object> yamlAction, final ParamInterpreter interpreter)
        throws MalformedURLException
    {
        if (yamlAction instanceof Map && yamlAction != null)
        {
            final Map<String, Object> yamlResponse = (Map<String, Object>) yamlAction.get(RESPONSE);

            if (yamlResponse.containsKey(HTTPRESPONSECODE))
            {
                this.httpResponseCode = (Integer) yamlResponse.get(HTTPRESPONSECODE);
            }
            else
            {
                // set 200 as default
                this.httpResponseCode = 200;
            }
            
            if (yamlResponse.containsKey(RESPONSEVALIDATE))
            {
                this.yamlResponseValidation = new YAMLResponseValidation(yamlResponse);
            }
            else
            {
                this.yamlResponseValidation = null;
            }
        }
        else
        {
            // set 200 as default
            this.httpResponseCode = 200;
            this.yamlResponseValidation = null;
        }
    }

    public int getHttpResponseCode()
    {
        return this.httpResponseCode;
    }
    
    public YAMLResponseValidation getYAMLResponseValidation()
    {
        return this.yamlResponseValidation;
    }
    
    public boolean YAMLResponseValidationExists()
    {
        return this.yamlResponseValidation != null;
    }

}
