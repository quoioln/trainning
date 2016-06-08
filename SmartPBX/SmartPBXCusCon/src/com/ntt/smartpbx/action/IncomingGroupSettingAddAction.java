//START [G06]
package com.ntt.smartpbx.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ntt.smartpbx.SPCCInit;
import com.ntt.smartpbx.model.Result;
import com.ntt.smartpbx.model.data.IncomingGroupSettingAddActionData;
import com.ntt.smartpbx.model.db.IncomingGroupChildNumberInfo;
import com.ntt.smartpbx.model.db.OutsideCallInfo;
import com.ntt.smartpbx.model.service.DBService;
import com.ntt.smartpbx.utils.Const;
import com.ntt.smartpbx.utils.Util;

/**
 * 名称: IncomingGroupSettingAddAction class.
 * 機能概要: Process the incoming group setting addition
 */
public class IncomingGroupSettingAddAction extends BaseAction {
    /** The default serial version. */
    private static final long serialVersionUID = 1L;
    /** The logger. */
    // Start step 2.5 #1946
    private static Logger log = Logger.getLogger(IncomingGroupSettingAddAction.class);
    // End step 2.5 #1946
    /** List extension number candidate. */
    private List<IncomingGroupSettingAddActionData> list2 = new ArrayList<IncomingGroupSettingAddActionData>();
    /** List extension number in table child. */
    private List<IncomingGroupSettingAddActionData> listChild = new ArrayList<IncomingGroupSettingAddActionData>();
    /** max incoming group member. */
    private int maxIncomingGroupMember;
    /** error message. */
    private String errorMgs;
    /** value of call type */
    private int callType;
    /** value of radio  be selected in representative number candidate table. */
    private long extensionNumberInfoIdSelect;
    /** The extension number info id of radio be selected. */
    private long idVal;
    /** location number : filter conditional in cand table. */
    private String locationNumberCandidate;
    /** terminal number : filter conditional in cand table. */
    private String terminalNumberCandidate;
    /**incoming group info id inserted. */
    private long incomingGroupIdInserted;
    /**The action type. */
    private int actionType;
    /**Json data of list2. */
    private String list2Json;
    /**Json data of listChild. */
    private String listChildJson;
    /**The extension number Info Id Child Selected. */
    private List extensionNumberInfoIdChild;

    /** The object for select tag terminal type. */
    private Map<Object, Object> selectTerminalType = new LinkedHashMap<Object, Object>();


