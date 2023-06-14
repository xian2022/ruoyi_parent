package com.xian.eduservice.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.eduservice.entities.Subject;
import com.xian.eduservice.entities.Teacher;
import com.xian.eduservice.entities.subject.OneSubject;
import com.xian.eduservice.entities.vo.TeacherQuery;
import com.xian.eduservice.service.SubjectService;
import com.xian.eduservice.utils.PageUtil;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/05/14 09:52
 */
@CrossOrigin
@RestController
@RequestMapping("/edu/subject")
public class EduSubjectController {
    @Autowired
    private SubjectService subjectService;
    //添加课程分类
    //获取上传过来的文件，把文件内容读取出来
    @PostMapping("/add")
    public R upload(MultipartFile file) {
        return subjectService.saveSubjectByFile(file)?R.ok():R.fail();
    }
    @GetMapping("/search/{current}/{count}")
    public R getList(@PathVariable(value = "current")Integer current, @PathVariable(value = "count",required = false)Integer count) {
        return pageList(current,count,null);
    }

    public R pageList(Integer current, Integer count, QueryWrapper<Subject> wrapper) {
        Page<Subject> page = new Page<>(current,count);
        subjectService.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("items",page.getRecords());
    }

    @GetMapping("/getAll")
    public R getAllSubject() {
        return R.ok().data("menu",subjectService.getAllSubject());
    }

    @DeleteMapping("/del/{subjectId}")
    public R delSubjectById(@PathVariable("subjectId")String subjectId) {
        return subjectService.delSubjectById(subjectId);
    }
}
