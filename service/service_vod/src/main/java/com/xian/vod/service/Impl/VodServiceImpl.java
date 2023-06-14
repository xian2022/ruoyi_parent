package com.xian.vod.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import com.xian.entities.R;
import com.xian.vod.service.VodService;
import com.xian.vod.utils.ConstantPropertiesUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    private DefaultAcsClient client;

    // 文件删除
    @Override
    public R deleteVideo(List<String> videoSourceId) {
        try {
            deleteAliyunVideo(videoSourceId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail().message(e.getMessage());
        }
        return R.ok();
    }

    @Override
    public R deleteVideo(String videoSourceId) {
        try {
            deleteAliyunVideo(videoSourceId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail().message(e.getMessage());
        }
        return R.ok();
    }

    // 视频文件上传
    @Override
    public R uploadVideo(MultipartFile video) {
        String fileName = video.getOriginalFilename();
        String title = fileName.substring(0,fileName.lastIndexOf("."));
        InputStream is;
        try {
            is = video.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail().message(e.getMessage());
        }
        UploadStreamResponse response = uploadStream(title, fileName, is);
        String videoId = null;
        if (response.isSuccess()) {
            videoId = response.getVideoId();
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            videoId = response.getVideoId();
        }
        return R.ok().data("videoId",videoId);
    }



    private UploadStreamResponse uploadStream(String title, String fileName, InputStream inputStream) {
        UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET, title, fileName, inputStream);
        return upload(request);
    }
    private UploadStreamResponse uploadStream(String title, String fileName, InputStream inputStream,Boolean ShowWaterMark) {
        UploadStreamRequest request = new UploadStreamRequest(ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET, title, fileName, inputStream);
        /* 是否使用默认水印(可选)，指定模板组ID时，根据模板组配置确定是否使用默认水印*/
        request.setShowWaterMark(ShowWaterMark);
        return upload(request);
    }

    private UploadStreamResponse upload(UploadStreamRequest request) {
        UploadVideoImpl uploader = new UploadVideoImpl();
        // System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        /*if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }*/
        return uploader.uploadStream(request);
    }
    /**
     * 获取音/视频上传地址和凭证
     * @return CreateUploadVideoResponse 获取音/视频上传地址和凭证响应数据
     * @throws Exception
     */
    public CreateUploadVideoResponse createUploadVideo(String title, String fileName) throws Exception {
        client = new DefaultAcsClient(DefaultProfile.getProfile(ConstantPropertiesUtils.KEY_REGIONID, ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET));
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(title);
        request.setFileName(fileName);
        return client.getAcsResponse(request);
    }

    /**
     * 获取 播放凭证 和 VideoMeta信息
     * @return CreateUploadVideoResponse 获取音/视频上传地址和凭证响应数据
     * @throws Exception
     */
    public R getVideoPlayAuth(String videoId) throws ClientException {
        client = new DefaultAcsClient(DefaultProfile.getProfile(ConstantPropertiesUtils.KEY_REGIONID, ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET));
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(videoId);
        GetVideoPlayAuthResponse response = client.getAcsResponse(request);
        String playAuth = response.getPlayAuth();
        return R.ok().message("获取凭证成功").data("playAuth", playAuth);
    }

    /**
     * 获取播放地址 和 Base信息
     * @return CreateUploadVideoResponse 获取音/视频上传地址和凭证响应数据
     * @throws Exception
     */
    public GetPlayInfoResponse getPlayInfo(String videoId) throws Exception {
        client = new DefaultAcsClient(DefaultProfile.getProfile(ConstantPropertiesUtils.KEY_REGIONID, ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET));
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }

    /**
     * 删除视频
     * @return CreateUploadVideoResponse 获取音/视频上传地址和凭证响应数据
     * @throws Exception
     */
    public DeleteVideoResponse deleteAliyunVideo(List<String> videoIds) throws Exception {
        client = new DefaultAcsClient(DefaultProfile.getProfile(ConstantPropertiesUtils.KEY_REGIONID, ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET));
        StringBuilder builder = new StringBuilder();
        int size = videoIds.size();
        for (int i = 0; i < size; i++) {
            if(i == size -1){
                builder.append(videoIds.get(i));
            }else{
                builder.append(videoIds.get(i)).append(",");
            }
        }
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(builder.toString());
        return client.getAcsResponse(request);
    }
    /**
     * 删除视频
     * @return CreateUploadVideoResponse 获取音/视频上传地址和凭证响应数据
     * @throws Exception
     */
    public DeleteVideoResponse deleteAliyunVideo(String videoIds) throws Exception {
        client = new DefaultAcsClient(DefaultProfile.getProfile(ConstantPropertiesUtils.KEY_REGIONID, ConstantPropertiesUtils.KEY_ID, ConstantPropertiesUtils.KEY_SECRET));
        DeleteVideoRequest request = new DeleteVideoRequest();
        //支持传入多个视频ID，多个用逗号分隔
        request.setVideoIds(videoIds);
        return client.getAcsResponse(request);
    }
}
