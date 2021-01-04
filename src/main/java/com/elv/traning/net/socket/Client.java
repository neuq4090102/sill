package com.elv.traning.net.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author lxh
 * @since 2020-12-22
 */
public class Client {

    public static void main(String[] args) {
        startClient(Config.of().host("127.0.0.1").port(1234));
    }

    private static void startClient(Config config) {
        try {
            Socket socket = new Socket(config.getHost(), config.getPort());

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            while (true) {
                bw.write("This is a test msg.");
                bw.flush();

                Thread.sleep(1000);

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                System.out.println("Server msg:" + br.readLine());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
