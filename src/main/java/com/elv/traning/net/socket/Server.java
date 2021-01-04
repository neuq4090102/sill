package com.elv.traning.net.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author lxh
 * @since 2020-12-22
 */
public class Server {

    public static void main(String[] args) {
        startServer(Config.of().port(1234));
    }

    private static void startServer(Config config) {
        try {
            ServerSocket serverSocket = new ServerSocket(config.getPort());
            System.out.println("Server started...");
            Socket socket = null;
            while ((socket = serverSocket.accept()) != null) {
                System.out.println("Request Host:" + socket.getInetAddress().getLocalHost() + " is Connected.");

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String msg = null;
                while ((msg = br.readLine()) != null) {
                    System.out.println("Client msg:" + msg);
                    bw.write("Received msg:" + msg);
                    bw.flush();
                }

                System.out.println("Client has closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
