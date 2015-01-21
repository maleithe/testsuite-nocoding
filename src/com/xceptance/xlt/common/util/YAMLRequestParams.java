package com.xceptance.xlt.common.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class YAMLRequestParams
{
    public static final String PARAMETERS = "Parameters";
    
    private final List<NameValuePair> paramList;

    /**
     * encapsulate the YAML Parameter List
     * 
     * @param yamlRequest
     *            the record from YAML to process
     * @param interpreter
     *            the bean shell interpreter to use
     */    
    public YAMLRequestParams(final Map<String, Object> yamlRequest)
    {       
        if (yamlRequest.get(PARAMETERS) instanceof ArrayList && yamlRequest.get(PARAMETERS) != null)
        {
            final ArrayList _yamlParams = (ArrayList) yamlRequest.get(PARAMETERS);            
            this.paramList = setupYAMLParameters(_yamlParams);
        }
        else
        {
            this.paramList = null;
        }
    }
    
    /**
     * Takes the map of parameters and turns it into name value pairs for later usage.
     * 
     * @param parameters
     *            the yaml definition string of parameters
     * @return a list with parsed key value pairs
     * @throws UnsupportedEncodingException
     */
    private List<NameValuePair> setupYAMLParameters(final List parameters)
    {
        final List<NameValuePair> list = new ArrayList<NameValuePair>();
        
        for (final Object object : parameters)
        {
            final Map<String, String> map = (Map<String, String>) object;
            for (final String key : map.keySet())
            {
                list.add(new NameValuePair(key.toString(), map.get(key).toString()));
            }   
        }
         
        return list;
    }
    
    public List<NameValuePair> getParamList()
    {
        return this.paramList;
    }
}
