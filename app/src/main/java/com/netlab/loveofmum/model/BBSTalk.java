package com.netlab.loveofmum.model;

import com.netlab.loveofmum.api.MMloveConstants;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
//map.put("title", jo.getString("title"));
//        map.put("Name", jo.getString("bbsForumName"));
//        map.put("username", jo.getString("username"));
//        map.put("content", jo.getString("content"));
//        map.put("avatar", jo.getString("avatar"));
//        map.put("replyCount", jo.getString("replyCount"));
//        map.put("viewCount", jo.getString("viewCount"));
//        map.put("createTime", jo.getString("createTime"));
//        map.put("YuBirthTime", jo.getString("YuBirthTime"));
//        map.put("url", MMloveConstants.URLWEB + jo.getString("url"));
public class BBSTalk {
    public String title;
    public String bbsForumName;
    public String username;
    public String content;
    public String avatar;
    public String replyCount;
    public String viewCount;
    public String createTime;
    public String YuBirthTime;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = MMloveConstants.URLWEB +url;
    }
}
