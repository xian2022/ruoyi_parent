package com.xian.eduservice.fegin.fallback;

import com.xian.eduservice.fegin.VodFegin;
import com.xian.entities.R;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodFallBack implements VodFegin {
    @Override
    public R testController() {
        return R.fail().message("服务暂时不可用！");
    }

    @Override
    public R delAliyunVideo(List<String> videoSourceId) {
        return R.fail().message("服务暂时不可用！");
    }

    @Override
    public R getVideoPlayAuth(String videoId) {
        return R.fail().message("服务暂时不可用！");
    }
}
