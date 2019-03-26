package ctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;
import bean.*;


/**
 * Servlet implementation class Start
 */
@WebServlet("/Start")
public class Start extends HttpServlet {
	private static final long serialVersionUID = 1L;
	boolean loggedIn;
	boolean activeSearch;
	boolean activeFilter;

	String target;
	String query;
	boolean error;
	ArrayList <BookBean>books;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Start() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = getServletContext();
		
		loggedIn = Boolean.parseBoolean(this.getServletContext().getInitParameter("loggedIn"));
		activeSearch = Boolean.parseBoolean(this.getServletContext().getInitParameter("activeSearch"));
		activeFilter = Boolean.parseBoolean(this.getServletContext().getInitParameter("activeFilter"));

		target = "/Home.jspx";
		error = false;
		books = new ArrayList<BookBean>();
		
		try {
			context.setAttribute("myModel", new Model());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		/***************************************************************
			INITIALIZATION OF MODEL
		 ****************************************************************/
		Model myModel = (Model) this.getServletContext().getAttribute("myModel");

		/***************************************************************
			PAGE REDIRECTIONS
		 ****************************************************************/
		this.redirector(request, response, myModel);
		
		/***************************************************************
			SIGN UP
		 ****************************************************************/
		if (request.getParameter("signUpButton") != null) {
			this.signUp(request, response, myModel);
		}
		
		/***************************************************************
			LOGIN
		****************************************************************/
		if (request.getParameter("loginButton") != null) {
			this.logIn(request, response, myModel);
		}
		
		/***************************************************************
			SIGN OUT
	    ****************************************************************/
		if (request.getParameter("signoutButton") != null) {
			this.signOut(request, response);
		}
		
		/***************************************************************
			BOOK LISTINGS
		****************************************************************/
		
		// Calls method for listing all books
		if (request.getParameter("booksPageButton") != null) {
			this.listAllBooks(request, response, myModel);
		}
		
		// Calls method for listing books by category
		if (request.getParameter("headerCategory") != null) {
			this.listBooksByCategory(request, response, myModel);
		}
		
		/***************************************************************
			BOOK SORTINGS
		****************************************************************/
		if (request.getParameter("sortButton") != null) {
			this.sortBooks(request, response, myModel);	
		}
		
		/***************************************************************
			FILTER
		****************************************************************/
		if (request.getParameter("filterButton") != null) {
			this.filter(request, response, myModel);
		}
		
		/***************************************************************
			SEARCH BAR
		****************************************************************/
		if (request.getParameter("searchButton") != null) {
			this.searchStore(request, response, myModel);
		}
		
		
		/***************************************************************
			TESTING BLOCK
		 ****************************************************************/
		
		
		
		System.out.println(query);

		
		
		
		/***************************************************************
			TESTING BLOCK
		****************************************************************/
		
		request.getRequestDispatcher(target).forward(request, response);
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	protected void logIn(HttpServletRequest request, HttpServletResponse response, Model myModel) throws ServletException, IOException {

		this.activeSearch = false;
		this.activeFilter = false;
		
		String username = request.getParameter("loginName");
		String password = request.getParameter("loginPassword"); 
		
		// Sets errors, if any
		myModel.checkLoginError(username, password);

		if (!myModel.getErrorStatus()) {		// No errors, user is successfully logged in
			loggedIn = true;
			request.getSession().setAttribute("loggedInSession", loggedIn);
			request.getSession().setAttribute("loggedInUser", username);
			this.target = "/Home.jspx";

		}
		else {
			String loginErrorMessage = myModel.getErrorMessage();
			error = true;
			target = "/Login.jspx";
			request.setAttribute("error", loginErrorMessage);
		}
	}
	
	protected void signUp(HttpServletRequest request, HttpServletResponse response, Model myModel) throws ServletException, IOException {
		
		this.activeSearch = false;
		this.activeFilter = false;
		
		String username = request.getParameter("signUpName");
		String email = request.getParameter("signUpEmail");
		String password = request.getParameter("signUpPassword"); 
		String passwordConf = request.getParameter("signUpPasswordConf"); 

		// Sets errors, if any
		myModel.checkSignUpError(username, email, password, passwordConf);
		if (!myModel.getErrorStatus()) {
			myModel.addUser(username, email, password);
			loggedIn = true;
			request.getSession().setAttribute("loggedInSession", loggedIn);
			request.getSession().setAttribute("loggedInUser", username);
			this.target = "/Home.jspx";

		}else {
			String signUpErrorMessage = myModel.getErrorMessage();
			error = true;
			target = "/SignUp.jspx";
			request.setAttribute("error", signUpErrorMessage);
		}
	}
	
	protected void signOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.activeSearch = false;
		this.activeFilter = false;
		
		loggedIn = false;
		request.getSession().setAttribute("loggedInSession", loggedIn);
	}
	
