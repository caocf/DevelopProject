package com.xhl.world.mvp.domain;

import com.xhl.world.api.ApiControl;
import com.xhl.world.model.Base.Response;
import com.xhl.world.model.Base.ResponseModel;
import com.xhl.world.model.Entities.ShoppingOrderEntities;
import com.xhl.world.model.ShoppingCarProductModel;
import com.xhl.world.model.ShoppingItemChildDetailsModel;
import com.xhl.world.model.ShoppingItemDetailsModel;
import com.xhl.world.model.Type.ShoppingGoodsType;
import com.xhl.world.model.serviceOrder.Product;
import com.xhl.world.mvp.domain.back.ErrorEvent;
import com.xhl.world.mvp.presenters.Presenter;
import com.xhl.world.mvp.presenters.ShoppingPresenter;
import com.xhl.xhl_library.utils.NumberUtil;
import com.xhl.xhl_library.utils.log.Logger;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sum on 15/12/15.
 */
public class ShoppingUserCaseController implements ShoppingUserCase, Callback.CommonCallback<ResponseModel<Response<ShoppingCarProductModel>>> {

    private List<ShoppingCarProductModel> mServiceOrders;//服务的未排序订单

    private List<ShoppingItemDetailsModel> mOrders;//本地整合订单分类

    private ShoppingPresenter mPresenter;

    private ShoppingOrderEntities mEntities = new ShoppingOrderEntities();//clone对象

    private HashMap<Integer, ArrayList<ShoppingOrderEntities>> mCarGoods;

    private int mTotalCount;
    private boolean mExistNotSelectOrder = true;

    private boolean mIsQuerying = false;//查询购物车
    private boolean mIsModifyNum = false;//修改数量
    private int mMaxPage = -1;
    private int mCurPage = 0;
    private int mCurPageSize = 100;//尽量一次查出购物车全部数据

    public ShoppingUserCaseController() {
        mCarGoods = new HashMap<>();
    }

    //重组订单顺序，将数据相同的商店id分配到一块
    private void resetOrders() {
        if (mServiceOrders == null) {
            return;
        }
        if (mOrders == null) {
            //创建本地订单
            mOrders = new ArrayList<>();
        }
        if (mCurPage == 0) {//首次加载时需要清理数据
            mOrders.clear();
        }
        //分类订单
        HashMap<String, List<ShoppingCarProductModel>> serviceData = new HashMap();
        for (ShoppingCarProductModel carOrder : mServiceOrders) {
            String shopId = carOrder.getShopId();
            //根据shopId分类
            boolean containsKey = serviceData.containsKey(shopId);
            if (containsKey) {
                List<ShoppingCarProductModel> models = serviceData.get(shopId);
                models.add(carOrder);
            } else {
                //不存在创建新的集合存放
                List<ShoppingCarProductModel> list = new ArrayList<>();
                list.add(carOrder);
                serviceData.put(shopId, list);
            }
        }

        //组合成新订单
        Set<Map.Entry<String, List<ShoppingCarProductModel>>> entries = serviceData.entrySet();

        for (Map.Entry<String, List<ShoppingCarProductModel>> data : entries) {

            List<ShoppingCarProductModel> dataValue = data.getValue();
            //商家的店铺
            ShoppingItemDetailsModel shopData = createShopData(dataValue.get(0));
            if (shopData != null) {
                //创建店铺中的商品
                List<ShoppingItemChildDetailsModel> childOrders = new ArrayList<>();
                for (ShoppingCarProductModel shopItem : dataValue) {
                    childOrders.add(createChildData(shopItem));
                }
                shopData.setShop_details(childOrders);
            }
            mOrders.add(shopData);
        }
    }

    //生成一个商家
    private ShoppingItemDetailsModel createShopData(ShoppingCarProductModel carData) {
        if (carData == null) {
            return null;
        }
        ShoppingItemDetailsModel shop = new ShoppingItemDetailsModel();
        shop.setShop_details(new ArrayList<ShoppingItemChildDetailsModel>());
        shop.setShop_title(carData.getShopName());//店铺名称
        shop.setShop_icon(carData.getShopLogo());//店铺logo
        shop.setShop_id(carData.getShopId());//店铺id
        shop.setShop_url(carData.getShopUrl());//店铺地址
        return shop;
    }

