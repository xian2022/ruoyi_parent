package com.xian.eduservice.entities.subject;

import com.xian.eduservice.entities.Subject;
import com.xian.eduservice.utils.PageUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OneSubject {
    private String id;
    private String title;
    private List<Subject> children;
}
