package com.kg.projects.homework;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	
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
  
  @PostMapping("/delete/{id}")
  public String delete(@PathVariable Long id){

	  Optional<Entry> onePost = entries.findById(id);
	  Entry post = onePost.get();
	  entries.delete(post);
	  
	return "redirect:/posts";
	  
  }
  
  @ResponseBody
  @RequestMapping(value = "/createNew", method = RequestMethod.GET)
  public String newEntry() {
	return "hewwo";
	
  }
  
  @ResponseBody
  @RequestMapping(value = "/post/{id}", method = RequestMethod.GET)
  public String posts(@PathVariable Long id) {
	  
	  Optional<Entry> onePost = entries.findById(id);
	  Entry post = onePost.get();
	  
	  StringBuilder sb = new StringBuilder();
	  
	  sb.append(post.getTitle() + "<br>" + post.getDatePosted() + "<br>" + post.getBody() + "<br><br>");
	  sb.append("<form action=\"/delete/" + post.getId() + "\" method=\"POST\"><input type=\"hidden\"/>");
	  sb.append("<input type=\"submit\" value=\"Delete\"/></form>");
	  
	  return sb.toString();
  }
  
  @ResponseBody
  @RequestMapping("/posts")
  public String listEntries() {
	Iterable<Entry> all = entries.findAllByOrderByDateAsc();
	
	StringBuilder sb = new StringBuilder();
	
	sb.append("<form action=\"/createNew\" method=\"POST\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" value=\"Create new\"/></form>");
	
	all.forEach(p -> sb.append("<a href=\"post/" + p.getId() + "\">" + p.getTitle() + "</a>" + "<br>" + p.getDatePosted() + "<br>" + p.getBody() + "<br><br>"));
	
	sb.append("<form action=\"/logout\" method=\"POST\"><input type=\"hidden\"/>");
	sb.append("<input type=\"submit\" value=\"Logout\"/></form>");
	

	return sb.toString();
	
  }
}
