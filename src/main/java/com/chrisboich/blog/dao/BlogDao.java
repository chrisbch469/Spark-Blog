package com.chrisboich.blog.dao;


import com.chrisboich.blog.model.BlogEntry;

import java.util.List;

public interface BlogDao {

    int addEntry(BlogEntry blogEntry);

    List<BlogEntry> findAllBlogEntries();

    BlogEntry findBlogEntryById(int id);

    void removeBlogEntryById(int id);

    void updateEntry(BlogEntry blogEntry);



}
