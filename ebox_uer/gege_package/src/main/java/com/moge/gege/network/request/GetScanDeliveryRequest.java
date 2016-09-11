package com.moge.gege.network.request;

import com.moge.gege.config.NetworkConfig;
import com.moge.gege.model.ScanEboxDataModel;
import com.moge.gege.model.ScanEboxModel;
import com.moge.gege.network.util.BaseRequest;
import com.moge.gege.network.util.ResponseEventHandler;

/**
 * 获取扫码开柜订单信息
 */
public class GetScanDeliveryRequest extends BaseRequest<ScanEboxModel> {
    public GetScanDeliveryRequest(String qrcode_key,
                                  ResponseEventHandler<ScanEboxModel> listener) {
        super(Method.GET, getRequestUrl(qrcode_key), "",
                ScanEboxModel.class, listener);
    }

    private static String getRequestUrl(String qrcode_key) {
        return String.format("%s/v1/user/delivery/terminal/box/open?qrcode_key=%s", NetworkConfig.generalAddress, qrcode_key);
    }
}
