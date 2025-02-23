
const urlParams = new URLSearchParams(window.location.search);
   const bookId = urlParams.get("bookId");
   
   console.log("bookId : " + bookId);

   if (!bookId) {
       alert("Book ID is missing!");
       
	   }
	   
	function fetchUserLoginDetails(){
	
	
		window.location.href = "https://accounts.zoho.com/oauth/v2/auth"
		        + "?response_type=code"
		        + "&client_id=1000.88UX8X1EAZZPQVR5F9T2MZUCWOBK8U"
		        + "&scope=email"
		        + "&redirect_uri=http://localhost:8080/LibraryManagement/AuthServlet"
				+"&state="+bookId;
	}
	
	fetchUserLoginDetails();