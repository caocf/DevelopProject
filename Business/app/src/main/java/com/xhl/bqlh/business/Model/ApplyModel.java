package com.xhl.bqlh.business.Model;

import com.xhl.bqlh.business.R;
import com.xhl.xhl_library.utils.NumberUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Sum on 16/4/25.
 * 车存申请
 */
public class ApplyModel implements Serializable {

    public int pMinNum;//最小个数

    private SkuModel skuResult;//sku信息
    private String id;                //主键id
    private String salseManId;        //业务员员id，bqlh_sys_user
    private String productId;        //商品ID
    private int applyNum;            //申请库存
    private int confirmNum;        //确认申请数量
    private int shstate;            //审核状态 0待审核 1审核通过  2不通过
    private int del_flag;            //删除标志 0-未删除 1-已删除
    private String createTime;
    private String updateTime;

    //商品信息
    private String logId;//装车单id
    private String storeId;
    private String productName;
    private String productPic;
    private int productType;//装车单商品类型：1.订单商品，2.车销商品 ， 4.车削赠品    3.装车单新增商品 7.新增赠品
    private BigDecimal originalPrice;
    private BigDecimal bussinessPrice;

    private float unitPrice;//单价
    private int stock;
    private ArrayList<ApplyModel> list;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public ArrayList<ApplyModel> getList() {
        return list;
    }

    public void setList(ArrayList<ApplyModel> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSalseManId() {
        return salseManId;
    }

    public void setSalseManId(String salseManId) {
        this.salseManId = salseManId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getApplyNum() {
        return applyNum;
    }

    public void setApplyNum(int applyNum) {
        this.applyNum = applyNum;
    }

    public int getConfirmNum() {
        return confirmNum;
    }

    public void setConfirmNum(int confirmNum) {
        this.confirmNum = confirmNum;
    }

    public int getShstate() {
        return shstate;
    }

    public void setShstate(int shstate) {
        this.shstate = shstate;
    }

    public int getDel_flag() {
        return del_flag;
    }

    public void setDel_flag(int del_flag) {
        this.del_flag = del_flag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public BigDecimal getBussinessPrice() {
        return bussinessPrice;
    }

    public void setBussinessPrice(BigDecimal bussinessPrice) {
        this.bussinessPrice = bussinessPrice;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getProductPrice() {
        if (bussinessPrice == null) {
            return originalPrice;
        }
        return bussinessPrice;
    }

    public int getProductPriceHint() {
        if (bussinessPrice != null) {
            return R.string.product_price;
        }
        return R.string.product_price_or;
    }

    //总额根据创建的时候保存的单价计算（包含订单商品单价和车销商品单价）
    public String getAllPrice() {
        float all = 0;
        for (ApplyModel apply : list) {
            float productPrice = apply.getUnitPrice() * apply.getApplyNum();
            all += productPrice;
        }
        return NumberUtil.getDouble(all);
    }

    public int getAllProNum() {
        int all = 0;
        for (ApplyModel apply : list) {
            all += apply.getApplyNum();
        }
        return all;
    }

    //0待审核 1审核通过  2不通过
    public String getStateDesc() {
        if (shstate == 0) {
            return "当前状态:可修改";
        } else if (shstate == 1) {
            return "当前状态:已通过";
        } else {
            return "当前状态:已拒绝";
        }
    }

    public int getStateBg() {
        if (shstate == 0) {
            return R.drawable.code_store_apply_state_1;
        } else if (shstate == 1) {
            return R.drawable.code_store_apply_state_2;
        } else {
            return R.drawable.code_store_apply_state_3;
        }
    }

    public SkuModel getSkuResult() {
        if (skuResult == null) {
            return SKU_MODEL;
        }
        return skuResult;
    }

    public void setSkuResult(SkuModel skuResult) {
        this.skuResult = skuResult;
    }

    public static final SkuModel SKU_MODEL = new SkuModel();
}
