package com.example.extraclase_1;

import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientThread extends Thread{
    private DatagramSocket socket;
    /**
     * Se está declarando una clase llamada ClientThread que extiende la clase Thread, clase diseñada para trabajar
     * para ejecutarse como un hilo separado. Además se está declarando una variable de instancia privada llamada
     * socket.
     */


    private byte[] incoming =new byte[256];
    /**
     * Está declarando una variable de instancia llamada incoming, que es un arreglo de bytes con
     * una longitud de 256 elementos.
     */
    private TextArea textArea;

    public ClientThread(DatagramSocket socket, TextArea textArea) {
        this.socket=socket;
        this.textArea=textArea;
        /**
         * Se crea un constructor para la clase ClientThread,donde se establecen
         * los valores iniciales para las variables de instancia socket y textArea.
         */
    }
    public void run(){
        System.out.println("iniciando");
        while (true) {
            DatagramPacket packet= new DatagramPacket(incoming, incoming.length);
            /**
             * Se crea un nuevo objeto DatagramPacket llamado packet que se utilizará
             * para recibir los datos entrantes. Se pasa el arreglo de bytes incoming y
             * su longitud como parámetros al constructor.
             */
            try {
                socket.receive(packet);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
            /**
             * Se utiliza un bloque try-catch para manejar la excepción que podría ocurrir
             * al recibir un paquete a través del socket.
             */
            String message=new String(packet.getData(), 0, packet.getLength()) + "\n";
            /**
             *  Esto crea una cadena message que contiene el contenido del paquete como texto.
             */
            String current=textArea.getText();
            textArea.setText(current+message);
            /**
             *  Esto agrega el mensaje recibido al final del contenido existente del área
             *  de texto, haciendo que los mensajes se muestren uno tras otro.
             */
        }
    }
}