    /**
     * Default constructor.
     */
    public IncomingGroupSettingAddAction() {
        super();
        this.locationNumberCandidate = Const.EMPTY;
        this.terminalNumberCandidate = Const.EMPTY;
        this.incomingGroupIdInserted = 0;
        this.actionType = ACTION_INIT;
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    @Override
    protected void initMap() {
        selectTerminalType.put(Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING, getText("common.CallMethod.1"));
        selectTerminalType.put(Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING, getText("common.CallMethod.2"));
        selectTerminalType.put(Const.GROUP_CALL_TYPE.CALL_PICKUP, getText("common.CallMethod.3"));
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingAdd.jsp
     *      SEARCH: ExtensionSettingView.jsp
     *      INPUT: IncomingGroupSettingAdd.jsp
     *      CHANGE: ExtensionSettingUpdate.jsp
     *      ERROR: SystemError.jsp
     */
    public String execute() {
        //Init list map
        initMap();
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
     * The init method of action
     *
     * @return
     *      SUCCESS: IncomingGroupSettingAdd.jsp
     *      ERROR: SystemError.jsp
     *      OK: do something success
     */
    public String doInit() {

        // START Step1.x #1091
        createToken();
        // END Step1.x #1091

        String rs = getDataForList2();
        if (!rs.equals(OK)) {
            return rs;
        }
        return SUCCESS;
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
     * append child to json.
     *
     * @return result of method.
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
            // try {
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
     * The search method of action
     *
     * @return
     *      SEARCH: IncomingGroupSettingAdd.jsp
     *      INPUT: IncomingGroupSettingAdd.jsp
     *      ERROR: SystemError.jsp
     *      OK: do something success
     */
    private String doSearch2() {
        list2 = null;
        list2Json = null;
        //Start Step1.x #1091
        if (!validateSearchFieldsNoOutputError(locationNumberCandidate, terminalNumberCandidate)) {
            //addFieldError("search2", getText("input.validate.InvalidInput"));
            locationNumberCandidate = Const.EMPTY;
            terminalNumberCandidate = Const.EMPTY;

            /*return INPUT;*/
        }
        //End Step1.x #1091
        String rs = getDataForList2();
        if (!rs.equals(OK)) {
            return rs;
        }
        return SEARCH;
    }

    /**
     * validate for input search.
     *
     * @param arg1 input field 1.
     * @param arg2 input field 2.
     * @return true: have error, false: no error
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
     * get data for list 2.
     *
     * @return result of method.
     */
    public String getDataForList2() {
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Result<List<IncomingGroupSettingAddActionData>> result2 = DBService.getInstance().getExtensionNumberInfoFilterIgnoreTerminalType3(loginId, sessionId, nNumberInfoId, locationNumberCandidate, terminalNumberCandidate);
        if (result2.getRetCode() == Const.ReturnCode.NG || result2.getData() == null) {
            error = result2.getError();
            return ERROR;
        } else {
            list2 = result2.getData();
            list2Json = (new Gson()).toJson(list2);
        }
        return OK;
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
     * The change method of action
     *
     * @return
     *      CHANGE: IncomingGroupSettingAddFinish.jsp
     *      INPUT: IncomingGroupSettingAdd.jsp
     *      ERROR: SystemError.jsp
     *      OK: do something success
     */
    @SuppressWarnings("unchecked")
    public String doChange() {
        Long accountInfoId = (Long) session.get(Const.Session.ACCOUNT_INFO_ID);
        String loginId = (String) session.get(Const.Session.LOGIN_ID);
        String sessionId = (String) session.get(Const.Session.SESSION_ID);
        Long nNumberInfoId = (Long) session.get(Const.Session.N_NUMBER_INFO_ID);

        String callMethodName = Const.EMPTY;
        if (callType == Const.GROUP_CALL_TYPE.SEQUENCE_INCOMING) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.SEQUENCE_INCOMING;
        } else if (callType == Const.GROUP_CALL_TYPE.SIMULTANEOUS_INCOMING) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.SIMULTANEOUS_INCOMING;
        } else if (callType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
            callMethodName = Const.GROUP_CALL_TYPE_NAME.CALL_PICKUP;
        }

        String oldLastTime = Const.EMPTY;
        String representativeNumber = Const.EMPTY;
        try {
            // START #429
            //find incoming group name unuse to insert
            String incomingGroupName = null;
            //Start Step1.x #1136
            Result<List<String>> resultIncomingName = DBService.getInstance().getListIncomingGroupNameUndelete(loginId, sessionId, nNumberInfoId);
            //End Step1.x #1136
            if (resultIncomingName.getRetCode() == Const.ReturnCode.NG || resultIncomingName.getData() == null) {
                setError(resultIncomingName.getError());
                return ERROR;
            }
            List<String> rsIncomingName = resultIncomingName.getData();
            for (int j = 1; j <= SPCCInit.config.getCusconMaxGroupNumber(); j++) {
                //j not exist into table >> insert and return
                //Start #1457
                String temp = StringUtils.leftPad(String.valueOf(j), Const.NUMBER_CHARACTER_INCOMING_GROUP_NAME, "0");
                //End #1457
                if (!rsIncomingName.contains(temp)) {
                    incomingGroupName = temp;
                    break;
                }
            }
            // END #429

            //1. if group call type !=3 check representative extension number select
            if (callType != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                if (extensionNumberInfoIdSelect == 0) {
                    errorMgs = getText("g0602.errors.NoSelection");
                    return INPUT;
                }
                idVal = extensionNumberInfoIdSelect;
                oldLastTime = getObjectFromList(listChild, idVal).getLastUpdateTime();

                IncomingGroupSettingAddActionData tmp = getObjectFromList(listChild, idVal);
                representativeNumber = tmp.getLocationNumber() + tmp.getTerminalNumber();
            }

            //2. check if group child number not set
            if (callType != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                if (listChild == null || listChild.size() == 1) {
                    errorMgs = getText("g0602.errors.IncomingGroupChildSetting");
                    return INPUT;
                }
            } else if (listChild == null || listChild.size() == 0) {
                errorMgs = getText("g0602.errors.IncomingGroupChildSetting");
                return INPUT;
            }

            //4. check if group child member (except 1 representative number if group call type is call pickup) over maximum
            if (callType != Const.GROUP_CALL_TYPE.CALL_PICKUP) {
                if (listChild.size() - 1 > getMaxIncomingGroupMember()) {
                    errorMgs = getText("g0602.errors.MaxExtensionNumber", new String[]{String.valueOf(getMaxIncomingGroupMember())});
                    //Start Step1.6 TMA #1398
                    //log.info(Util.message(Const.ERROR_CODE.I030405, String.format(Const.MESSAGE_CODE.I030405, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    //End Step1.6 TMA #1398
                    return INPUT;
                }
            } else if (listChild.size() > getMaxIncomingGroupMember()) {
                errorMgs = getText("g0602.errors.MaxExtensionNumber", new String[]{String.valueOf(getMaxIncomingGroupMember())});
                //Start Step1.6 TMA #1398
                //log.info(Util.message(Const.ERROR_CODE.I030405, String.format(Const.MESSAGE_CODE.I030405, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                //End Step1.6 TMA #1398
                return INPUT;
            }

            //5. check rep extension number info deleted or changed
            if (callType != Const.GROUP_CALL_TYPE.CALL_PICKUP) {

                // Start 1.x TMA-CR#138970
                Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(idVal), oldLastTime);
                // End 1.x TMA-CR#138970

                if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                    error = resultCheck.getError();
                    return ERROR;
                }

                if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                    errorMgs = getText("g0602.errors.ExtensionChanged");
                    log.info(Util.message(Const.ERROR_CODE.I030406, String.format(Const.MESSAGE_CODE.I030406, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    return INPUT;
                }
                /*if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                    log.info(Util.message(Const.ERROR_CODE.I030406, String.format(Const.MESSAGE_CODE.I030406, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    errorMgs = getText("g0602.errors.ExtensionChanged");
                    return INPUT;
                }*/
            }

            //6. check group child deleted or changed
            for (int i = 0; i < listChild.size(); i++) {
                long idChild = listChild.get(i).getExtensionNumberInfoId();
                if (idChild == idVal) {
                    continue;
                }
                oldLastTime = getObjectFromList(listChild, idChild).getLastUpdateTime();

                // Start 1.x TMA-CR#138970
                Result<Integer> resultCheck = DBService.getInstance().checkDeleteFlag(loginId, sessionId, nNumberInfoId, Const.TableName.EXTENSION_NUMBER_INFO, Const.TableKey.EXTENSION_NUMBER_INFO_ID, String.valueOf(idChild), oldLastTime);
                // End 1.x TMA-CR#138970

                if (resultCheck.getRetCode() == Const.ReturnCode.NG) {
                    error = resultCheck.getError();

                    return ERROR;
                }

                if (resultCheck.getData() == Const.ReturnCheck.IS_CHANGE || resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                    log.info(Util.message(Const.ERROR_CODE.I030407, String.format(Const.MESSAGE_CODE.I030407, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    errorMgs = getText("g0602.errors.GroupChildChanged");
                    return INPUT;
                }
                /*if (resultCheck.getData() == Const.ReturnCheck.IS_DELETE) {
                    log.info(Util.message(Const.ERROR_CODE.I030407, String.format(Const.MESSAGE_CODE.I030407, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    errorMgs = getText("g0602.errors.GroupChildChanged");
                    return INPUT;
                }*/

            }

            //7. check max incoming group
            Result<Long> rsMaxGroup = DBService.getInstance().getCountGroupIncomingInfo(loginId, sessionId, nNumberInfoId);
            if (rsMaxGroup.getRetCode() == Const.ReturnCode.NG || rsMaxGroup.getData() == null) {
                error = rsMaxGroup.getError();

                return ERROR;
            } else if (rsMaxGroup.getData() >= SPCCInit.config.getCusconMaxGroupNumber()) {
                errorMgs = getText("g0602.errors.MaxIncomingGroup");
                //Start Step1.6 TMA #1398
                log.info(Util.message(Const.ERROR_CODE.I030405, String.format(Const.MESSAGE_CODE.I030405, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                //End Step1.6 TMA #1398
                return INPUT;
            }

            //8. check extension representative number candidate id(idVal) has been selected has already
            // Start 1.x TMA-CR#138970
            Result<Long> rsIncoming = DBService.getInstance().getCountIncomingGroupInfoByExtensionId(loginId, sessionId, nNumberInfoId, idVal);
            // End 1.x TMA-CR#138970
            if (rsIncoming.getRetCode() == Const.ReturnCode.NG || rsIncoming.getData() == null) {
                error = rsIncoming.getError();
                return ERROR;
            } else if (rsIncoming.getData() >= 1) {
                log.info(Util.message(Const.ERROR_CODE.I030408, String.format(Const.MESSAGE_CODE.I030408, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                errorMgs = getText("g0602.errors.AlreadySetting");
                return INPUT;
            }

            //9. check extension representative number candidate has been selected
            //is set to the arriving extension number of outside line information on 050pfb
            // Start 1.x TMA-CR#138970
            Result<List<OutsideCallInfo>> resultOutside = DBService.getInstance().getOutsideCallInfoByExtensionId(loginId, sessionId, nNumberInfoId, idVal);
            // End 1.x TMA-CR#138970
            if (resultOutside.getRetCode() == Const.ReturnCode.NG || resultOutside.getData() == null) {

                error = resultOutside.getError();
                return ERROR;
            } else if (resultOutside.getData().size() > 0) {
                for (int i = 0; i < resultOutside.getData().size(); i++) {
                    if (resultOutside.getData().get(i).getOutsideCallServiceType() == Const.OUTSIDE_CALL_SERVICE_TYPE.O50_PLUS_FOR_BIZ) {
                        log.info(Util.message(Const.ERROR_CODE.I030409, String.format(Const.MESSAGE_CODE.I030409, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                        errorMgs = getText("g0602.input.OutsideServiceType");
                        return INPUT;
                    }
                }
            }

            //10. if group call type = 3 check group child exist in DB
            if (callType == Const.GROUP_CALL_TYPE.CALL_PICKUP) {
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
                    /* if (idChild == idVal) {
                        continue;
                    }*/
                    for (int i = 0; i < rsGroupChild.size(); i++) {
                        if (idChild == rsGroupChild.get(i).getFkExtensionNumberInfoId()) {
                            log.info(Util.message(Const.ERROR_CODE.I030410, String.format(Const.MESSAGE_CODE.I030410, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                            errorMgs = getText("g0602.errors.IncomingGroupSettingExist");
                            return INPUT;
                        }
                    }
                }
            }

            //11. add into incoming group info
            //12. add group child to DB
            //13. refleced in the extension server.
            //get arraylist child number from list child string
            ArrayList<Long> listIdChild = new ArrayList<Long>();
            for (int i = 0; i < listChild.size(); i++) {
                if (idVal == listChild.get(i).getExtensionNumberInfoId()) {
                    continue;
                }
                listIdChild.add(listChild.get(i).getExtensionNumberInfoId());
            }
            log.info(Util.message(Const.ERROR_CODE.I030401, String.format(Const.MESSAGE_CODE.I030401, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
            Result<Long> rs = DBService.getInstance().addIncomingGroupSetting(loginId, sessionId, accountInfoId, nNumberInfoId, idVal, callType, listIdChild, incomingGroupName);
            if (rs.getRetCode() == Const.ReturnCode.NG) {
                //START #406
                // If data is locked (is updating by other user)
                if (rs.getLockTable() != null) {
                    errorMgs = getText("common.errors.LockTable", new String[]{rs.getLockTable()});
                    // START #596
                    log.info(Util.message(Const.ERROR_CODE.I030407, String.format(Const.MESSAGE_CODE.I030407, loginId, sessionId, incomingGroupName, callMethodName, representativeNumber)));
                    // END #596
                    return INPUT;
                } else {
                    error = rs.getError();
                    return ERROR;
                }
                //END #406

            }
            incomingGroupIdInserted = rs.getData();

            /* } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return ERROR;*/
        } catch (Exception e) {
            log.error(e.getMessage());
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
     * getter error message.
     *
     * @return  error message.
     */
    public String getErrorMgs() {
        return errorMgs;
    }

    /**
     * setter  error message.
     *
     * @param errorMgs  error message.
     */
    public void setErrorMgs(String errorMgs) {
        this.errorMgs = errorMgs;
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
     * @return selectTerminalType
     */
    public Map<Object, Object> getSelectTerminalType() {
        return selectTerminalType;
    }

    /**
     * @param selectTerminalType
     */
    public void setSelectTerminalType(Map<Object, Object> selectTerminalType) {
        this.selectTerminalType = selectTerminalType;
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
    public long getIncomingGroupIdInserted() {
        return incomingGroupIdInserted;
    }

    /**
     * setter for incoming group info id inserted.
     *
     * @param incomingGroupIdInserted incoming group info id inserted.
     */
    public void setIncomingGroupIdInserted(long incomingGroupIdInserted) {
        this.incomingGroupIdInserted = incomingGroupIdInserted;
    }

    /**
     * getter selecte tag value.
     *
     * @return value of selecte tag
     */
    public int getCallType() {
        return callType;
    }

    /**
     * setter selected tag value.
     *
     * @param callType value of select tag
     */
    public void setCallType(int callType) {
        this.callType = callType;
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
     * @param list2Json
     */
    public void setList2Json(String list2Json) {
        this.list2Json = list2Json;
    }

    /**
     * Getter of listChild.
     *
     * @return listChild
     */
    public List<IncomingGroupSettingAddActionData> getListChild() {
        return listChild;
    }

    /**
     * Setter of listChild.
     *
     * @param listChild
     */
    public void setListChild(List<IncomingGroupSettingAddActionData> listChild) {
        this.listChild = listChild;
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
     * Getter of extensionNumberInfoIdChild.
     *
     * @return extensionNumberInfoIdChild
     */
    public List getExtensionNumberInfoIdChild() {
        return extensionNumberInfoIdChild;
    }

    /**
     * Setter of extensionNumberInfoIdChildSelect.
     *
     * @param extensionNumberInfoIdChild
     */
    public void setExtensionNumberInfoIdChild(List extensionNumberInfoIdChild) {
        this.extensionNumberInfoIdChild = extensionNumberInfoIdChild;
    }
}
//END [G06]
//(C) NTT Communications  2013  All Rights Reserved
