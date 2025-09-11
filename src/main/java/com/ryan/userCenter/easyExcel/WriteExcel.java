package com.ryan.userCenter.easyExcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class WriteExcel {

    public static void main(String[] args) {
        // 数据库连接参数
        String url = "jdbc:mysql://localhost:3306/friend-sys";
        String user = "username";
        String password = "password";
        String fileName = "C:\\Users\\86180\\Desktop\\zr\\friend-sys\\src\\main\\resources\\static\\test.xlsx";
        EasyExcel.read(fileName, TableUserInfo.class, new PageReadListener<TableUserInfo>(dataList -> {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String sql = "INSERT INTO user (userName, planetCode) VALUES (?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                for (TableUserInfo demoData : dataList) {
                    log.info("读取到一条数据{}", JSON.toJSONString(demoData));
                    //todo写入数据库
                    ps.setString(1, demoData.getUserName());
                    ps.setString(2, demoData.getPlanetCode());
                    ps.addBatch();
                }
                ps.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        })).sheet().doRead();
    }
}
