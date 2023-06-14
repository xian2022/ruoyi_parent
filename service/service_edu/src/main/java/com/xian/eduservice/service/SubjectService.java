package com.xian.eduservice.service;

import com.xian.eduservice.entities.Subject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.eduservice.entities.subject.OneSubject;
import com.xian.eduservice.entities.subject.TwoSubject;
import com.xian.entities.R;
import org.apache.ibatis.annotations.One;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author xian
 * @since 2022/05/14 09:52
 */
public interface SubjectService extends IService<Subject> {

    boolean saveSubjectByFile(MultipartFile file);

    List<OneSubject> getAllSubject();

    R delSubjectById(String subjectId);
    List<TwoSubject> getSubjectCourseByCount(Integer count);
}
