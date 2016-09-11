package com.ebox.pub.service.task.report.I;

/**
 * Created by Android on 2015/9/1.
 */
public interface IReport extends ReportType{

    void create();

    void report();

    int reportType();
}
