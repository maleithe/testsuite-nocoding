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
package com.xceptance.xlt.common.actions;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.common.util.RegExUtils;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.UserAgentUtils;
import com.xceptance.xlt.common.util.Validator;
import com.xceptance.xlt.engine.XltWebClient;

/**
 * This is a simple test class for pulling URLs. It is fully configurable using properties.
 */
public class SimpleURL extends AbstractHtmlPageAction
{
    /**
     * the action to be executed (CSV or YAML)
     */
    protected final URLAction urlAction;

    /**
     * the test case reference for property lookup in the actions
     */
    protected final AbstractURLTestCase testCase;

    /**
     * Downloader for additional requests belonging to this action (i.e. static content)
     */
    protected final Downloader downloader;

    /**
     * The constructor for CSV-based actions when a new web session should be started.
     * 
     * @param previousAction
     * @param timerName
     */
    public SimpleURL(final AbstractURLTestCase testCase, final URLAction urlAction, final String login,
        final String password)
    {
        super(urlAction.getName(testCase));
        this.urlAction = urlAction;
        this.testCase = testCase;
        this.downloader = new Downloader((XltWebClient) getWebClient());

        // add credentials, if any
        if (login != null && password != null)
        {
            final DefaultCredentialsProvider credentialsProvider = new DefaultCredentialsProvider();
            credentialsProvider.addCredentials(login, password);

            this.getWebClient().setCredentialsProvider(credentialsProvider);
        }
    }

    /**
     * @param previousAction
     * @param timerName
     */
    public SimpleURL(final AbstractURLTestCase testCase, final AbstractHtmlPageAction prevAction,
        final URLAction urlAction)
    {
        super(prevAction, urlAction.getName(testCase));
        this.testCase = testCase;
        this.urlAction = urlAction;
        this.downloader = new Downloader((XltWebClient) getWebClient());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#preValidate()
     */
    @Override
    public void preValidate() throws Exception
    {
        // do not prevalidate anything here, we assume a correct URL
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#execute()
     */
    @Override
    protected void execute() throws Exception
    {
        // set the user agent UID if required
        UserAgentUtils.setUserAgentUID(this.getWebClient(), testCase.getProperty("userAgent.UID", false));

        loadPage(urlAction.getURL(testCase), urlAction.getMethod(), urlAction.getParameters(testCase));

        // make element Look Up and add to interpreter
        final HtmlPage page = getHtmlPage();
        addPageToInterpreter(page);

        // downloader.loadRequests(this.testCase, this.yamlAction);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.xceptance.xlt.api.actions.AbstractAction#postValidate()
     */
    @Override
    protected void postValidate() throws Exception
    {
        final HtmlPage page = getHtmlPage();

        // response code correct?
        urlAction.getHttpResponseCodeValidator().validate(page);

        // check the validators
        if (urlAction.validatorListExists())
        {
            final ArrayList<Validator> validatorList = urlAction.getValidatorList();
            for (final Validator validator : validatorList)
            {
                final String validatorName = validator.getValidatorName();
                final String validatorXPath = urlAction.getXPath(testCase, validator.getValidatorXpath());
                final String validatorText = urlAction.getText(testCase, validator.getValidatorText());
                final Integer validatorCount = validator.getValidatorCount();

                if (validatorXPath != null)
                {
                    // get the elements from the page
                    @SuppressWarnings("unchecked")
                    final List<HtmlElement> elements = (List<HtmlElement>) page.getByXPath(validatorXPath);

                    // verify existence
                    Assert.assertFalse("Xpath on validation step " + validatorXPath + " not found: <" + validatorName
                                       + ">", elements.isEmpty());

                    // shall we check the text as well?
                    if (validatorText != null)
                    {
                        final String actual = elements.get(0).asText().trim();
                        Assert.assertNotNull(MessageFormat.format("Text on validation step " + validatorName
                                                                      + " does not match. Expected:<{0}> but was:<{1}>",
                                                                  validatorText, actual),
                                             RegExUtils.getFirstMatch(actual, validatorText));
                    }

                    // shall we check the count of the xpath as well?
                    if (validatorCount != null)
                    {
                        final Integer currentCount = elements.size();
                        Assert.assertEquals("Xpath Count of he validation step " + validatorName
                                            + " has not the expected Count:", validatorCount, currentCount);
                    }

                }
                else if (validatorText != null)
                {
                    // ok, xpath was null, so we go for the text on the page only
                    final String responseString = page.getWebResponse().getContentAsString();
                    Assert.assertNotNull("Page was totally empty", responseString);

                    Assert.assertNotNull(MessageFormat.format("Text is not on the page. Expected:<{0}>", validatorText),
                                         RegExUtils.getFirstMatch(responseString, validatorText));
                }

            }
        }
    }

    /**
     * Make some data resolution before post validation which is necessary for executing the page load.
     *
     * @param page
     */
    private void addPageToInterpreter(final HtmlPage page)
    {
        // take care of the parameters to fill up the interpreter
        final List<String> xpathGetters = urlAction.getXPathGetterList(testCase);
        final List<Object> xpathGettersResults = new ArrayList<Object>(xpathGetters.size());
        for (int i = 0; i < xpathGetters.size(); i++)
        {
            final String xp = xpathGetters.get(i);

            // nothing to do, skip and return empty result
            if (xp == null)
            {
                xpathGettersResults.add(null);
                continue;
            }

            // get the elements from the page
            @SuppressWarnings("unchecked")
            final List<HtmlElement> elements = (List<HtmlElement>) page.getByXPath(xp);

            if (!elements.isEmpty())
            {
                if (elements.size() > 1)
                {
                    // keep the entire list
                    xpathGettersResults.add(elements);
                }
                else
                {
                    // keep only the elements
                    xpathGettersResults.add(elements.get(0));
                }
            }
            else
            {
                xpathGettersResults.add(null);
            }

        }
        // send it back for spicing up the interpreter
        urlAction.setXPathGetterResult(xpathGettersResults);
    }

    /**
     * Add an additional request to the current action.
     * 
     * @param url
     *            request URL
     */
    public void addRequest(final String url)
    {
        downloader.addRequest(url);
    }
}
