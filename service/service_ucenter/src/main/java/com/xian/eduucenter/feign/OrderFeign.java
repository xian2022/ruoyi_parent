package com.xian.eduucenter.feign;

import com.xian.entities.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-order")
public interface OrderFeign {
    @GetMapping("/serviceorder/front/order/getOrderAndCourseBymemberId/{memberId}")
    R getOrderAndCourseBymemberId(@PathVariable("memberId") String memberId);
}
