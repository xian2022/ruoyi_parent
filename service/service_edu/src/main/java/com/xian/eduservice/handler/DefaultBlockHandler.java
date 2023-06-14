package com.xian.eduservice.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.xian.entities.R;
import org.springframework.stereotype.Component;

@Component
public class DefaultBlockHandler {
    public static R defaultHandlerException(BlockException exception){
        return R.fail().message("出错啦！请稍后再试！");
    }
}
