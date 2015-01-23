package com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YAMLResponseValidation
{
    public static final String RESPONSEVALIDATOR = "Validate";
    
    private final List<YAMLValidator> validatorList;

    /**
     * encapsulate the YAML Validator List
     * 
     * @param yamlResponse
     *            the record from YAML to process
     */    
    public YAMLResponseValidation(final Map<String, Object> yamlResponse)
    {       
        if (yamlResponse.get(RESPONSEVALIDATOR) instanceof ArrayList && yamlResponse.get(RESPONSEVALIDATOR) != null)
        {
            //this.validatorList = (ArrayList) yamlResponse.get(RESPONSEVALIDATOR);
            
            this.validatorList.add(new YAMLValidator((ArrayList)yamlResponse.get(RESPONSEVALIDATOR))); 
            
            System.out.println(this.validatorList);
            System.out.println(this.validatorList.get(0));
            
            final Map<String, Object> listElement = (Map) this.validatorList.get(0);
            System.out.println(listElement.get("Header"));
            
            final Map<String, Object> xpath = (Map) listElement.get("Header");
            System.out.println(xpath.containsKey("Xpath"));
            System.out.println(xpath.get("Xpath"));
            
            
            //this.validatorList.add();
            
            
        }
        else
        {
            this.validatorList = null;
        }
    }
    
    public List<YAMLValidator> getYAMLResponseValidatorList()
    {
        return this.validatorList;
    }
    
    public boolean YAMLResponseValidationListExists()
    {
        return this.validatorList != null;
    }
}
