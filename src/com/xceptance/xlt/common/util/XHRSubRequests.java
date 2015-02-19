package com.xceptance.xlt.common.util;

import java.net.MalformedURLException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.HttpMethod;

public class XHRSubRequests
{
    public static final String GET = "GET";
    
    public static final String POST = "POST";
    
    private final String url;

    private final String method;

    /**
     * encapsulate the Common SubRequests for XHR
     * 
     * @param xhrSubRequestURL
     * @param xhrSubRequestMethod
     */
    public XHRSubRequests(final String xhrSubRequestURL, final String xhrSubRequestMethod)
    {
        this.url = xhrSubRequestURL;
        this.method = xhrSubRequestMethod;
    }
    
    public URL getXHRSubRequestURL() throws MalformedURLException
    {
        return new URL(this.url);
    }
    
    public HttpMethod getXHRSubRequestMethod()
    {
        return this.method.equals(GET) ? HttpMethod.GET : HttpMethod.POST;
    }
    

}
