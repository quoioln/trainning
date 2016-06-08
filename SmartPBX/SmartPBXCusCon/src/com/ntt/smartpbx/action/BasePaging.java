package com.ntt.smartpbx.action;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ntt.smartpbx.utils.Const;

/**
 * 名称: BasePaging<T> class
 * 機能概要: Define common methods, variables for paging action
 */
public class BasePaging<T> extends BaseAction {
    /** The default serial version */
    private static final long serialVersionUID = 1L;
    /** The current page */
    protected int currentPage = 0;
    /** The count of total records */
    protected long totalRecords = 0;
    /** The number of rows per page */
    protected int rowsPerPage = 0;
    /** The count of total pages */
    protected int totalPages = 0;
    /** List data */
    protected List<T> data;
    /** The list object for select tag. */
    protected Map<Object, Object> selectRowPerPage = new LinkedHashMap<Object, Object>();


    /**
     * Default constructor
     */
    public BasePaging() {
        currentPage = Const.DEFAULT_CURENT_PAGE;
        rowsPerPage = Const.DEFAULT_ROW_PER_PAGE;
    }

    @Override
    protected void initMap(){
        selectRowPerPage.put(Const.RowPerPage.option1, Const.RowPerPage.option1 + Const.SPACE + getText("common.Unit"));
        selectRowPerPage.put(Const.RowPerPage.option2, Const.RowPerPage.option2 + Const.SPACE + getText("common.Unit"));
        selectRowPerPage.put(Const.RowPerPage.option3, Const.RowPerPage.option3 + Const.SPACE + getText("common.Unit"));
    }
    /**
     * @return the currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * @param currentPage the currentPage to set
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * @return the totalRecords
     */
    public long getTotalRecords() {
        return totalRecords;
    }

    /**
     * @param totalRecords the totalRecords to set
     */
    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }

    /**
     * @return the rowsPerPage
     */
    public int getRowsPerPage() {
        return rowsPerPage;
    }

    /**
     * @param rowsPerPage the rowsPerPage to set
     */
    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * @return the totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages the totalPages to set
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * @return the data
     */
    public List<T> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<T> data) {
        this.data = data;
    }

    /**
     * @return selectRowPerPage
     */
    public Map<Object, Object> getSelectRowPerPage() {
        return selectRowPerPage;
    }

    /**
     * @param selectRowPerPage
     */
    public void setSelectRowPerPage(Map<Object, Object> selectRowPerPage) {
        this.selectRowPerPage = selectRowPerPage;
    }

}
//(C) NTT Communications  2013  All Rights Reserved