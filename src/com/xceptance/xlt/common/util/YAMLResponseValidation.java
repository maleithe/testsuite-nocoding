package com.xceptance.xlt.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YAMLResponseValidation
{
    public static final String RESPONSEVALIDATOR = "Validate";

    private final ArrayList<YAMLValidator> validatorList;

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
            final ArrayList<Object> _validationList = (ArrayList) yamlResponse.get(RESPONSEVALIDATOR);

            this.validatorList = new ArrayList<YAMLValidator>();
            for(final Object validator : _validationList)
            {
                this.validatorList.add(new YAMLValidator(validator));
            }
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
