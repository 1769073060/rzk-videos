package com.rzk.service;

import com.rzk.pojo.Comments;
import com.rzk.pojo.Videos;
import com.rzk.utils.PagedResult;

import java.util.List;

public interface VideosService {
    /**
     * 保存视频
     * @param videos
     */
    public String insertVideos(Videos videos);

    /**
     * 上传视频封面
     * @param videoId
     * @param coverPath
     * @return
     */
    public String updateVideos(String videoId ,String coverPath);


    /**
     * 分页视频列表
     *
     * @return
     */
    public PagedResult getAllVideosAndUsers( Videos video,Integer isSaveRecord,Integer page,Integer pageSize);

    /**
     * 获取用户所点赞的用户视频信息
     */
    public PagedResult selectMyLikeVideos(String userId, Integer pages,Integer pageSize);
    /**
     * 获取用户所关注的用户视频信息
     **/
    public PagedResult selectFollowVideos(String userId, Integer pages,Integer pageSize);


    /**
     *热搜列表
     */
    public  List<String> getHotselect();


    /**
     * 用户点赞视频
     * @param userId
     * @param videoId
     * @param videoUserId
     */
    public void userLikeVideos(String userId,String videoId, String videoUserId);
    /**
     * 用户取消点赞视频
     * @param userId
     * @param videoId
     * @param videoUserId
     */
    public void userDislikeVideos(String userId,String videoId, String videoUserId);

    /**
     * 获取用户评论
     * @param videoId
     * @param page
     * @param pageSize
     * @return
     */

    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize);
    /**
     * 保存用户评论
     */
    public void saveComment(Comments comment) throws Exception;

}
