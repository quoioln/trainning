package com.ntt.smartpbx.action;

import com.ntt.smartpbx.utils.Const;

/**
 * 名称: OutsideIncomingSettingDeleteFinishAction class
 * 機能概要: Finish delete setting for Outside Incoming Setting page
 */
public class OutsideIncomingSettingDeleteFinishAction extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public OutsideIncomingSettingDeleteFinishAction() {
        super();
        this.authenRoles.add(Const.ACCOUNT_TYPE.USER_MANAGER);
    }

    /**
     * The execute method of action
     *
     * @return
     *      SUCCESS: OutsideIncomingSettingDeleteFinish.jsp
     */
    public String execute() {
        return SUCCESS;
    }
}
// (C) NTT Communications  2013  All Rights Reserved
