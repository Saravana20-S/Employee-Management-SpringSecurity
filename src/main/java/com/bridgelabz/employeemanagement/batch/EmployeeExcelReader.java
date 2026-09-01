package com.bridgelabz.employeemanagement.batch;

import com.bridgelabz.employeemanagement.exception.FileProcessingException;
import com.bridgelabz.employeemanagement.model.EmployeeExcelRow;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.ItemStreamReader;


import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;

public class EmployeeExcelReader
        implements ItemStreamReader<EmployeeExcelRow> {

    private final String filePath;

    private Workbook workbook;

    private Sheet sheet;

    private int currentRow;

    public EmployeeExcelReader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void open(ExecutionContext executionContext)
            throws ItemStreamException {

        try {

            FileInputStream inputStream =
                    new FileInputStream(filePath);

            workbook =
                    new XSSFWorkbook(inputStream);

            sheet =
                    workbook.getSheetAt(0);

            currentRow = 1;

        } catch (IOException exception) {

            throw new ItemStreamException(
                    "Unable to open Excel file",
                    exception
            );
        }
    }

    @Override
    public EmployeeExcelRow read() {

        if (sheet == null ||
                currentRow > sheet.getLastRowNum()) {

            return null;
        }

        Row row =
                sheet.getRow(currentRow);

        int rowNumber = currentRow + 1;

        currentRow++;

        if (row == null) {
            return read();
        }

        try {

            return EmployeeExcelRow.builder()
                    .rowNumber(rowNumber)
                    .employeeId(
                            getLongValue(
                                    row.getCell(0)
                            )
                    )
                    .name(
                            getStringValue(
                                    row.getCell(1)
                            )
                    )
                    .email(
                            getStringValue(
                                    row.getCell(2)
                            )
                    )
                    .department(
                            getStringValue(
                                    row.getCell(3)
                            )
                    )
                    .salary(
                            getBigDecimalValue(
                                    row.getCell(4)
                            )
                    )
                    .build();

        } catch (Exception exception) {

            throw new FileProcessingException(
                    "Error reading Excel row: "
                            + rowNumber,
                    exception
            );
        }
    }

    private String getStringValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        cell.setCellType(CellType.STRING);

        return cell.getStringCellValue()
                .trim();
    }

    private Long getLongValue(Cell cell) {

        if (cell == null) {
            return null;
        }

        return (long)
                cell.getNumericCellValue();
    }

    private BigDecimal getBigDecimalValue(
            Cell cell) {

        if (cell == null) {
            return null;
        }

        return BigDecimal.valueOf(
                cell.getNumericCellValue()
        );
    }

    @Override
    public void update(
            ExecutionContext executionContext) {

        executionContext.putInt(
                "currentRow",
                currentRow
        );
    }

    @Override
    public void close()
            throws ItemStreamException {

        try {

            if (workbook != null) {
                workbook.close();
            }

        } catch (IOException exception) {

            throw new ItemStreamException(
                    "Unable to close workbook",
                    exception
            );
        }
    }
}