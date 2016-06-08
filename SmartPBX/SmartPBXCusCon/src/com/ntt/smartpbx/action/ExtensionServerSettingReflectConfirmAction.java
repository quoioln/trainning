//Start step2.5 #ADD-step2.5-05
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.db.ServerRenewQueueInfo;
import com.ntt.smartpbx.model.db.VmInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;
import com.opensymphony.xwork2.ActionContext;

public class ExtensionServerSettingReflectConfirmAction extends BaseAction {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** The logger */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(ExtensionServerSettingReflectConfirmAction.class);
    // End step 2.5 #1946
    /** VM Info **/
    private List<VmInfo> listVmInfo = new ArrayList<VmInfo>();
    /** Server renew queue info **/
    private List<ServerRenewQueueInfo> listServerRenewQueueInfoData = new ArrayList<ServerRenewQueueInfo>();
    /** NNumber Info Id */
    private long nNumberInfoId;
    /** Vm Id**/
    private String vmId;
    /** The action type */
    private int actionType;
    /** List VM Id **/
    private List selectedVmIds;
    /** List old last update time **/
    private List<String> oldLastUpdateTime;


    /**
     * Default constructor.
     */
    public ExtensionServerSettingReflectConfirmAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.OPERATOR);
        this.actionType = ACTION_INIT;
        this.nNumberInfoId = 0;
        this.vmId = Const.EMPTY;
        this.selectedVmIds = new ArrayList<>();
        this.oldLastUpdateTime = new ArrayList<String>();
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: ExtensionServerSettingReflectConfirm.jsp
     *      CHANGE: ExtensionServerSettingReflectFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        Locale locale = new Locale(Const.JAPANESE);
        ActionContext.getContext().setLocale(locale);
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                log.debug("nonece invalid.");
                return ERROR;
            }
        }

        //Start step2.5 #1931
        //Get data from session if it exists
        String rs = getSelectedVmIdsFromSession();
        if (!rs.equals(OK)) {
            return rs;
        }
        //End step2.5 #1931

        switch (actionType) {
            case ACTION_CHANGE:
                return doChange();
            case ACTION_BACK:
                return doBack();
            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     *execute back action
     * @return back
     */
    private String doBack(){
        session.put(Const.Session.G2101_BACK_FLAG, true);
        return BACK;
    }

    /**
     * The init method of action
     *
     * @return
     *      SUCCESS: ExtensionServerSettingReflectConfirm.jsp
     *      ERROR: SystemError.jsp
     */
    public String doInit() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        createToken();

        oldLastUpdateTime = new ArrayList<String>(selectedVmIds.size());
        if (selectedVmIds.isEmpty()) {
            log.error("no vm is selected.");
            return ERROR;
        }
        for (Object vmId : selectedVmIds) {
            //Get VM info from DB by VM id
            Result<VmInfo> rsVmInfo = DBService.getInstance().getVmInfoByVmId(loginId, sessionId, (String) vmId);

            if (rsVmInfo.getRetCode() == Const.ReturnCode.NG || rsVmInfo.getData() == null) {
                log.error("fail to get vm_info.");
                error = rsVmInfo.getError();
                return ERROR;
            }
            oldLastUpdateTime.add(rsVmInfo.getData().getLastUpdateTime().toString());
        }
        return SUCCESS;
    }

    //Start step2.5 #1931
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
                //Start step2.5 #1978
                log.error("Can not get selected vm ids from session!");
                //End step2.5 #1978
                return ERROR;
            }
        }
        return OK;
    }
    //End step2.5 #1931

    /**
     * The change method of action
     *
     * @return
     *      SUCCESS: ExtensionServerSettingReflectConfirm.jsp
     *      CHANGE: ExtensionServerSettingReflectFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String doChange() {
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);

        //Check last update time
        //Start step2.5 #1921
        int index = 0;
        for (Object vmId : selectedVmIds) {
            Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, null, Const.TableName.VM_INFO, Const.TableKey.VM_ID, String.valueOf(vmId), oldLastUpdateTime.get(index));
            if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                log.error("fail to get vm_info.");
                error = resultCheck.getError();
                return ERROR;
            }
            //Check vmId have changed
            if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                log.debug("data is changed.   time:" + oldLastUpdateTime.get(index) + "   (list):" + oldLastUpdateTime.toString() );
                addFieldError("errorDBChange", getText("common.errors.DataChanged", new String[]{getText("table.VmInfo")}));
                return INPUT;
            }
            index++;
            //End step2.5 #1921
        }

        // Get list vmInfo
        Result<List<VmInfo>> resultListVmInfo = DBService.getInstance().getListVmInfoByVmId(loginId, sessionId, selectedVmIds);
        if (resultListVmInfo.getRetCode() == Const.ReturnCode.NG) {
            log.error("fail to get vm_info. " );
            error = resultListVmInfo.getError();
            return ERROR;
        }
        // Check vmInfo status failure
        listVmInfo = resultListVmInfo.getData();
        for (VmInfo vmInfo : listVmInfo) {
            if (vmInfo != null && vmInfo.getVmStatus() == Const.VMInfoStatus.Failure) {
                log.debug("selected VM is changed status to Failure.  :" + vmInfo.getVmId() );
                addFieldError("errorSettingReflect", getText("g2102.errors.SettingReflect", new String[]{(String) vmInfo.getVmId()}));
            }
        }
        if (!getFieldErrors().isEmpty()) {
            return INPUT;
        }

        //Get list server Renew Queue Info
        Result<List<ServerRenewQueueInfo>> resultListserverRenewQueue = DBService.getInstance().getListServerRenewQueueInfoByvmId(loginId, sessionId, selectedVmIds);
        if (resultListserverRenewQueue.getRetCode() == Const.ReturnCode.NG) {
            log.error("Fail to get server_renew_queue_info. " );
            error = resultListserverRenewQueue.getError();
            return ERROR;
        }
        //Check ServerRenewQueueInfo status waiting
        listServerRenewQueueInfoData = resultListserverRenewQueue.getData();
        //Start step2.5 #1921
        index = 0;
        for (ServerRenewQueueInfo serverRenewQueueInfo : listServerRenewQueueInfoData) {
            if (serverRenewQueueInfo != null && serverRenewQueueInfo.getServerRenewStatus() == Const.ExtensionServerRenewStatus.WaittingExecute) {
                log.debug("Selectes VM is now waitting for execute.   vm_info_id" +serverRenewQueueInfo.getVmInfoId() );
                addFieldError("errorRegistered", getText("g2102.errors.RegisteredErr", new String[]{(String) selectedVmIds.get(index)}));
            }
            index++;
            //End step2.5 #1921
        }
        if (!getFieldErrors().isEmpty()) {
            return INPUT;
        }

        //Delete correspond record
        Result<Boolean> result = DBService.getInstance().deleteServerRenewQueueCorrespondRecord(loginId, sessionId, selectedVmIds);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            log.error("Fail to delete server_renew_queue_info.");
            error = result.getError();
            return ERROR;
        }

        //Insert record
        String strSelectedVmIds = selectedVmIds.toString().replace("[", "").replace("]", "").trim();
        log.info(Util.message(Const.ERROR_CODE.I032401, String.format(Const.MESSAGE_CODE.I032401, strSelectedVmIds)));
        result = DBService.getInstance().insertServerRenewQueueReservation(loginId, sessionId, selectedVmIds, accountInfoId);
        if (result.getRetCode() == Const.ReturnCode.NG) {
            log.error("Fail to insert server_renew_queue_info.");
            error = result.getError();
            return ERROR;
        }

        return CHANGE;
    }

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

    /**
     * Get action type
     * @return actionType
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * Set action type
     * @param actionType
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Get nNumber InfoId
     * @return nNumberInfoId
     */
    public long getnNumberInfoId() {
        return nNumberInfoId;
    }

    /**
     * Set nNumber InfoId
     * @param nNumberInfoId
     */
    public void setnNumberInfoId(long nNumberInfoId) {
        this.nNumberInfoId = nNumberInfoId;
    }

    /**
     * Get VmId
     * @return vmId
     */
    public String getVmId() {
        return vmId;
    }

    /**
     * Set VmId
     * @param vmId
     */
    public void setVmId(String vmId) {
        this.vmId = vmId;
    }

    /**
     * Get old last update time
     * @return oldLastUpdateTime
     */
    public List<String> getOldLastUpdateTime() {
        return oldLastUpdateTime;
    }

    /**
     * Set old last update time
     * @param oldLastUpdateTime
     */
    public void setOldLastUpdateTime(List<String> oldLastUpdateTime) {
        this.oldLastUpdateTime = oldLastUpdateTime;
    }

}
//End step2.5 #ADD-step2.5-05
//(C) NTT Communications  2015  All Rights Reserved
