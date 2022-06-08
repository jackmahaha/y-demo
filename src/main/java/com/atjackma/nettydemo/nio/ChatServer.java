package com.atjackma.nettydemo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Set;

/**
 * @author hui
 * @version 1.0
 * @Title: ChatServer
 * @Description:
 * @date 2022/6/8 13:53
 *
 * 首先会创建一个Selector，用来监视管理各个不同的Channel
 */
public class ChatServer {
    //设置缓冲区的大小，这里设置为1024个字节
    private static final int BUFFER = 1024;

    //Channel都要配合缓冲区进行读写，所以这里创建一个读缓冲区和一个写缓冲区
    //allocate()静态方法就是设置缓存区大小的方法
    private ByteBuffer read_buffer = ByteBuffer.allocate(BUFFER);
    private ByteBuffer write_buffer = ByteBuffer.allocate(BUFFER);

    private int port;

    public ChatServer(int port){
        this.port = port;
    }

    private void start(){
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            //把server注册到Selector并监听Accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("启动服务器，监听端口:" + port);

            while(true){
                if(selector.select() > 0){
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for(SelectionKey key : selectionKeys){
                        handle(key,selector);
                    }
                    selectionKeys.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handle(SelectionKey key, Selector selector) throws IOException {
        if(key.isAcceptable()){
            ServerSocketChannel server_channel = (ServerSocketChannel)key.channel();
            SocketChannel client_channel = server_channel.accept();
            client_channel.configureBlocking(false);
            client_channel.register(selector,SelectionKey.OP_READ);
        }
        if(key.isReadable()){
            SocketChannel client_channel = (SocketChannel) key.channel();
            String msg = receive(client_channel);
            sendMessage(msg,selector,client_channel);
        }
    }

    private void sendMessage(String msg, Selector selector, SocketChannel client_channel) throws IOException {
        Set<SelectionKey> keys = selector.keys();
        for(SelectionKey key : keys){
            if(key.channel() instanceof ServerSocketChannel || client_channel.equals(key.channel()) || !key.isValid()){
                continue;
            }
            SocketChannel channel = (SocketChannel)key.channel();
            write_buffer.clear();
            write_buffer.flip();
            write_buffer.put(charset.encode(msg));
            while(write_buffer.hasRemaining()){
                channel.write(write_buffer);
            }
        }
    }

    //这不就是api调用工程师吗！！！！？？？
    //编码方式设置为utf-8，下面字符和字符串互转时用得到
    private Charset charset = Charset.forName("UTF-8");
    private String receive(SocketChannel client_channel) throws IOException {
        read_buffer.clear();
        while(client_channel.read(read_buffer) > 0);
        read_buffer.flip();
        CharBuffer decode = charset.decode(read_buffer);
        String msg = String.valueOf(decode);
        return msg;
    }
}
