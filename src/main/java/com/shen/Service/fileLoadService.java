package com.shen.Service;
import com.shen.CosineUtils.CosineSimilarity;
import com.shen.CosineUtils.TextSimilarity;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文本处理Service
 */
@Service
public class fileLoadService {

    double similaryScore;//定义一个全局相似度

    /**
     * 读取word文件，以docx格式
     * @param filename
     * @return
     */
    public static String readFile(String filename) {
        String text = null;
        // 通过XWPFWordExtractor读取docx文档，只能获取文本，不能获取文本对应的属性值
        try {
            InputStream inputStream = new FileInputStream(filename);
            XWPFDocument doc = new XWPFDocument(inputStream); // 创建word文件
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            text = extractor.getText();
            // 关闭输入流
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * 读取pdf格式的合同
     * @param filePath
     * @return
     */



    /**
     * 余弦查重算法的调用，查重。
     * @param filenameA
     * @param filenameB
     * @throws Exception
     */
    public void Consine(String filenameA, String filenameB) throws Exception {

        String textA="";
        String textB="";
        long startTime = System.currentTimeMillis();//设置当前程序开始运行时间

        int count = 0; //对重复字段数进行计数

        String[] fileA=filenameA.split("[.]");
        String[] fileB=filenameB.split("[.]");
        if (fileA[fileA.length-1].equals("docx")&&fileB[fileB.length-1].equals("docx")) {
            //读取文本
             textA = readFile(filenameA);
             textB = readFile(filenameB);
        }

        // 分割文章 - 逗号，句号、问号、感叹号
        String[] stringsAA = textA.split("[。？！；]"); // 对filenameA、B不替换 换行符 分割，保留文本原先格式
        String[] stringsBB = textB.split("[。？！；]");

        //对filenameA、B替换 换行符 分割，不保留文本原先格式
        String[] stringsA = textA.replaceAll("\n", "").split("[。？！；]"); // 正则表达式
        String[] stringsB = textB.replaceAll("\n", "").split("[。？！；]"); // 正则表达式

        int allword = 0; //求出对filenameB文章所有的字数

        for (int all = 0; all < stringsB.length; all++) {
            if (stringsB[all].length()<2)
                continue;
            allword = stringsB[all].length() + allword;
        }

        double sum = 0.00;//对filenameB文章分割后的每一段重复字数计数

        //A、B数组存储重复的片段
        String[] A = new String[stringsA.length * stringsB.length];
        String[] B = new String[stringsB.length * stringsA.length];

        int aa = 0, bb = 0;//A、B重复的片段的数组下标

        TextSimilarity similarity = new CosineSimilarity();//对余弦算法查重类进行调用

        for (int b = 0; b < stringsB.length; b++) {
            if (stringsB[b].length() < 2)
                continue;

            double temp=0.0;//相似度暂存值
            int  stringBLocal = 0;//记录temp最大值时 stringsA数组的下标

            for (int a = 0; a < stringsA.length; a++) {
                if (stringsA[a].length() < 2)
                    continue;

                double score1pk2 = similarity.getSimilarity(stringsB[b],stringsA[a]);//单对片段重复度
                if (temp<score1pk2){
                    temp=score1pk2;
                    //选取B抄袭文本中一个片段与A原始文本的每一片段的相似度最高值，
                    stringBLocal=a;
                }
                
            }
            if (temp > 0.58) {   //设定相似度大于0.58记为重复部分，存入数组、输出
                //将filenameA、B相重复的片段存入A，B数组
                A[aa++] = stringsA[stringBLocal];
                B[bb++] = stringsB[b];
                double socor = stringsB[b].length() * temp; //根据相似度求B此句重复的字数，累加
                sum = socor + sum;

            }
        }
        similaryScore = sum / allword;//filenameB文本相似率=相似字数/总字数

        System.out.println("B比对A相似度为" + similaryScore);

            for (int c = 0; c < A.length * B.length; c++) {
                if (A[c] == null && B[c] == null) {
                    break;
                }
                count++;
            System.out.println("相似文章段落为：\n" + "   A:" + A[c] + "\n" + "   B:" + B[c]);
        }

        long endTime = System.currentTimeMillis();
        long allTime = endTime - startTime;
        System.out.println("程序运行时间：" + allTime / 1000 + "秒");
        System.out.println("重复字段数：" + count + " 个");
        writeFile(stringsAA,stringsBB,A,B);
    }

    /**
     * 写入文本函数
     * @param aOrign  a原始文本
     * @param bSimli  b抄袭文本
     * @param aOrignSim  a相似部分
     * @param bSimliSim  b相似部分
     * @throws IOException
     */
    public void writeFile(String[] aOrign, String[] bSimli,String[] aOrignSim,String [] bSimliSim) throws IOException {

        //通过时间格式来生成输出文本的文件名
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_MM_SS");
        String fileName="输出抄袭文本"+df.format(new Date())+".docx";
        String filePath = "E:\\test\\"+fileName;// 取得抄袭文件路径

        String fileOrign="输出原始文本"+df.format(new Date())+".docx";
        String fileOrignPath="E:\\test\\"+fileOrign;//取得原始文本路径

        //写入抄袭文档B
        // 将数组的数据写入到文件中
        FileWriter fw = new FileWriter(filePath);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("抄袭文本比对原始文本相似度为" + similaryScore);
        bw.newLine();//换行写入

        for (String wordbSimli : bSimli) {
            for (int j=0;j<bSimliSim.length;j++){
                if (bSimliSim[j]==null)
                    break;
                String sb=bSimliSim[j];
                sb=sb.replaceAll("\\s+","");
                if (wordbSimli.replaceAll("\\s+","").equals(sb)){

                    bw.newLine();//换行写入
                    wordbSimli="{##开始@ "+j+" 号 "+wordbSimli+" ##结束@}";
                    bw.newLine();//换行写入

                }
            }
            bw.write(wordbSimli);
        }
        bw.flush();
        fw.flush();

        //写入原始文档A
        FileWriter fwOrign=new FileWriter(fileOrignPath);
        BufferedWriter bwOrign=new BufferedWriter(fwOrign);


        for (String wordOrign : aOrign){
            for (int i=0;i<aOrignSim.length;i++){
              if (aOrignSim[i]==null)
                  break;
               String s=aOrignSim[i];
               s=s.replaceAll("\\s+","");
                if (wordOrign.replaceAll("\\s+","").equals(s)){
                    bwOrign.newLine();//换行写入
                    wordOrign="{##开始@ "+ i+" 号 "+wordOrign+" ##结束@}";
                    bwOrign.newLine();//换行写入
            }
            }
            bwOrign.write(wordOrign);
        }
        bwOrign.flush();
        fwOrign.flush();
    }

}
