package com.ntt.smartpbx.action.user;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.ntt.smartpbx.action.BaseAction;
import com.ntt.smartpbx.utils.Const;

/**
 * 名称: LogoutAction class
 * 機能概要: Process the Logout request
 */
public class LogoutAction extends BaseAction implements ServletResponseAware, ServletRequestAware {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(LogoutAction.class);
    // End step 2.5 #1946
    /** Servlet Response */
    private HttpServletResponse servletResponse;
    /** Servlet Request */
    private HttpServletRequest servletRequest;


    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: Logout.jsp
     */
    public String execute() {
        session.clear();
        //START 1.x TMA-CR#138849
        Cookie cookies[] = servletRequest.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(Const.Cookie.JSESSIONID)) {
                    cookies[i].setMaxAge(0);
                    cookies[i].setPath(servletRequest.getContextPath() + "/");
                    servletResponse.addCookie(cookies[i]);
                    break;
                }
            }
        }
        //END 1.x TMA-CR#138849
        log.debug("Log out successfully");
        return SUCCESS;
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

//(C) NTT Communications  2013  All Rights Reserved
