package com.xceptance.xlt.common.util;

import java.util.Map;

public class YAMLValidator
{
    public static final String XPATH = "Xpath";

    public static final String MATCHES = "Matches";

    public static final String COUNT = "Count";

    private final String name;

    private final String xpath;

    private final String text;

    private final Integer count;

    /**
     * encapsulate the YAML Validator
     * 
     * @param yamlResponse
     *            the record from YAML to process
     */
    public YAMLValidator(final Object validator)
    {
        final Map<String, Object> _validator = (Map) validator;
        String _name = null;

        // get the name of the validation
        for (final String key : _validator.keySet())
        {
            _name = key.toString();
        }
        this.name = _name;

        // get the xpath if available
        this.xpath = ((Map) _validator.get(this.name)).containsKey(XPATH) ? (String) ((Map) _validator.get(this.name)).get(XPATH)
                                                                         : null;
        // get the text for matching if available
        this.text = ((Map) _validator.get(this.name)).containsKey(MATCHES) ? (String) ((Map) _validator.get(this.name)).get(MATCHES)
                                                                          : null;

        // get the count for matching if available
        this.count = ((Map) _validator.get(this.name)).containsKey(COUNT) ? (Integer) ((Map) _validator.get(this.name)).get(COUNT)
                                                                          : null;
    }

    public String getValidatorName()
    {
        return this.name;
    }

    public String getValidatorXpath()
    {
        return this.xpath;
    }

    public String getValidatorText()
    {
        return this.text;
    }
    
    public Integer getValidatorCount()
    {
        return this.count;
    }
}
