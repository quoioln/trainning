//START 880
package com.ntt.smartpbx.action.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 名称: ChangeLanguageAction class.
 * 機能概要: Process changing language.
 */
public class ChangeLanguageAction extends ActionSupport implements ServletResponseAware, ServletRequestAware {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The language*/
    private String language;
    /** ServletResponse */
    private HttpServletResponse servletResponse;
    /** ServletRequest */
    private HttpServletRequest servletRequest;

    public String execute(){
        //Check language
        if(!this.language.equals(Const.Cookie.ENGLISH) && !this.language.equals(Const.Cookie.JAPANESE)){
            this.language = Const.Cookie.JAPANESE;
        }

        Cookie languageCookie = new Cookie(Const.Cookie.LANGUAGE, language);
        languageCookie.setPath(servletRequest.getContextPath());

        languageCookie.setMaxAge(SPCCInit.config.getOtherCookieTimeOut());
        servletResponse.addCookie(languageCookie);
        return SUCCESS;
    }

    /**
     * Get the language
     * @return The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set the language
     * @param language
     *      The language
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    @Override
    public void setServletResponse(HttpServletResponse response) {
        this.servletResponse = response;
    }

    @Override
    public void setServletRequest(HttpServletRequest resquest) {
        this.servletRequest = resquest;
    }
}
//END 880
// (C) NTT Communications  2013  All Rights Reserved
