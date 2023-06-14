package com.xian.serviceorder.service.impl;

import com.xian.serviceorder.entities.PayLog;
import com.xian.serviceorder.mapper.PayLogMapper;
import com.xian.serviceorder.service.PayLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author xian
 * @since 2022/06/26 23:32
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

}
