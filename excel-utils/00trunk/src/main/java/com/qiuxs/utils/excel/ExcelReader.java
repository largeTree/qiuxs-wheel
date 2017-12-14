package com.qiuxs.utils.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Excel读取器
 *
 */
public class ExcelReader {

	private Workbook workBook;

	public ExcelReader(String path) throws InvalidFormatException, FileNotFoundException, IOException {
		File excelFile = new File(path);
		workBook = WorkbookFactory.create(new FileInputStream(excelFile));
	}

	/**
	 * 读取excel 返回sheet列表
	 * 	sheet的第一行用做map的Key
	 * 
	 * [
	 * 		[
	 * 			"行"
	 * 		]
	 * ]
	 * 
	 * @return
	 */
	public List<List<Map<String, String>>> toMap() {
		int sheetCount = workBook.getNumberOfSheets();
		List<List<Map<String, String>>> ls_sheets = new ArrayList<>(sheetCount);
		if (sheetCount == 0) {
			return ls_sheets;
		}
		for (int i = 0; i < sheetCount; i++) {
			Sheet sheet = workBook.getSheetAt(i);
			int rowCount = sheet.getLastRowNum() + 1;
			Row titleRow = sheet.getRow(0);
			if (titleRow == null) {
				continue;
			}
			int cols = titleRow.getPhysicalNumberOfCells();
			// 读取标题行 
			List<String> title = new ArrayList<String>();
			for (int j = 0; j < cols; j++) {
				title.add(titleRow.getCell(j).getStringCellValue());
			}
			List<Map<String, String>> ls_sheet = new ArrayList<Map<String, String>>();
			// 从第一行开始读取数据
			for (int j = 1; j < rowCount; j++) {
				Row r = sheet.getRow(j);
				Map<String, String> map_row = new HashMap<>();
				for (int z = 0; z < cols; z++) {
					map_row.put(title.get(z), getCellString(r.getCell(z)));
				}
				ls_sheet.add(map_row);
			}
			ls_sheets.add(ls_sheet);
		}
		return ls_sheets;
	}

	private String getCellString(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_ERROR:
			return String.valueOf(cell.getErrorCellValue());
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case Cell.CELL_TYPE_NUMERIC:
			return String.valueOf(cell.getNumericCellValue());
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		default:
			return "";
		}
	}

}
