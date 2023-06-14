package com.xian.vod.service;

import com.aliyuncs.exceptions.ClientException;
import com.xian.entities.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VodService {
    R uploadVideo(MultipartFile video);

    R deleteVideo(List<String> videoSourceId);

    R deleteVideo(String videoSourceId);

    R getVideoPlayAuth(String videoId) throws ClientException;
}
