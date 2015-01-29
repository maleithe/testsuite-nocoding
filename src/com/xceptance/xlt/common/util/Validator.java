package com.xceptance.xlt.common.util;

public class Validator
{
    private final String name;

    private final String xpath;

    private final String text;
    
    private final Integer count;
    
    private final String regex;

    /**
     * encapsulate the Common Validator
     * 
     * @param validator
     *            input fromYAML,... etc.
     */
    public Validator(final YAMLValidator validator)
    {
        this.name = validator.getValidatorName();
        this.xpath = validator.getValidatorXpath();
        this.text = validator.getValidatorText();
        this.count = validator.getValidatorCount();
        this.regex = validator.getValidatorRegex();
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
    
    public String getValidatorRegex()
    {
        return this.regex;
    }
}
