package com.xian.eduservice.controller;


import com.xian.eduservice.entities.Chapter;
import com.xian.eduservice.entities.vo.ChapterVo;
import com.xian.eduservice.service.ChapterService;
import com.xian.entities.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/05/16 18:52
 */
@CrossOrigin
@RestController
@RequestMapping("/edu/chapter")
public class EduChapterController {
    @Autowired
    private ChapterService chapterService;
    @GetMapping("/getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable("courseId") String courseId) {
        List<ChapterVo> list = chapterService.getChapterVideoByCourseId(courseId);
        return R.ok().data("items",list);
    }
    @GetMapping("/deleteChapter/{chapterId}")
    public R deleteChapterById(@PathVariable("chapterId") String chapterId) {
        return R.returnR(chapterService.deleteChapterById(chapterId));
    }
    @PostMapping("addChapter")
    public R addChapter(@RequestBody List<ChapterVo> chapterList) {
        return R.returnR(chapterService.saveList(chapterList));
    }
    @PutMapping("modifyChapter")
    public R modifyChapter(@RequestBody Chapter chapter) {
        return R.returnR(chapterService.updateById(chapter));
    }
}
