package com.ntt.smartpbx.utils;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import com.ntt.smartpbx.SPCCInit;

/**
 * 名称: SmartPBXCusConFilter class.
 *
 */
public class SmartPBXCusConFilter extends StrutsPrepareAndExecuteFilter {
    //Start Step1.x #1091
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(SmartPBXCusConFilter.class);
    // End step 2.5 #1946
    //End Step1.x #1091

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;


        // Set cookie if account type is 2
        if (response instanceof HttpServletResponse) {

            HttpSession session = httpRequest.getSession();
            Object accountType = session.getAttribute(Const.Session.ACCOUNT_TYPE);

            if (session != null && session.getId() != null && accountType != null) {
                if(Integer.parseInt(accountType.toString()) != SPCCInit.config.getPermittedAccountType()){
                    Cookie cookies[] = httpRequest.getCookies();
                    if (cookies != null) {
                        for (int i = 0; i < cookies.length; i++) {
                            if (cookies[i].getName().equals(Const.Cookie.JSESSIONID)) {
                                cookies[i].setMaxAge(0);
                                cookies[i].setPath(httpRequest.getContextPath() + "/");
                                httpResponse.addCookie(cookies[i]);
                                break;
                            }
                        }
                    }
                } else if(Integer.parseInt(accountType.toString()) == Const.ACCOUNT_TYPE.USER_MANAGER){
                    Cookie sessionCookie = new Cookie(Const.Cookie.JSESSIONID, session.getId());
                    sessionCookie.setPath(httpRequest.getContextPath() + "/");
                    sessionCookie.setHttpOnly(true);

                    sessionCookie.setMaxAge(SPCCInit.config.getSsoCookieTimeOut());
                    httpResponse.addCookie(sessionCookie);
                }
            }
        }
        //Start Step1.x #1091
        //Set CharacterEncoding for show japanese
        httpRequest.setCharacterEncoding("UTF-8");
        //Get all parameter
        Map<String, String[]> params = httpRequest.getParameterMap();
        // Start step2.5 #1944
        if (params.size() > Const.MAX_PARAMS) {
            log.warn("Requested parameters are too many.");
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        // End step2.5 #1944

        //Start step1.7 G1902
        //Remove parameter have special character
        if (Const.PageName.listPageName.contains(httpRequest.getServletPath())) {
        //Start step1.7 G1902

            log.debug("Igrone parameter");
            super.doFilter(request, response, chain);
            return;

        } else {
            for (Map.Entry<String, String[]> entrys : params.entrySet()) {
                // Start step2.5 #1944
                if (Const.MAX_PARAMS < entrys.getValue().length) {
                    log.warn("Requested parameters are too many.");
                    httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                // End step2.5 #1944

                for (int i = 0; i < entrys.getValue().length; i++) {
                    if (!Util.checkTimetamp(entrys.getValue()[i])) {
                        if (Util.validateSpecialCharacterXSS(entrys.getValue()[i])) {
                            log.debug(entrys.getKey() + " have sepecial character");
                            entrys.getValue()[i] = Const.EMPTY;
                        }
                    }
                }
            }
        }
        //End Step1.x #1091

        super.doFilter(request, response, chain);
    }
}
// (C) NTT Communications  2013  All Rights Reserved
