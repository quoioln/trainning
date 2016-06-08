// START [G06]
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.IncomingGroupSettingAddActionData;
import com.ntt.smartpbx.model.db.ExtensionNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.model.db.IncomingGroupInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: IncomingGroupSettingUpdateAction class.
 * 機能概要: Process the incoming group setting update
 */
public class IncomingGroupSettingUpdateAction extends BaseAction {
    /** The default serial version. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(IncomingGroupSettingUpdateAction.class);
    // End step 2.5 #1946
    /** List extension number candidate.. */
    private List<IncomingGroupSettingAddActionData> list2 = new ArrayList<IncomingGroupSettingAddActionData>();
    /** List extension number in table child.. */
    private List<IncomingGroupSettingAddActionData> listChild = new ArrayList<IncomingGroupSettingAddActionData>();
    /** max incoming group member. */
    private int maxIncomingGroupMember;
    /** error message. */
    private String errorMsg;
    /** value of radio  be selected in representative number candidate table. */
    private long extensionNumberInfoIdSelect;
    /** The extension number info id of radio be selected. */
    private long idVal;
    /** The old extension group info id. */
    private long oldExtensionValue;
    /** location number : filter conditional in cand table. */
    private String locationNumberCandidate;
    /** terminal number : filter conditional in cand table. */
    private String terminalNumberCandidate;
    /** incoming group info id want to change. */
    private long incomingGroupId;
    /** The group call type. */
    private int callMethod;
    /** The action type. */
    private int actionType;
    /** Json data of list2. */
    private String list2Json;
    /** Json data of listChild. */
    private String listChildJson;
    /** The extension number Info Id Child. */
    private List extensionNumberInfoIdChild;
    // START #429
    /** The incoming group name. */
    /*
    private String incomingGroupName;*/
    // END #429
    /** Session id */
    private String loginId;
    /** Login id */
    private String sessionId;

    // START #471
    /** The last update time of data */
    private String lastUpdateTime;


    // END #471

