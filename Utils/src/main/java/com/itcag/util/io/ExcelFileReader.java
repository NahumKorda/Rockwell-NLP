package com.itcag.util.io;

import java.io.File;
import java.io.FileInputStream;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * <p>This class opens an MS Excel file, parses a worksheet in it, and extracts data from it.</p>
 */
public class ExcelFileReader {
    
    private final Iterator<Row> rowIterator;
    
    /**
     * @param filePath String holding a local path to the MS Excel file.
     * @param worksheetName String holding the name of the worksheet that is to be parsed.
     * @throws Exception if anything goes wrong.
     */
    public ExcelFileReader(String filePath, String worksheetName) throws Exception {
        
        FileInputStream excelFile = new FileInputStream(new File(filePath));
        
        Workbook workbook = new XSSFWorkbook(excelFile);
        
        Sheet sheet = workbook.getSheet(worksheetName);
        if (sheet == null) throw new IllegalArgumentException("Unknown worksheet: " + worksheetName);
        
        rowIterator = sheet.iterator();
    
    }

    /**
     * @return Array list containing strings - each string representing a raw in the worksheet (cells are tab delimited).
     */
    public ArrayList<String> getData() {
        
        ArrayList<String> retVal = new ArrayList<>();
        
        while (rowIterator.hasNext()) {

            Row currentRow = rowIterator.next();
            Iterator<Cell> cellIterator = currentRow.iterator();

            StringBuilder tmp = new StringBuilder();
            
            int count = 0;
            
            while (cellIterator.hasNext()) {

                Cell currentCell = cellIterator.next();

                if (count > 0) {
                    tmp.append("\t");
                    int diff = currentCell.getColumnIndex() - count;
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            tmp.append("\t");
                        }
                        count = currentCell.getColumnIndex();
                    }
                }

                switch(currentCell.getCellType()) {
                    case STRING:
                        tmp.append(currentCell.getStringCellValue());
                        break;
                    case NUMERIC:
                        tmp.append(currentCell.getNumericCellValue());
                        break;
                    default:
                }
                
                count++;

            }
            
            retVal.add(tmp.toString());

        }

        return retVal;
        
    }
    
}
