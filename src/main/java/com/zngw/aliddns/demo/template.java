package com.zngw.aliddns.demo;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.spire.xls.FileFormat;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class template {
    public static void main(String[] args) throws IOException {
        exportExcel();
        totalExcelToPDF("C:/Users/huangchengliu/Desktop/工作簿2.xlsx", "C:/Users/huangchengliu/Desktop/工作簿2.pdf");
    }

    private static void exportExcel() throws IOException {
        Map<Integer, List<Map<String, Object>>> resultMap = new TreeMap<>();
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("number", "1234567890");

        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        int id = 0;
        for (int i2 = 0; i2 < 5; i2++) {
            id++;
            Map<String, String> lm = new TreeMap<String, String>();
            lm.put("id", id + "");
            lm.put("item1", "商品" + id + "-1");
            lm.put("item2", "商品" + id + "-2");
            lm.put("item3", null);
            lm.put("item5", "商品" + id + "-5");
            lm.put("item6", "商品" + id + "-6");
            lm.put("item7", "商品" + id + "-7");
            lm.put("item8", "商品" + id + "-8");
            lm.put("item17", "商品" + id + "-17");
            listMap.add(lm);
        }
        map.put("maplist", listMap);
        resultList.add(map);

        List<Map<String, Object>> reverseResultList = new ArrayList<>();
        for (int i = resultList.size() - 1; i >= 0; i--) {
            reverseResultList.add(resultList.get(i));
        }

        resultMap.put(0, reverseResultList);

        // 注意这里传递的是 resultList 而不是 map
        TemplateExportParams params = new TemplateExportParams("C:/Users/huangchengliu/Desktop/工作簿1.xlsx");
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        workbook.setSheetName(0, "Sheet0");

        FileOutputStream fos = new FileOutputStream("C:/Users/huangchengliu/Desktop/工作簿2.xlsx");
        workbook.write(fos);
        fos.close();
        workbook.close();
    }

    /**

     * 使用spire简单整个Excel转换为pdf

     *

     * @param inputFilePath Excel文件路径

     * @param outputFilePath 导出的PDF文件路径

     */

    public static void totalExcelToPDF(String inputFilePath, String outputFilePath) {

        com.spire.xls.Workbook wb = new com.spire.xls.Workbook();

//        引入Excel文件

        wb.loadFromFile(inputFilePath);

//        导出PDF文件

        wb.saveToFile(outputFilePath, FileFormat.PDF);

    }

}