	protected void redirector(HttpServletRequest request, HttpServletResponse response, Model myModel) throws ServletException, IOException {
		//checks if 'Home' button was pressed, sets target to the Sign Up page if true
		if (request.getParameter("homeButton") != null) {
			target = "/Home.jspx";
		}
		//checks if 'Sign Up' button was pressed, sets target to the Sign Up page if true
		if (request.getParameter("signUpPageButton") != null) {
			target = "/SignUp.jspx";
		}
		//checks if 'Login' button was pressed, sets target to the Login page if true
		if (request.getParameter("loginPageButton") != null) {
			target = "/Login.jspx";
		}
		//checks if 'List Books', 'Sort', or a search was submitted, sets target to the Login page if true
		if (request.getParameter("booksPageButton") != null || (request.getParameter("searchButton") != null) || (request.getParameter("sortButton") != null) || (request.getParameter("filterButton") != null) || (request.getParameter("headerCategory") != null)) {
			target = "/Books.jspx";

			//Retrieves unique categories and sets the filter display
			ArrayList <String>categories = new ArrayList<String>();
			categories = myModel.retrieveUniqueCategories();
			request.setAttribute("categoriesFilterList", categories);
		}
	}
	
	protected void listAllBooks(HttpServletRequest request, HttpServletResponse response, Model myModel) throws ServletException, IOException {
		
		this.activeSearch = false;
		this.activeFilter = false;

		query = "select * from BOOKS";
		request.getSession().setAttribute("query", query);
		books = myModel.retrieveByQuery(query);
		request.setAttribute("booksMap", books);
	}
	
	protected void listBooksByCategory(HttpServletRequest request, HttpServletResponse response, Model myModel) throws ServletException, IOException {
		
		this.activeSearch = false;
		this.activeFilter = false;
		
		String category = request.getParameter("headerCategory");
				
		query = "select * from BOOKS where category = '" + category + "'";
		request.getSession().setAttribute("query", query);
		books = myModel.retrieveByQuery(query);
		request.setAttribute("booksMap", books);
	}
	
	protected void sortBooks(HttpServletRequest request, HttpServletResponse response, Model myModel) throws ServletException, IOException {
		//obtains the sort option
		String sortOption = request.getParameter("sortOption");
		
		query = (String) request.getSession().getAttribute("query");
		
		if (sortOption.equals("Newest to Oldest")) {
			query += " order by publishYear desc";
		}
		else if (sortOption.equals("Oldest to Newest")) {
			query += " order by publishYear asc";
		}
		else if (sortOption.equals("Review")) {
			query += " order by review desc";
		}
		else if (sortOption.equals("Price - Low to High")) {
			query += " order by price asc";
		}
		else if (sortOption.equals("Price - High to Low")) {
			query += " order by price desc";
		}
		
		books = myModel.retrieveByQuery(query);
		request.setAttribute("booksMap", books);
		
	}
	
	protected void searchStore(HttpServletRequest request, HttpServletResponse response, Model myModel) throws ServletException, IOException {
		
		this.activeSearch = true;
		
		//obtains the searched term
		String searchTerm = request.getParameter("searchBar").toUpperCase();
		
		query = "select * from BOOKS where UPPER(title) like '%" + searchTerm + "%' or UPPER(author) like '%" + searchTerm + "%' or UPPER(category) like '%" + searchTerm + "%'";
		request.getSession().setAttribute("query", query);
		
		//does a store-wide search by with the retrieveBySearch query
		books = myModel.retrieveByQuery(query);
		request.setAttribute("booksMap", books);	
	}
	
