package com.ebox.mgt.ui.fragment.pollingfg.model;

import com.ebox.ex.network.model.base.BaseReq;

/**
 * Created by prin on 2015/9/30.
 */

public class ReqBoard extends BaseReq {
    private BoardModel board;
    private ProductModel product;

    public BoardModel getBoard() {
        return board;
    }

    public void setBoard(BoardModel board) {
        this.board = board;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }
}
