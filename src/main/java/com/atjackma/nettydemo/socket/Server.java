package com.atjackma.nettydemo.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hui
 * @version 1.0
 * @Title: Server
 * @Description:
 * @date 2022/6/8 10:07
 */
public class Server {


    public static void main(String[] args) {
        final int DEFAULT_PORT = 8888;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(DEFAULT_PORT);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client[" + socket.getPort() + "]Online");
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                String msg = "";
                while((msg = bufferedReader.readLine()) != null){
                    System.out.println("Client[" + socket.getInetAddress() + socket.getPort() + "]:" + msg);
                    bufferedWriter.write("Server:" + msg + "\n");
                    bufferedWriter.flush();
                    if(msg.equals("exit")){
                        System.out.println("Client[" + socket.getPort() + "]:Offline");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        //executorService.execute();
    }
}
