package com.springboot.generateExcelPDF.service;

import com.springboot.generateExcelPDF.entity.Employee;
import com.springboot.generateExcelPDF.repository.EmployeeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public byte[] generatePDFBytes() throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                List<Employee> data = employeeRepository.findAll(); // Fetch data from the database

                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - (2 * margin);
                float yPosition = yStart;
                float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);

                int rowsPerPage = 30;
                int rowCount = 0;

                // Draw table headers
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("Name");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Address");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("City");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Mobile");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Mail");
                contentStream.endText();

                yPosition -= 20;

                // Draw table rows
                for (Employee emp : data) {
                    if (rowCount == rowsPerPage) {
                        contentStream.close();
                        PDPage newPage = new PDPage(PDRectangle.A4);
                        document.addPage(newPage);

//                        contentStream = new PDPageContentStream(document, newPage);
                        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                        yStartNewPage = newPage.getMediaBox().getHeight() - (2 * margin);
                        yPosition = yStartNewPage;
                        rowCount = 0;

                        // Draw table headers on each new page
                        contentStream.beginText();
                        contentStream.newLineAtOffset(margin, yPosition);
                        contentStream.showText("Name");
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("Address");
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("City");
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("Mobile");
                        contentStream.newLineAtOffset(100, 0);
                        contentStream.showText("Mail");
                        contentStream.endText();

                        yPosition -= 20;
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(emp.getName());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(emp.getAddress());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(emp.getCity());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(String.valueOf(emp.getMobile()));
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(emp.getMail());
                    contentStream.endText();

                    yPosition -= 20;
                    rowCount++;
                }

                contentStream.close();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                document.save(baos);
                return baos.toByteArray();
            }
        }
    }


    public byte[] generateExcelBytes() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employee Sheet");

        List<Employee> data = employeeRepository.findAll(); // Fetch data from the database

        // Create headers row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Name", "Email", "Address", "City", "Mobile"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Create data rows
        int rowNum = 1;
        for (Employee emp : data) {
            Row row = sheet.createRow(rowNum++);
            Cell nameCell = row.createCell(0);
            nameCell.setCellValue(emp.getName());

            Cell mailCell = row.createCell(1);
            mailCell.setCellValue(emp.getMail());

            Cell phoneCell = row.createCell(2);
            phoneCell.setCellValue(emp.getAddress());

            Cell cityCell = row.createCell(3);
            cityCell.setCellValue(emp.getCity());

            Cell mobileCell = row.createCell(4);
            mobileCell.setCellValue(emp.getMobile());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);

        return baos.toByteArray();
    }

}
