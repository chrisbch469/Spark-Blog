package com.chrisboich.blog.model;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {

    //private attributes
    private int post_id;
    private int comment_id;
    private String content;
    private Date submissiondate;
    private String author;


    public Comment(int post_id, String author, String content, Date submissionDate) {
        this.post_id = post_id;
        this.author = author;
        this.content = content;
        this.submissiondate = submissionDate;
    }


    public Comment(int post_id, String author, String content) {
        this(post_id, author, content, new Date());
    }

    public int getPost_id() {
        return this.post_id;
    }

    //getters and setters
    public void setPost_id(int id) {
        this.post_id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String body) {
        this.content = body;
    }

    public Date getSubmissiondate() {
        return this.submissiondate;
    }

    public void setSubmissiondate(Date date) {
        this.submissiondate = date;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getComment_id() {
        return this.comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getPostDate() {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm");
        return dateFormat.format(submissiondate);
    }

    public String getPostDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY 'at' hh:mm");
        return dateFormat.format(submissiondate);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (post_id != comment.post_id) return false;
        if (comment_id != comment.comment_id) return false;
        if (content != null ? !content.equals(comment.content) : comment.content != null) return false;
        if (submissiondate != null ? !submissiondate.equals(comment.submissiondate) : comment.submissiondate != null)
            return false;
        return author != null ? author.equals(comment.author) : comment.author == null;

    }

    @Override
    public int hashCode() {
        int result = post_id;
        result = 31 * result + comment_id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (submissiondate != null ? submissiondate.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        return result;
    }
}
