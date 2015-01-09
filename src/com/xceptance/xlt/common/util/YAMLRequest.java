package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.xceptance.xlt.common.util.bsh.ParamInterpreter;


public class YAMLRequest
{
    private final URL url;

    private final String urlString;

    private final String method;
    
    private final boolean encoded;
    
    public YAMLRequest(final Map<String, Object> yamlAction, final ParamInterpreter interpreter) throws MalformedURLException
    {
        if(yamlAction instanceof Map && yamlAction != null)
        {
            final Map<String, ?> yamlRequest = (Map<String, ?>) yamlAction.get("Requests");
            
            if(yamlRequest.containsKey("URL"))
            {
                this.urlString = yamlRequest.get("URL").toString();
                this.url = interpreter == null ? new URL(this.urlString) : null;
            }
            else
            {
                this.urlString = null;
                this.url = null;
            }
            
            final String _method;
            if(yamlRequest.containsKey("Type"))
            {
                _method = yamlRequest.get("Type").toString();
                this.method = _method.equals("GET") ? "GET" : "POST";
            }
            else
            {
                this.method = null;
            }
            
            final String _encoded;
            if(yamlRequest.containsKey("Encoded"))
            {
                _encoded = yamlRequest.get("Encoded").toString();
                this.encoded = _encoded.contains("true") ? true : false;
            }
            else
            {
                this.encoded = false;
            }
        }
        else
        {
            this.urlString = null;
            this.url = null;
            this.method = null;
            this.encoded = false;
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
}