    //生成一个商家的一个商品
    private ShoppingItemChildDetailsModel createChildData(ShoppingCarProductModel carData) {
        if (carData == null) {
            return null;
        }

        Product product = carData.getProduct();

        ShoppingItemChildDetailsModel child = new ShoppingItemChildDetailsModel();

        child.setSeller_id(carData.getSellerId());//卖家id

        Integer goodType = carData.getProduct().getProductType();

        child.setGoods_typ(String.valueOf(goodType));//商品类型

        child.setGoods_count(carData.getPurchaseQuantity());//商品数量

        child.setGoods_title(product.getProductName());

        child.setGoods_id(carData.getProductId());//商品id

        child.setCar_id(carData.getId());//订单生成的唯一id

        child.setGoods_in_car(ShoppingGoodsType.STATE_UN_IN_CAR);
//        //活动价不为空，显示为当前价格
//        if (product.getActivityPrice() != null) {
//            child.setGoods_price(carData.getActivityPrice());
//            child.setGoods_old_price(carData.getOriginalPrice());
//        } else {
//            child.setGoods_price(carData.getOriginalPrice());
//        }

        child.setGoods_price(carData.getProductPrice());

        child.setGoods_icon(product.getProductPicUrl());

        return child;
    }


    //重置订单数据结构
    private void resetCarGoods() {
        if (mOrders == null) {
            return;
        }
        if (mCarGoods == null) {
            mCarGoods = new HashMap<>();
        } else {
            mCarGoods.clear();
        }
        for (int i = 0; i < mOrders.size(); i++) {
            ShoppingItemDetailsModel model = mOrders.get(i);
            mCarGoods.put(i, getOrders(model.getShop_details()));
        }
    }

