package com.zngw.aliddns.demo;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public void exportJobCardExtendDetails() {

        // 表头定义 可以将表头配置在数据库中，然后在代码里动态生成表头
        // 这里只是展示如何用代码生成表头
        List<ExcelExportEntity> columnList = new ArrayList<ExcelExportEntity>();
        ExcelExportEntity colEntity1 = new ExcelExportEntity("序号", "id");
        colEntity1.setNeedMerge(true);//是否需要纵向合并单元格(用于含有list中,单个的单元格,合并list创建的多个row)
        columnList.add(colEntity1);

        ExcelExportEntity colEntity2 = new ExcelExportEntity("部门", "deptName");
        colEntity2.setNeedMerge(true);
        columnList.add(colEntity2);

        ExcelExportEntity colEntity3 = new ExcelExportEntity("姓名", "realName");
        colEntity3.setNeedMerge(true);
        columnList.add(colEntity3);

        ExcelExportEntity colEntity4 = new ExcelExportEntity("入职日期", "employmentTime");
        colEntity4.setNeedMerge(true);
        //导出时间设置,如果字段是Date类型则不需要设置 数据库如果是string 类型,这个需要设置这个数据库格式,用以转换时间格式输出
        colEntity4.setDatabaseFormat("yyyy-MM-dd");
        colEntity4.setFormat("yyyy-MM-dd");//时间格式,相当于同时设置了exportFormat 和 importFormat
        columnList.add(colEntity4);

        ExcelExportEntity evaluatePersonnel = new ExcelExportEntity("评定人员", "evaluatePersonnel");
        List<ExcelExportEntity> personnelExcelList = new ArrayList<ExcelExportEntity>();
        evaluatePersonnel.setMergeVertical(true);//纵向合并内容相同的单元格
        evaluatePersonnel.setMergeRely(new int[]{0});//合并单元格依赖关系,比如第二列合并是基于第一列 则{0}就可以了
        evaluatePersonnel.setList(personnelExcelList);
        columnList.add(evaluatePersonnel);

        ExcelExportEntity character = new ExcelExportEntity("性格力", "character");
        List<ExcelExportEntity> characterExcelList = new ArrayList<ExcelExportEntity>();
        characterExcelList.add(new ExcelExportEntity("考拉1", "kaola1"));
        characterExcelList.add(new ExcelExportEntity("考拉2", "kaola2"));
        character.setList(characterExcelList);
        columnList.add(character);

        ExcelExportEntity selfDriving = new ExcelExportEntity("自驱动能力", "selfDriving");
        selfDriving.setGroupName("自驱动能力");
        List<ExcelExportEntity> selfDrivingExcelList = new ArrayList<ExcelExportEntity>();
        //自驱动能力
        selfDrivingExcelList.add(new ExcelExportEntity("其他1", "other1"));
        selfDrivingExcelList.add(new ExcelExportEntity("其他2", "other2"));
        selfDriving.setList(selfDrivingExcelList);
        columnList.add(selfDriving);

        //固定数据
        List<Map<String, Object>> personnelList = new ArrayList<Map<String, Object>>();
        Map<String, Object> self = new HashMap<String, Object>();
        self.put("personnel", "自我评定");
        personnelList.add(self);
        Map<String, Object> leader1 = new HashMap<String, Object>();
        leader1.put("personnel", "部门领导评定");
        personnelList.add(leader1);
        Map<String, Object> leader2 = new HashMap<String, Object>();
        leader2.put("personnel", "部门领导评定");
        personnelList.add(leader2);

        // 数据拉取 一般需要从数据库中拉取
        // 这里是手动模拟数据
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
//            Map<String, Object> data = new HashMap<String, Object>();
//            data.put("id", i + 1);
//            data.put("deptName", "部门" + i);
//            data.put("realName", "姓名" + i);
//            data.put("employmentTime", "2020-01-01");
//            data.put("evaluatePersonnel", personnelList);
//            dataList.add(data);
        }

        // 定义标题和sheet名称
        ExportParams exportParams = new ExportParams("工卡信息表", "工卡信息表");
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, columnList, dataList);
        // 导入到本地目录，如果需要从浏览器导出，参看上一篇文章

        try {
            OutputStream outputStream = Files.newOutputStream(Paths.get("C:\\Users\\huangchengliu\\Desktop\\工卡信息表.xlsx"));
            //用流的方式传出
            workbook.write(outputStream);//导出数据
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.exportJobCardExtendDetails();
    }
}
