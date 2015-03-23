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

import com.gargoylesoftware.htmlunit.DefaultPageCreator;
import com.gargoylesoftware.htmlunit.DefaultPageCreator.PageType;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.xceptance.common.util.RegExUtils;
import com.xceptance.xlt.api.actions.AbstractHtmlPageAction;
import com.xceptance.xlt.api.util.HtmlPageUtils;
import com.xceptance.xlt.common.tests.AbstractURLTestCase;
import com.xceptance.xlt.common.util.URLAction;
import com.xceptance.xlt.common.util.UserAgentUtils;
import com.xceptance.xlt.common.util.Validator;

/**
 * Performs an AJAX request and parses the response into an container element that can be used for validation.
 */
public class SimpleURL_XHR extends SimpleURL
{
    protected WebResponse response;

    /**
     * @param testCase
     * @param prevAction
     * @param action
     */
    public SimpleURL_XHR(final AbstractURLTestCase testCase, final AbstractHtmlPageAction prevAction,
        final URLAction action)
    {
        super(testCase, prevAction, action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() throws Exception
    {
        final HtmlPage page = getPreviousAction().getHtmlPage();

        UserAgentUtils.setUserAgentUID(this.getWebClient(), testCase.getProperty("userAgent.UID", false));

        final WebRequest request = createWebRequestSettings(urlAction.getURL(this.testCase), urlAction.getMethod(),
                                                            urlAction.getParameters(this.testCase));
        request.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
        request.setXHR();

        response = getWebClient().loadWebResponse(request);
        setHtmlPage(page);
        
        downloader.loadRequests(this.testCase, this.urlAction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postValidate() throws Exception
    {
        final HtmlPage page = getHtmlPage();

        // response code correct?
        urlAction.getHttpResponseCodeValidator().validate(page);

        final HtmlElement container;
        if (DefaultPageCreator.determinePageType(response.getContentType()) == PageType.HTML)
        {
            container = HtmlPageUtils.createHtmlElement("div", page.getBody());
            HTMLParser.parseFragment(container, response.getContentAsString());
        }
        else
        {
            return;
        }
        
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
                final String validatorRegex = validator.getValidatorRegex();

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

                if (validatorRegex != null)
                {
                    final String responseString = page.getWebResponse().getContentAsString();
                    Assert.assertTrue("On validation step " + validator.getValidatorName()
                                          + " the Text does not match on the given Regex:",
                                      RegExUtils.getFirstMatch(responseString, validatorRegex).contains(validatorText));
                }
            }
        }
    }
}