    private ArrayList<ShoppingOrderEntities> getOrders(List<ShoppingItemChildDetailsModel> childData) {
        ArrayList<ShoppingOrderEntities> list = new ArrayList<>();
        for (ShoppingItemChildDetailsModel child : childData) {
            ShoppingOrderEntities entities = null;
            try {
                entities = mEntities.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (entities == null) {
                entities = new ShoppingOrderEntities();
            }
            entities.setSeller_id(child.getSeller_id());
            //商品数量
            entities.setCount(child.getGoods_count());
            //购物车中的一项唯一id
            entities.setCar_id(child.getCar_id());
            //商品id
            entities.setGoods_id(child.getGoods_id());
            //商品类型
            entities.setGoods_type(child.getGoods_typ());
            //商品价格
            entities.setGoods_price(Float.valueOf(child.getGoods_price()));
            //商品状态
            entities.setIn_car(child.goodsInCar());
            //商品图片
            entities.setGoods_url(child.getGoods_icon());
            //商品名称
            entities.setGoods_title(child.getGoods_title());
            list.add(entities);
        }
        return list;
    }

    @Override
    public void execute() {
        if (mIsQuerying) {
            return;
        }
        mIsQuerying = true;

        ApiControl.getApi().shoppingCar(mCurPage, mCurPageSize, this);

    }

    private void carDo() {
        //重组订单
        resetOrders();
        //重置结算订单数据
        resetCarGoods();
        //通知代理处理数据
        mPresenter.onSuccess(mOrders);
        //更新费用
        updateTotalFree(-1);
    }

    //修改一个商品到数量
    private void modifyGoodsNum(Integer position, ShoppingOrderEntities obj) {
        boolean exist = mCarGoods.containsKey(position);
        if (exist) {
            boolean childExist = false;
            ArrayList<ShoppingOrderEntities> list = mCarGoods.get(position);
            for (ShoppingOrderEntities entity : list) {
                if (entity.getCar_id().equals(obj.getCar_id())) {
                    childExist = true;
                    //修改商品数目
                    entity.setCount(obj.getCount());
                    //修改商品选中状态
                    entity.setIn_car(obj.isIn_car());
                    break;
                }
            }
            if (!childExist) {
                list.add(obj);
            }
        } else {
            LogUtil.e("shopping goods not in car [sku:" + obj.getGoods_id() + "]");
        }
    }

    private void updateTotalFree(int position) {
        //更新总价费用
        String total = countTotalFree();
        mPresenter.updateTotalFree(total, mTotalCount);

        //更新小计费用
        String itemFree = countItemFree(position);
        mPresenter.updateItemFree(position, itemFree);
        //更新全选状态
        updateGeneralBoxState();
    }

    private String countTotalFree() {
        if (mCarGoods.size() == 0) {
            return null;
        }
        float totalMoney = 0L;
        int totalCount = 0;
        boolean isExistNotInCar = false;
        Set<Map.Entry<Integer, ArrayList<ShoppingOrderEntities>>> set = mCarGoods.entrySet();
        for (Map.Entry<Integer, ArrayList<ShoppingOrderEntities>> entry : set) {
            ArrayList<ShoppingOrderEntities> value = entry.getValue();
            for (ShoppingOrderEntities shop : value) {
                if (shop.isIn_car()) {
                    totalCount += shop.getCount();
                    totalMoney = totalMoney + (shop.getCount() * shop.getGoods_price());
                } else {
                    isExistNotInCar = true;
                }
            }
        }
        if (isExistNotInCar) {
            mExistNotSelectOrder = true;
        } else {
            mExistNotSelectOrder = false;
        }
        mTotalCount = totalCount;
        return NumberUtil.getDouble(totalMoney);
    }

    //通知更新父容器Item的选中状态
    private void checkParentItemState(int position) {
        ArrayList<ShoppingOrderEntities> list = mCarGoods.get(position);
        boolean isExistUnInCar = false;
        for (ShoppingOrderEntities shop : list) {
            if (!shop.isIn_car()) {
                isExistUnInCar = true;
                break;
            }
        }
        mPresenter.checkItemBoxState(position, !isExistUnInCar);
    }

    //更新全选的选中状态
    private void updateGeneralBoxState() {

        mPresenter.updateTotalBoxState(!mExistNotSelectOrder);
    }

    //计算一个商铺中选中的费用
    private String countItemFree(int position) {
        if (mCarGoods.size() == 0 || position == -1) {
            return null;
        }
        float totalMoney = 0L;
        ArrayList<ShoppingOrderEntities> list = mCarGoods.get(position);

        for (ShoppingOrderEntities shop : list) {
            if (shop.isIn_car()) {
                totalMoney = totalMoney + (shop.getCount() * shop.getGoods_price());
            }
        }
        return NumberUtil.getDouble(totalMoney);
    }


    @Override
    public void selectAllChildGoods(int position, Object obj) {
        if (obj instanceof ShoppingItemDetailsModel) {
            ShoppingItemDetailsModel details = (ShoppingItemDetailsModel) obj;

            List<ShoppingItemChildDetailsModel> shop_details = details.getShop_details();
            for (ShoppingItemChildDetailsModel child : shop_details) {
                modifyNum(position, child);
            }
        }
        //更新费用
        updateTotalFree(position);
    }

    @Override
    public void unSelectAllChildGoods(int position, Object obj) {
        if (obj instanceof ShoppingItemDetailsModel) {
            ShoppingItemDetailsModel details = (ShoppingItemDetailsModel) obj;

            List<ShoppingItemChildDetailsModel> shop_details = details.getShop_details();
            for (ShoppingItemChildDetailsModel child : shop_details) {
                modifyNum(position, child);
            }
        }
        updateTotalFree(position);
    }

    @Override
    public void selectChildGoods(int position, Object obj) {
        modifyNum(position, obj);
        updateTotalFree(position);
        //检测父类选中状态
        checkParentItemState(position);
    }

    @Override
    public void unSelectChildGoods(int position, Object obj) {
        modifyNum(position, obj);
        updateTotalFree(position);
        //检测父类选中状态
        checkParentItemState(position);
    }

    @Override
    public void addGoods(int position, Object obj) {
        modifyOneGoodNum(position, obj, "+1");
    }

    @Override
    public void reduceGoods(int position, Object obj) {
        modifyOneGoodNum(position, obj, "-1");
    }

    private void modifyOneGoodNum(final int position, final Object obj, String range) {
        ShoppingItemChildDetailsModel shop = (ShoppingItemChildDetailsModel) obj;
        if (mIsModifyNum) {
            return;
        }
        mIsModifyNum = true;
        final String carId = shop.getCar_id();
        String productId = shop.getGoods_id();
        int curNum = shop.getGoods_count();
        //修改服务器上订单数量
        ApiControl.getApi().updateShoppingCartNum(carId, productId, String.valueOf(curNum), range, new CommonCallback<ResponseModel>() {
            @Override
            public void onSuccess(ResponseModel result) {
                if (result.isSuccess()) {
                    modifyNum(position, obj);
                    updateTotalFree(position);
                    //检测父类选中状态
                    checkParentItemState(position);
                } else {
                    mPresenter.onFailed(ErrorEvent.OTHER_ERROR, result.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                errorDo(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                mIsModifyNum = false;
            }
        });
    }

    //修改某个商品的数量
    private void modifyNum(int position, Object obj) {
        if (obj instanceof ShoppingItemChildDetailsModel) {
            ShoppingItemChildDetailsModel details = (ShoppingItemChildDetailsModel) obj;
            ShoppingOrderEntities entities = null;
            try {
                entities = mEntities.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            if (entities == null) {
                entities = new ShoppingOrderEntities();
            }
            //商品数量
            entities.setCount(details.getGoods_count());
            //购物车中的一项唯一标示
            entities.setCar_id(details.getCar_id());
            //是否选购中
            entities.setIn_car(details.goodsInCar());
            modifyGoodsNum(position, entities);
        }
    }

    public void setPresenter(Presenter mPresenter) {
        this.mPresenter = (ShoppingPresenter) mPresenter;
    }

    @Override
    public void deleteSelectGoods() {
        ArrayList<ShoppingOrderEntities> orders = getAccountOrders();
        if (orders.size() > 0) {
            String[] array = new String[orders.size() + 1];
            for (int i = 0; i < orders.size(); i++) {
                array[i] = orders.get(i).getCar_id();
            }

            ApiControl.getApi().deleteShoppingCart(array, new CommonCallback<ResponseModel<Boolean>>() {
                @Override
                public void onSuccess(ResponseModel<Boolean> result) {
                    if (result.isSuccess()) {
                        if (result.getResultObj()) {
                            mPresenter.messageHint("删除商品成功");
                            refreshTop();
                        } else {
                            mPresenter.messageHint("删除商品错误");
                        }
                    } else {
                        mPresenter.onFailed(ErrorEvent.OTHER_ERROR, result.getMessage());
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });

        }
    }

    @Override
    public void moveToCollection() {
        ArrayList<ShoppingOrderEntities> orders = getAccountOrders();
        if (orders.size() > 0) {
            for (ShoppingOrderEntities shop : orders) {
                moveGoodToCollection(shop);
            }
        }
    }

    //添加商品收藏
    private void moveGoodToCollection(ShoppingOrderEntities item) {
        String productId = item.getGoods_id();

        ApiControl.getApi().collectionAdd(productId, "1", new CommonCallback<ResponseModel>() {
            @Override
            public void onSuccess(ResponseModel result) {
                if (result.isSuccess()) {
                    mPresenter.messageHint("收藏成功~");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    @Override
    public void refreshTop() {
        mCurPage = 0;
        execute();
    }

    @Override
    public void refreshBottom() {
        if (!mIsQuerying) {
            if (mMaxPage < mCurPage) {
                mPresenter.messageHint(ErrorEvent.NO_MORE_ERROR);
            } else {
                mCurPage++;
                execute();
            }
        }
    }

    @Override
    public ArrayList<ShoppingOrderEntities> getAccountOrders() {

        ArrayList<ShoppingOrderEntities> list = new ArrayList<>();

        Set<Map.Entry<Integer, ArrayList<ShoppingOrderEntities>>> entrySet = mCarGoods.entrySet();

        for (Map.Entry<Integer, ArrayList<ShoppingOrderEntities>> entry : entrySet) {

            ArrayList<ShoppingOrderEntities> value = entry.getValue();

            for (ShoppingOrderEntities order : value) {
                if (order.isIn_car()) {
                    list.add(order);
                }
            }
        }

        return list;
    }

    @Override
    public void onSuccess(ResponseModel<Response<ShoppingCarProductModel>> result) {
        if (result.isSuccess()) {
            mMaxPage = result.getResultObj().getTotal();
            mServiceOrders = result.getResultObj().getRows();
            carDo();
        } else {
            mPresenter.onFailed(ErrorEvent.OTHER_ERROR, result.getMessage());
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        errorDo(ex);
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {
        mIsQuerying = false;
    }

    private void errorDo(Throwable ex) {
        Logger.e("errorMsg:" + ex.getMessage());
        {
            mPresenter.onFailed(ErrorEvent.NETWORK_ERROR, ex.getMessage());
        }
    }
}
