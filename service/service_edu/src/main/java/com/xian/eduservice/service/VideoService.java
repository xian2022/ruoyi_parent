package com.xian.eduservice.service;

import com.xian.eduservice.entities.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xian.entities.R;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:52
 */
public interface VideoService extends IService<Video> {

    R getVideoList(String videoId, Boolean isBuy);

    R getCourse(String videoId);

    R getTeacher(String videoId);

    String getResourceId(String videoId);
}
