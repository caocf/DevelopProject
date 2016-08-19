package com.xhl.bqlh.business.Model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.List;

public class ProductReturn implements Serializable {

    private String returnId;//退货表主键
    private String returnType;//退货类型：1-有订单退货；2-无订单退货
    private String storeOrderCode;//子订单编号
    private float applyReturnMoney;//申请退货金额
    private float verifyReturnMoney;//核定退货金额
    private String returnState;//退货状态：1-待审核；2-仓管预审通过；3-仓管预审不通过；4-仓管确认入库；5-仓库拒绝入库；6-财务审核通过；7-财务审核不通过
    private String retailer;             //零售商id，对应bqlh_sys__user的id
    private String dealer;//经销商id，对应bqlh_user的uid
    private String applicant;//申请业务员id,对应bqlh_sys_user的uid
    private String applyTime;//申请时间
    private String returnDesc;//退货说明
    private String auditor;//审核人id，对应bqlh_sys_user的uid
    private String auditTime;//审核时间
    private String auditDesc;//审核说明
    private String retailerName;         //零售商名称
    private String auditorName;             //审核人名称
    private int delFlag;                 //删除标志 0-未删除 1-已删除

    private List<ProductReturnDetail> returnDetailList;//退货商品详情

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    private String attrReturnDetail;//退货详情字符串

    public String getAttrReturnDetail() {
        return attrReturnDetail;
    }

    public void setAttrReturnDetail(String attrReturnDetail) {
        this.attrReturnDetail = attrReturnDetail;
    }

    private String queryState;//查询状态，对returnState进行归类，用于查询
    public static final String QUERY_STATE_ONE = "1";//查询状态1-待审核，对应退货状态returnState的1-待审核
    public static final String QUERY_STATE_TWO = "2";//查询状态2-审核中，对应退货状态returnState的2-仓管预审通过；4-仓管确认入库；
    public static final String QUERY_STATE_THREE = "3";//查询状态3-已完成，对应退货状态returnState的3-仓管预审不通过；5-仓库拒绝入库；6-财务审核通过；7-财务审核不通过

    public List<ProductReturnDetail> getReturnDetailList() {
        return returnDetailList;
    }

    public void setReturnDetailList(List<ProductReturnDetail> returnDetailList) {
        this.returnDetailList = returnDetailList;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getRetailerName() {
        return retailerName;
    }

    public void setRetailerName(String retailerName) {
        this.retailerName = retailerName;
    }

    public int getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(int delFlag) {
        this.delFlag = delFlag;
    }

    public String getReturnId() {
        return returnId;
    }

    public void setReturnId(String returnId) {
        this.returnId = returnId;
    }


    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getStoreOrderCode() {
        return storeOrderCode;
    }

    public void setStoreOrderCode(String storeOrderCode) {
        this.storeOrderCode = storeOrderCode;
    }

    public float getApplyReturnMoney() {
        return applyReturnMoney;
    }

    public void setApplyReturnMoney(float applyReturnMoney) {
        this.applyReturnMoney = applyReturnMoney;
    }

    public float getVerifyReturnMoney() {
        return verifyReturnMoney;
    }

    public void setVerifyReturnMoney(float verifyReturnMoney) {
        this.verifyReturnMoney = verifyReturnMoney;
    }

    //1-待审核；2-仓管预审通过；3-仓管预审不通过；4-仓管确认入库；5-仓库拒绝入库；6-财务审核通过；7-财务审核不通过
    public String getReturnState() {
        if (TextUtils.isEmpty(returnState)) {
            return "异常状态";
        }
        if (returnState.equals("1")) {
            return "待审核";
        }
        if (returnState.equals("2")) {
            return "仓管预审通过";
        }
        if (returnState.equals("3")) {
            return "仓管预审拒绝";
        }
        if (returnState.equals("4")) {
            return "仓管确认入库";
        }
        if (returnState.equals("5")) {
            return "仓库拒绝入库";
        }
        if (returnState.equals("6")) {
            return "财务审核通过";
        }
        if (returnState.equals("7")) {
            return "财务审核拒绝";
        }
        return returnState;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public String getQueryState() {
        return queryState;
    }

    public void setQueryState(String queryState) {
        this.queryState = queryState;
    }
}
