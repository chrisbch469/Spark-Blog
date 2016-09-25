package com.chrisboich.blog.dao;


import com.chrisboich.blog.model.BlogEntry;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

;

public class SimpleBlogDao implements BlogDao {

    private Sql2o sql2o;
    private List<BlogEntry> blogEntries;
    private ArrayList entries;
    private Connection conn;


    public SimpleBlogDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public SimpleBlogDao() {
        blogEntries = new ArrayList<>();
    }

    @Override
    public int addEntry(BlogEntry blogEntry) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("INSERT INTO posts(title, content, creationdate) VALUES (:title, :content, :creationdate)")
                    .addParameter("title", blogEntry.getTitle())
                    .addParameter("content", blogEntry.getContent())
                    .addParameter("creationdate", new Date())
                    .executeUpdate();
            conn.commit();
            return blogEntry.getId();
        }

    }

    @Override
    public List<BlogEntry> findAllBlogEntries() {
        String queryStr = "SELECT * FROM posts";
        try (Connection conn = sql2o.open()) {
            List<BlogEntry> entries = conn.createQuery(queryStr)
                    .executeAndFetch(BlogEntry.class);
            return entries;
        }
    }


    @Override
    public BlogEntry findBlogEntryById(int id) {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("SELECT * FROM posts WHERE post_id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(BlogEntry.class);
        }
    }


    @Override
    public void removeBlogEntryById(int id) {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DELETE FROM posts WHERE post_id = :post_id")
                    .addParameter("post_id", id)
                    .executeUpdate();
        }
    }

    @Override
    public void updateEntry(BlogEntry blogEntry) {
        try (Connection conn = sql2o.beginTransaction()) {
            conn.createQuery("UPDATE posts SET content = :content, title = :title, creationdate = :creationdate " +
                    "WHERE post_id = :post_id")
                    .addParameter("content", blogEntry.getContent())
                    .addParameter("title", blogEntry.getTitle())
                    .addParameter("creationdate", new Date())
                    .addParameter("post_id", blogEntry.getId())
                    .executeUpdate();
            conn.commit();

        }
    }




}
