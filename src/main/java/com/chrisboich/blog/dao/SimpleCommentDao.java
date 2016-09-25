package com.chrisboich.blog.dao;

import com.chrisboich.blog.model.Comment;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Date;
import java.util.List;

/**
 * Created by chris on 8/24/2016.
 */
public class SimpleCommentDao implements CommentDao {

    private Sql2o cSql2o;
    private Connection conn;

   public SimpleCommentDao(Sql2o cSql2o) {
        this.cSql2o = cSql2o;

    }

    @Override
    public int addComment(int post_id, String author, String content) {
        try (Connection conn = cSql2o.open()) {
            conn.createQuery("INSERT INTO comments(post_id, author, content, submissiondate) " +
                    "VALUES (:post_id, :author, :content, :submissiondate)")
                    .addParameter("post_id", post_id)
                    .addParameter("author", author)
                    .addParameter("content", content)
                    .addParameter("submissiondate", new Date())
                    .executeUpdate();

            Comment comment = new Comment(post_id, author, content);

            return comment.getComment_id();
        }
    }


    @Override
    public void removeAllComments(int id) {
        try (Connection conn = cSql2o.open()) {
            conn.createQuery("DELETE FROM comments WHERE post_id = :post_id")
                    .addParameter("post_id", id)
                    .executeUpdate();
        }
    }

    @Override
    public List<Comment> findCommentsById(int id) {
        try (Connection conn = cSql2o.open()) {
            return conn.createQuery("SELECT * FROM comments WHERE post_id = :post_id")
                    .addParameter("post_id", id)
                    .executeAndFetch(Comment.class);
        }
    }
}
