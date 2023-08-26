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
/**
 *Se declara una variable llamada address con los modificadores private static final
 */

    static {
        try {
            address= InetAddress.getByName("localhost");
            /**
             *Dentro del bloque estático, se intenta obtener una instancia de InetAddress
             * que representa la dirección IP asociada al nombre de host "localhost".
             * InetAddress se utiliza para representar direcciones IP y nombres de host.
             * El método getByName(String host) se utiliza para obtener una instancia de
             * InetAddress a partir del nombre de host.
             */
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
            /**
             * Manejo de excepciones, maneja la excepción que no se pueda resolver la dirección IP para "localhost".
             */
        }
    }
    private static final String identifier ="usuario";
    /**
     * se está declarando una variable estática final llamada identifier y se le asigna
     * el valor de la cadena "usuario".
     */

    private static final int SERVER_PORT= 8000;
    /**
     * La intención de esta variable es indicar el número de puerto que se
     * utilizará para la comunicación con el servidor
     */
    private static final TextArea messageArea = new TextArea();

    private static final TextField inputBox = new TextField();
    /**
     * Dos objetos gráficos utilizados en la interface de usuario en JavaFX: messageArea y inputBox
     */

    public static void main(String[] args)throws IOException {
        /**
         * Método main La declaración throws IOException indica que el método puede lanzar excepciones del
         * tipo IOException, que está relacionado con operaciones de entrada y salida, como la lectura y
         * escritura de datos
         */
        ClientThread clientThread = new ClientThread(socket, messageArea);
        clientThread.start();
/**
 * Se está creando una instancia de ClientThread y pasan como argumentos el socket (socket) y el área de
 * mensajes (messageArea). Luego, el hilo se inicia con el método start().
 */

        byte[] uuid = ("init;" + identifier).getBytes();
        DatagramPacket initialize = new DatagramPacket(uuid, uuid.length, address, SERVER_PORT);
        socket.send(initialize);
        /**
         * Se crea un mensaje de inicialización en forma de arreglo de bytes. Luego, se crea un DatagramPacket
         * utilizando este arreglo de bytes, la dirección IP (address), el número de puerto del servidor
         * (SERVER_PORT), y se envía a través del socket.
         */

        launch();
        /**
         * función se llama para iniciar la aplicación gráfica
         */
    }


    public void start(Stage primaryStage) {

            messageArea.setMaxWidth(500);
            messageArea.setEditable(false);
        /**
         * Configuración y creación de la interfaz gráfica de una aplicación JavaFX.
         */


            inputBox.setMaxWidth(500);
            inputBox.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    String temp = identifier + ";" + inputBox.getText();
                    messageArea.setText(messageArea.getText() + inputBox.getText() + "\n");
                    byte[] msg = temp.getBytes();
                    inputBox.setText("");
/**
 * Manejo de teclas: se verifica si la tecla presionada es la tecla "Enter". Si es así, se
 * ejecuta el bloque de código que sigue. Esto indica que el código a continuación se ejecutará
 * cuando se presione la tecla enter en el campo inputBox. y también la creación y manipulación de mensajes.
 */

                    DatagramPacket send = new DatagramPacket(msg, msg.length, address, SERVER_PORT);
                    try {
                        socket.send(send);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    /**
                     * Manejo de excepciones.
                     */
                }
            });

            Scene scene = new Scene(new VBox(35, messageArea, inputBox), 550, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        /**
         *  crea la escena principal de la interfaz gráfica de la aplicación JavaFX y establece esa
         *  escena en la ventana principal (primaryStage) para mostrarla al usuario.
         */
        }
    }




