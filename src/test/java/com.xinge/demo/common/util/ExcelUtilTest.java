package com.xinge.demo.common.util;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author duanxq
 * @date 2018/1/23
 */
public class ExcelUtilTest {

    @Test
    public void importExcelXlsx() {
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