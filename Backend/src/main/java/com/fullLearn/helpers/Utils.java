package com.fullLearn.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by amandeep on 21/07/17.
 */
public class Utils {

    public String getRequestBody(HttpServletRequest req) throws IOException {
        String requestBody = "";
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        InputStream inputStream = req.getInputStream();
        if (inputStream != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } else {
            stringBuilder.append("");
        }

        if (bufferedReader != null) {

            bufferedReader.close();

        }

        requestBody = stringBuilder.toString();
        return requestBody;
    }
}



