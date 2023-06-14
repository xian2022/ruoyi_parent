package com.xian.eduservice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xian.eduservice.entities.Teacher;
import com.xian.eduservice.entities.vo.TeacherQuery;
import com.xian.eduservice.service.TeacherService;
import com.xian.entities.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author xian
 * @since 2022/04/19 20:57
 */
@Api("讲师管理")
@CrossOrigin
@RestController
@RequestMapping("/edu/teacher")
public class EduTeacherController {
    @Autowired
    private TeacherService teacherService;

    /**
     * 查询全部讲师
     * @return
     */
    @ApiOperation("findAllTeacher")
    @GetMapping("/findAll")
    public R findAll(){
        return R.ok().message("查询成功！").data("items",teacherService.list(null));
    }

    /**
     * 根据Id逻辑删除讲师
     * @param id
     * @return
     */
    @ApiOperation("deleteTeacherById")
    @DeleteMapping("/del/{id}")
    public R logicDel(@ApiParam(name = "id" , value = "讲师Id" , required = true) @PathVariable("id")String id){
        return teacherService.removeById(id) ? R.ok().message("删除成功！") : R.fail().message("删除失败！");
    }

    /**
     * 分页查询
     * @param current
     * @param count
     * @return
     */
    @ApiOperation("selectTeacherByCondition")
    @PostMapping({"/search/{current}/{count}"})
    public R selectTeacherByCondition(@PathVariable(value = "current")Integer current, @PathVariable(value = "count",required = false)Integer count, @RequestBody(required = false) TeacherQuery teacherQuery) {        //创建page对象
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        if (teacherQuery!=null) {
            String name = teacherQuery.getName();
            Integer level = teacherQuery.getLevel();
            String begin = teacherQuery.getBegin();
            String end = teacherQuery.getEnd();
            if (!ObjectUtils.isEmpty(name)) {
                wrapper.like("name",name);
            } if (!ObjectUtils.isEmpty(level)) {
                wrapper.eq("level",level);
            } if (!ObjectUtils.isEmpty(begin)) {
                wrapper.ge("gmt_create",begin);
            } if (!ObjectUtils.isEmpty(end)) {
                wrapper.le("gmt_modified",end);
            }
            wrapper.orderByDesc("gmt_create");
        }
        return pageList(current,count,wrapper);
    }

    @ApiOperation("addTeacher")
    @PostMapping("/add")
    public R addTeacher(@RequestBody Teacher teacher) {
        return teacherService.save(teacher)?R.ok():R.fail();
    }

    @ApiOperation("modifyTeacher")
    @PostMapping("/mod")
    public R modifyTeacher(@RequestBody Teacher teacher) {
        return teacherService.updateById(teacher)?R.ok():R.fail();
    }

    @ApiOperation("getTeacherById")
    @GetMapping("/find/{id}")
    public R getTeacherById(@PathVariable("id") String id){
        return teacherService.getTeacherById(id);
    }

    private R pageList(Integer current, Integer count, QueryWrapper<Teacher> wrapper) {
        Page<Teacher> page = new Page<>(current,count);
        teacherService.page(page,wrapper);
        return R.ok().data("total",page.getTotal()).data("pages",page.getPages()).data("current",page.getCurrent()).data("items",page.getRecords());
    }
}
