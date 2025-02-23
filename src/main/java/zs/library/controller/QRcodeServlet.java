package zs.library.controller;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import zs.library.model.Book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

// Servlet URL Mapping
@WebServlet("/generateQRCode")
public class QRcodeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final ObjectMapper objectMapper = new ObjectMapper();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Read JSON from request
        Book book = objectMapper.readValue(request.getReader(), Book.class);

        if (book == null || book.getBookId() == 0 || book.getBookName() == null || book.getIsbnNo() == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book data");
            return;
        }

        // Data to encode in QR Code
//        String qrData = "Book ID: " + book.getBookId() + "\n" +
//                        "Book Name: " + book.getBookName() + "\n" +
//                        "Author: " + book.getAuthor() + "\n" +
//                        "ISBN: " + book.getIsbnNo();
       String qrData = "http://localhost:8080/LibraryManagement/LoginBookPage.html?bookId="+book.getBookId();

        int size = 300; // QR code size
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, size, size);
            response.setContentType("image/png");

            OutputStream outputStream = response.getOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            outputStream.close();
        } catch (WriterException e) {
            throw new ServletException("Error generating QR Code", e);
        }
    }
}

