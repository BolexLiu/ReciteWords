package com.bolex.recitewords;

import java.io.*;

/**
 * Created by Bolex on 2017/9/3.
 */
public class MarkDownProcessing {



    /**
     * 是否保存过，防止重复保存
     *
     * @return
     */
    public static boolean isSave(String basePath, String Words) {
        boolean isSave = false;
        StringBuffer mdWords=new StringBuffer();
        File file = new File(basePath + File.separator + "翻译历史记录.md");// 要写入的文本文件
        file.setExecutable(true);
        file.setReadable(true);
        file.setWritable(true);
        if (file.exists()) {// 如果文件存在
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(basePath + File.separator + "翻译历史记录.md");
                InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    mdWords.append(line);
                }
                if (mdWords.indexOf(Words)!=-1){
                    return true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return isSave;
    }

    /**
     * 存单词
     *
     * @param Words     原文
     * @param translate 译文
     * @throws IOException
     */
    public static void saveWords(String Words, String translate, String basePath) {
        try {

            File file = new File(basePath + File.separator + "翻译历史记录.md");// 要写入的文本文件
            file.setExecutable(true);
            file.setReadable(true);
            file.setWritable(true);
            if (!file.exists()) {// 如果文件不存在，则创建该文件
                file.createNewFile();
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8");
                writer.write("# 翻译历史记录 \r\n");
                writer.write("这里我们翻译过后的结果集，建议用Markdown编辑器打开它，下方是项目地址。欢迎PR或Issues，喜欢的话给个Star，交个朋友吧。\r\n");
                writer.write("### [ReciteWords](https://github.com/BolexLiu/ReciteWords)\r\n\r\n");

                writer.write("## History：\r\n\r\n");
                writer.write("---\r\n\r\n");
                writer.flush();// 清空缓冲区，立即将输出流里的内容写到文件里
                writer.close();// 关闭输出流，施放资源
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8");
            writer.write("- " + Words + "\r\n");
            writer.write("```\r\n");
            writer.write(translate);
            writer.write("```\r\n");
            writer.flush();// 清空缓冲区，立即将输出流里的内容写到文件里
            writer.close();// 关闭输出流，施放资源
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
