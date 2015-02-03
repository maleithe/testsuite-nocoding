package com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.Map;

public class YAMLSubRequestElement
{
    public static final String STATIC = "Static";
    
    //TODO create an object for XHR
    
    private final String staticUrl;
    
    /**
     * encapsulate the YAML SubRequestElement
     * 
     * @param yamlSubRequestElement
     *            the record from YAML to process
     */
    public YAMLSubRequestElement(final Object yamlSubRequestElement)
    {
        System.out.println(((ArrayList) ((Map) yamlSubRequestElement).get(STATIC)).get(0));
        System.out.println(((ArrayList) ((Map) yamlSubRequestElement).get(STATIC)).get(1));
        System.out.println(((ArrayList) yamlSubRequestElement).get(1));
        
        final Map<String, Object> _yamSubRequestElement = (Map) yamlSubRequestElement;

        System.out.println(_yamSubRequestElement);
        System.out.println(_yamSubRequestElement.getClass());
        
        // get the xpath if available
//        this.staticUrl = ((Map) _yamSubRequestElement.get(this.name)).containsKey(XPATH) ? (String) ((Map) _validator.get(this.name)).get(XPATH)
//                                                                         : null;


        
        this.staticUrl = _yamSubRequestElement.get(STATIC).toString();
        
        System.out.println(this.staticUrl);
        
    }

}
