package com.xhl.bqlh.business.doman;

import android.text.TextUtils;

import com.xhl.bqlh.business.Db.DbTaskHelper;
import com.xhl.bqlh.business.Db.Member;
import com.xhl.bqlh.business.Model.App.SearchShopModel;
import com.xhl.bqlh.business.Model.App.ShopCarModel;
import com.xhl.bqlh.business.Model.ApplyModel;
import com.xhl.bqlh.business.Model.GiftModel;
import com.xhl.bqlh.business.Model.OrderDetail;
import com.xhl.bqlh.business.Model.ProductModel;
import com.xhl.bqlh.business.Model.ShopApplyModel;
import com.xhl.bqlh.business.Model.SkuModel;
import com.xhl.bqlh.business.Model.Type.ProductType;
import com.xhl.bqlh.business.Model.Type.StoreProductType;
import com.xhl.bqlh.business.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sum on 16/5/9.
 */
public class ModelHelper {

    public static ShopCarModel ProductModel2ShopCarModel(ProductModel product, String shopId) {
        //商品单位信息
        SkuModel skuResult = product.getSkuResult();
        ShopCarModel carModel = new ShopCarModel();
        //换算比
        carModel.conversionRate = skuResult.getCommisionProportion();
        //最小单位
        carModel.unitMin = skuResult.getUnit();
        carModel.unitMax = skuResult.getLargestUnit();

        carModel.curNum = product.curNum;
        carModel.maxNum = product.getStock();
        carModel.inCar = true;
        carModel.productRemark = product.getPromoteRemark();
        carModel.productPic = product.getProductPic();
        carModel.productId = product.getId();
        carModel.productName = product.getProductName();
        carModel.companyId = product.getStoreId();
        carModel.productPrice = product.getProductPrice().floatValue();
        carModel.productType = product.getProductType();
        //批发价
        if (!TextUtils.isEmpty(shopId)) {
            carModel.priceHint = R.string.product_price;
        } else {
            //建议零售价
            carModel.priceHint = R.string.product_price_or;
        }

        return carModel;
    }

    public static ShopCarModel OrderDetail2ShopCarModel(OrderDetail product) {
        //商品单位信息
        SkuModel skuResult = product.getSkuResult();
        ShopCarModel carModel = new ShopCarModel();
        //换算比
        carModel.conversionRate = skuResult.getCommisionProportion();
        //最小单位
        carModel.unitMin = skuResult.getUnit();
        carModel.unitMax = skuResult.getLargestUnit();

        carModel.maxNum = product.getNum().intValue();
        carModel.curNum = carModel.maxNum;

        carModel.productPic = product.getProductPic();
        carModel.productId = product.getGoodId();
        carModel.productName = product.getProductName();
        carModel.productPrice = product.getUnitPrice().floatValue();
        carModel.priceHint = R.string.product_price;

        return carModel;
    }

    public static SkuModel ShopCarModel2SkuModel(ShopCarModel shopCar) {
        SkuModel skuModel = new SkuModel();
        skuModel.setCommisionProportion(shopCar.conversionRate);
        skuModel.setUnit(shopCar.unitMin);
        skuModel.setLargestUnit(shopCar.unitMax);
        return skuModel;
    }

