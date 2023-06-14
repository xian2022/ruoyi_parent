package com.xian.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.Subject;
import com.xian.eduservice.entities.subject.OneSubject;
import com.xian.eduservice.entities.subject.TwoSubject;
import com.xian.eduservice.excel.listener.SubjectListener;
import com.xian.eduservice.excel.model.SubjectExcelModel;
import com.xian.eduservice.mapper.SubjectMapper;
import com.xian.eduservice.service.CourseService;
import com.xian.eduservice.service.SubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/05/14 09:52
 */
@Service
public class SubjectServiceImpl extends ServiceImpl<SubjectMapper, Subject> implements SubjectService {

    @Autowired
    private CourseService courseService;

    @Transactional
    @Override
    public boolean saveSubjectByFile(MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            EasyExcel.read(is, SubjectExcelModel.class,new SubjectListener(this)).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<OneSubject> getAllSubject() {
        List<OneSubject> menu = new ArrayList<>();
        List<Subject> subjects = this.list(new QueryWrapper<Subject>().eq("parent_id",0));
        subjects.forEach(one -> {
            OneSubject oneSubject = new OneSubject();
            oneSubject.setId(one.getId());
            oneSubject.setTitle(one.getTitle());
            List<Subject> children = this.list(new QueryWrapper<Subject>().eq("parent_id", one.getId()));
            oneSubject.setChildren(children);
            menu.add(oneSubject);
        });
        return menu;
    }

    @Override
    public List<TwoSubject> getSubjectCourseByCount(Integer count) {
        List<TwoSubject> menu = new ArrayList<>();
        List<Subject> subjects = this.list(new QueryWrapper<Subject>().eq("parent_id",0).last("limit "+count));
        subjects.forEach(one -> {
            TwoSubject twoSubject = new TwoSubject();
            twoSubject.setId(one.getId());
            twoSubject.setTitle(one.getTitle());
            List<Course> courseList = courseService.list(new QueryWrapper<Course>().eq("subject_parent_id", one.getId()).last("limit 8"));
            twoSubject.setCourseList(courseList);
            menu.add(twoSubject);
        });
        return menu;
    }

    @Override
    public R delSubjectById(String subjectId) {
        List<Subject> children = list(new QueryWrapper<Subject>().eq("parent_id", subjectId));
        if (children.size()>0) {
            removeBatchByIds(children);
        }
        return R.returnR(removeById(subjectId));
    }
}
