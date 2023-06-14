package com.xian.serviceorder.feign;

import com.xian.entities.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("service-edu")
public interface EduFeign {
    @GetMapping("/edu/course/getCourseInfoByBatchId")
    R getCourseInfoByBatchId(@RequestBody List<String> courseIdList);
}
