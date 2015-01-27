package com.xceptance.xlt.common.util;


public class Validator
{
    private final String name;

    private final String xpath;

    private final String text;
    
    /**
     * encapsulate the Common Validator
     * 
     * @param validator
     *            input fromYAML,... etc.
     */
    public Validator(final YAMLValidator validator)
    {

        System.out.println(validator.getValidatorName());
        System.out.println(validator.getValidatorXpath());
        System.out.println(validator.getValidatorText());
        
        this.name = validator.getValidatorName();
        this.xpath = validator.getValidatorXpath();
        this.text = validator.getValidatorText();
    }
}
