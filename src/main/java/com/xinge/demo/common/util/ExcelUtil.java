package com.xinge.demo.common.util;

import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelUtil {

    private static final Log log = LogFactory.getLog(ExcelUtil.class);

    /**
     * 导入excle,兼容xls和xlsx俩种格式
     *
     * @param file
     * @param titles
     * @return
     */
    public static List<Map<String, String>> importExcel(File file, String[] titles, int sheetIndex) {
        List<Map<String, String>> res = new ArrayList<>();
        try {
            if (null != file && file.exists()) {
                org.apache.poi.ss.usermodel.Workbook book = WorkbookFactory.create(file);
                org.apache.poi.ss.usermodel.Sheet sheet = book.getSheetAt(sheetIndex);

                // 获取列数、行数
                int rowNum = sheet.getLastRowNum();
                for (int i = 1; i < rowNum + 1; i++) {
                    Row row = sheet.getRow(i);
                    // 非空行
                    if (row.getPhysicalNumberOfCells() > 0) {
                        Map<String, String> map = new HashMap<>();
                        for (int j = 0; j < titles.length; j++) {
                            Cell cell = row.getCell(j);

                            String key = titles[j];
                            String value = "";
                            if (cell != null) {
                                cell.setCellType(Cell.CELL_TYPE_STRING);
                                value = cell.getStringCellValue() == null ? "" : cell.getStringCellValue();
                            }
                            map.put(key, value);
                        }
                        res.add(map);
                    }
                }

            }
        } catch (Exception e) {
            log.info("excel导入数据出错.", e);
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param os        文件输出流
     * @param list      页面查询返回的结果集
     * @param headerMap table表格的headItems
     * @param sheetName excel中的sheet名称
     * @author dell
     */
    public static void export(OutputStream os, List<Map<String, Object>> list, Map<String, String> headerMap, String sheetName) {
        try {
            WritableWorkbook wbook = Workbook.createWorkbook(os);

            double maxSheetSize = 60000.0;
            int sheetNum = 1;// 默认为1,防止list为空时导出空excel后台报异常
            if (CollectionUtils.isNotEmpty(list)) {
                sheetNum = (int) Math.ceil(list.size() / maxSheetSize);
            }
            for (int num = 0; num < sheetNum; num++) {
                WritableSheet wsheet = wbook.createSheet(sheetName + "(" + (num + 1) + ")", num);
                wsheet.getSettings().setShowGridLines(false);
                wsheet.setColumnView(0, 2);// 设置第一列宽度

                Set<String> set = headerMap.keySet();
                String[] titles = set.toArray(new String[set.size()]);

                for (int i = 0; i < titles.length; i++) {
                    wsheet.setColumnView(i + 1, 20);// 设置列宽
                }
                // 循环写入表头内容
                for (int i = 0; i < titles.length; i++) {
                    String title = titles[i];
                    wsheet.addCell(new Label(i + 1, 1, title, ExcelUtil.getHeaderCellStyle()));
                }

                // 写入数据
                for (int i = num * (int) maxSheetSize; i < (num + 1) * (int) maxSheetSize && i < list.size(); i++) {
                    Map<String, Object> m = list.get(i);
                    int temp = i - num * (int) maxSheetSize + 2;
                    for (int j = 0; j < titles.length; j++) {
                        Object objValue = m.get(headerMap.get(titles[j]));
                        if (objValue == null) {
                            objValue = "";
                        }
                        String value = objValue.toString();
                        wsheet.addCell(new Label(j + 1, temp, value, ExcelUtil.getBodyCellStyle()));
                    }
                }
            }

            wbook.write();
            wbook.close();
            os.flush();
            os.close();
            os = null;

        } catch (Exception e) {
            log.info("exception heppended in " + ExcelUtil.class + " cause: ", e);
        }
    }

    /***
     * 解析格式为xls的excel文件
     * @author liaohui
     * @param file
     * @param titles
     * @return
     */
    public static List<Map<String, String>> importExcelXls(File file, String[] titles) {
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();

        try {
            if (null != file && file.exists()) {
                Workbook book = Workbook.getWorkbook(file);
                Sheet[] sheets = book.getSheets();
                if (sheets.length > 0) {
                    Sheet sheet = sheets[0];
                    // 获取列数、行数
                    int colNum = sheet.getColumns();
                    int rowNum = sheet.getRows();
                    if (colNum == titles.length) {
                        // 从第二行获取表格内容
                        for (int i = 1; i < rowNum; i++) {
                            Map<String, String> map = new HashMap<String, String>();

                            for (int j = 0; j < colNum; j++) {
                                String key = titles[j];
                                String value = sheet.getCell(j, i).getContents();

                                map.put(key, value);
                            }
                            res.add(map);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /***
     * 解析格式为xlsx的excel文件
     * @author liaohui
     * @param file
     * @param titles
     * @return
     */
    public static List<Map<String, String>> importExcelXlsx(File file, String[] titles) {
        List<Map<String, String>> res = new ArrayList<Map<String, String>>();

        try {
            if (null != file && file.exists()) {
                XSSFWorkbook xwb = new XSSFWorkbook(new FileInputStream(file));
                XSSFSheet sheet = xwb.getSheetAt(0);
                if (null != sheet) {
                    XSSFRow row;

                    int rowNum = sheet.getPhysicalNumberOfRows();
                    for (int i = 1; i < rowNum; i++) {
                        row = sheet.getRow(i);
                        int colNum = row.getPhysicalNumberOfCells();
                        if (colNum == titles.length) {
                            Map<String, String> map = new HashMap<String, String>();

                            for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                                String key = titles[j];
                                String value = "";

                                if (Cell.CELL_TYPE_NUMERIC == row.getCell(j).getCellType()) {
                                    value = String.valueOf(new Double(row.getCell(j).getNumericCellValue()).intValue());
                                } else {
                                    value = row.getCell(j).getStringCellValue();
                                }

                                map.put(key, value);
                            }

                            res.add(map);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static WritableCellFormat getHeaderCellStyle() {
        WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat headerFormat = new WritableCellFormat(NumberFormats.TEXT);
        try {
            // 添加字体设置
            headerFormat.setFont(font);
            // 设置单元格背景色：表头为 灰色
            headerFormat.setBackground(Colour.GRAY_25);
            // 设置表头表格边框样式
            // 整个表格线为粗线、黑色
            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
            // 表头内容水平居中显示
            headerFormat.setAlignment(Alignment.CENTRE);
        } catch (WriteException e) {
            System.out.println("表头单元格样式设置失败！");
        }
        return headerFormat;
    }

    public static WritableCellFormat getBodyCellStyle() {
        WritableFont font = new WritableFont(WritableFont.createFont("宋体"), 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat bodyFormat = new WritableCellFormat(font);
        try {
            // 设置单元格背景色：表体为白色
            bodyFormat.setBackground(Colour.WHITE);
            // 设置表头表格边框样式
            // 整个表格线为细线、黑色
            bodyFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
        } catch (WriteException e) {
            System.out.println("表体单元格样式设置失败！");
        }
        return bodyFormat;
    }

    public static void main(String[] a) {
        File file = new File("e:\\11\\11.xlsx");
        String[] titles = new String[]{"col1", "col2", "col3"};
        List<Map<String, String>> list = ExcelUtil.importExcelXlsx(file, titles);
        for (Map<String, String> map : list) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
        }
    }

}
