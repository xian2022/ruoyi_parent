package com.xian.eduservice.entities.subject;

import com.xian.eduservice.entities.Course;
import com.xian.eduservice.entities.Subject;
import lombok.Data;

import java.util.List;

@Data
public class TwoSubject {
    private String id;
    private String title;
    private List<Course> courseList;
}
