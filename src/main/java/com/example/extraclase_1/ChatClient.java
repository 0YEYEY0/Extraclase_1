package com.example.extraclase_1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.*;

/**
 * Creación de la clase ChatClient, destinida a ser una aplicación.
 */
public class ChatClient extends Application {

    private static final DatagramSocket socket;
    /**
     * Parte estática de la clase. Se declara una variable llamada socket, con los modificadores private static final,
     * lo que nos hace la variable privada(solo accesible dentro de esta clase), estática (pertenece a la clase, en lugar
     * una instancia) y final (su valor no puede ser modificada)
     */
    static {
        try {
            socket=new DatagramSocket();
            /**
             * Dentro del bloque estático, se crea una instancia de la clase DatagramSocket utilizando el constructor
             * predeterminado, que crea un socket UPD. La clase Datagram Socket se utiliza para enviar y recibir paquetes de datos
             * a través de sockets UPD en Java.
             */
        } catch (SocketException e) {
            throw new RuntimeException(e);
            /**
             * Manejo de excepciones, maneja la excepción que pueda ocurrir al intentar crear el socket.
             */
        }
    }
    private static final InetAddress address;

    static {
        try {
            address= InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    private static final String identifier ="usuario";

    private static final int SERVER_PORT= 8000;

    private static final TextArea messageArea = new TextArea();

    private static final TextField inputBox = new TextField();

    public static void main(String[] args)throws IOException {

        // thread for receiving messages
        ClientThread clientThread = new ClientThread(socket, messageArea);
        clientThread.start();

        // send initialization message to the server
        byte[] uuid = ("init;" + identifier).getBytes();
        DatagramPacket initialize = new DatagramPacket(uuid, uuid.length, address, SERVER_PORT);
        socket.send(initialize);

        launch();
    }


    public void start(Stage primaryStage) {

            messageArea.setMaxWidth(500);
            messageArea.setEditable(false);


            inputBox.setMaxWidth(500);
            inputBox.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    String temp = identifier + ";" + inputBox.getText(); // message to send
                    messageArea.setText(messageArea.getText() + inputBox.getText() + "\n"); // update messages on screen
                    byte[] msg = temp.getBytes(); // convert to bytes
                    inputBox.setText(""); // remove text from input box

                    // create a packet & send
                    DatagramPacket send = new DatagramPacket(msg, msg.length, address, SERVER_PORT);
                    try {
                        socket.send(send);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // put everything on screen
            Scene scene = new Scene(new VBox(35, messageArea, inputBox), 550, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }




