package com.DH.Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.DH.Entity.Book;
import com.DH.Entity.Rating;
import com.DH.Entity.Search;
import com.DH.Entity.User;
import com.DH.Service.UserService;
import com.DH.Service.bookService;

@Controller
public class WebController extends WebMvcConfigurerAdapter {
	
	@Autowired
	UserService userService;
	
	@Autowired
	bookService bookService;

	public User loggedInUser;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }
    
    //Home pages
    @GetMapping("/")
    public String homepage() {
    	 if (loggedInUser == null)
    	 {
    		 return "home";
    	 }
    	 else if (loggedInUser.isAdmin())
       	 {
       		 return "adminHome";
       	 }
    	 else
    	 {
    		 return "welcome";
    	 }
    }
    
    @GetMapping("/home")
    public String home() {
   	 if (loggedInUser == null)
   	 {
   		 return "home";
   	 }
   	 else if (loggedInUser.isAdmin())
   	 {
   		 return "adminHome";
   	 }
   	 else
   	 {
   		 return "welcome";
   	 }
    }
    
    //Login Pages
    @GetMapping("/login")
    public String showLogin(User user) {
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout() {
    	loggedInUser = null;
        return "loginRegister";
    }
	  
	    @PostMapping("/loginProcess")
	    public void loginProcess(@Valid User user, HttpServletRequest request, HttpServletResponse response)throws IOException {
	    	response.setContentType("text/html");
	    	PrintWriter out = response.getWriter();
	    	if (loginCheck(user))
	    	{
	    	htmlHeader( out);
 		    toolBarStart(out);
		    toolBarEnd(out);
		    container(out);
		    out.println("<p>You are logged in as :" + loggedInUser.getUsername() + "</p>");
		    out.println("<form action=\"http://localhost:8080/logout\">");
		    out.println("<input type=\"submit\" class=\"btn btn-default\" value=\"Logout\" /></form>");
		    htmlFooter(out);
	    	}
	    	else
	    	{
	    		htmlHeader( out);
	    		container(out);
			    out.println("<p>Incorrect username or password</p>");
			    out.println("");
			    out.println("<form action=\"http://localhost:8080/login\">");
			    out.println("<input type=\"submit\" class=\"btn btn-default\" value=\"Back to Login\" /></form>");
			    htmlFooter(out);
	    	}
	    }
    
    public boolean loginCheck(User user)
    {
    	boolean result = false;
    	User temp;
    	if (userService.findByUsername(user.getUsername()).isEmpty())
    	{
    		return result;
    	}
    	else
    	{
    		temp = userService.findByUsername(user.getUsername()).get(0);
    		if (temp.getUsername().equalsIgnoreCase(user.getUsername()) && temp.getPassword().equalsIgnoreCase(user.getPassword()))
    		{
    			loggedInUser = temp;
    			return true;
    		}
    	}
    	return result;
    }
    
    //Registrations
    @GetMapping("/register")
    public String showForm(User user) {
        return "form";
    }
    

    @PostMapping("/registerProcess")
   public void register(@Valid User user, HttpServletRequest request, HttpServletResponse response)throws IOException {
    	response.setContentType("text/html");
    	PrintWriter out = response.getWriter();
        if (registrationCheck(user)) {
        	userService.save(user);
        	htmlHeader( out);
        	container(out);
		    out.println("<p>Successfully registered user:" + user.getUsername() + "</p>");
		    out.println("<form action=\"http://localhost:8080/login\">");
		    out.println("<input type=\"submit\" class=\"btn btn-default\" value=\"Login\" /></form>");
		    htmlFooter(out);
        }
        else
        {
        	htmlHeader( out);
        	container(out);
		    out.println("<p>Invalid registration details</p>");
		    out.println("");
		    out.println("<form action=\"http://localhost:8080/register\">");
		    out.println("<input type=\"submit\" class=\"btn btn-default\" value=\"Back to Register\" /></form>");
		    htmlFooter(out);
        }
    }
    
   
    
    public boolean registrationCheck(User user)
    {
    	boolean result = false;
    	if (userService.findByUsername(user.getUsername()).isEmpty() && !user.getUsername().isEmpty() && !user.getPassword().isEmpty())
    	{
    		result = true;
    	}
    	return result;
    }
    
    //Searches
    
    @GetMapping("/search")
    public String search(Search search) {
    	if (loggedInUser == null)
    	{
            return "loginRegister";
    	}
    	else if (loggedInUser.isAdmin())
      	 {
      		 return "adminSearch";
      	 }
    	else
    	{
    		return "search";
    	}
    }
    
    @GetMapping("/createBook")
    public String createBook(Book book) {
        return "createBook";
    }
    
    @PostMapping("/bookCreated")
    public void createBook(@Valid Book book, HttpServletRequest request, HttpServletResponse response)throws IOException {
     	response.setContentType("text/html");
     	PrintWriter out = response.getWriter();
         	bookService.save(book);
         	htmlHeader( out);
         	container(out);
 		    out.println("<p>Successfully created book:" + book.getTitle() + "</p>");
 		    out.println("<form action=\"http://localhost:8080/home\">");
		    out.println("<input type=\"submit\" class=\"btn btn-default\" value=\"Back\" /></form>");
 		    htmlFooter(out);
     }
    
    @PostMapping("/searchResults")
    public void search(@Valid Search search, HttpServletRequest request, HttpServletResponse response)throws IOException {
     	response.setContentType("text/html");
     	PrintWriter out = response.getWriter();
     	List<Book> books = bookService.search("%"+search.getInput()+"%");
         	htmlHeader( out);
         	toolBarStart(out);
         	toolBarEnd(out);
         	container(out);
 		   out.println("<p>Search : " + search.getInput() + " (" + books.size() +" Results Found)</p>");
 		    out.println("");
 		    out.println("<table style=\"border-collapse: separate; border-spacing: 50px;\" class=\"sortable\">");
 		    out.println("<thead>");
 		    out.println("<tr>");
 		    out.println("<th>	Title	</th>");
 		    out.println("<th>	Author	</th>");
 		    out.println("<th>	Price	</th>");
 		    out.println("<th>	Category	</th>");
 		    out.println("<th>	Image	</th>");
 		    out.println("<th></th>");
 		    out.println("</tr>");
 		    out.println("</thead>");
 		    out.println("<tbody>");
 		    for (int i=0; i<books.size(); i++)
 		    {
 		    	boolean inCart = false;
 		    	for (Book a : loggedInUser.getCart())
 		    	{
 		    		if (a.getId()==books.get(i).getId())
 		    		{
 		    			inCart = true;
 		    		}
 		    	}
 		    	
 		    	boolean prevPurchased = false;
 		    	for (Book a : loggedInUser.getPurchases())
 		    	{
 		    		if (a.getId()==books.get(i).getId())
 		    		{
 		    			prevPurchased = true;
 		    		}
 		    	}
 		    	out.println("<tr>");
 		    	out.println("<td>" + books.get(i).getTitle() + "</td>");
 		    	out.println("<td>"+ books.get(i).getAuthor()+"</td>");
 		    	out.println("<td>"+ books.get(i).getPrice()+"</td>");
 		    	out.println("<td>"+ books.get(i).getCategory()+"</td>");
 		    	out.println("<td><img src=\"/" + books.get(i).getImage()+ "\" style=\"width:64px;height:64px;\"></td>");
 		    	if (inCart == true)
 		    	{
 		    		out.println("<td><a href=\"cart\">View in Cart</a></td>");
 		    	}
 		    	else if (prevPurchased)
 		    	{
 		    		out.println("<td><a href=\"myPurchases\">View in My Purchases</a></td>");
 		    	}
 		    	else if (books.get(i).getStock() > 0)
 		    	{
 		    		out.println("<td><a href=\"addToCart/" + books.get(i).getId() + "\">Add to Cart</a></td>");
 		    	}
 		    	else
 		    	{
 		    		out.println("<td><font color=\"red\">OUT OF STOCK</font></td>");
 		    	}
 		    	
 		    	out.println("</tr>");
 		    }
 		    out.println("</tbody>");
 		    out.println("</table>");
 		    htmlFooter(out);
     }
    
    //Purchases
    @GetMapping("/cart")
    public void cart( HttpServletRequest request, HttpServletResponse response)throws IOException {
    	if (loggedInUser != null)
    	{
     	response.setContentType("text/html");
     	PrintWriter out = response.getWriter();
     	List<Book> cart = loggedInUser.getCart();
     	double total = 0;
     	
         	htmlHeader( out);
         	toolBarStart(out);
         	toolBarEnd(out);
         	container(out);
 		    out.println("");
 		    out.println("<table class=\"table\">");
 		    out.println("<thead>");
 		    out.println("<tr>");
 		    out.println("<th>Image</th>");
 		    out.println("<th>Title</th>");
 		    out.println("<th>Price</th>");
 		    out.println("<th></th>");
 		    out.println("</tr>");
 		    out.println("</thead>");
 		    out.println("<tbody>");
 		    for (int i=0; i<cart.size(); i++)
 		    {
 		    	total = total + Double.valueOf(cart.get(i).getPrice());
 		    	out.println("<tr>");
 		    	out.println("<td><img src=\"/" + cart.get(i).getImage()+ "\" style=\"width:64px;height:64px;\"></td>");
 		    	out.println("<td>" + cart.get(i).getTitle() + "</td>");
 		    	out.println("<td>"+ cart.get(i).getPrice()+"</td>");
 		    	out.println("<td><a href=\"removeFromCart/" + cart.get(i).getId() + "\">Delete</a></td>");
 		    	out.println("</tr>");
 		    }
 		    out.println("</tbody>");
 		    out.println("</table>");
 		    if (total > 0)
 		    {
 		    out.println("<center><p>Total : " + total +"</p></center>");
 		    out.println("<form action=\"http://localhost:8080/checkout\">");
		    out.println("<center><input type=\"submit\" class=\"btn btn-default\" value=\"Checkout\" /></center></form>");
 		    }
 		    htmlFooter(out);
    	}
    	else
    	{
    		response.sendRedirect("http://localhost:8080/login");
    	}
     }
    
    @GetMapping("/addToCart/{id}")
    @ResponseBody
    public void addToCart(@PathVariable(value = "id") int id, HttpServletRequest request, HttpServletResponse response)throws IOException {
    	Book book = bookService.findById(id).get(0);
    	loggedInUser.addToCart(book);
    	response.sendRedirect("http://localhost:8080/cart");
    }
    
    @GetMapping("/removeFromCart/{id}")
    @ResponseBody
    public void removeFromCart(@PathVariable(value = "id") int id, HttpServletRequest request, HttpServletResponse response)throws IOException {
    	Book book = bookService.findById(id).get(0);
    	loggedInUser.removeFromCart(book);
    	response.sendRedirect("http://localhost:8080/cart");
    }
    
  //Purchases
    @GetMapping("/checkout")
    public void checkout( HttpServletRequest request, HttpServletResponse response)throws IOException {
    	if (loggedInUser != null)
    	{
    		for (Book a : loggedInUser.getCart())
    		{
    			Book tempBook = bookService.findById(a.getId()).get(0);
    			tempBook.setStock(tempBook.getStock() -1);
    			bookService.save(tempBook);
    			User tempUser = userService.findByUsername(loggedInUser.getUsername()).get(0);
    		    tempUser.addPurchase(tempBook);
    		    userService.save(tempUser);
    		    loginCheck(loggedInUser);
    		}
    		response.sendRedirect("http://localhost:8080/myPurchases");
    	}
    	else
    	{
    		response.sendRedirect("http://localhost:8080/login");
    	}
     }
    
  //Purchases
    @GetMapping("/myPurchases")
    public void myPurchases( HttpServletRequest request, HttpServletResponse response)throws IOException {
    	if (loggedInUser != null)
    	{
     	response.setContentType("text/html");
     	PrintWriter out = response.getWriter();
     	List<Book> purchases = loggedInUser.getPurchases();
     	
         	htmlHeader( out);
         	toolBarStart(out);
         	toolBarEnd(out);
         	container(out);
 		    out.println("");
 		   out.println("<table class=\"table\">");
 		    out.println("<thead>");
 		    out.println("<tr>");
 		    out.println("<th>Image</th>");
 		    out.println("<th>Title</th>");
 		    out.println("<th>Price</th>");
 		    out.println("<th></th>");
 		    out.println("</tr>");
 		    out.println("</thead>");
 		    out.println("<tbody>");
 		    for (int i=0; i<purchases.size(); i++)
 		    {
 		    	boolean ratingLeft = false;
 		    	out.println("<tr>");
 		    	out.println("<td><img src=\"/" + purchases.get(i).getImage()+ "\" style=\"width:64px;height:64px;\"></td>");
 		    	out.println("<td>" + purchases.get(i).getTitle() + "</td>");
 		    	out.println("<td>"+ purchases.get(i).getPrice()+"</td>");
 		    	for (Rating r : purchases.get(i).getRatings())
 		    	{
 		    		if (r.getUserID()==loggedInUser.getId())
 		    		{
 		    			ratingLeft = true;
 		    		}
 		    	}
 		    	if (ratingLeft==true)
 		    	{
 		    		out.println("<td><a href=\"deleteRating/" + purchases.get(i).getId() + "\">Delete Rating</a></td>");
 		    	}
 		    	else
 		    	{
 		    		out.println("<td><a href=\"rating?id=" + purchases.get(i).getId() + "\">Leave a Rating</a></td>");
 		    	}
 		    	out.println("<td><a href=\"viewRatings/" + purchases.get(i).getId() + "\">View Ratings</a></td>");
 		    	out.println("</tr>");
 		    }
 		    out.println("</tbody>");
 		    out.println("</table>");
 		    htmlFooter(out);
    	}
    	else
    	{
    		response.sendRedirect("http://localhost:8080/login");
    	}
     }
    
    @GetMapping("/rating")
    public String rating(HttpServletRequest request, HttpServletResponse response, Rating rating)throws IOException {
    	if (loggedInUser == null)
    	{
            return "loginRegister";
    	}
    	else if (loggedInUser.isAdmin())
      	 {
      		 return "createRating";
      	 }
    	else
    	{
    		return "createRating";
    	}
    }
    
    @PostMapping("/createRating/{id}")
   public void createRating(@PathVariable(value = "id") int id, @Valid Rating rating, HttpServletRequest request, HttpServletResponse response)throws IOException {
    	Book tempBook = bookService.findById(id).get(0);
		rating.setUser(loggedInUser.getId());
		tempBook.addToRatings(rating);
		bookService.save(tempBook);
		loginCheck(loggedInUser);
    	response.sendRedirect("http://localhost:8080/myPurchases");
    }
    
    @GetMapping("/deleteRating/{id}")
    public void deleteRating(@PathVariable(value = "id") int id, HttpServletRequest request, HttpServletResponse response)throws IOException {
     	Book tempBook = bookService.findById(id).get(0);
     	Rating tempRating = null;
     			for (Rating r : tempBook.getRatings())
     			{
     				if (r.getUserID()==loggedInUser.getId())
     				{
     					tempRating = r;
     				}
     			}
     			tempBook.removeFromRatings(tempRating);
 		bookService.save(tempBook);
 		loginCheck(loggedInUser);
     	response.sendRedirect("http://localhost:8080/myPurchases");
     }
    
    @GetMapping("/viewRatings/{id}")
    public void viewRatings(@PathVariable(value = "id") int id, HttpServletRequest request, HttpServletResponse response)throws IOException {
    	if (loggedInUser != null)
    	{
     	response.setContentType("text/html");
     	PrintWriter out = response.getWriter();
     	Book book = bookService.findById(id).get(0);
     	
         	htmlHeader( out);
         	toolBarStart(out);
         	toolBarEnd(out);
         	container(out);
 		    out.println("");
 		    out.println("<table class=\"table\">");
 		    out.println("<thead>");
 		    out.println("<tr>");
 		    out.println("<th>Image</th>");
 		    out.println("<th>Title</th>");
 		    out.println("<th>Price</th>");
 		    out.println("<th></th>");
 		    out.println("</tr>");
 		    out.println("</thead>");
 		    out.println("<tbody>");
 		    out.println("<tr>");
 		    out.println("<td><img src=\"/" + book.getImage()+ "\" style=\"width:64px;height:64px;\"></td>");
 		    out.println("<td>" + book.getTitle() + "</td>");
 		    out.println("<td>"+ book.getPrice()+"</td>");
 		    out.println("</tr>");
 		    out.println("</tbody>");
 		    out.println("</table>");
 		    
 		   out.println("<p>Ratings for book : " + book.getTitle() + "</p>");
 		    
 		   out.println("<table class=\"table\">");
		    out.println("<thead>");
		    out.println("<tr>");
		    out.println("<th>User</th>");
		    out.println("<th>Score</th>");
		    out.println("<th>Comment</th>");
		    out.println("<th></th>");
		    out.println("</tr>");
		    out.println("</thead>");
		    out.println("<tbody>");
		    for (int i=0; i<book.getRatings().size(); i++)
		    {
		    	
		    	out.println("<tr>");
		    	out.println("<td>" + userService.findById(book.getRatings().get(i).getUserID()).get(0).getUsername() + "</td>");
		    	out.println("<td>"+ book.getRatings().get(i).getScore() +"</td>");
		    	out.println("<td>"+ book.getRatings().get(i).getComment() +"</td>");
		    }
		    out.println("</tbody>");
		    out.println("</table>");
 		    htmlFooter(out);
    	}
    	else
    	{
    		response.sendRedirect("http://localhost:8080/login");
    	}
     }
    
    @GetMapping("/viewBooks")
    public void viewBooks( HttpServletRequest request, HttpServletResponse response)throws IOException {
    	if (loggedInUser != null)
    	{
     	response.setContentType("text/html");
     	PrintWriter out = response.getWriter();
     	List<Book> stock = bookService.findAll();
     	
         	htmlHeader( out);
         	toolBarStart(out);
         	toolBarEnd(out);
         	container(out);
 		    out.println("");
 		    out.println("<table style=\"border-collapse: separate; border-spacing: 50px;\" class=\"sortable\">");
 		    out.println("<thead>");
 		    out.println("<tr>");
 		    out.println("<th>Image</th>");
 		    out.println("<th>Title</th>");
 		    out.println("<th>Price</th>");
 		    out.println("<th>Category</th>");
 		    out.println("<th>Stock</th>");
 		    out.println("<th></th>");
 		    out.println("</tr>");
 		    out.println("</thead>");
 		    out.println("<tbody>");
 		    for (int i=0; i<stock.size(); i++)
 		    {
 		    	out.println("<tr>");
 		    	out.println("<td><img src=\"/" + stock.get(i).getImage().getName()+ "\" style=\"width:64px;height:64px;\"></td>");
 		    	out.println("<td>" + stock.get(i).getTitle() + "</td>");
 		    	out.println("<td>"+ stock.get(i).getPrice()+"</td>");
 		    	out.println("<td>"+ stock.get(i).getCategory()+"</td>");
 		    	out.println("<td>"+ stock.get(i).getStock()+"</td>");
 		    	out.println("<td><a href=\"book/edit/" + stock.get(i).getId() + "\">Edit Stock</a></td>");
 		    	out.println("</tr>");
 		    }
 		    out.println("</tbody>");
 		    out.println("</table>");
 		    htmlFooter(out);
    	}
    	else
    	{
    		response.sendRedirect("http://localhost:8080/login");
    	}
     }
    
    @GetMapping("/editStock")
    public String editStock(HttpServletRequest request, HttpServletResponse response, Rating rating)throws IOException {
    	if (loggedInUser == null)
    	{
            return "loginRegister";
    	}
    	else if (loggedInUser.isAdmin())
      	 {
      		 return "editStock";
      	 }
    	else
    	{
    		return "editStock";
    	}
    }
    
    @RequestMapping(value= "/book/edit/{id}", method = RequestMethod.GET)
    public String editBook(@PathVariable("id") int id, ModelMap model ) {
      model.put("editBook", bookService.findById(id).get(0));
                return  "editStock";
      }
    
    @RequestMapping(value="/bookEdited",method=RequestMethod.POST)
    public String bookEdited(@ModelAttribute("editBook") Book editBook,BindingResult result, ModelMap model)
    {
    	if (result.hasErrors()) {
    	    return "welcome";}
    	System.out.println(editBook.getId());
    	Book temp = bookService.findById(editBook.getId()).get(0);
    	temp = editBook;
    	bookService.save(temp);
    
    	return "redirect:/viewBooks";
            }
    
    @GetMapping("/viewCustomers")
    public void viewCustomers( HttpServletRequest request, HttpServletResponse response)throws IOException {
    	if (loggedInUser != null)
    	{
     	response.setContentType("text/html");
     	PrintWriter out = response.getWriter();
     	List<User> users = userService.findAll();
     	
         	htmlHeader( out);
         	toolBarStart(out);
         	toolBarEnd(out);
         	container(out);
 		    out.println("");
 		    out.println("<table class=\"table\">");
 		    out.println("<thead>");
 		    out.println("<tr>");
 		    out.println("<th>User ID</th>");
 		    out.println("<th>Username</th>");
 		    out.println("<th>First Name</th>");
 		    out.println("<th>Last Name</th>");
 		    out.println("<th>Address</th>");
 		    out.println("<th>Account type</th>");	
 		    out.println("<th>Purchase History</th>");	
 		    out.println("<th></th>");
 		    out.println("</tr>");
 		    out.println("</thead>");
 		    out.println("<tbody>");
 		    for (int i=0; i<users.size(); i++)
 		    {
 		    	out.println("<tr>");
 		    	out.println("<td>" + users.get(i).getId()+ "</td>");
 		    	out.println("<td>" + users.get(i).getUsername() + "</td>");
 		    	out.println("<td>"+ users.get(i).getFirstName()+"</td>");
 		    	out.println("<td>"+ users.get(i).getLastName()+"</td>");
 		    	out.println("<td>"+ users.get(i).getAddress()+"</td>");
 		    	if (users.get(i).isAdmin())
 		    	{
 		    		out.println("<td>Admin</td>");
 		    	}
 		    	else
 		    	{
 		    		out.println("<td>Customer</td>");
 		    	}
 		    	out.println("<td><a href=\"user/history/" + users.get(i).getId() + "\">View Purchases</a></td>");
 		    	out.println("</tr>");
 		    }
 		    out.println("</tbody>");
 		    out.println("</table>");
 		    htmlFooter(out);
    	}
    	else
    	{
    		response.sendRedirect("http://localhost:8080/login");
    	}
     }
    
    @GetMapping("/user/history/{id}")
    public void viewPurchaseHistory(@PathVariable(value = "id") int id, HttpServletRequest request, HttpServletResponse response)throws IOException {
    	if (loggedInUser != null)
    	{
     	response.setContentType("text/html");
     	PrintWriter out = response.getWriter();
     	User user = userService.findById(id).get(0);
     	List<Book> purchases = user.getPurchases();
     	
         	htmlHeader( out);
         	toolBarStart(out);
         	toolBarEnd(out);
         	container(out);
 		    out.println("");
 		    
 		    out.println("<p>Purchase History for User : " + user.getUsername() + "</p>");
 		   
 		    out.println("<table class=\"table\">");
		    out.println("<thead>");
		    out.println("<tr>");
		    out.println("<th>Image</th>");
		    out.println("<th>Title</th>");
		    out.println("<th>Price</th>");
		    out.println("<th></th>");
		    out.println("</tr>");
		    out.println("</thead>");
		    out.println("<tbody>");
		    for (int i=0; i<purchases.size(); i++)
		    {
		    	out.println("<tr>");
		    	out.println("<td><img src=\"/" + purchases.get(i).getImage()+ "\" style=\"width:64px;height:64px;\"></td>");
		    	out.println("<td>" + purchases.get(i).getTitle() + "</td>");
		    	out.println("<td>"+ purchases.get(i).getPrice()+"</td>");
		    }
		    out.println("</tbody>");
		    out.println("</table>");
 		    htmlFooter(out);
    	}
    	else
    	{
    		response.sendRedirect("http://localhost:8080/login");
    	}
     }
    
	
	//HTML writer methods
     public void toolBarStart(PrintWriter out)
     {
    	 out.println("<nav class=\"navbar navbar-inverse\">");
    	 out.println("<div class=\"container-fluid\">");
    	 out.println("<ul class=\"nav navbar-nav\">");
    	 if (loggedInUser != null)
    	 {
    	 out.println("<li><a href=\"http://localhost:8080/logout\">Sign Out</a></li>");
    	 }
    	 else
    	 {
    		 out.println("<li><a href=\"http://localhost:8080/register\">Register</a></li>");
    		 out.println("<li><a href=\"http://localhost:8080/login\">Sign In</a></li>");
    	 }
    	 out.println("<li><a href=\"/\">Home</a></li>");
    	 out.println("<li><a href=\"http://localhost:8080/search\">Search</a></li>");
    	 out.println("<li><a href=\"http://localhost:8080/myPurchases\">My Purchases</a></li>");
    	 out.println("<li><a href=\"http://localhost:8080/cart\">My Cart</a></li>");
    	 out.println("</li>"); 
    	 if (loggedInUser != null && loggedInUser.isAdmin())
    	 {
    		 out.println("<li class=\"dropdown\">");
        	 out.println("<a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">Admin Tasks <span class=\"caret\"></span></a>");
        	 out.println("<ul class=\"dropdown-menu\">");
        	 out.println("<li><a href=\"http://localhost:8080/createBook\">Create Book</a></li>");
        	 out.println("<li><a href=\"http://localhost:8080/viewBooks\">View Books</a></li>");
        	 out.println("<li><a href=\"http://localhost:8080/viewCustomers\">View Customers</a></li>");
        	 out.println("</ul>");
        	 out.println("</li>");
    	 }
     }
     
     public void toolBarEnd(PrintWriter out)
     {
    	 out.println("</ul>");
    	 out.println("</div>");
    	 out.println("</nav>");
     }
     
     public void container(PrintWriter out)
     {
    	 out.println(" <div class=\"container\">");
		 out.println("<div class=\"jumbotron\">");
     }
     
     public void htmlHeader(PrintWriter out)
     {
	    	out.println("<!DOCTYPE html>");
		    out.println("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\"/>");
		    out.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>");
		    out.println("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>");
		    out.println("<script src=\"sorttable.js\"></script>");
		    out.println("<html xmlns:th=\"http://www.thymeleaf.org\">");
		    out.println("<body>");
     }
     
     public void htmlFooter(PrintWriter out)
     {
    	 out.println("<div>");
		 out.println("<div>");
		 out.println("</body>");
		 out.println("</html>");
     }
 	
 	//Javascript Methods
 	public void scriptStart(PrintWriter out)
 	{
 		out.println("<script>");
	    out.println("window.onload = function () {");
 	}
 	
 	public void scriptEnd(PrintWriter out)
 	{
 		out.println("}");
 		out.println("</script>");
	    out.println("<script type=\"text/javascript\" src=\"https://canvasjs.com/assets/script/jquery-1.11.1.min.js\"></script>");
	    out.println("<script type=\"text/javascript\" src=\"https://canvasjs.com/assets/script/jquery.canvasjs.min.js\"></script>");
 	}
}