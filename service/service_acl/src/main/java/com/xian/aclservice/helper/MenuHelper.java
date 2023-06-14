package com.xian.aclservice.helper;

import com.alibaba.fastjson.JSONObject;
import com.xian.aclservice.entities.Permission;
import com.xian.aclservice.entities.vo.PermissionVo;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MenuHelper {
    /**
     * 构建菜单
     * @param treeNodes
     * @return
     */
    public static List<JSONObject> bulid(List<PermissionVo> treeNodes) {
        List<JSONObject> meuns = new ArrayList<>();
        if(treeNodes.size() == 1) {
            PermissionVo topNode = treeNodes.get(0);
            //左侧一级菜单
            List<PermissionVo> oneMeunList = topNode.getChildren();
            for(PermissionVo one :oneMeunList) {
                JSONObject oneMeun = new JSONObject();
                oneMeun.put("path", one.getPath());
                oneMeun.put("component", one.getComponent());
                oneMeun.put("redirect", "noredirect");
                oneMeun.put("name", "name_"+one.getId());
                oneMeun.put("hidden", false);
                JSONObject oneMeta = new JSONObject();
                oneMeta.put("title", one.getName());
                oneMeta.put("icon", one.getIcon());
                oneMeun.put("meta", oneMeta);

                List<JSONObject> children = new ArrayList<>();
                List<PermissionVo> twoMeunList = one.getChildren();
                for(PermissionVo two :twoMeunList) {
                    JSONObject twoMeun = new JSONObject();
                    twoMeun.put("path", two.getPath());
                    twoMeun.put("component", two.getComponent());
                    twoMeun.put("name", "name_"+two.getId());
                    if (two.getPath().contains(":id")) {
                        twoMeun.put("hidden", true);
                    } else {
                        twoMeun.put("hidden", false);
                    }
                    JSONObject twoMeta = new JSONObject();
                    twoMeta.put("title", two.getName());
                    twoMeun.put("meta", twoMeta);
                    children.add(twoMeun);

                    List<PermissionVo> threeMeunList = two.getChildren();
                    for(PermissionVo three :threeMeunList) {
                        if(!StringUtils.hasText(three.getPath())) continue;
                        JSONObject threeMeun = new JSONObject();
                        threeMeun.put("path", three.getPath());
                        threeMeun.put("component", three.getComponent());
                        threeMeun.put("name", "name_"+three.getId());
                        threeMeun.put("hidden", true);
                        JSONObject threeMeta = new JSONObject();
                        threeMeta.put("title", three.getName());
                        threeMeun.put("meta", threeMeta);
                        children.add(threeMeun);
                    }
                }
                oneMeun.put("children", children);
                meuns.add(oneMeun);
            }
        }
        return meuns;
    }
}
