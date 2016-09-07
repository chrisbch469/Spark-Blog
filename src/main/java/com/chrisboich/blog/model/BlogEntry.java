package com.chrisboich.blog.model;


import com.github.slugify.Slugify;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class BlogEntry implements Comparator<BlogEntry> {

    //private attributes
    private String title;
    private String content;
    private Date creationdate;
    private int post_id;
    private String mSlug;

    /*blog entry constructor with 3 params
    @param - title = title of blog entry
    @param - entry = body of blog entry
     */
    public BlogEntry(String title, String content) {
        this(title, content, new Date());
    }

    public BlogEntry(String title, String content, Date date) {
        creationdate = date;
        this.content = content;
        this.title = title;
    }

    public String getTitleSlug() {
        mSlug = "";
        //try to use title as slug or catch io exception
        try {
            Slugify slugify = new Slugify();
            mSlug = slugify.slugify(title);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return mSlug;
    }


    public int getId() {
        return post_id;
    }

    public void setId(int id) {
        post_id = id;
    }

    public void setDate(Date date) {
        creationdate = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSlug() {
        return mSlug;
    }


    public String getPostDate() {
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm");
        return dateFormat.format(creationdate);
    }

    public String getPostDateAsString() {
        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, YYYY 'at' hh:mm");
        return dateFormat.format(creationdate);
    }


    public boolean addComment(Comment comment) {
        // Store these comments!
        return false;
    }

    @Override
    public int compare(BlogEntry b1, BlogEntry b2) {
        return b1.getPostDate().compareTo(b2.getPostDate());
    }

    //generated equals method for comparing date
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogEntry blogEntry = (BlogEntry) o;

        if (post_id != blogEntry.post_id) return false;
        if (content != null ? !content.equals(blogEntry.content) : blogEntry.content != null) return false;
        if (!title.equals(blogEntry.title)) return false;
        return creationdate.equals(blogEntry.creationdate);

    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (creationdate != null ? creationdate.hashCode() : 0);
        return result;
    }

}
