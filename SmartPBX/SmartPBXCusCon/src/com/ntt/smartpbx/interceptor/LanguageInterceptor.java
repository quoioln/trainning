package com.ntt.smartpbx.interceptor;

import java.util.Locale;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;

import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * 名称: LanguageInterceptor class.
 *
 */
public class LanguageInterceptor implements Interceptor {

    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(LanguageInterceptor.class);
    // End step 2.5 #1946
    private HttpServletRequest servletRequest;
    public static String langCookie;


    @Override
    public String intercept(ActionInvocation ai) throws Exception {
        this.servletRequest = (HttpServletRequest)ai.getInvocationContext().
                get(StrutsStatics.HTTP_REQUEST);
        Cookie cookies [] = servletRequest.getCookies ();
        Cookie myCookie = null;
        boolean isUpdate = false;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(Const.Cookie.LANGUAGE)) {
                    myCookie = cookies[i];
                    isUpdate = true;
                    break;
                }
            }
        }

        String laguage = Const.JAPANESE;
        if (isUpdate && myCookie.getValue().equals(Const.Cookie.ENGLISH)) {
            laguage = Const.ENGLISH;
        }
        langCookie = laguage;
        Locale locale = new Locale(laguage);
        ActionContext.getContext().setLocale(locale);
        return ai.invoke();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void init() {

    }
}
// (C) NTT Communications  2013  All Rights Reserved
