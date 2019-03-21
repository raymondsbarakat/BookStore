# e-men

***************************************************************************************************************************************************
***************************************************************************************************************************************************

Thursday, March 21, 2019:

FIXES:

- Search is now case-insensitive



***************************************************************************************************************************************************
***************************************************************************************************************************************************

Wednesday, March 20, 2019:

FIXES:

- Changed the return type of BookDAO to list from maps. We likely won't need maps, and sorting doesn't work with maps.
- Sorting didn't work when the books were listed by the "List Books" button or a category button. Fixed this issue

ADDITIONS:
- Sorting
	- Sort by price ascending
	- Sort by price descending
	- Sort by year ascending
	- Sort by year descending
	- Sort by review descending

- Password confirmation on signup
	- Asks you to enter password twice, gives error if they don't match

***************************************************************************************************************************************************
***************************************************************************************************************************************************

Tuesday, March 19, 2019:

FIXES:

- The database issue where added tables were not found.
- ***if this happens, choose 'Upgrade Database to Current Version' under your database properties, and change the url in your context.xml accordingly***

ADDITIONS:

- BookBean, BookDOA, corresponding methods inside model
- Updated the BookBean with fields of description, publishYear, and review.
- Updated the SQL tables with above (saved in DBCreate.sql)
- Added a header to all pages. The header shows signup and login if user hasn't signed in, cart and checkout if user has signed in
	- Header has a 'Specials' button I haven't implemented yet. We can add a sale feature and show only the books that are on sale when this button is clicked
- Added a search bar.
	- You can do a store-wide search, which will be useful. However this is case sensitive so it will need to be fixed.
	- The search checks for title, author, category. If there's anything else, we can just edit the retrieveBySearch query
- Added a singleBook.jspx, but only added a header so far. 
- Added a "sort by" dropdown menu, but haven't implemented it yet. In the servlet we can see what the user chose to sort by, but it doesn't return anything yet.
	- My thought was to sort the 'books' map and display it again, but sorting this map by a field of its value is harder than I thought it would be. Let's discuss this.
- Added a signout button and implemented it

***************************************************************************************************************************************************
***************************************************************************************************************************************************

Monday, March 18, 2018:

FIXES:

- addUser query was fixed: changed the PreparedStatement declaration to Statement. We need a Statement object when adding/updating items to tables.

- other queries were fixed: Connection, PreparedStatement and ResultSet objects were not closed after queries. Added closing statements.
without the closing statements website kept locking

- there was an error on login error checking. 2 queries were called from the same method (checkUserExists and passwordValidation).
apparently there can only be one ResultSet open at a time. one of these methods locked the other one which kept giving SQl errors.
instead of using a ResultSet, called the next() method directly on p.executeQuery() on checkUserExists, this fixed the issue.

ADDITIONS:

- added error checking for signUp
- added updatePassword method to model

