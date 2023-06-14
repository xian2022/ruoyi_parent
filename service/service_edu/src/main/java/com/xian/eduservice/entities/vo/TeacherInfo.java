package com.xian.eduservice.entities.vo;

import com.xian.eduservice.entities.Teacher;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;

@Data
public class TeacherInfo {
    private Integer type;
    private List<Teacher> numbers;
}