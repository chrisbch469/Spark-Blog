package com.chrisboich.blog.dao;

import com.chrisboich.blog.model.Comment;

import java.util.List;

/**
 * Created by chris on 8/24/2016.
 */
public interface CommentDao {

    int addComment(int post_id, String author, String content);

    //int addComment(Comment comment);
    void removeAllComments(int id);

    List<Comment> findCommentsById(int id);


}
