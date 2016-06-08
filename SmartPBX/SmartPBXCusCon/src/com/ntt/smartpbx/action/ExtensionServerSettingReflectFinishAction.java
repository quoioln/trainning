//Start step2.5 #ADD-step2.5-06
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.utils.Const;
import com.opensymphony.xwork2.ActionContext;

public class ExtensionServerSettingReflectFinishAction extends BaseAction {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ExtensionServerSettingReflectFinishAction.class);
    // End step 2.5 #1946
    /** List VM Id **/
    private List selectedVmIds;


    /**
     * Default constructor.
     */
    public ExtensionServerSettingReflectFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.selectedVmIds = new ArrayList<>();
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: ExtensionServerSettingReflectFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }

        //Start step2.5 #1978
        //Get data from session if it exists
        String rs = getSelectedVmIdsFromSession();
        if (!rs.equals(OK)) {
            return rs;
        }
        //End step2.5 #1978

        return SUCCESS;
    }

    //Start step2.5 #1978
    /**
     * Get selected vm ids from session
     * @return
     */
    private String getSelectedVmIdsFromSession() {
        //Get data from session if it exists
        if (session.containsKey(Const.Session.G2101_SEARCH_CONDITION_FLAG)) {
            try {
                if (session.containsKey(Const.Session.G2101_SELECTED_VM_IDS) && session.get(Const.Session.G2101_SELECTED_VM_IDS) != null) {
                    selectedVmIds = (List) session.get(Const.Session.G2101_SELECTED_VM_IDS);
                }
            } catch (Exception ex) {
                log.debug("Can not get selected vm ids from session!");
                return ERROR;
            }
        }
        return OK;
    }

    //End step2.5 #1978

    /**
     * Get selected VmIds
     * @return selectedVmIds
     */
    public List getSelectedVmIds() {
        return selectedVmIds;
    }

    /**
     * Set selected VmIds
     * @param selectedVmIds
     */
    public void setSelectedVmIds(List selectedVmIds) {
        this.selectedVmIds = selectedVmIds;
    }
}
//End step2.5 #ADD-step2.5-06
//(C) NTT Communications  2015  All Rights Reserved
