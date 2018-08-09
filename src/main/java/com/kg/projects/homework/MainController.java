package com.kg.projects.homework;

import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController implements ErrorController {
	
  @Autowired
  private EntryRepository entries;
	
  // Login form
  @RequestMapping("/login.html")
  public String login() {
    return "login.html";
  }
  
  // Login form with error
  @RequestMapping("/login-error.html")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    return "login.html";
  }  
  
  @ResponseBody
  @RequestMapping("/error")
  public String error() {  
	StringBuilder sb = new StringBuilder();	  
	sb.append("<h1>Error 404</h1>");
	sb.append("Oops! You have encountered an error, or tried to access a page that doesn't exist.<br><br>");
	sb.append(" <a href=\"/\"> Return to home page! </a> ");  
	  
	return sb.toString();
  }
  
  @Override
  public String getErrorPath() {
	return "/error";
  } 
   
  // Shows all entries in the database
  @ResponseBody
  @RequestMapping("/")
  public String listEntries() {
	Iterable<Entry> all = entries.findAllByOrderByDateAsc();
	
	StringBuilder sb = new StringBuilder();
	
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	String user = auth.getName();
	
	sb.append("<h1>Welcome home, " + user + "!</h1>");	
	sb.append("<form action=\"/new\" method=\"POST\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" value=\"Create new\"/></form><br>");	
	
	all.forEach(p -> sb.append("<b>Title:</b> " + "<a href=\"post/" + p.getId() + "\">" + p.getTitle() + "</a>" + "<br>" + "<b>Date created:</b> " + p.getDatePosted() + "<br>" + "<b>Body:</b><br>" + p.getBody() + "<br><br>"));
	
	sb.append("<br><form action=\"/logout\" method=\"POST\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" value=\"Log out\"/></form>");
	
	return sb.toString();
  }  
  
  // Shows a specific entry
  @ResponseBody
  @RequestMapping("/post/{id}")
  public String posts(@PathVariable Long id) {	  
	Optional<Entry> onePost = entries.findById(id);
	Entry post = onePost.get();
		  
	StringBuilder sb = new StringBuilder();
	
	sb.append("<form action=\"/\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" value=\"Home\"/></form>");
	
	sb.append("<b>Title:</b> " + post.getTitle() + "<br>" + "<b>Date created:</b> " + post.getDatePosted() + "<br>" + "<b>Body:</b><br>" + post.getBody() + "<br><br>");
		  
	sb.append("<form action=\"/edit/" + post.getId() + "\" method=\"POST\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" value=\"Edit\"/></form>");	  	 
	sb.append("<form action=\"/delete/" + post.getId() + "\" method=\"POST\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" value=\"Delete\"/></form>");
		  
	return sb.toString();
  }
  
  // Update form for Entry by ID
  @ResponseBody
  @RequestMapping("/edit/{id}")
  public String edit(@PathVariable Long id) {
	Optional<Entry> onePost = entries.findById(id);
	Entry post = onePost.get();
	  
	StringBuilder sb = new StringBuilder();

	sb.append("<form action=\"/\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" value=\"Home\"/></form>");	
	
	sb.append("<form action=\"/update/" + post.getId() + "\" method=\"post\"><input type=\"hidden\"/>");	  
	sb.append("<label for=\"date\">" + "Date created: " + post.getDatePosted() + "</label>" + "<br>");
	sb.append("<label for=\"title\"> Title</label>:" + "<br>");
	sb.append("<input type=\"text\" id=\"title\" name=\"title\" autofocus=\"autofocus\" value =\"" +  post.getTitle() + "\"/> <br/>");
	sb.append("<label for=\"body\"> Body</label>:" + "<br>");
	sb.append("<textarea rows=\"4\" cols=\"50\" id = \"body\" name = \"body\">" + post.getBody() + "</textarea>" + "<br><br>");
	
	sb.append("<form action=\"/update/" + post.getId() + "\" method=\"POST\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" name = \"submit\" value=\"Submit\"/></form>");	
	
	return sb.toString();
  }
  
  // Submit entry update, return to post
  @RequestMapping("/update/{id}")
  public String submitUpdate(@PathVariable Long id, @RequestParam(name = "title") String title, @RequestParam(name = "body") String body){
	Optional<Entry> onePost = entries.findById(id);
	Entry post = onePost.get();
	
	post.setTitle(title);
	post.setBody(body);
	
	entries.save(post);
	
	return "redirect:/post/" + post.getId();  
  }
  
  // Delete entry by ID
  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id){

	Optional<Entry> onePost = entries.findById(id);
	Entry post = onePost.get();
	entries.delete(post);
	  
	return "redirect:/";  
  }
  
  // Creates new entry
  @ResponseBody
  @RequestMapping("/new")
  public String newEntry() {
	StringBuilder sb = new StringBuilder();	  
	sb.append("<form action=\"/create\" method=\"post\"><input type=\"hidden\"/>");	 
	sb.append("<label for=\"title\"> Title</label>:" + "<br>");
	sb.append("<input type=\"text\" id=\"title\" name=\"title\" autofocus=\"autofocus\" value =\"\"/> <br/>");
	sb.append("<label for=\"body\"> Body</label>:" + "<br>");
	sb.append("<textarea rows=\"4\" cols=\"50\" id = \"body\" name = \"body\"></textarea>" + "<br><br>");
	
	sb.append("<form action=\"/create/\" method=\"POST\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" name = \"submit\" value=\"Submit\"/></form>");	
	
	return sb.toString();
  }
  
  // Pushes the new entry into the database
  @RequestMapping("/create")
  public String submitEntry(@RequestParam(name = "title") String title, @RequestParam(name = "body") String body) {	  
	 Entry post = new Entry(); 
	 post.setTitle(title);
	 post.setBody(body);
	 Date datePosted = new Date();
	 post.setDatePosted(datePosted);
	 
	 entries.save(post);
	 
	return "redirect:/post/" + post.getId();	  
  }
}
