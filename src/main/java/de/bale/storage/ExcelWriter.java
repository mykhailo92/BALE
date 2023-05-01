package de.bale.storage;

import de.bale.Utils;
import de.bale.analyze.EyetrackingRow;
import de.bale.logger.Logger;
import de.bale.messages.ErrorMessage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelWriter {

    private String excelWorksheetName;
    private final XSSFWorkbook workbook;
    private String excelFilePath;

    public ExcelWriter(String excelName) {
        this.excelFilePath = Paths.get(Utils.getSettingsDir(), excelName + ".xlsx").toString();
        if (new File(excelFilePath).exists()) {
            try {
                this.workbook = new XSSFWorkbook(new FileInputStream(excelFilePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.workbook = new XSSFWorkbook();
        }
    }

    private void createCell(Row row, int columnIndex, String cellContent) {
        try {
            Cell cell = row.createCell(columnIndex);
            cell.setCellValue(cellContent);
        } catch (NullPointerException nullPointerException) {
            Logger.getInstance().post(new ErrorMessage("Error: Row is null while trying to add Cell Content: " + cellContent));
        }
    }

    private void createCell(Row row, int columnIndex, long cellContent) {
        try {
            Cell cell = row.createCell(columnIndex);
            cell.setCellValue(cellContent);
        }catch (NullPointerException nullPointerException) {
            Logger.getInstance().post(new ErrorMessage("Error: Row is null while trying to add Cell Content: " + cellContent));
        }
    }

    public void writeExcelSheet(int experimentID, String childName, List<EyetrackingRow> excelData,
                                int totalFixationDuration, int totalFixationCount, Map<String, List<Long>> viewTimeMap) {
        XSSFSheet excelSheet = null;
        if (workbook.getSheet(childName + experimentID) != null) {
            excelSheet = workbook.getSheet(childName + " " + experimentID);
        } else {
            excelSheet = workbook.createSheet(childName + " " + experimentID);
        }
        int rowCount = excelSheet.getLastRowNum();
        Row rowHeader = excelSheet.createRow(++rowCount);
        createCell(rowHeader, 0, "Screen Position X");
        createCell(rowHeader, 1, "Screen Position Y");
        createCell(rowHeader, 2, "Area of Interest");
        createCell(rowHeader, 3, "Fixation duration (ms)");

        int columnCount = 0;
        for (EyetrackingRow eyetrackingRow : excelData) {
            Row row = excelSheet.createRow(++rowCount);
            String aoi = eyetrackingRow.getAoi() == null ? "undefined" : eyetrackingRow.getAoi(); //In Case no defined AoI was looked at
            createCell(row, 0, eyetrackingRow.getPosX());
            createCell(row, 1, eyetrackingRow.getPosY());
            createCell(row, 2, eyetrackingRow.getAoi());
            createCell(row, 3, eyetrackingRow.getDuration());
        }

        addSummary(excelSheet, totalFixationDuration, totalFixationCount, viewTimeMap);

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSummary(XSSFSheet excelSheet, int totalFixationDuration, int totalFixationCount, Map<String, List<Long>> viewTimeMap) {
        Row summaryRowHeader = excelSheet.getRow(0);
        int summaryColumnIndex = summaryRowHeader.getLastCellNum() + 2;
        createCell(summaryRowHeader, summaryColumnIndex, "Area of Interest");
        createCell(summaryRowHeader, summaryColumnIndex + 1, "Total Fixation Count");
        createCell(summaryRowHeader, summaryColumnIndex + 2, "Total Fixation Duration");

        Row summaryRow = excelSheet.getRow(1);
        createCell(summaryRow, summaryColumnIndex, "Total");
        createCell(summaryRow, summaryColumnIndex + 1, totalFixationCount - 1);
        createCell(summaryRow, summaryColumnIndex + 2, totalFixationDuration);
        AtomicInteger rowIndex = new AtomicInteger(2);
        viewTimeMap.forEach((key, value) -> {
            Row aoiSummaryRow = excelSheet.getRow(rowIndex.get());
            String aoi = key == null ? "undefined" : key; //In Case no defined AoI was looked at
            long totalFixationDurationAoi = value.get(0);
            long totalFixationCountAoi = value.get(1);
            createCell(aoiSummaryRow, summaryColumnIndex, aoi);
            createCell(aoiSummaryRow, summaryColumnIndex + 1, totalFixationCountAoi);
            createCell(aoiSummaryRow, summaryColumnIndex + 2, totalFixationDurationAoi);
            rowIndex.addAndGet(1);
        });
    }
}
