package com.xian.educms.service.impl;

import com.xian.educms.entities.Banner;
import com.xian.educms.mapper.BannerMapper;
import com.xian.educms.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/05/27 16:31
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
    @Cacheable(value = "banner", key = "'getAllBanner'")
    @Override
    public List<Banner> selectAllBanner() {
        return list();
    }
}
