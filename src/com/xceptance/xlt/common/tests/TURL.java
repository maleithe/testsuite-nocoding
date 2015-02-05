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

import org.junit.Test;

import com.xceptance.xlt.common.actions.SimpleURL;
import com.xceptance.xlt.common.util.URLAction;

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

        // let's loop about the data we have
        for (final URLAction urlAction : urlActions)
        {
            // ok, usual action or static?
            if (urlAction.isAction())
            {
                // System.out.println(urlAction.getStaticSubRequestURLs().get(0));

                if (lastAction == null)
                {
                    // our first action, so start the browser too
                    lastAction = new SimpleURL(this, urlAction, login, password);
                }
                else
                {
                    // Until know just the request URLs were collected. So run the action now.
                    lastAction.run();

                    // And prepare the subsequent action
                    lastAction = new SimpleURL(this, lastAction, urlAction);
                }

                // check if static content is available
                if (urlAction.isStaticContentAvailable())
                {
                    for(final String staticContentURL : urlAction.getStaticSubRequestURLs())
                    {
                        lastAction.addRequest(staticContentURL);
                    }
                }
            }

            // handle XHR actions
            // else if (csvBasedAction.isXHRAction())
            // {
            // if (lastAction == null)
            // {
            // Assert.fail("AJAX actions cannot be used as first action");
            // }
            //
            // lastAction.run();
            // lastAction = new SimpleURL_XHR(this, lastAction, csvBasedAction);
            // }
        }
        if (lastAction != null)
        {
            lastAction.run();
        }

    }
}
