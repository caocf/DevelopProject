package com.ebox.pub.service.task.report;

import com.ebox.pub.service.task.report.I.IReport;
import com.ebox.pub.service.task.report.I.ReportType;
import com.ebox.pub.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by Android on 2015/9/1.
 */
public class AutoReportManager {

    private static AutoReportManager manager;

    private ArrayList<IReport> reports;

    private AutoReportManager() {
        reports = new ArrayList<IReport>();
    }


    public synchronized static AutoReportManager instance() {
        if (manager == null) {
            manager = new AutoReportManager();
        }
        return manager;
    }


    public void executeReport(int reportType) {

        boolean isExist = false;
        IReport rep = null;

        if (reports == null)
        {
            reports = new ArrayList<IReport>();
        }
        for (IReport report : reports)
        {
            if (report.reportType() == reportType)
            {
                isExist = true;
                rep = report;
                break;
            }
        }

        if (isExist)
        {
            execute(rep);
        } else
        {
            createTask(reportType);
        }
    }

    /**
     * 上报存件订单
     */
    public void reportConfirmDelivery(){
        executeReport(ReportType.Type_ConfirmDelivery);
    }

    /**
     * 上报取件订单
     */
    public void reportPickupOrder(){
        executeReport(ReportType.Type_PickupItem);
    }


    private void execute(IReport report) {

        synchronized (report)
        {
            report.create();

            report.report();
        }
    }

    private void createTask(int reportType) {

        IReport report = null;

        switch (reportType) {
            case ReportType.Type_TerminalStatusReport:
                report = new TerminalBoxReport();
                break;

            case ReportType.Type_PickupItem:
                report = new PickupItemReport();
                break;

            case ReportType.Type_ConfirmDelivery:
                report = new ConfirmDeliveryReport();
                break;

            case ReportType.Type_AlarmDelivery:
                report = new AlarmDeliveryReport();
                break;

            case ReportType.Type_Alarm:
                report = new AlarmReport();
                break;

            case ReportType.Type_Trading:
                report = new TradingOrderReport();
                break;

            case ReportType.Type_TimeoutOrder:
                report = new PullTimeoutTask();
                break;

            case ReportType.Type_UpdateInfo:
                report = new PullUpdateInfoTask();
                break;
        }

        if (report != null)
        {
            reports.add(report);
            LogUtil.i("reportTask","create report task: "+reportType);
            execute(report);
        } else {
            LogUtil.e("task not exist");
        }
    }


}