    /**
     * Default constructor.
     */
    public IncomingGroupSettingUpdateAction() {
        super();
        this.locationNumberCandidate = Const.EMPTY;
        this.terminalNumberCandidate = Const.EMPTY;
        this.actionType = ACTION_INIT;
        this.incomingGroupId = 0;
        this.loginId = Const.EMPTY;
        this.sessionId = Const.EMPTY;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     *  Getter max incoming group member from config.
     *
     * @return max incoming group member
     */
    public int getMaxIncomingGroupMember() {
        maxIncomingGroupMember = SPCCInit.config.getCusconMaxGroupMember();
        return maxIncomingGroupMember;
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingUpdate.jsp
     *      SEARCH: IncomingGroupSettingUpdate.jsp
     *      INPUT: IncomingGroupSettingUpdate.jsp
     *      CHANGE: IncomingGroupSettingUpdateFinish.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        // Check login session
        if (!checkLogin()) {
            log.debug("Login session does not exist");
            return ERROR;
        }
        // START Step1.x #1091
        if (actionType != ACTION_INIT) {
            if (!checkToken()) {
                // goto SystemError.jsp
                log.debug("nonece invalid.");
                return ERROR;
            }
        }
        // END Step1.x #1091
        loginId = (String) session.get(Const.Session.LOGIN_ID);
        sessionId = (String) session.get(Const.Session.SESSION_ID);

        String rs = parseJSonData();
        if (!rs.equals(OK)) {
            return rs;
        }
        rs = appendChild();
        if (!rs.equals(OK)) {
            return rs;
        }

        switch (actionType) {
            case ACTION_SEARCH_2:
                return doSearch2();

            case ACTION_CHANGE:
                return doChange();

            case ACTION_INIT:
            default:
                return doInit();
        }
    }

    /**
     * get data from json string.
     *
     * @return result of method.
     */
    @SuppressWarnings("unchecked")
    private String parseJSonData() {
        //Parse data from json
        try {
            if (list2Json != null && !list2Json.equals(Const.EMPTY)) {
                list2 = (List<IncomingGroupSettingAddActionData>) (new Gson()).fromJson(list2Json, new TypeToken<List<IncomingGroupSettingAddActionData>>() {
                }.getType());
            }
            if (listChildJson != null && !listChildJson.equals(Const.EMPTY)) {
                listChild = (List<IncomingGroupSettingAddActionData>) (new Gson()).fromJson(listChildJson, new TypeToken<List<IncomingGroupSettingAddActionData>>() {
                }.getType());
            }
        } catch (Exception e) {
            return ERROR;
        }
        return OK;
    }

    /**
     * append child to json string.
     *
     * @return result of method
     */
    private String appendChild() {
        List<IncomingGroupSettingAddActionData> temp = new ArrayList<IncomingGroupSettingAddActionData>();
        if (extensionNumberInfoIdChild == null) {
            listChild = new ArrayList<IncomingGroupSettingAddActionData>();
            return OK;
        }
        /*if (listChild == null) {
            listChild = new ArrayList<IncomingGroupSettingAddActionData>();
        }*/
        for (int i = 0; i < extensionNumberInfoIdChild.size(); i++) {
            //try {
            IncomingGroupSettingAddActionData obj = getObjectFromList(listChild, Long.valueOf((String) extensionNumberInfoIdChild.get(i)));
            if (obj == null) {
                IncomingGroupSettingAddActionData obj2 = getObjectFromList(list2, Long.valueOf((String) extensionNumberInfoIdChild.get(i)));
                if (obj2 == null) {
                    log.debug("Data not found");
                    return ERROR;
                }
                temp.add(obj2);
            } else {
                temp.add(obj);
            }
            /*} catch (Exception e) {
                log.debug("getData error: " + e.getMessage());
                return ERROR;
            }*/
        }
        listChild = temp;
        listChildJson = (new Gson()).toJson(listChild);
        return OK;
    }

    /**
     * validate for input search.
     *
     * @param arg1 input field 1.
     * @param arg2 input field 2.
     * @return result of method.
     */
    private boolean validateSearch(String arg1, String arg2) {
        if (!Util.validateString(arg1)) {
            return false;
        }

        if (!Util.validateString(arg2)) {
            return false;
        }
        return true;
    }

    /**
     * The search method of action
     *
     * @return
     *      SEARCH: IncomingGroupSettingUpdate.jsp
     *      INPUT: IncomingGroupSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    private String doSearch2() {
        list2 = null;
        list2Json = null;
        //Start Step1.x #1091
        if (!validateSearchFieldsNoOutputError(locationNumberCandidate, terminalNumberCandidate)) {
            //addFieldError("search2", getText("input.validate.InvalidInput"));
            locationNumberCandidate = Const.EMPTY;
            terminalNumberCandidate = Const.EMPTY;
            /* return INPUT;*/
        }
        //End Step1.x #1091
        //check if incoming group info id receive from G0601 is null
        if (incomingGroupId < 1) {
            return ERROR;
        }

        //get nNumberInfoId from session
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        //set idVal and callMethod if extension number info id exist in incoming_group_info
        Result<IncomingGroupInfo> resultIncoming = new Result<IncomingGroupInfo>();
        // Start 1.x TMA-CR#138970, 798
        resultIncoming = DBService.getInstance().getIncomingGroupInfoById(loginId, sessionId, nNumberInfoId, incomingGroupId, Const.GET_DATA_INIT);
        // End 1.x TMA-CR#138970, 798
        if (resultIncoming.getRetCode() == Const.ReturnCode.NG || resultIncoming.getData() == null) {
            error = resultIncoming.getError();

            return ERROR;
        } else {
            callMethod = resultIncoming.getData().getGroupCallType();
            if (callMethod != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                idVal = resultIncoming.getData().getFkExtensionNumberInfoId();
                oldExtensionValue = idVal;
            }
        }

        if (!getDataList2(nNumberInfoId).equals(OK)) {
            return ERROR;
        }

