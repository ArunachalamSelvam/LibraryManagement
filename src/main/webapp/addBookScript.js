document.getElementById("bookForm").addEventListener("submit", async function(event) {
    event.preventDefault();

    const book = {
        isbnNo: document.getElementById("isbn").value,
        bookName: document.getElementById("bookName").value,
        author: document.getElementById("author").value,
        status: "AVAILABLE" // Default status
    };

    try {
        // Send request to add book
        const response = await fetch("http://localhost:8080/LibraryManagement/BookServlet?action=addBook", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(book)
        });

        if (!response.ok) throw new Error("Failed to add book");

        const addedBook = await response.json();
        alert("Book added successfully!");

        // Generate QR Code for added book
        fetchQRCode(addedBook);
    } catch (error) {
        console.error("Error:", error);
        alert("Failed to add book!");
    }
});

async function fetchQRCode(book) {
    try {
        const response = await fetch("http://localhost:8080/LibraryManagement/generateQRCode", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(book)
        });

        if (!response.ok) throw new Error("Failed to generate QR code");

        // Convert Response to Blob and Display Image
        const blob = await response.blob();
        const qrCodeUrl = URL.createObjectURL(blob);
        document.getElementById("qrCode").src = qrCodeUrl;
        document.getElementById("qrCode").style.display = "block";

        // Set Download Button
        const downloadBtn = document.getElementById("downloadBtn");
        downloadBtn.href = qrCodeUrl;
        downloadBtn.style.display = "block";
    } catch (error) {
        console.error("Error:", error);
        alert("Failed to generate QR code!");
    }
}
