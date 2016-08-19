package com.xhl.bqlh.business.Model.App;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sum on 16/4/9.
 */
public class ShopCarModel implements Parcelable, Cloneable {

    public String productId;
    public String productName;
    public String productPic;
    public int productType;
    public String productRemark;
    public String companyId;
    //商品原价
    public float productPrice;

    //修改后价格
    public float productFixPrice = 0;
    //是否显示修改价格按钮
    public boolean showFixPrice = true;

    //购物数量
    public int curNum;
    //最大数量
    public int maxNum;
    //最大单位
    public String unitMax;
    //最小单位
    public String unitMin;
    //换算比例(控制显示)
    public int conversionRate;
    //是否在购物车中
    public boolean inCar;
    //价格提示关联R.string.文本信息
    public int priceHint;

    public int operatorType;//操作类型，ConfirmProductActivity中定义

    public ShopCarModel() {

    }

    public float getProductPrice() {
        if (productFixPrice > 0) {
            return productFixPrice;
        }
        return productPrice;
    }

    //商品优惠金额
    public float getDiscount() {
        if (productFixPrice == 0) {
            return 0;
        } else {
            return productPrice - productFixPrice;
        }
    }

    @Override
    public ShopCarModel clone() throws CloneNotSupportedException {
        return (ShopCarModel) super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productId);
        dest.writeString(this.productName);
        dest.writeString(this.productPic);
        dest.writeInt(this.productType);
        dest.writeString(this.productRemark);
        dest.writeString(this.companyId);
        dest.writeFloat(this.productPrice);
        dest.writeFloat(this.productFixPrice);
        dest.writeByte(this.showFixPrice ? (byte) 1 : (byte) 0);
        dest.writeInt(this.curNum);
        dest.writeInt(this.maxNum);
        dest.writeString(this.unitMax);
        dest.writeString(this.unitMin);
        dest.writeInt(this.conversionRate);
        dest.writeByte(this.inCar ? (byte) 1 : (byte) 0);
        dest.writeInt(this.priceHint);
        dest.writeInt(this.operatorType);
    }

    protected ShopCarModel(Parcel in) {
        this.productId = in.readString();
        this.productName = in.readString();
        this.productPic = in.readString();
        this.productType = in.readInt();
        this.productRemark = in.readString();
        this.companyId = in.readString();
        this.productPrice = in.readFloat();
        this.productFixPrice = in.readFloat();
        this.showFixPrice = in.readByte() != 0;
        this.curNum = in.readInt();
        this.maxNum = in.readInt();
        this.unitMax = in.readString();
        this.unitMin = in.readString();
        this.conversionRate = in.readInt();
        this.inCar = in.readByte() != 0;
        this.priceHint = in.readInt();
        this.operatorType = in.readInt();
    }

    public static final Creator<ShopCarModel> CREATOR = new Creator<ShopCarModel>() {
        @Override
        public ShopCarModel createFromParcel(Parcel source) {
            return new ShopCarModel(source);
        }

        @Override
        public ShopCarModel[] newArray(int size) {
            return new ShopCarModel[size];
        }
    };
}
