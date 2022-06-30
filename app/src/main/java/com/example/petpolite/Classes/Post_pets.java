package com.example.petpolite.Classes;

public class Post_pets extends User {
    String postImageURL;
    String postVideoURL;
    String postDescription;
  public   String postID;

    public String getPostVideoURL() {
        return postVideoURL;
    }

    public void setPostVideoURL(String postVideoURL) {
        this.postVideoURL = postVideoURL;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostImageURL() {
        return postImageURL;
    }

    public void setPostImageURL(String postImageURL) {
        this.postImageURL = postImageURL;
    }

    public String getPostVideURL() {
        return postVideoURL;
    }

    public void setPostVideURL(String postVideURL) {
        this.postVideoURL = postVideURL;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
}
