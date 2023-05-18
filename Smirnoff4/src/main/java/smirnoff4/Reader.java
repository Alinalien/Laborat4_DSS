/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package smirnoff4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Alina
 */
public class Reader {
    
    public Reader() { 
    }
    
    public void ReadXLSX(Storage s, String fileName, int variant) throws FileNotFoundException, IOException, InvalidFormatException{
        ArrayList<ArrayList<Double>> samples = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        File file = new File(fileName);
        Workbook workbook = new XSSFWorkbook(file);
        Sheet worksheet = workbook.getSheetAt(variant-1);
        int cols = worksheet.getRow(0).getLastCellNum();
        for (int i = 0; i < cols; ++i){
            samples.add(new ArrayList<>());
        }
        Iterator<Row> ri = worksheet.rowIterator();
        XSSFRow row1 = (XSSFRow) ri.next();
        Iterator<Cell> ci1 = row1.cellIterator();
        while(ci1.hasNext()) {
            XSSFCell cell = (XSSFCell) ci1.next();
            name.add(cell.getStringCellValue());
        }
        
        while(ri.hasNext()) {
            XSSFRow row = (XSSFRow) ri.next();
            Iterator<Cell> ci = row.cellIterator();
            int i = 0;
            while(ci.hasNext()) {
                XSSFCell cell = (XSSFCell) ci.next();
                System.out.println(cell.getNumericCellValue());
                samples.get(i).add(cell.getNumericCellValue());
                i++;
            }                
        }        
        s.setName(name);
        s.setSamples(samples);       
    }
    
    private void AddNamesRow(Row row, ArrayList<String> nameSample){
        row.createCell(0).setCellValue("Параметры");
        int numbCell = 1;
        for (String s : nameSample){
            Cell nameTemp = row.createCell(numbCell++);
            nameTemp.setCellValue(s);        
        }
        
        
        
    }
    
    
    public void ExportXLSX(Storage s, String fileName) throws FileNotFoundException, IOException{
        FileOutputStream fous;
        fous = new FileOutputStream(fileName);
        Workbook wb = new HSSFWorkbook();
        Sheet sh = wb.createSheet("Результаты");
        String[] name = s.getNameParam();
        String[] name2 = s.getNameParam2();
        int names_length = name.length;
        int n = names_length + name2.length;
        ArrayList<ArrayList<Object>> results = s.getResult();
        
        if (results.size() <= 0){
            throw new IOException("Данные не выгружены, потому что они отсутствуют");
        }
        ArrayList<String> nameSample = s.getName();
        ArrayList<String> nameCorSample = s.getName2();

        int shift = 0;
        for (int i = 0; i < n; ++i){
            Row row = sh.createRow(i+shift);
            if (i == 0){
                AddNamesRow(row, nameSample);
                shift++;
                row = sh.createRow(i+shift);
            } 
            if (i == names_length){                
                AddNamesRow(row, nameCorSample);
                shift++;
                row = sh.createRow(i+shift);
            }
            
            int j = 0;
            
            Cell param = row.createCell(j++);
            if (i < names_length){
                param.setCellValue(name[i]);
            } else {
                param.setCellValue(name2[i-names_length]);
            }
            
            for (ArrayList<Object> r : results){
                Cell value = row.createCell(j++);
        
                value.setCellValue(((Number) r.get(i)).doubleValue());
            }
        }
        sh.autoSizeColumn(1);
        wb.write(fous);
        wb.close();   
    
    
    }
    
}
