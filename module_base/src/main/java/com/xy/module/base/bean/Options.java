package com.xy.module.base.bean;

import androidx.annotation.IntegerRes;

import com.xy.module.base.constants.Constants;

import java.util.List;

public class Options<T> {
    @IntegerRes
    private int imageResource;

    private String[] content;

    private int imageNext;

    @Constants.OptionsType
    private int type;

    private List<T> mediaList;

    private boolean important;

    public Options(String... content) {
        this(Constants.OptionsType.TEXT,0,0,content);
    }

    public Options(@Constants.OptionsType int type,boolean important,List<T> media,String... content) {
        this(type,0,0,important,media,content);
    }

    public Options(@Constants.OptionsType int type,String... content) {
        this(type,0,0,content);
    }

    public Options(@Constants.OptionsType int type,boolean important,String... content) {
        this(type,0,0,important,content);
    }

    public Options(@Constants.OptionsType int type, int imageResource, String... content) {
        this(type,imageResource,0,content);
    }

    public Options(@Constants.OptionsType int type,int imageResource, int imageNext,String... content) {
      this(type,imageResource,imageNext,true,null,content);
    }

    public Options(@Constants.OptionsType int type,int imageResource, int imageNext,List<T> medias,String... content) {
      this(type,imageResource,imageNext,true,medias,content);
    }

    public Options(@Constants.OptionsType int type,int imageResource, int imageNext,boolean important,String... content) {
      this(type,imageResource,imageNext,important,null,content);
    }

    public Options(@Constants.OptionsType int type,int imageResource, int imageNext,boolean important,List<T> medias,String... content) {
        this.imageResource = imageResource;
        this.content = content;
        this.imageNext = imageNext;
        this.type = type;
        this.mediaList = medias;
        this.important = important;
    }


    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

    public int getImageNext() {
        return imageNext;
    }

    public void setImageNext(int imageNext) {
        this.imageNext = imageNext;
    }

    @Constants.OptionsType
    public int getType() {
        return type;
    }

    public void setType(@Constants.OptionsType int type) {
        this.type = type;
    }

    public List<T> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<T> mediaList) {
        this.mediaList = mediaList;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }
}
