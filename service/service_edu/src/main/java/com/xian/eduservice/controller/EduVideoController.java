package com.xian.eduservice.controller;


import com.xian.eduservice.annotations.VideoPlay;
import com.xian.eduservice.annotations.VideoView;
import com.xian.eduservice.entities.Video;
import com.xian.eduservice.fegin.VodFegin;
import com.xian.eduservice.service.VideoService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:52
 */
@CrossOrigin
@RestController
@RequestMapping("/edu/video")
public class EduVideoController {
    @Autowired
    private VideoService videoService;
    @Autowired
    private VodFegin vodFegin;
    @PostMapping("addVideo")
    public R addVideo(@RequestBody Video video) {
        return R.returnR(videoService.save(video));
    }
    @DeleteMapping("deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable("videoId") String videoId) {
        return R.returnR(videoService.removeById(videoId));
    }
    @PutMapping("modifyVideo")
    public R modifyVideo(@RequestBody Video video) {
        return R.returnR(videoService.updateById(video));
    }

    @GetMapping("/getVideoList/{videoId}/{isBuy}")
    public R getVideoList(@PathVariable("videoId") String videoId,@PathVariable("isBuy") Boolean isBuy) {
        return videoService.getVideoList(videoId,isBuy);
    }

    @GetMapping("/getCourse/{videoId}")
    public R getCourse(@PathVariable("videoId") String videoId) {
        return videoService.getCourse(videoId);
    }

    @GetMapping("/getTeacher/{videoId}")
    public R getTeacher(@PathVariable("videoId") String videoId) {
        return videoService.getTeacher(videoId);
    }

    @GetMapping("/getPlayAuth/{videoId}")
    @VideoPlay
    @VideoView
    public R getPlayAuth(@PathVariable("videoId") String videoId) {
        String video_source_id = videoService.getResourceId(videoId);
        if (!StringUtils.hasText(video_source_id)) {
            return R.ok().data("auth",null);
        }
        return vodFegin.getVideoPlayAuth(video_source_id);
    }
    @GetMapping("/getVideo/{videoId}")
    public R getVideo(@PathVariable("videoId") String videoId) {
        return R.ok().data("video",videoService.getById(videoId));
    }
}
