package by.bsuir.client;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
    private Socket socket;

    public Connection() {
        try { // установка соединения с сервером

            socket = new Socket(InetAddress.getLocalHost(), 8071);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            String masInt = "Hello";
            objectOutputStream.writeObject(masInt);


            String[] strings = (String[]) objectInputStream.readObject();

            System.out.println(strings[0]);
            System.out.println(strings[1]);
            System.out.println(strings[2]);

        } catch (ConnectException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}