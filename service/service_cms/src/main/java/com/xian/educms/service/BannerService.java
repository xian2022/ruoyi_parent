package com.xian.educms.service;

import com.xian.educms.entities.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author xian
 * @since 2022/05/27 16:31
 */
public interface BannerService extends IService<Banner> {

    List<Banner> selectAllBanner();
}
