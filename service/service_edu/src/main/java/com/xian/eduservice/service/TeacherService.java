package com.xian.eduservice.service;

import com.xian.eduservice.entities.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.eduservice.entities.vo.TeacherInfo;
import com.xian.entities.R;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author xian
 * @since 2022/04/19 20:57
 */
public interface TeacherService extends IService<Teacher> {
    R getTeacherById(String id);

    R getTeacherInfoByCourseId(String courseId);
}
