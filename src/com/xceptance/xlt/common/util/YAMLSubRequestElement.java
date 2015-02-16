package com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.Map;

public class YAMLSubRequestElement
{
    public static final String STATIC = "Static";

    public static final String XHR = "XHR";

    // TODO create an object for XHR

    private final ArrayList<String> staticUrl;

    private final Map<String, Object> xhrUrl;

    /**
     * encapsulate the YAML SubRequestElement
     * 
     * @param yamlSubRequestElement
     *            the record from YAML to process
     */
    public YAMLSubRequestElement(final Object yamlSubRequestElement)
    {
        final Map<?, ?> _yamlSubRequestElementType = (Map) yamlSubRequestElement;
        final ArrayList<String> _yamlSubRequestElementStatic;

        boolean isStatic = false;
        boolean isXHR = false;

        if (_yamlSubRequestElementType.containsKey(STATIC))
        {
            System.out.println("ist static");
            _yamlSubRequestElementStatic = (ArrayList) ((Map) yamlSubRequestElement).get(STATIC);
            this.xhrUrl = null;
            isStatic = true;
        }
        else if (_yamlSubRequestElementType.containsKey(XHR))
        {
            System.out.println("ist XHR");
            this.xhrUrl = (Map) ((Map) yamlSubRequestElement).get(XHR);
            _yamlSubRequestElementStatic = null;
            isXHR = true;
        }
        else
        {
            System.out.println("ist null");
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

}
