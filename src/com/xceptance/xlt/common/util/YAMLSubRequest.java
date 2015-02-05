package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Map;

import com.xceptance.xlt.common.util.bsh.ParamInterpreter;

public class YAMLSubRequest
{
    public final static String SUBREQUEST = "Subrequest";

    private final ArrayList<YAMLSubRequestElement> yamlSubRequestList;

    /**
     * encapsulate the YAML SubRequest
     * 
     * @param yamlAction
     *            the record from YAML to process
     * @param interpreter
     *            the bean shell interpreter to use
     * @throws MalformedURLException
     */
    public YAMLSubRequest(final Map<String, Object> yamlAction, final ParamInterpreter interpreter)
    {
        if (yamlAction.get(SUBREQUEST) instanceof ArrayList && yamlAction.get(SUBREQUEST) != null)
        {
            final ArrayList<Object> _subRequestList = (ArrayList) yamlAction.get(SUBREQUEST);

            this.yamlSubRequestList = new ArrayList<YAMLSubRequestElement>();
            for (final Object subRequest : _subRequestList)
            {
                this.yamlSubRequestList.add(new YAMLSubRequestElement(subRequest));
            }
        }
        else
        {
            this.yamlSubRequestList = null;
        }
    }

    public ArrayList<YAMLSubRequestElement> getYAMLSubRequestList()
    {
        return this.yamlSubRequestList;
    }

    public boolean yamlSubRequestListExists()
    {
        return this.yamlSubRequestList != null;
    }

}
