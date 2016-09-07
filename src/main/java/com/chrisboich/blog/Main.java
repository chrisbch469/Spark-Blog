package com.chrisboich.blog;

import com.chrisboich.blog.dao.*;
import com.chrisboich.blog.model.BlogEntry;
import org.sql2o.Sql2o;
import spark.Filter;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        //TODO: cb fix db information
        //database information
        String dbHost = "localhost";
        String dbPort = "5432";
        String dbUsername = "postgres"; //unable to access CRUD options with new users with all permissions
        String dbPassword = "ctboich4";
        String dbName = "simplesparkblog";

        //new sql2o object with database information
        Sql2o sql2o = new Sql2o("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName, dbUsername, dbPassword);

        //password for CRUD options
        String blogPassword = "blogowner";

        //static file location for css
        staticFileLocation("/public");

        //dao object
        BlogDao dao = new SimpleBlogDao(sql2o);
        CommentDao comDao = new SimpleCommentDao(sql2o);

        //protected routes
        String protectedNew = "/new.html";
        String protectedNewEntry = "/new-entry.html";
        String protectedEdit = "/entries/edit/*";
        String protectedDelete = "/entries/delete/*";

        Filter routeFilter = (req, res) -> {
            req.session().attribute("unauthorized-page", req.uri());
            if (req.cookie("password") == null || !req.cookie("password").equals(blogPassword)) {
                res.status(401);
                res.redirect("/password-entry.html");

                halt();
            }
        };

        //before filter to halt unauthorized access
        before(protectedDelete, routeFilter);
        before(protectedEdit, routeFilter);
        before(protectedNew, routeFilter);
        before(protectedNewEntry, routeFilter);

        //get homepage
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            try {
                model.put("entries", dao.findAllBlogEntries());
            } catch (Exception exc) {
                System.out.println("Error Finding Entries.");
            }
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());


        //get new entry page and display
        get("/new.html", (req, res) -> {
            return new ModelAndView(null, "new.hbs");
        }, new HandlebarsTemplateEngine());

        //get admin page and display
        get("passwordEntry.html", (req, res) -> {
            return new ModelAndView(null, "password.hbs");
        }, new HandlebarsTemplateEngine());

        //post new entry post from the post forms
        post("/new-entry", (req, res) -> {
            String title = req.queryParams("title");
            String content = req.queryParams("content");
            BlogEntry blogEntry = new BlogEntry(title, content);
            try {
                dao.addEntry(blogEntry);
            } catch (Exception exc) {
                System.out.println("Unable to Create New Entry, Try Again.");
            }
            res.redirect("/");
            return null;
        });

        //get details page and display
        get("/entries/details/:id/:TitleSlug.html", (req, res) -> {
            int entryId;
            entryId = Integer.parseInt(req.params("id"));
            Map<String, Object> model = new HashMap<>();
            BlogEntry blogEntry = dao.findBlogEntryById(entryId);
            model.put("entry", blogEntry);
            model.put("comments", comDao.findCommentsById(entryId));
            return new ModelAndView(model, "detail.hbs");
        }, new HandlebarsTemplateEngine());

        //details post getting post information based on params
        post("/entries/details/:id/:TitleSlug.html", (req, res) -> {
            int entryId;
            entryId = Integer.parseInt(req.params("id"));
            BlogEntry blogEntry = dao.findBlogEntryById(entryId);
            String titleSlug = blogEntry.getTitleSlug();
            String commentAuthor = req.queryParams("author");
            String commentContent = req.queryParams("content");

            if (commentAuthor.isEmpty()) {
                commentAuthor = "Anonymous";
            }

            try {
                comDao.addComment(entryId, commentAuthor, commentContent);
            } catch (Exception exc) {
                System.out.println("Error Adding New Comment For entry " + entryId);
            }

            res.redirect("/entries/details/" + entryId + "/" + titleSlug + ".html");
            return null;
        });

        //get edit page and display
        get("/entries/edit/:id/:TitleSlug.html", (req, res) -> {
            int entryId;
            entryId = Integer.parseInt(req.params("id"));
            BlogEntry blogEntry = dao.findBlogEntryById(entryId);
            Map<String, Object> model = new HashMap<>();
            model.put("entry", blogEntry);
            return new ModelAndView(model, "edit.hbs");
        }, new HandlebarsTemplateEngine());

        //post for edit page
        post("/entries/edit/:id/:TitleSlug.html", (req, res) -> {
            int entryId;
            entryId = Integer.parseInt(req.params("id"));
            String newContent = req.queryParams("content");
            String newTitle = req.queryParams("title");
            BlogEntry newBlogEntry = new BlogEntry(newTitle, newContent);
            newBlogEntry.setId(entryId);
            dao.updateEntry(newBlogEntry);
            res.redirect("/");
            return null;
        });

        //get for deleting an entry based on the id param
        get("/entries/delete/:id/:TitleSlug.html", (req, res) -> {
            int entryId;
            entryId = Integer.parseInt(req.params("id"));
            comDao.removeAllComments(entryId);
            dao.removeBlogEntryById(entryId);
            res.redirect("/");
            return null;
        });

        //get password page
        get("/password-entry.html", (req, res) -> {
            return new ModelAndView(null, "password.hbs");
        }, new HandlebarsTemplateEngine());

        //post password data
        post("/password-entry.html", (req, res) -> {
            String password = req.queryParams("password");
            res.cookie("password", password);
            res.redirect((req.session().attribute("unauthorized-page")));
            return null;
        });

        get("/contact.html", (req, res) ->{
            return new ModelAndView(null, "contact.hbs");
        }, new HandlebarsTemplateEngine());

        get("/terms.html", (req, res) ->{
            return new ModelAndView(null, "terms.hbs");
        }, new HandlebarsTemplateEngine());


        //slug not found exception page display
        exception(NotFoundException.class, (exc, req, res) -> {
            res.status(404);
            HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
            String html = engine.render(new ModelAndView(null, "not-found.hbs"));
            res.body(html);
        });

    }//end main
}//end main class
