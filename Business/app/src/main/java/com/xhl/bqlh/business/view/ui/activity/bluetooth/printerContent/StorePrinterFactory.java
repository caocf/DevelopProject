package com.xhl.bqlh.business.view.ui.activity.bluetooth.printerContent;

import com.xhl.bqlh.business.Model.ApplyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sum on 16/6/25.
 */
public class StorePrinterFactory implements PrinterFactory {

    List<ApplyModel> apply;

    public StorePrinterFactory(List<ApplyModel> apply) {
        this.apply = apply;
    }

    @Override
    public Printer create() {
        return new PrinterStore(apply);
    }

    private static class PrinterStore extends BasePrinter {
        List<ApplyModel> apply;

        public PrinterStore(List<ApplyModel> apply) {
            this.apply = apply;
        }

        @Override
        public String print() {
            List<String> printItem = getPrintItem();
            //info
            StringBuilder builder = new StringBuilder();
            for (String line : printItem) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        }

        private List<String> getPrintItem() {
            List<String> list = new ArrayList<>();


            return list;
        }
    }
}