        return SEARCH;
    }

    /**
     * The search method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    private String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        //check if incoming group info id receive from G0601 is null
        if (incomingGroupId < 1) {
            return ERROR;
        }

        //get nNumberInfoId from session
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        //set idVal and callMethod if extension number info id exist in incoming_group_info
        Result<IncomingGroupInfo> resultIncoming = new Result<IncomingGroupInfo>();
        // Start 1.x TMA-CR#138970, 798
        resultIncoming = DBService.getInstance().getIncomingGroupInfoById(loginId, sessionId, nNumberInfoId, incomingGroupId, Const.GET_DATA_INIT);
        // End 1.x TMA-CR#138970, 798
        if (resultIncoming.getRetCode() == Const.ReturnCode.NG || resultIncoming.getData() == null) {
            error = resultIncoming.getError();

            return ERROR;
        } else {
            callMethod = resultIncoming.getData().getGroupCallType();
            // START #483
            if (callMethod != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                // END #483
                idVal = resultIncoming.getData().getFkExtensionNumberInfoId();
                oldExtensionValue = idVal;
            }
            // START #471
            lastUpdateTime = resultIncoming.getData().getLastUpdateTime().toString();
            // END #471
        }

        if (!getDataList2(nNumberInfoId).equals(OK)) {
            return ERROR;
        }

        // Start 1.x FD
        if (!getOldData().equals(OK)) {
            return ERROR;
        }
        // End 1.x FD

        return SUCCESS;
    }

    /**
     * get data for list child member.
     *
     * @return result of method.
     */
    public String getOldData() {
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        // End 1.x TMA-CR#138970

        //set listChild
        listChild = new ArrayList<IncomingGroupSettingAddActionData>();
        IncomingGroupSettingAddActionData obj = null;
        // Start 1.x FD
        // Get pilot extension number
        // Start 1.x TMA-CR#138970, 798
        Result<IncomingGroupInfo> rsPilot = DBService.getInstance().getIncomingGroupInfoById(loginId, sessionId, nNumberInfoId, incomingGroupId, Const.GET_DATA_INIT);
        // End 1.x TMA-CR#138970, 798
        if (rsPilot.getRetCode() == Const.ReturnCode.NG || rsPilot.getData() == null) {
            error = rsPilot.getError();
            return ERROR;
        }
        if (rsPilot.getData().getGroupCallType() != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
            // Start 1.x TMA-CR#138970
            Result<ExtensionNumberInfo> resultExt = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, rsPilot.getData().getFkExtensionNumberInfoId());
            // End 1.x TMA-CR#138970
            if (resultExt.getRetCode() == Const.ReturnCode.NG || resultExt.getData() == null) {
                error = resultExt.getError();
                return ERROR;
            }
            ExtensionNumberInfo ext = resultExt.getData();

            obj = new IncomingGroupSettingAddActionData();
            obj.setExtensionNumberInfoId(ext.getExtensionNumberInfoId());
            obj.setLocationNumber(ext.getLocationNumber());
            obj.setTerminalNumber(ext.getTerminalNumber());
            obj.setLastUpdateTime(ext.getLastUpdateTime().toString());

            listChild.add(obj);
        }
        // End 1.x FD

        // Get list child
        List<IncomingGroupChildNumberInfo> rsGroupChild = new ArrayList<IncomingGroupChildNumberInfo>();
        Result<List<IncomingGroupChildNumberInfo>> result3 = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupId(loginId, sessionId, incomingGroupId);
        if (result3.getRetCode() == Const.ReturnCode.NG || result3.getData() == null) {
            error = result3.getError();

            return ERROR;
        } else {
            rsGroupChild = result3.getData();
            for (int i = 0; i < rsGroupChild.size(); i++) {
                // Start 1.x TMA-CR#138970
                Result<ExtensionNumberInfo> resultExt = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, rsGroupChild.get(i).getFkExtensionNumberInfoId());
                // End 1.x TMA-CR#138970
                if (resultExt.getRetCode() == Const.ReturnCode.NG || resultExt.getData() == null) {
                    error = resultExt.getError();

                    return ERROR;
                } else {
                    ExtensionNumberInfo ext = resultExt.getData();

                    obj = new IncomingGroupSettingAddActionData();
                    obj.setExtensionNumberInfoId(ext.getExtensionNumberInfoId());
                    obj.setLocationNumber(ext.getLocationNumber());
                    obj.setTerminalNumber(ext.getTerminalNumber());
                    obj.setLastUpdateTime(ext.getLastUpdateTime().toString());

                    listChild.add(obj);
                }
            }
        }
        listChildJson = (new Gson()).toJson(listChild);

        return OK;
    }

    /**
     * get data for list 2.
     *
     * @param nNumberInfoId n number info id.
     * @return reuslt of method.
     */
    public String getDataList2(Long nNumberInfoId) {
        //display data for list2
        Result<List<IncomingGroupSettingAddActionData>> result = DBService.getInstance().getExtensionNumberInfoFilterIgnoreTerminalType3(loginId, sessionId, nNumberInfoId, locationNumberCandidate, terminalNumberCandidate);
        if (result.getRetCode() == Const.ReturnCode.NG || result.getData() == null) {
            error = result.getError();

            return ERROR;
        } else {
            list2 = result.getData();
            list2Json = (new Gson()).toJson(list2);
        }
        return OK;
    }

    /**
     * The search method of action
     *
     * @return
     *      INPUT: IncomingGroupSettingUpdate.jsp
     *      CHANGE: IncomingGroupSettingUpdateFinish.jsp
     *      ERROR: SystemError.jsp
     */
    @SuppressWarnings("unchecked")
    public String doChange() {
        // START #429
        //get incoming group name from incoming group id
        // Start 1.x TMA-CR#138970
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        Result<IncomingGroupInfo> rsIncomingName = DBService.getInstance().getIncomingGroupInfoByIdIgnoreDeleteFlag(loginId, sessionId, nNumberInfoId, incomingGroupId);
        // End 1.x TMA-CR#138970
        if (rsIncomingName.getRetCode() == Const.ReturnCode.NG || rsIncomingName.getData() == null) {
            setError(rsIncomingName.getError());
            return ERROR;
        }

        String incomingGroupName = rsIncomingName.getData().getIncomingGroupName();
        // END #429

        String oldLastTime = Const.EMPTY;

        //get login id, account info id, n number info id from session
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);


        String callMethodName = "";
        if (callMethod == Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.SEQUENCE_INCOMING;
        } else if (callMethod == Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.SIMULTANEOUS_INCOMING;
        } else if (callMethod == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.CALL_PICKUP;
        }

        String representativeNumber = Const.EMPTY;
        if (callMethod != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
            // Start 1.x TMA-CR#138970
            Result<ExtensionNumberInfo> rsextension = DBService.getInstance().getExtensionNumberInfoById(loginId, sessionId, nNumberInfoId, rsIncomingName.getData().getFkExtensionNumberInfoId());
            // End 1.x TMA-CR#138970
            if (rsextension.getRetCode() == Const.ReturnCode.NG || rsextension.getData() == null) {
                error = rsextension.getError();

                return ERROR;
            }
            representativeNumber = rsextension.getData().getExtensionNumber();
        }
        // START #471
        //check if data have been deleted before by other user
        // Start 1.x TMA-CR#138970
        Result<Integer> resultCheckGroup = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.INCOMING_GROUP_INFO, Const.TableKey.INCOMING_GROUP_INFO_ID, String.valueOf(incomingGroupId), lastUpdateTime);
        // End 1.x TMA-CR#138970
        if (resultCheckGroup.getRetCode() == Const.ReturnCode.NG) {
            error = resultCheckGroup.getError();

            return ERROR;
        }
        if (resultCheckGroup.getData() == Const.ReturnCheck.IS_CHANGE || resultCheckGroup.getData() == Const.ReturnCheck.IS_DELETE) {

            log.info(Util.message(Const.ERROR_CODE.I030411, String.format(Const.MESSAGE_CODE.I030411, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
            errorMsg = getText("common.errors.DataChanged", new String[]{getText("table.IncomingGroupInfo")});
            return INPUT;
        }
        /*if (resultCheckGroup.getData() == Const.ReturnCheck.IS_DELETE) {
            log.info(Util.message(Const.ERROR_CODE.I030411, String.format(Const.MESSAGE_CODE.I030411, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
            errorMsg = getText("common.errors.DataDeleted", new String[]{getText("table.IncomingGroupInfo")});
            return INPUT;
        }*/
        // END #471

        try {
            //1. if group call type !=3 check representative extension number select
            if (callMethod != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                if (extensionNumberInfoIdSelect == 0) {
                    errorMsg = getText("g0602.errors.NoSelection");
                    return INPUT;
                }
                idVal = extensionNumberInfoIdSelect;
                IncomingGroupSettingAddActionData tmp = getObjectFromList(listChild, idVal);
                oldLastTime = tmp.getLastUpdateTime();
                representativeNumber = tmp.getLocationNumber() + tmp.getTerminalNumber();
            }

            //2. check if group child number not set
            if (callMethod != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                if (listChild == null || listChild.size() == 1) {
                    errorMsg = getText("g0602.errors.IncomingGroupChildSetting");
                    return INPUT;
                }
            } else
                if (listChild == null || listChild.size() == 0) {
                    errorMsg = getText("g0602.errors.IncomingGroupChildSetting");
                    return INPUT;
                }

            //4. check if group child member (except 1 representative number) over maximum
            if (callMethod != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                if (listChild.size() - 1 > getMaxIncomingGroupMember()) {
                    errorMsg = getText("g0602.errors.MaxExtensionNumber", new String[]{String.valueOf(getMaxIncomingGroupMember())});
                    //Start Step1.6 TMA #1398
                    //log.info(Util.message(Const.ERROR_CODE.I030405, String.format(Const.MESSAGE_CODE.I030405, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    //End Step1.6 TMA #1398
                    return INPUT;
                }
            } else
                if (listChild.size() > getMaxIncomingGroupMember()) {
                    errorMsg = getText("g0602.errors.MaxExtensionNumber", new String[]{String.valueOf(getMaxIncomingGroupMember())});
                    //Start Step1.6 TMA #1398
                    //log.info(Util.message(Const.ERROR_CODE.I030405, String.format(Const.MESSAGE_CODE.I030405, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    //End Step1.6 TMA #1398
                    return INPUT;
                }

            //5. check rep extension number info deleted or changed
            if (callMethod != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                // Start 1.x TMA-CR#138970
                Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(idVal), oldLastTime);
                // End 1.x TMA-CR#138970

                if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                    error = resultCheck.getError();

                    return ERROR;
                }
                if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                    errorMsg = getText("g0602.errors.ExtensionChanged");
                    log.info(Util.message(Const.ERROR_CODE.I030412, String.format(Const.MESSAGE_CODE.I030412, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    return INPUT;
                }
            }

            //6. check group child deleted or changed
            for (int i = 0; i < listChild.size(); i++) {
                long idChild = listChild.get(i).getExtensionNumberInfoId();
                if (idChild == idVal) {
                    continue;
                }
                oldLastTime = listChild.get(i).getLastUpdateTime();
                // Start 1.x TMA-CR#138970
                Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(idChild), oldLastTime);
                // End 1.x TMA-CR#138970

                if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                    error = resultCheck.getError();

                    return ERROR;
                }
                if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                    log.info(Util.message(Const.ERROR_CODE.I030413, String.format(Const.MESSAGE_CODE.I030413, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    errorMsg = getText("g0602.errors.GroupChildChanged");
                    return INPUT;
                }
            }

            //8. check representative extension number id(idVal) has been selected has already
            if (oldExtensionValue != idVal) {
                // Start 1.x TMA-CR#138970
                Result<Long> rsIncoming = DBService.getInstance().getCountIncomingGroupInfoByExtensionId(loginId, sessionId, nNumberInfoId, idVal);
                // End 1.x TMA-CR#138970
                if (rsIncoming.getRetCode() == Const.ReturnCode.NG || rsIncoming.getData() == null) {
                    error = rsIncoming.getError();

                    return ERROR;
                } else
                    if (rsIncoming.getData() >= 1) {
                        log.info(Util.message(Const.ERROR_CODE.I030414, String.format(Const.MESSAGE_CODE.I030414, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                        errorMsg = getText("g0602.errors.AlreadySetting");
                        return INPUT;
                    }
            }

            //9. check extension representative number candidate has been selected
            //is set to the arriving extension number of outside line information on 050pfb
            // Start 1.x TMA-CR#138970
            Result<List<OutsideCallInfo>> resultOutside = DBService.getInstance().getOutsideCallInfoByExtensionId(loginId, sessionId, nNumberInfoId, idVal);
            // End 1.x TMA-CR#138970
            if (resultOutside.getRetCode() == Const.ReturnCode.NG || resultOutside.getData() == null) {
                error = resultOutside.getError();

                return ERROR;
            } else
                if (resultOutside.getData().size() > 0) {
                    for (int i = 0; i < resultOutside.getData().size(); i++) {
                        if (resultOutside.getData().get(i).getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                            log.info(Util.message(Const.ERROR_CODE.I030415, String.format(Const.MESSAGE_CODE.I030415, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                            errorMsg = getText("g0602.input.OutsideServiceType");
                            return INPUT;
                        }
                    }
                }

            //10. if group call type = 3 check group child exist in DB
            //ignore if it's old value
            // START #547
            ArrayList<Long> oldListIdChild = new ArrayList<Long>();
            Result<List<IncomingGroupChildNumberInfo>> rsIncoming = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupId(callMethodName, representativeNumber, incomingGroupId);
            if (rsIncoming.getRetCode() == Const.ReturnCode.NG || rsIncoming.getData() == null) {
                error = rsIncoming.getError();
                return ERROR;
            }

            for (IncomingGroupChildNumberInfo obj : rsIncoming.getData()) {
                oldListIdChild.add(obj.getFkExtensionNumberInfoId());
            }

            // END #547
            if (callMethod == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                // START #471
                //get arraylist old child number from old list child json
                //                if (!commpareList(oldListChildJson, listChild)) {
                // END #471

                List<IncomingGroupChildNumberInfo> rsGroupChild = null;
                // Start 1.x TMA-CR#138970
                Result<List<IncomingGroupChildNumberInfo>> resultGroupChild = DBService.getInstance().getIncomingGroupChildNumberInfoByGroupCallType(loginId, sessionId, nNumberInfoId, Const.GROUP_CALL_TYPE.CALL_PICKUP);
                // End 1.x TMA-CR#138970
                if (resultGroupChild.getRetCode() == Const.ReturnCode.NG || resultGroupChild.getData() == null) {
                    error = resultGroupChild.getError();

                    return ERROR;
                } else {
                    rsGroupChild = resultGroupChild.getData();
                }

                for (int j = 0; j < listChild.size(); j++) {
                    long idChild = listChild.get(j).getExtensionNumberInfoId();
                    /*if (idChild == idVal) {
                        continue;
                    }*/
                    //if idChilld is old value, ignore this
                    if (oldListIdChild.contains(idChild)) {
                        continue;
                    }
                    for (int i = 0; i < rsGroupChild.size(); i++) {
                        if (idChild == rsGroupChild.get(i).getFkExtensionNumberInfoId()) {
                            log.info(Util.message(Const.ERROR_CODE.I030416, String.format(Const.MESSAGE_CODE.I030416, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                            errorMsg = getText("g0602.errors.IncomingGroupSettingExist");
                            return INPUT;
                        }
                    }
                }
                // START #471
                /*} else { //return success if have no change
                    return CHANGE;
                }*/
                // END #471
            }


            //11. update into incoming group info
            //12. update group child to DB
            //13. refleced in the extension server.

            //get arraylist child number from list child string
            ArrayList<Long> listIdChild = new ArrayList<Long>();
            for (int i = 0; i < listChild.size(); i++) {
                if (idVal == listChild.get(i).getExtensionNumberInfoId()) {
                    continue;
                }
                listIdChild.add(listChild.get(i).getExtensionNumberInfoId());
            }

            IncomingGroupSettingAddActionData tmp = getObjectFromList(listChild, idVal);
            if (callMethod != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                representativeNumber = tmp.getLocationNumber() + tmp.getTerminalNumber();
            }
            /*if (callMethod == Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING) {
                callMethodName = Const.GROUP_CALL_TYPE_NAME.SEQUENCE_INCOMING;
            } else if (callMethod == Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING) {
                callMethodName = Const.GROUP_CALL_TYPE_NAME.SIMULTANEOUS_INCOMING;
            } else if (callMethod == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                callMethodName = Const.GROUP_CALL_TYPE_NAME.CALL_PICKUP;
            }*/
            log.info(Util.message(Const.ERROR_CODE.I030402, String.format(Const.MESSAGE_CODE.I030402, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
            // Start 1.x TMA #1285
            //Result<Boolean> rs = DBService.getInstance().updateIncomingGroupSetting(loginId, sessionId, accountInfoId, nNumberInfoId, incomingGroupId, idVal, callMethod, listIdChild, oldListIdChild);
            Result<Boolean> rs = DBService.getInstance().updateIncomingGroupSetting(loginId, sessionId, accountInfoId, nNumberInfoId, incomingGroupId, idVal, oldExtensionValue, callMethod, listIdChild, oldListIdChild);
            // End 1.x TMA #1285
            if (rs.getRetCode() == Const.ReturnCode.NG) {
                //START #406
                // If data is locked (is updating by other user)
                if (rs.getLockTable() != null) {
                    errorMsg = getText("common.errors.LockTable", new String[]{rs.getLockTable()});
                    // START #596
                    log.info(Util.message(Const.ERROR_CODE.I030411, String.format(Const.MESSAGE_CODE.I030411, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    // END #596
                    return INPUT;
                } else {
                    error = rs.getError();
                    return ERROR;
                }
                //END #406

            }

        } catch (Exception e) {
            return ERROR;
        }

        return CHANGE;
    }

    /**
     * get incoming group setting from list data.
     *
     * @param data list data.
     * @param id id
     * @return incoming group setting add action data
     */
    private IncomingGroupSettingAddActionData getObjectFromList(List<IncomingGroupSettingAddActionData> data, long id) {
        if (data == null) {
            return null;
        }
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getExtensionNumberInfoId() == id) {
                return data.get(i);
            }
        }
        return null;
    }

    /**
     * getter for list2.
     *
     * @return list extension number info
     */
    public List<IncomingGroupSettingAddActionData> getList2() {
        return list2;
    }

    /**
     * setter for list 2.
     *
     * @param list2 list extension number info
     */
    public void setList2(List<IncomingGroupSettingAddActionData> list2) {
        this.list2 = list2;
    }

    /**
     * getter for  the errorMsg.
     *
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * setter for  the errorMsg.
     *
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * getter extension number info id in rep table.
     *
     * @return extension number info id in rep table
     */
    public long getExtensionNumberInfoIdSelect() {
        return extensionNumberInfoIdSelect;
    }

    /**
     * setter for extension number info id in rep table.
     *
     * @param extensionNumberInfoIdSelect extension number info id in rep table
     */
    public void setExtensionNumberInfoIdSelect(long extensionNumberInfoIdSelect) {
        this.extensionNumberInfoIdSelect = extensionNumberInfoIdSelect;
    }

    /**
     * getter for representative extension number info id.
     *
     * @return extension number info id
     */
    public long getIdVal() {
        return idVal;
    }

    /**
     * setter for representative extension number info id.
     *
     * @param idVal representative extension number info id.
     */
    public void setIdVal(long idVal) {
        this.idVal = idVal;
    }

    /**
     * getter location number group child candidate.
     *
     * @return location number
     */
    public String getLocationNumberCandidate() {
        return locationNumberCandidate;
    }

    /**
     * setter location number group child candidate.
     *
     * @param locationNumberCandidate location number group child candidate
     */
    public void setLocationNumberCandidate(String locationNumberCandidate) {
        this.locationNumberCandidate = locationNumberCandidate;
    }

    /**
     * getter terminal number group child candidate.
     *
     * @return terminal number
     */
    public String getTerminalNumberCandidate() {
        return terminalNumberCandidate;
    }

    /**
     * setter terminal number group child candidate.
     *
     * @param terminalNumberCandidate terminal number
     */
    public void setTerminalNumberCandidate(String terminalNumberCandidate) {
        this.terminalNumberCandidate = terminalNumberCandidate;
    }

    /**
     * getter for incoming group info id inserted.
     *
     * @return incoming group info id inserted.
     */
    public long getIncomingGroupId() {
        return incomingGroupId;
    }

    /**
     * setter for incoming group info id inserted.
     *
     * @param incomingGroupId incoming group info id inserted.
     */
    public void setIncomingGroupId(long incomingGroupId) {
        this.incomingGroupId = incomingGroupId;
    }

    /**
     * getter for list child.
     *
     * @return list child
     */
    public List<IncomingGroupSettingAddActionData> getListChild() {
        return listChild;
    }

    /**
     * setter for list child.
     *
     * @param listChild list child number
     */
    public void setListChild(List<IncomingGroupSettingAddActionData> listChild) {
        this.listChild = listChild;
    }

    /**
     * getter for group call type.
     *
     * @return group call type
     */
    public int getCallMethod() {
        return callMethod;
    }

    /**
     * setter for group call type.
     *
     * @param callMethod group call type
     */
    public void setCallMethod(int callMethod) {
        this.callMethod = callMethod;
    }

    /**
     * getter for old extension number info id.
     *
     * @return old extension number info id.
     */
    public long getOldExtensionValue() {
        return oldExtensionValue;
    }

    /**
     * setter for old extension number info id.
     *
     * @param oldExtensionValue old extension number info id.
     */
    public void setOldExtensionValue(long oldExtensionValue) {
        this.oldExtensionValue = oldExtensionValue;
    }

    /**
     * getter for action type.
     *
     * @return action type.
     */
    public int getActionType() {
        return actionType;
    }

    /**
     * setter for action type.
     *
     * @param actionType action type.
     */
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    /**
     * Getter of list2Json.
     *
     * @return list2Json
     */
    public String getList2Json() {
        return list2Json;
    }

    /**
     * Setter of list2Json.
     *
     * @param list2Json list2Json
     */
    public void setList2Json(String list2Json) {
        this.list2Json = list2Json;
    }

    /**
     * Getter of listChildJson.
     *
     * @return listChildJson
     */
    public String getListChildJson() {
        return listChildJson;
    }

    /**
     * Setter of listChildJson.
     *
     * @param listChildJson
     */
    public void setListChildJson(String listChildJson) {
        this.listChildJson = listChildJson;
    }

    /**
     * Getter of listChildJson.
     *
     * @return listChildJson
     */
    public List getExtensionNumberInfoIdChild() {
        return extensionNumberInfoIdChild;
    }

    /**
     * Setter of listChildJson.
     *
     * @param extensionNumberInfoIdChild
     */
    public void setExtensionNumberInfoIdChild(List extensionNumberInfoIdChild) {
        this.extensionNumberInfoIdChild = extensionNumberInfoIdChild;
    }

    // START #471
    /**
     * Getter for last update time.
     *
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     *
     * Setter for last update time.
     *
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    // END #471
}
//END [G06]
//(C) NTT Communications  2013  All Rights Reserved
