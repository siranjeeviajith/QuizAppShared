package com.services;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.List;

public class TemplateService {

    public static String modify(ServletContext servletContext, List<String> data, String path) throws IOException {
        String modifiedHtml="";

        InputStream st=servletContext.getResourceAsStream(path);
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(st));

        String string = new String();
        while( (string = buffReader.readLine() ) != null){
            modifiedHtml=modifiedHtml+string;
        }

        buffReader.close();
        st.close();
        for(String content:data){
            modifiedHtml = modifiedHtml.replaceFirst("@@config@@", content);
        }

        return modifiedHtml;
    }
}
