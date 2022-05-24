package com.rzk.pojo.vo;


import org.springframework.stereotype.Component;

@Component
public class PublisherVideos {

    public  UsersVo publisher;
    public  boolean userLikeVideo;

    public UsersVo getPublisher() {
        return publisher;
    }

    public void setPublisher(UsersVo publisher) {
        this.publisher = publisher;
    }

    public boolean isUserLikeVideo() {
        return userLikeVideo;
    }

    public void setUserLikeVideo(boolean userLikeVideo) {
        this.userLikeVideo = userLikeVideo;
    }
}