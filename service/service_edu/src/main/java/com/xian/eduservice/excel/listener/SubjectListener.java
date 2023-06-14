package com.xian.eduservice.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.entities.Subject;
import com.xian.eduservice.excel.model.SubjectExcelModel;
import com.xian.eduservice.service.SubjectService;
import com.xian.servicebase.handler.RuoyiException;


public class SubjectListener implements ReadListener<SubjectExcelModel> {
    private SubjectService service;

    public SubjectListener(SubjectService service) {
        this.service = service;
    }

    @Override
    public void invoke(SubjectExcelModel subjectExcelModel, AnalysisContext analysisContext) {
        if(subjectExcelModel == null) {
            throw new RuoyiException(20001,"文件数据为空");
        }
        //一行一行读取，每次读取有两个值，第一个值一级分类，第二个值二级分类
        //判断一级分类是否重复
        Subject existOneSubject = this.existOneSubject(subjectExcelModel.getOneSubjectName());
        if(existOneSubject == null) { //没有相同一级分类，进行添加
            existOneSubject = new Subject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectExcelModel.getOneSubjectName());//一级分类名称
            service.save(existOneSubject);
        }
        //获取一级分类id值
        String pid = existOneSubject.getId();
        //添加二级分类
        //判断二级分类是否重复
        Subject existTwoSubject = this.existTwoSubject(subjectExcelModel.getTwoSubjectName(), pid);
        if(existTwoSubject == null) {
            existTwoSubject = new Subject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectExcelModel.getTwoSubjectName());//二级分类名称
            service.save(existTwoSubject);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {}

    //判断一级分类不能重复添加
    private Subject existOneSubject(String name) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id","0");
        Subject oneSubject = service.getOne(wrapper);
        return oneSubject;
    }

    //判断二级分类不能重复添加
    private Subject existTwoSubject(String name,String pid) {
        QueryWrapper<Subject> wrapper = new QueryWrapper<>();
        wrapper.eq("title",name);
        wrapper.eq("parent_id",pid);
        Subject twoSubject = service.getOne(wrapper);
        return twoSubject;
    }

}
