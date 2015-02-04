package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.util.Map;

import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class YAMLAction
{
    public static final String NAME = "Name";

    public static final String REQUEST = "Request";
    
    public static final String SUBREQUEST = "Subrequest";
    
    public static final String RESPONSE = "Response";

    public static final String TYPE_ACTION_YAML_NOTATION = "Action";

    public static final String TYPE_ACTION = "A";

    private final String name;

    private final YAMLRequest yamlRequest;
    
    private final String type;
    
    private final YAMLResponse yamlResponse;
    
    private final YAMLSubRequest yamlSubRequest;
    
    /**
     * encapsulate the whole YAML Action
     * 
     * @param yamlRecord
     *            the record from YAML to process
     * @param interpreter
     *            the bean shell interpreter to use
     * @throws MalformedURLException
     */
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
            this.yamlRequest = yamlAction.containsKey(REQUEST) ? new YAMLRequest(yamlAction, interpreter) : null;
            this.yamlResponse = yamlAction.containsKey(RESPONSE) ? new YAMLResponse(yamlAction, interpreter) : null;
            this.yamlSubRequest = yamlAction.containsKey(SUBREQUEST) ? new YAMLSubRequest(yamlAction, interpreter) : null;
        }
        else
        {
            this.name = null;
            this.yamlRequest = null;
            this.yamlResponse = null;
            this.type = null;
            this.yamlSubRequest = null;
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
    
    public YAMLResponse getYAMLResponse()
    {
        return this.yamlResponse;
    }
    
    public String getType()
    {
        return this.type;
    }
    
    public YAMLSubRequest getYAMLSubRequest()
    {
        return this.yamlSubRequest;
    }
    
    public boolean YAMLActionExists()
    {
        return this.type != null;
    }
    
    public boolean YAMLNameExists()
    {
        return this.name != null;
    }
    
    public boolean YAMLRequestExists()
    {
        return this.yamlRequest != null;
    }

    public boolean YAMLResponseExists()
    {
        return this.yamlResponse != null;
    }
}
