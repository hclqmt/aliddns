package com.zngw.aliddns;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadJarRemoteMavenRepo {
    //批量安装jar所在目录，本文已本地maven仓库为例
    public static String filePath = "C:\\Users\\huangchengliu\\.m2\\repository";
    public static List<GroupInfo> jarList=new ArrayList<>();
 
    public static void main(String[] args) {
        File f = new File(filePath);
        //得到所有jar包信息，包括groupId，artifactId，version等信息
        getJarList(f);
//        jarList.forEach(System.out::println);
        //循环上传本地maven仓库jar包到远程maven仓库
        uploadJarToRemoteMavenRepo();
    }
 
    /**
     *   <groupId>com.alibaba.cloud</groupId>
     *                 <artifactId>spring-cloud-alibaba-dependencies</artifactId>
     * @param file
     */
    private static void getJarList(File file) {
        if(file.isDirectory()){
            for (File listFile : file.listFiles()) {
                getJarList(listFile);
            }
        }else if(file.getName().endsWith(".jar")){
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.version=file.getParentFile().getName();
            groupInfo.artifactId=file.getParentFile().getParentFile().getName();
            groupInfo.groupId=getGroupId(file);
            groupInfo.file=file;
 
            //加判断，只找自己需要的jar包，不是所有的jar包都要上传到远程maven仓库，例如公共的jar包
//            if (groupInfo.groupId.contains("net")){
//                jarList.add(groupInfo);
//            }
            jarList.add(groupInfo);
        }else if(file.getName().endsWith(".pom")){
            if(ifPomDir(file.getParentFile())){
                GroupInfo groupInfo = new GroupInfo();
                groupInfo.version=file.getParentFile().getName();
                groupInfo.artifactId=file.getParentFile().getParentFile().getName();
                groupInfo.groupId=getGroupId(file);
                groupInfo.isPom=true;
                groupInfo.file=file;
                //加判断，只找自己需要的jar包的pom，不是所有的jar包都要上传到远程maven仓库，例如公共的jar包
                if (groupInfo.groupId.contains("net")){
                    jarList.add(groupInfo);
                }
            }
        }
    }
 
    private static boolean ifPomDir(File parentFile) {
        return !Arrays.stream(parentFile.listFiles()).anyMatch(f->f.getName().endsWith(".jar"));
    }
 
    private static String getGroupId(File file) {
        String replace = file.getAbsolutePath().replace(filePath, "");
        String [] array = replace.split("\\\\");
        int length = array.length;
        StringBuilder sb = new StringBuilder();
        for (int i=1; i<length-3;i++){
            if (i==1) sb.append(array[i]);
            sb.append("."+array[i]);
        }
//        String artifactId = file.getParentFile().getParentFile().getName();
//        return replace.substring(1,replace.indexOf(artifactId)-1).replaceAll("\\\\",".");
        return sb.toString();
    }
 
    /**
     * 功能：循环上传本地仓库jar包到指定的远程maven仓库
     */
    public static void uploadJarToRemoteMavenRepo() {
        //D:\git\ips-fj\libs>mvn install:install-file -Dfile=ips-data-core-0.0.1-SNAPSHOT.jar -DgroupId=boco -DartifactId=ips-data-core  -Dversion=0.0.1-SNAPSHOT  -Dpackaging=pom
        //mvn install:install-file -DgroupId=com.xxx -DartifactId=xxx -Dversion=1.1.1 -Dpackaging=pom -Dfile=xxx-1.1.1.pom
        jarList.forEach(jar->{
            System.out.println("导入jar到本地仓库"+jar);
            String sb = "mvn deploy:deploy-file -Dfile="+jar.file.getAbsolutePath()
                    +" -DgroupId="+jar.groupId
                    +" -DartifactId="+jar.artifactId
                    +" -Dversion="+jar.version
                    +" -Dpackaging="+(jar.isPom?"pom":"jar")
                    +" -Durl=http://192.168.44.155/nexus/content/groups/public/"
                    +" -DrepositoryId=hcl";
            System.out.println(sb);
            execCommandAndGetOutput(sb);
        });
    }
 
    public static void execCommandAndGetOutput(String comond) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("cmd.exe /c "+comond);
            // 输出结果，必须写在 waitFor 之前
            String outStr = getStreamStr(process.getInputStream());
            // 错误结果，必须写在 waitFor 之前
            String errStr = getStreamStr(process.getErrorStream());
            int exitValue = process.waitFor(); // 退出值 0 为正常，其他为异常
            System.out.println("exitValue: " + exitValue);
            System.out.println("outStr: " + outStr);
            System.out.println("errStr: " + errStr);
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
 
    public static String getStreamStr(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        return sb.toString();
    }
 
    static class GroupInfo {
        String artifactId;
        String version;
        String groupId;
        Boolean isPom = false;
        File file;
 
        @Override
        public String toString() {
            return "GroupInfo{" +
                    "artifactId='" + artifactId + '\'' +
                    ", version='" + version + '\'' +
                    ", groupId='" + groupId + '\'' +
                    ", filename='" + file.getName() + '\'' +
                    '}';
        }
    }
}