    /**
     * 车销申请商品转换
     */
    public static List<ProductModel> ApplyModel2ProductModel(List<ApplyModel> applys, String shopId) {
        if (applys == null) {
            return null;
        }
        ProductModel Pro = new ProductModel();
        List<ApplyModel> objList = applys;
        List<ProductModel> products = new ArrayList<>();
        for (ApplyModel apply : objList) {
            int confirmNum = apply.getConfirmNum();
           /* if (confirmNum == 0) {
                continue;
            }*/
            ProductModel product = null;
            try {
                product = Pro.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (product == null) {
                product = new ProductModel();
            }
            product.applyId = apply.getId();
            product.setProductName(apply.getProductName());
            //库存限制在申请的数量之内
            product.setStock(confirmNum);
            //业态价格和建议零售价
            if (TextUtils.isEmpty(shopId)) {
                product.setOriginalPrice(apply.getProductPrice());
            } else {
                product.setBussinessPrice(apply.getProductPrice());
            }
            product.setProductPic(apply.getProductPic());
            product.setProductType(apply.getProductType());
            product.setStoreId(apply.getStoreId());
            product.setId(apply.getProductId());
            //sku信息
            product.setSkuResult(apply.getSkuResult());
            products.add(product);
        }
        return products;
    }

    /**
     * 礼物转换购物商品
     */
    public static List<ProductModel> GitfModel2ProductModel(List<GiftModel> gifts) {
        if (gifts == null) {
            return null;
        }
        ProductModel Pro = new ProductModel();
        List<GiftModel> objList = gifts;
        List<ProductModel> products = new ArrayList<>();
        for (GiftModel apply : objList) {
            ProductModel product = null;
            try {
                product = Pro.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (product == null) {
                product = new ProductModel();
            }
            product.applyId = apply.getId();
            product.setProductName(apply.getGiftName());
            //库存限制在申请的数量之内
            product.setStock(apply.getStock());
            product.setProductPic(apply.getImage());
            product.setBussinessPrice(new BigDecimal(0));
            //礼品商品
            product.setProductType(ProductType.PRODUCT_GIFT);
            product.setId(apply.getId());
            //sku信息
            product.setSkuResult(apply.getSkuResult());
            products.add(product);
        }
        return products;
    }

    /**
     * 订单商品转换
     */
    public static List<ProductModel> OrderDetail2ProductModel(List<OrderDetail> applys) {
        if (applys == null) {
            return new ArrayList<>();
        }
        ProductModel Pro = new ProductModel();
        List<OrderDetail> objList = applys;
        List<ProductModel> products = new ArrayList<>();
        for (OrderDetail order : objList) {
            ProductModel product = null;
            try {
                product = Pro.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (product == null) {
                product = new ProductModel();
            }
            product.setProductName(order.getProductName());
            product.setStock(order.getNum().intValue());
            //订单商品均为批发价
            product.setBussinessPrice(order.getUnitPrice());
            product.setProductPic(order.getProductPic());
            product.setId(order.getGoodId());
            //添加sku信息
            product.setSkuResult(order.getSkuResult());
            products.add(product);
        }
        return products;
    }

    /**
     * 购物车商品转申请商品
     */
    public static List<ApplyModel> ShopCarModel2ApplyModel(List<ShopCarModel> cars) {
        List<ApplyModel> applyList = new ArrayList<>();

        for (ShopCarModel car : cars) {
            ApplyModel apply = new ApplyModel();
            apply.setApplyNum(car.curNum);
            apply.setProductId(car.productId);
            apply.setUnitPrice(car.productPrice);//单价保存
            apply.setProductName(car.productName);
            apply.setProductPic(car.productPic);
            apply.setStock(car.maxNum);
//            apply.setProductType(StoreProductType.TYPE_UPDATE_PRODUCT);
            apply.setProductType(car.productType);
            apply.setSkuResult(ShopCarModel2SkuModel(car));
            //添加
            applyList.add(apply);
        }
        return applyList;
    }

    /**
     * 合并申请的商品数量
     */
    public static void mergeApplyProductModel(List<ApplyModel> applys) {
        LinkedHashMap<String, ApplyModel> products = new LinkedHashMap<>();

        for (ApplyModel newApply : applys) {
            String productId = newApply.getProductId();

            boolean containsKey = products.containsKey(productId);

            if (containsKey) {
                ApplyModel exitApply = products.get(productId);
                //相加申请的数量
                int applyNum = exitApply.getApplyNum() + newApply.getApplyNum();
                exitApply.setApplyNum(applyNum);
                //订单商品需要添加最小数量限制
                if (newApply.getProductType() == StoreProductType.TYPE_ORDER_PRODUCT) {
                    //修改已经存在的最小数量限制数据
                    exitApply.pMinNum += newApply.getApplyNum();
                }
            } else {
                //订单商品
                if (newApply.getProductType() == StoreProductType.TYPE_ORDER_PRODUCT) {
                    newApply.pMinNum = newApply.getApplyNum();
                }
                products.put(productId, newApply);
            }
        }
        applys.clear();
        applys.addAll(products.values());
    }


    /**
     * 合并订单商品和车销商品
     * 将订单商品的申请数量作为车销商品的最小数量显示
     *
     * @param orderProduct 订单商品
     * @param applyProduct 车销申请的商品
     */
    public static void mergeApplyProductModel(List<ApplyModel> orderProduct, List<ApplyModel> applyProduct) {

        LinkedHashMap<String, ApplyModel> orderProducts = new LinkedHashMap<>();
        //合并的数据集合
        List<ApplyModel> mergeApplyList = new ArrayList<>();
        //先将订单商品保存到去重数据集合中
        if (orderProduct != null) {
            for (ApplyModel orderP : orderProduct) {
                orderProducts.put(orderP.getProductId(), orderP);
            }
        }
        //判断是否存在订单商品，存在商品数量加入到新集合中，不存在则作为新增车销商品，申请数量是0显示为最小订单的商品数量
        for (ApplyModel newApply : applyProduct) {
            String productId = newApply.getProductId();
            boolean containsKey = orderProducts.containsKey(productId);
            //存在订单商品的时候，将最低数量加入每一项显示
            if (containsKey) {
                ApplyModel exitApply = orderProducts.get(productId);
                //订单商品数量
                newApply.pMinNum = exitApply.getApplyNum();
                //移除已经加入显示的商品
                orderProducts.remove(productId);
            }
            //将之前的可能新增的商品类型设置为车销商品
//            newApply.setProductType(StoreProductType.TYPE_CREATE_PRODUCT);

            mergeApplyList.add(newApply);
        }
        //添加订单商品中存在的商品，车销中不存在的商品，外加显示订单数量
        Set<Map.Entry<String, ApplyModel>> entries = orderProducts.entrySet();
        for (Map.Entry<String, ApplyModel> apply : entries) {
            ApplyModel value = apply.getValue();
            //最小显示
            value.pMinNum = value.getApplyNum();
            //申请数量为0
            value.setApplyNum(0);
            //设置为新增车销商品
            value.setProductType(StoreProductType.TYPE_UPDATE_PRODUCT);
            mergeApplyList.add(value);
        }
        applyProduct.clear();
        applyProduct.addAll(mergeApplyList);
    }

    /**
     * 组合订单商品集合，相同的商品数量相加
     *
     * @param product 商品集合数组
     * @return 返回组合后的商品集合
     */
    public static List<ProductModel> mergeProductModel(List<List<ProductModel>> product) {
        List<ProductModel> res;
        int length = product.size();
        //一个集合
        if (length == 1) {
            res = new ArrayList<>();
            res.addAll(product.get(0));
        } else {
            LinkedHashMap<String, ProductModel> products = new LinkedHashMap<>();
            for (List<ProductModel> listPro : product) {
                for (ProductModel pro : listPro) {
                    String id = pro.getId();
                    boolean containsKey = products.containsKey(id);
                    if (containsKey) {
                        ProductModel p = products.get(id);
                        int stock = p.getStock();
                        stock += pro.getStock();
                        p.setStock(stock);
                    } else {
                        products.put(id, pro);
                    }
                }
            }
            res = new ArrayList<>(products.values());
        }
        return res;
    }

    /**
     * 组合车销商品集合，相同的商品数量相加
     *
     * @param source    原集合商品
     * @param newSource 新集合商品
     */
    public static void mergeShopCarModel(List<ShopCarModel> source, List<ShopCarModel> newSource) {
        if (newSource == null) {
            return;
        }
        if (source.size() == 0) {
            source.addAll(newSource);
        } else {
            LinkedHashMap<String, ShopCarModel> products = new LinkedHashMap<>();
            for (ShopCarModel shop : source) {
                products.put(shop.productId, shop);
            }
            for (ShopCarModel newShop : newSource) {
                boolean containsKey = products.containsKey(newShop.productId);
                if (containsKey) {
                    products.get(newShop.productId).curNum += newShop.curNum;
                } else {
                    products.put(newShop.productId, newShop);
                }
            }
            source.clear();
            source.addAll(products.values());
        }
    }

    private static HashMap<String, Member> mMembers;

    private static HashMap<String, Member> getMembers() {
        if (mMembers == null) {
            synchronized (ModelHelper.class) {
                List<Member> members = DbTaskHelper.getInstance().getAllMember();
                mMembers = new HashMap<>();
                if (members != null) {
                    for (Member member : members) {
                        mMembers.put(member.getRetailerId(), member);
                    }
                }
            }
        }
        return mMembers;
    }

    /**
     * 保存一个会员到内存
     */
    public static void saveOneMember(Member member) {
        HashMap<String, Member> members = getMembers();
        if (member != null) {
            boolean containsKey = members.containsKey(member.getRetailerId());
            if (!containsKey) {
                members.put(member.getRetailerId(), member);
            }
        }

    }

    /**
     * 添加百度数据店铺状态
     */
    public static void addMember2SearchShopModel(List<SearchShopModel> shops) {
        HashMap<String, Member> members = getMembers();
        for (SearchShopModel shop : shops) {
            String shopId = shop.getShopId();
            if (members.containsKey(shopId)) {
                shop.setMember(members.get(shopId));
            } else {
                shop.setMember(new Member(shopId));
            }
        }
    }

    /**
     * 添加审核店铺数据店铺状态
     */
    public static void addMember2ShopApplyModel(List<ShopApplyModel> shops) {

        HashMap<String, Member> members = getMembers();
        for (ShopApplyModel shop : shops) {
            String shopId = shop.getRetailerId();
            if (members.containsKey(shopId)) {
                shop.setMember(members.get(shopId));
            } else {
                shop.setMember(new Member(shopId));
            }
        }
    }

}
