package com.xian.aclservice.service;

import com.alibaba.fastjson.JSONObject;
import com.xian.aclservice.entities.Permission;
import com.xian.aclservice.entities.vo.UserLogin;
import com.xian.entities.R;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IndexService {
    R getUserInfo(HttpServletRequest request);

    List<JSONObject> getMenu(HttpServletRequest request);

    R login(UserLogin userLogin);

    R logout();
}
