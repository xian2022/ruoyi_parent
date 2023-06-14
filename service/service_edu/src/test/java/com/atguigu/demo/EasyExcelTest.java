package com.atguigu.demo;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.atguigu.excel.DemoData;
import com.atguigu.listener.ExcelListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EasyExcelTest {
    //实现excel写的操作
    //1 实现写入文件夹地址和excel文件名称
    String filename = "C:\\Users\\Xian\\Desktop\\stu.xlsx";
    @Test
    public void write() {
        //2. 调用easyexcel里面的方法实现写操作
        // write第一个参数路径名称，第二个参数实体类
        EasyExcel.write(filename, DemoData.class).sheet("学生列表").doWrite(EasyExcelTest::getList);
    }
    public static List<DemoData> getList() {
        List<DemoData> demoDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            DemoData demoData = new DemoData(RandomUtil.randomInt(10000, 99999), RandomUtil.randomString(5));
            demoDataList.add(demoData);
        }
        return demoDataList;
    }
    @Test
    public void read() {
        //EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
        EasyExcel.read(filename,DemoData.class,new PageReadListener<DemoData>(dataList -> {
            for (DemoData data : dataList) {
                System.out.println(data);
            }
        })).sheet().doRead();
    }
}
