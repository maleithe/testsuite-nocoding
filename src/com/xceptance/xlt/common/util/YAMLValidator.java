package com.xceptance.xlt.common.util;

import java.util.ArrayList;

public class YAMLValidator
{
    private final String xpath;
    
    private final String matcher;
    
    /**
     * encapsulate a YAML Validator
     * 
     * @param yamlValidationList
     *            the record from YAML to process
     * @return yamlValidator
     *            the validator to be checked, contains of xpath and matcher
     */    
    public YAMLValidator(final ArrayList yamlValidationList)
    {       
        this.xpath = null;
        this.matcher = null;
    }
}
