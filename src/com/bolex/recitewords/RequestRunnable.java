package com.bolex.recitewords;

import com.bolex.recitewords.Logger;
import com.google.gson.Gson;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.JBColor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by sky on 16/5/18.
 */
public class RequestRunnable implements Runnable {
    private static final String HOST = "fanyi.youdao.com";
    private static final String PATH = "/openapi.do";
    private static final String PARAM_KEY_FROM = "keyfrom";
    private static final String PARAM_KEY = "key";
    private static final String PARAM_TYPE = "type";
    private static final String TYPE = "data";
    private static final String PARAM_DOC_TYPE = "doctype";
    private static final String DOC_TYPE = "json";
    private static final String PARAM_CALL_BACK = "callback";
    private static final String CALL_BACK = "show";
    private static final String PARAM_VERSION = "version";
    private static final String VERSION = "1.1";
    private static final String PARAM_QUERY = "q";
    //replace your own key, see http://fanyi.youdao.com/openapi?path=data-mode
    private static final String KEY_FROM = "Skykai521";
    private static final String KEY = "977124034";
    private Editor mEditor;
    private String mQuery;

    public RequestRunnable(Editor editor, String query) {
        this.mEditor = editor;
        this.mQuery = query;
    }

    public void run() {
        try {
            URI uri = createTranslationURI(mQuery);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000).build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(requestConfig);
            HttpClient client = HttpClients.createDefault();
            HttpResponse response = client.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity resEntity = response.getEntity();
                String json = EntityUtils.toString(resEntity, "UTF-8");
                Gson gson = new Gson();
                Translation translation = gson.fromJson(json, Translation.class);
                //show result
                showPopupBalloon(translation.toString());
                Logger.info(translation.toString());
                saveWords(translation.getQuery(),translation.toString());
            } else {
                showPopupBalloon(response.getStatusLine().getReasonPhrase());
            }
        } catch (IOException e) {
            showPopupBalloon(e.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void showPopupBalloon(final String result) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            public void run() {
                JBPopupFactory factory = JBPopupFactory.getInstance();
                factory.createHtmlTextBalloonBuilder(result, null, new JBColor(new Color(186, 238, 186), new Color(73, 117, 73)), null)
                        .setFadeoutTime(5000)
                        .createBalloon()
                        .show(factory.guessBestPopupLocation(mEditor) , Balloon.Position.below);
            }
        });
    }

    /**
     *  存单词
     * @param Words 原文
     * @param translate 译文
     * @throws IOException
     */
    private void saveWords( String Words,String translate) throws IOException {
        String usrHome = System.getProperty("user.home");
        File file = new File(usrHome+"\\ReciteWords.md");// 要写入的文本文件
        if (!file.exists()) {// 如果文件不存在，则创建该文件
            file.createNewFile();
            FileWriter writer = new FileWriter(file,true);// 获取该文件的输出流
            writer.write("# 这里是你该记住的单词。请用Markdown编辑器打开它。\r\n");
            writer.write("---\r\n\r\n");
            writer.flush();// 清空缓冲区，立即将输出流里的内容写到文件里
            writer.close();// 关闭输出流，施放资源
        }
        FileWriter writer = new FileWriter(file,true);
        writer.write("- "+Words+"\r\n");
        writer.write("```\r\n");
        writer.write(translate);
        writer.write("```\r\n");
        writer.flush();
        writer.close();
    }


    private URI createTranslationURI(String query) throws URISyntaxException {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http")
                .setHost(HOST)
                .setPath(PATH)
                .addParameter(PARAM_KEY_FROM, KEY_FROM)
                .addParameter(PARAM_KEY, KEY)
                .addParameter(PARAM_TYPE, TYPE)
                .addParameter(PARAM_VERSION, VERSION)
                .addParameter(PARAM_DOC_TYPE, DOC_TYPE)
                .addParameter(PARAM_CALL_BACK, CALL_BACK)
                .addParameter(PARAM_QUERY, query);
        return builder.build();
    }
}
