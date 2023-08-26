package com.example.extraclase_1;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 * Esta clase representa un servidor simple que recibe y reenvía mensajes utilizando UDP.
 */
public class NewServer {

    private static byte[] incoming = new byte[256];
    private static final int PORT = 8000;

    private static DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Integer> users = new ArrayList<>();

    private static final InetAddress address;

    static {
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * El método principal del servidor, responsable de recibir y reenviar mensajes.
     *
     * @param args Argumentos de la línea de comandos (no utilizados en esta aplicación).
     */
    public static void main(String[] args) {

        System.out.println("Servidor empezó en el puerto " + PORT);

        while (true) {
            DatagramPacket packet = new DatagramPacket(incoming, incoming.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Servidor recibido: " + message);

            // Verificar si el mensaje recibido es un mensaje "init;"
            if (message.contains("init;")) {
                users.add(packet.getPort());
            }
            // Reenviar el mensaje a todos los usuarios conectados
            else {
                int userPort = packet.getPort();
                byte[] byteMessage = message.getBytes();

                // Reenviar el mensaje a cada usuario excepto al remitente
                for (int forward_port : users) {
                    if (forward_port != userPort) {
                        DatagramPacket forward = new DatagramPacket(byteMessage, byteMessage.length, address, forward_port);
                        try {
                            socket.send(forward);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}

