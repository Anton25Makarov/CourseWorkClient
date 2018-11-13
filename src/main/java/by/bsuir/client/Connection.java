package by.bsuir.client;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Connection {
    private Socket socket;

    public Connection() {
        try { // установка соединения с сервером

            socket = new Socket(InetAddress.getLocalHost(), 8071);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            String masInt = "Hello";
            objectOutputStream.writeObject(masInt);

//            TimeUnit.SECONDS.sleep(1);

            String[] strings = (String[]) objectInputStream.readObject();

            System.out.println(strings[0]);
            System.out.println(strings[1]);
            System.out.println(strings[2]);

        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
//// запускаем подключение сокета по известным координатам и нициализируем приём сообщений с консоли клиента
//        try (Socket socket = new Socket("localhost", 3345);
//             BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
//             DataInputStream ois = new DataInputStream(socket.getInputStream());) {
//
//            System.out.println("Client connected to socket.");
//            System.out.println();
//            System.out.println("Client writing channel = oos & reading channel = ois initialized.");
//
//// проверяем живой ли канал и работаем если живой
//            while (!socket.isOutputShutdown()) {
//
//// ждём консоли клиента на предмет появления в ней данных
//                if (br.ready()) {
//
//// данные появились - работаем
//                    System.out.println("Client start writing in channel...");
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    String clientCommand = br.readLine();
//
//// пишем данные с консоли в канал сокета для сервера
//                    oos.writeUTF(clientCommand);
//                    oos.flush();
//                    System.out.println("Client sent message " + clientCommand + " to server.");
//                    Thread.sleep(1000);
//// ждём чтобы сервер успел прочесть сообщение из сокета и ответить
//
//// проверяем условие выхода из соединения
//                    if (clientCommand.equalsIgnoreCase("quit")) {
//
//// если условие выхода достигнуто разъединяемся
//                        System.out.println("Client kill connections");
//                        Thread.sleep(2000);
//
//// смотрим что нам ответил сервер на последок перед закрытием ресурсов
//                        if (ois.read() > -1) {
//                            System.out.println("reading...");
//                            String in = ois.readUTF();
//                            System.out.println(in);
//                        }
//
//// после предварительных приготовлений выходим из цикла записи чтения
//                        break;
//                    }
//
//// если условие разъединения не достигнуто продолжаем работу
//                    System.out.println("Client sent message & start waiting for data from server...");
//                    Thread.sleep(2000);
//
//// проверяем, что нам ответит сервер на сообщение(за предоставленное ему время в паузе он должен был успеть ответить)
//                    if (ois.read() > -1) {
//
//// если успел забираем ответ из канала сервера в сокете и сохраняем её в ois переменную,  печатаем на свою клиентскую консоль
//                        System.out.println("reading...");
//                        String in = ois.readUTF();
//                        System.out.println(in);
//                    }
//                }
//            }
//// на выходе из цикла общения закрываем свои ресурсы
//            System.out.println("Closing connections & channels on clentSide - DONE.");
//
//        } catch (UnknownHostException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


