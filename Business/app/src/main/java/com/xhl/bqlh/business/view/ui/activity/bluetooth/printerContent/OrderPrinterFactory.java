package com.xhl.bqlh.business.view.ui.activity.bluetooth.printerContent;

import com.xhl.bqlh.business.AppDelegate;
import com.xhl.bqlh.business.Model.OrderDetail;
import com.xhl.bqlh.business.Model.OrderModel;
import com.xhl.bqlh.business.Model.Type.OrderType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/6/22.
 */
public class OrderPrinterFactory implements PrinterFactory {

    private OrderModel orderInfo;

    public OrderPrinterFactory(OrderModel orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Override
    public Printer create() {
        return new PrinterOrder(orderInfo);
    }

    private static class PrinterOrder extends BasePrinter {

        private OrderModel orderModel;

        public PrinterOrder(OrderModel orderModel) {
            this.orderModel = orderModel;
        }

        @Override
        public String print() {

            List<String> khl = orderPrinter(orderModel, "客户联");

            List<String> hdl = orderPrinter(orderModel, "回单联");

            khl.addAll(hdl);

            //info
            StringBuilder builder = new StringBuilder();
            for (String line : khl) {
                builder.append(line).append("\n");
            }

            return builder.toString();
        }

        private String getOrderType(OrderModel order) {
            //订单类型
            String type = order.getOrderType();
            int orderType = Integer.parseInt(type);
            if (orderType == OrderType.order_type_shop) {
                return "门店订单";
            }
            if (orderType == OrderType.order_type_self) {
                return "拜访订单";
            }
            if (orderType == OrderType.order_type_car) {
                return "车销订单";
            }
            return "";
        }

        public List<String> orderPrinter(OrderModel orderModel, String tag) {
            String name = AppDelegate.appContext.getUserName();

            List<String> printLine = new ArrayList<>();
            //head
            printLine.add(orderModel.getCompanyName());
            String head = getOrderType(orderModel) + "-" + tag;
            printLine.add(centerLineText(head));
            //line
            printLine.add(DIVIDER_LINE);

            printLine.add("门店: " + orderModel.getReceivingName());
            printLine.add("单号: " + orderModel.getStoreOrderCode());
            printLine.add("时间: " + orderModel.getOrderTime());
            //line
            printLine.add(DIVIDER_LINE);

            //商品信息
            List<OrderDetail> orderDetailList = orderModel.getOrderDetailList();
            List<String> productInfo = getProductInfo(orderDetailList);
            printLine.addAll(productInfo);
            //价格
            BigDecimal couponsMoney = orderModel.getCouponsMoney();
            if (couponsMoney == null) {
                couponsMoney = new BigDecimal("0.00");
            }
            printLine.add(averageLineText(TxtGravity.START, "订单金额:" + orderModel.getRealOrderMoney().toString(), "优惠金额:" + couponsMoney.toString()));
            //line
            printLine.add(DIVIDER_LINE);
            //签字
            printLine.add(averageLineText(TxtGravity.START, "门店签收:", "业务员:" + name));
            //line
            printLine.add(DIVIDER_LINE);

            printLine.add(RETURN);

            return printLine;
        }

        private List<String> getProductInfo(List<OrderDetail> details) {
            List<String> product = new ArrayList<>();
            //hint
            product.add(averageLineText(TxtGravity.START, "商品", "数量", "单价", "金额"));
            //line
            product.add(DIVIDER_LINE);

            for (OrderDetail order : details) {
                product.add(order.getSkuCode());
                product.add(order.getProductName());
                String info = averageLineText(TxtGravity.START, " ", " x" + order.getNum().toString(), order.getUnitPrice().toString(), order.getTotalPrice().toString());
                product.add(info);
            }
            //line
            product.add(DIVIDER_LINE);

            return product;
        }

    }
}
