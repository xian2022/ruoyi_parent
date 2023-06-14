package com.xian.eduservice.fegin;

import com.xian.eduservice.fegin.fallback.VodFallBack;
import com.xian.entities.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Component
@FeignClient(value = "service-vod", fallback = VodFallBack.class)
public interface VodFegin {
    @GetMapping("/eduvod/video/test")
    R testController();

    @PostMapping("/eduvod/video/delete")
    R delAliyunVideo(@RequestBody List<String> videoSourceId);

    @GetMapping("/eduvod/video/getPlayAuth/{videoId}")
    R getVideoPlayAuth(@PathVariable("videoId") String videoId);
}