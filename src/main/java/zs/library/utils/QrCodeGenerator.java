package zs.library.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import zs.library.model.Book;

public class QrCodeGenerator {
	public BitMatrix generateQRCode(Book book) throws IOException, WriterException {
        String qrCodeText = "Book ID: " + book.getBookId() + 
                            "\nTitle: " + book.getBookName() + 
                            "\nAuthor: " + book.getAuthor() + 
                            "\nISBN: " + book.getIsbnNo();

        String filePath = "D:/qrcodes/book_" + book.getBookId() + ".png";  // Change path as needed
        int width = 300;
        int height = 300;

        BitMatrix matrix = new MultiFormatWriter().encode(
                qrCodeText, BarcodeFormat.QR_CODE, width, height
        );

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
        System.out.println("QR Code generated: " + filePath);
        
        return matrix;
    }
}
