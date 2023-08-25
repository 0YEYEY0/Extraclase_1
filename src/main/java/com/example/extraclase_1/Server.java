package com.example.extraclase_1;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }

    }
    public void mensajeAcliente(String mensajeAenviar){
        try {
            bufferedWriter.write(mensajeAenviar);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("No se pudo enviar el mensaje.");
            cerrarT(socket,bufferedWriter,bufferedReader);
        }
    }

    public void mensajeDelCliente(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String mensajeDelCliente = bufferedReader.readLine();
                        HelloController.addLabel(mensajeDelCliente,vBox);
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Error");
                        cerrarT(socket,bufferedWriter,bufferedReader);
                        break;
                    }

                }

            }
        }).start();
    }



    public void cerrarT(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}