package com.ryan.userCenter.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson2.JSON;

@Slf4j
public class ImportExcel {
    public static void main(String[] args) {
        String fileName = "C:\\Users\\86180\\Desktop\\zr\\friend-sys\\src\\main\\resources\\static\\test.xlsx";
        EasyExcel.read(fileName, TableUserInfo.class, new PageReadListener<TableUserInfo>(dataList -> {
            for (TableUserInfo demoData : dataList) {
                log.info("读取到一条数据{}", JSON.toJSONString(demoData));
            }
        })).sheet().doRead();
    }
}
