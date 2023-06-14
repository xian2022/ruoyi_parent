package com.xian.security.entites;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {
    private User loginUserInfo;
    private List<String> permissionValues = new ArrayList<>();

    public SecurityUser() {}
    public SecurityUser(User user) {
        this.loginUserInfo = user;
    }
    public SecurityUser(User user,List<String> permissions) {
        this.loginUserInfo = user;
        if (ObjectUtil.isNotNull(permissions)) {
            this.permissionValues = permissions;
        }

    }

    public User getLoginUserInfo() {
        return loginUserInfo;
    }

    public void setLoginUserInfo(User loginUserInfo) {
        this.loginUserInfo = loginUserInfo;
    }

    public List<String> getPermissionValueList() {
        return permissionValues;
    }

    public void setPermissionValueList(List<String> permissions) {
        if (ObjectUtil.isNotNull(permissions)) {
            this.permissionValues = permissions;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for(String permissionValue : permissionValues) {
            if(!StringUtils.hasText(permissionValue)) continue;
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
            authorities.add(authority);
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return loginUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return loginUserInfo.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