	protected void filter(HttpServletRequest request, HttpServletResponse response, Model myModel) throws ServletException, IOException {		
			
		ArrayList<String> filterQueryList = new ArrayList<String>();
		Boolean filterSelected = false;
		
		// saves the state of the session query without any filters applied to it
		if (!activeFilter) {
			request.getSession().setAttribute("rawQuery", query);
		}
		
		query = (String) request.getSession().getAttribute("query");
		
		// checks if any filters were selected, if so, adds either 'and' or 'where' to the end of the existing query
		if (request.getParameter("categoryFilter") != null || request.getParameter("reviewFilter") != null || !request.getParameter("priceLowFilter").equals("") || !request.getParameter("priceHighFilter").equals("")) {
			filterSelected = true;
		}	
		
		// checks if a filter was selected
		if (filterSelected) {
			
			// obtains the selected category filter
			if (request.getParameter("categoryFilter") != null) {	
				String categoryFilter = request.getParameter("categoryFilter");
				String categoryFilterQuery = "category = '" + categoryFilter + "'";
				filterQueryList.add(categoryFilterQuery);
			}

			// obtains the selected review filter
			if (request.getParameter("reviewFilter") != null) {
			
				String reviewFilter = request.getParameter("reviewFilter");
				String reviewFilterQuery;
				
				if (reviewFilter.equals("above1")) {
					reviewFilterQuery = "review >= 1";
				}
				else if (reviewFilter.equals("above2")) {
					reviewFilterQuery = "review >= 2";
				}
				else if (reviewFilter.equals("above3")) {
					reviewFilterQuery = "review >= 3";
				}			
				else {
					reviewFilterQuery = "review >= 4";
				}
				filterQueryList.add(reviewFilterQuery);
			}
			
			// obtains the selected price range filter
			if (!request.getParameter("priceLowFilter").equals("") || !request.getParameter("priceHighFilter").equals("")) {
				
				String priceFilterQuery;	
				double priceLowFilter;
				double priceHighFilter;
				
				if (!request.getParameter("priceLowFilter").equals("") && !request.getParameter("priceHighFilter").equals("")) {
					priceLowFilter = Double.parseDouble(request.getParameter("priceLowFilter"));
					priceHighFilter = Double.parseDouble(request.getParameter("priceHighFilter"));
					priceFilterQuery = "price >= '" + priceLowFilter + "' and price <= '" + priceHighFilter + "'";
					System.out.println("HITS HERE");
				}
				else if (!request.getParameter("priceLowFilter").equals("") && request.getParameter("priceHighFilter").equals("")) {
					priceLowFilter = Double.parseDouble(request.getParameter("priceLowFilter"));
					priceFilterQuery = "price >= '" + priceLowFilter + "'";

				}
				else{
					priceHighFilter = Double.parseDouble(request.getParameter("priceHighFilter"));
					priceFilterQuery = "price <= '" + priceHighFilter + "'";
				}
				filterQueryList.add(priceFilterQuery);
			}
			
			
			if (activeFilter) {
				query = (String) request.getSession().getAttribute("rawQuery");
			}
			
			// if the session query already contains a 'where' clause, appends it with 'and'
			if (query.contains("where")) {
				query += " and ";
			}
			// if the session query does not contain a 'where' clause, appends it with 'where'
			else {
				query += " where ";
			}
			
			// if multiple filters were selected, adds 'and' terms in between
			if (filterQueryList.size() > 1) {
				int index = 1;
				while (index <= filterQueryList.size() - 1) {
					filterQueryList.add(index, " and ");
					index += 2;
				}
			}
			
			// iteratively adds the filterQuery items to the end of session query
			for (int i = 0; i < filterQueryList.size(); i++) {
				query += filterQueryList.get(i);
			}
			
			request.getSession().setAttribute("query", query);
			
			this.activeFilter = true;
					
		}
			

		System.out.println("ACTIVE FILTER: " + activeFilter);

		books = myModel.retrieveByQuery(query);
		request.setAttribute("booksMap", books);
	}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		
		
		
	

}
