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

    
    public YAMLRequestParams(final Map<String, Object> yamlRequest)
    {
        //System.out.println(yamlRequest.get(PARAMETERS));
        
        if (yamlRequest.get(PARAMETERS) instanceof ArrayList && yamlRequest.get(PARAMETERS) != null)
        {
            final ArrayList _yamlParams = (ArrayList) yamlRequest.get(PARAMETERS);
            
            System.out.println(_yamlParams);
            
            this.paramList = !(_yamlParams == null) && !_yamlParams.isEmpty() ? setupYAMLParameters(_yamlParams) : null;
        }
        else
        {
            this.paramList = null;
        }
        
        //System.out.println(paramList);
    }
    
    /**
     * Takes the map of parameters and turns it into name value pairs for later usage.
     * 
     * @param paramers
     *            the yaml definition string of parameters
     * @return a list with parsed key value pairs
     * @throws UnsupportedEncodingException
     */
    private List<NameValuePair> setupYAMLParameters(final ArrayList parameters)
    {
        final List<NameValuePair> list = new ArrayList<NameValuePair>();

        System.out.println(parameters);
        
//        for (final String parameterKey : parameters.keySet())
//        {
//            list.add(new NameValuePair(parameterKey.toString(), parameters.get(parameterKey).toString()));
//        }

        return list;
    }
    
    public List<NameValuePair> getParamList()
    {
        return this.paramList;
    }

}
