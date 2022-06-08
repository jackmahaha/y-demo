package com.atjackma.nettydemo.socket;

import java.io.*;
import java.net.Socket;

/**
 * @author hui
 * @version 1.0
 * @Title: Client
 * @Description:
 * @date 2022/6/8 10:07
 */
public class Client {
    public static void main(String[] args) {
        final String DEFAULT_SERVER_HOST = "127.0.0.1";
        final int DEFAULT_SERVER_PORT = 8888;
        try {
            Socket socket = new Socket(DEFAULT_SERVER_HOST, DEFAULT_SERVER_PORT);
            OutputStream outputStream = socket.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write("hello hello\n");
            bufferedWriter.flush();
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String msg = "";
            while ((msg = bufferedReader.readLine()) != null){
                System.out.println("msg = " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
