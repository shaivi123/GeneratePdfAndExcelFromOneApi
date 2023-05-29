package com.springboot.generateExcelPDF.controller;


import com.springboot.generateExcelPDF.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/download/{type}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String type) throws IOException {
        byte[] fileBytes;
        String fileName;
        if ("pdf".equalsIgnoreCase(type)) {
            fileBytes = employeeService.generatePDFBytes();
            fileName = "example.pdf";

        } else if ("excel".equalsIgnoreCase(type)) {
            fileBytes = employeeService.generateExcelBytes();
            fileName = "example.xlsx";

        } else {
            // Handle invalid type
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData(fileName, fileName);
//        headers.setContentType(mediaType);

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }
}
