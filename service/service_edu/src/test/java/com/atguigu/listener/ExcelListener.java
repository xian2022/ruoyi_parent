package com.atguigu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.atguigu.excel.DemoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExcelListener implements ReadListener<DemoData> {
    List<DemoData> list = new ArrayList<>();

    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println(demoData);
        list.add(demoData);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
