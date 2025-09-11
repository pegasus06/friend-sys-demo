package com.ryan.userCenter.easyExcel;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.Data;

@Data
public class TableListener implements ReadListener<TableUserInfo> {
    @Override
    public void invoke(TableUserInfo tableUserInfo, AnalysisContext analysisContext) {
        System.out.println(tableUserInfo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("调用结束");
    }
}
