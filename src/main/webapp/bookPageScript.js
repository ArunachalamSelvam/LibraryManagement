/**
 * Load book details from API based on bookId from the URL.
 */
document.addEventListener("DOMContentLoaded", function () {
    const borrowBtn = document.getElementById("borrow-btn");
    const bookStatus = document.getElementById("book-status");
    const bookTitle = document.getElementById("book-title");
    const bookAuthor = document.getElementById("book-author");
    const bookIsbn = document.getElementById("book-isbn");

    // Extract bookId from URL
    const urlParams = new URLSearchParams(window.location.search);
    const bookId = urlParams.get("bookId");
	console.log("bookId : " + bookId);
    if (!bookId) {
        alert("Book ID is missing!");
        return;
    }

    // Fetch book details
    function fetchBookDetails() {
        fetch(`http://localhost:8080/LibraryManagement/BookServlet?action=viewBook&bookId=${bookId}`)
            .then(response => response.json())
            .then(data => {
				console.log(data);
                bookTitle.textContent = data.bookName;
                bookAuthor.textContent = data.author;
                bookIsbn.textContent = data.isbnNo;
               // updateBookStatus(data.status);
            })
            .catch(error => {
                console.error("Error fetching book details:", error);
                alert("Failed to load book details.");
            });
    }

	// Function to update book status UI
	    function updateBookStatus(newStatus) {
	        bookStatus.textContent = newStatus;
	        if (newStatus === "AVAILABLE") {
	            bookStatus.classList.remove("borrowed");
	            bookStatus.classList.add("available");
	            borrowBtn.textContent = "Borrow";
	            borrowBtn.classList.remove("return-btn");
	        } else {
	            bookStatus.classList.remove("available");
	            bookStatus.classList.add("borrowed");
	            borrowBtn.textContent = "Return";
	            borrowBtn.classList.add("return-btn");
	        }
	    }

	    // Handle borrow/return button click
	    borrowBtn.addEventListener("click", function () {
	        fetch(`http://localhost:8080/LibraryManagement/BookServlet?action=updateBookStatus&bookId=${bookId}`, {
	            method: "POST"
	        })
	        .then(response => response.json())
	        .then(data => {
	            if (data.isUpdated) {
	                updateBookStatus(data.status);
	            } else {
	                alert("Failed to update book status.");
	            }
	        })
	        .catch(error => {
	            console.error("Error updating book status:", error);
	        });
	    });

    
	
	
	//fetchUserLoginDetails();
    fetchBookDetails();
});
