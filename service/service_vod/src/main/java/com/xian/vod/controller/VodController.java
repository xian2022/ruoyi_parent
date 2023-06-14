package com.xian.vod.controller;

import com.xian.entities.R;
import com.xian.vod.service.VodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/eduvod/video")
public class VodController {
    @Autowired
    private VodService vodService;

    @PostMapping("/upload")
    public R uploadVideo(MultipartFile file) {
        return vodService.uploadVideo(file);
        //return R.fail();
    }
    @GetMapping("/delete/{videoSourceId}")
    public R deleteVideo(@PathVariable("videoSourceId") String videoSourceId) {
        return vodService.deleteVideo(videoSourceId);
    }
    @PostMapping("/delete")
    public R deleteVideo(@RequestBody List<String> videoSourceId) {
        return vodService.deleteVideo(videoSourceId);
    }
    @GetMapping("/test")
    public R testController() {
        return R.ok().message("ok!");
    }

    @GetMapping("getPlayAuth/{videoId}")
    public R getVideoPlayAuth(@PathVariable("videoId") String videoId) throws Exception {
        return vodService.getVideoPlayAuth(videoId);
    }
}
