package com.bridgelabz.employeemanagement.util;

import com.bridgelabz.employeemanagement.exception.FileProcessingException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelValidator {

    private static final List<String> REQUIRED_HEADERS =
            List.of(
                    "Employee ID",
                    "Name",
                    "Email",
                    "Department",
                    "Salary"
            );

    public void validateHeaders(MultipartFile file) {

        try (Workbook workbook =
                     new XSSFWorkbook(
                             file.getInputStream())) {

            Sheet sheet =
                    workbook.getSheetAt(0);

            Row headerRow =
                    sheet.getRow(0);

            if (headerRow == null) {

                throw new FileProcessingException(
                        "Excel header row is missing"
                );
            }

            for (int i = 0;
                 i < REQUIRED_HEADERS.size();
                 i++) {

                String expectedHeader =
                        REQUIRED_HEADERS.get(i);

                String actualHeader =
                        headerRow
                                .getCell(i)
                                .getStringCellValue()
                                .trim();

                if (!expectedHeader
                        .equalsIgnoreCase(actualHeader)) {

                    throw new FileProcessingException(
                            "Invalid Excel format. Expected column: "
                                    + expectedHeader
                    );
                }
            }

        } catch (IOException exception) {

            throw new FileProcessingException(
                    "Unable to read Excel file",
                    exception
            );
        }
    }

    public int countRecords(String filePath) {

        try (
                FileInputStream inputStream =
                        new FileInputStream(filePath);

                Workbook workbook =
                        new XSSFWorkbook(inputStream)
        ) {

            Sheet sheet =
                    workbook.getSheetAt(0);

            return Math.max(
                    sheet.getPhysicalNumberOfRows() - 1,
                    0
            );

        } catch (IOException exception) {

            throw new FileProcessingException(
                    "Unable to count Excel records",
                    exception
            );
        }
    }
}