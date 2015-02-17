package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;

import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class YAMLSubRequestElement
{
    public static final String STATIC = "Static";

    public static final String XHR = "XHR";

    private final ArrayList<String> staticUrl;

    private final YAMLRequest xhrUrl;

    /**
     * encapsulate the YAML SubRequestElement
     * 
     * @param yamlSubRequestElement
     *            the record from YAML to process
     * @throws MalformedURLException 
     */
    public YAMLSubRequestElement(final Object yamlSubRequestElement, final ParamInterpreter interpreter) throws MalformedURLException
    {
        final Map<?, ?> _yamlSubRequestElementType = (Map) yamlSubRequestElement;
        final ArrayList<String> _yamlSubRequestElementStatic;

        boolean isStatic = false;

        if (_yamlSubRequestElementType.containsKey(STATIC))
        {
            _yamlSubRequestElementStatic = (ArrayList) ((Map) yamlSubRequestElement).get(STATIC);
            this.xhrUrl = null;
            isStatic = true;
        }
        else if (_yamlSubRequestElementType.containsKey(XHR))
        {
            this.xhrUrl = new YAMLRequest((Map) ((Map) yamlSubRequestElement).get(XHR), interpreter);
            _yamlSubRequestElementStatic = null;
        }
        else
        {
            _yamlSubRequestElementStatic = null;
            this.xhrUrl = null;
        }

        this.staticUrl = new ArrayList<String>();

        // write static URLs to constructor
        if (isStatic)
        {
            for (final String url : _yamlSubRequestElementStatic)
            {
                this.staticUrl.add(url);
            }
        }
    }

    public ArrayList<String> getYAMLSubRequestElement()
    {
        return this.staticUrl;
    }

    public boolean isStaticContentURL()
    {
        return !this.staticUrl.isEmpty();
    }

    public boolean isXHR()
    {
        return this.xhrUrl != null;
    }
    
    public YAMLRequest getYAMLxhrURL()
    {
        return this.xhrUrl;
    }
    
}
