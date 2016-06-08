package com.ntt.smartpbx.model.data;

/**
 * 名称: CountVMType class
 * 機能概要: Data class for Count VM Type
 */
public class CountVMType {
    /**The vm_resource_type_master_id */
    private Long vmResourceTypeId;
    /**The vm_resource_type_master_id */
    private String vmResourceTypeName;
    /**The count of row have n_number_info_id is NULL */
    private Integer coutRowNull;
    //Step3.0 START #ADD-02
    /** The wholesale type */
    private Integer wholesaleType;
    //Step3.0 END #ADD-02

    /**
     * Default constructor
     */
    public CountVMType() {
        super();
    }

    /**
     * @return the vmResourceTypeId
     */
    public Long getVmResourceTypeId() {
        return vmResourceTypeId;
    }

    /**
     * @param vmResourceTypeId the vmResourceTypeId to set
     */
    public void setVmResourceTypeId(Long vmResourceTypeId) {
        this.vmResourceTypeId = vmResourceTypeId;
    }

    /**
     * @return the vmResourceTypeName
     */
    public String getVmResourceTypeName() {
        return vmResourceTypeName;
    }

    /**
     * @param vmResourceTypeName the vmResourceTypeName to set
     */
    public void setVmResourceTypeName(String vmResourceTypeName) {
        this.vmResourceTypeName = vmResourceTypeName;
    }

    /**
     * @return the coutRowNull
     */
    public Integer getCoutRowNull() {
        return coutRowNull;
    }

    /**
     * @param coutRowNull the coutRowNull to set
     */
    public void setCoutRowNull(Integer coutRowNull) {
        this.coutRowNull = coutRowNull;
    }

    /**
     * 
     * @return wholesaleType
     */
    public Integer getWholesaleType() {
        return wholesaleType;
    }

    /**
     * 
     * @param wholesaleType
     */
    public void setWholesaleType(Integer wholesaleType) {
        this.wholesaleType = wholesaleType;
    }
    
}
// (C) NTT Communications  2013  All Rights Reserved
