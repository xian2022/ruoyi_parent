package com.xian.edusms.service;

import com.xian.entities.R;

public interface SmsService {
    R sendRegisteCode(String phoneNum);

    R sendChangeCode(String phoneNum);

    R sendLoginCode(String phoneNum);
}
