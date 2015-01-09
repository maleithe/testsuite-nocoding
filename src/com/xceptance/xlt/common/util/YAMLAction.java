package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.util.Map;

import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class YAMLAction
{
    public static final String NAME = "Name";

    public static final String TYPE_ACTION_YAML_NOTATION = "Action";

    public static final String TYPE_ACTION = "A";

    private final String name;

    private final YAMLRequest yamlRequest;
    
    private final String type;

    public YAMLAction(final Map<String, Object> yamlRecord, final ParamInterpreter interpreter) throws MalformedURLException
    {
        if (yamlRecord instanceof Map && yamlRecord != null)
        {
            final Map<String, Object> yamlAction;
            if(yamlRecord.containsKey(TYPE_ACTION_YAML_NOTATION))
            {
                yamlAction = (Map<String, Object>) yamlRecord.get(TYPE_ACTION_YAML_NOTATION);
                this.type = TYPE_ACTION;
            }
            else
            {
                yamlAction = null;
                this.type = null;
            }

            //TODO autonaming
            this.name = yamlAction.containsKey(NAME) ? yamlAction.get(NAME).toString() : null;

            this.yamlRequest = new YAMLRequest(yamlAction, interpreter);
        }
        else
        {
            this.name = null;
            this.yamlRequest = null;
            this.type = null;
        }

    }

    public String getYAMLName()
    {
        return this.name;
    }

    public YAMLRequest getYAMLRequest()
    {
        return this.yamlRequest;
    }
    
    public String getType()
    {
        return this.type;
    }

}
