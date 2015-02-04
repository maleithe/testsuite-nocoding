package com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.Map;

public class YAMLSubRequestElement
{
    public static final String STATIC = "Static";

    // TODO create an object for XHR

    private final ArrayList<String> staticUrl;

    /**
     * encapsulate the YAML SubRequestElement
     * 
     * @param yamlSubRequestElement
     *            the record from YAML to process
     */
    public YAMLSubRequestElement(final Object yamlSubRequestElement)
    {
        final ArrayList<String> _yamlSubRequestElement = (ArrayList) ((Map) yamlSubRequestElement).get(STATIC);

        this.staticUrl = new ArrayList<String>();
        for (final String url : _yamlSubRequestElement)
        {
            this.staticUrl.add(url);
        }
    }
    
    public ArrayList<String> getYAMLSubRequestElement()
    {
        return this.staticUrl;
    }

}
