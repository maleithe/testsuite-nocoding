/**  
 *  Copyright 2014 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */
package com.xceptance.xlt.common.tests;

import org.junit.Assert;
import org.junit.Test;

import com.xceptance.xlt.api.util.XltProperties;
import com.xceptance.xlt.common.actions.SimpleURL;
import com.xceptance.xlt.common.actions.SimpleURL_XHR;
import com.xceptance.xlt.common.util.CSVBasedURLAction;
import com.xceptance.xlt.common.util.YAMLBasedURLAction;

/**
 * This is a simple single URL test case. It can be used to create considerable load for simple investigations. Cookie
 * handling, as well as content comparison is handled automatically. See the properties too.
 */
public class TURL extends AbstractURLTestCase
{
    @Test
    public void testURLs() throws Throwable
    {
        // our action tracker to build up a correct chain of pages
        SimpleURL lastAction = null;

        final boolean useYaml = XltProperties.getInstance().getProperty("useYAML", false);
        if (useYaml == true)
        {
            //TODO do the yaml tests, just for action yet
            // let's loop about the data we have
            for (final YAMLBasedURLAction yamlBasedAction : yamlBasedActions)
            {
                // check if usual action
                if(yamlBasedAction.isAction())
                {
                    if (lastAction == null)
                    {
                        // our first action, so start the browser too
                        //TODO in case of csv still null, only for trying
                        lastAction = new SimpleURL(this, yamlBasedAction, null, login, password);
                    }
                    else
                    {
                        // Until know just the request URLs were collected. So run the action now.
                        lastAction.run();

                        // And prepare the subsequent action
                        lastAction = new SimpleURL(this, lastAction, yamlBasedAction, null);
                    }
                }
            }
            if (lastAction != null)
            {
                lastAction.run();
            }
            
        }
        else
        {
            // let's loop about the data we have
            for (final CSVBasedURLAction csvBasedAction : csvBasedActions)
            {
                // ok, usual action or static?
                if (csvBasedAction.isAction())
                {
                    if (lastAction == null)
                    {
                        // our first action, so start the browser too
                        lastAction = new SimpleURL(this, csvBasedAction, null, login, password);
                    }
                    else
                    {
                        // Until know just the request URLs were collected. So run the action now.
                        lastAction.run();

                        // And prepare the subsequent action
                        lastAction = new SimpleURL(this, lastAction, csvBasedAction, null);
                    }
                }

                // this is the part that deals with the static downloads
                else if (csvBasedAction.isStaticContent())
                {
                    if (lastAction == null)
                    {
                        // we do not have any action yet, so we have to make one up
                        lastAction = new SimpleURL(this, csvBasedAction, null, login, password);
                    }
                    else
                    {
                        // it's content that belong to the last known action
                        lastAction.addRequest(csvBasedAction.getUrlString());
                    }
                }

                // handle XHR actions
                else if (csvBasedAction.isXHRAction())
                {
                    if (lastAction == null)
                    {
                        Assert.fail("AJAX actions cannot be used as first action");
                    }

                    lastAction.run();
                    lastAction = new SimpleURL_XHR(this, lastAction, csvBasedAction);
                }
            }
            if (lastAction != null)
            {
                lastAction.run();
            }
        }
    }
}
