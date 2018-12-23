package org.suai.poker.network;

import org.suai.poker.model.Table;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Server {
    private static ServerSocket server;
    private static HashMap userList;
    private static HashMap tableList;
    public static void main(String[] args) throws IOException {
        server = new ServerSocket(8080);
        userList = new HashMap();
        tableList = new HashMap();
        for (int i = 0; i < 5; i++) {
            LinkedList players = new LinkedList();
            Table table = new Table();
            tableList.put(table, players);
        }
        System.out.println("Server has been started");
        while (true) {
            new ServerInputThread(server.accept());
        }
    }
    private static class ServerInputThread extends Thread {
        private Socket client;
        private String name;
        private DataInputStream dataInputStream;
        private ObjectInputStream objectInputStream;
        private DataOutputStream dataOutputStream;
        private ServerOutputThread out;
        private Table table;
        public ServerInputThread(Socket client) throws IOException {
            Random rand = new Random();
            name = "Client" + rand.nextInt(100500);
            while (userList.containsKey(name)) {
                name = "Client" + rand.nextInt(100500);
            }
            this.client = client;
            dataInputStream = new DataInputStream(client.getInputStream());
            objectInputStream = new ObjectInputStream(client.getInputStream());
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            out = new ServerOutputThread(client);
            userList.put(name, out);
            table = null;
            start();
        }
        synchronized void sendTable() {
            for (Object o: userList.entrySet()) {
                Map.Entry pair = (Map.Entry)o;
                String name = (String)pair.getKey();
                if (name.equals(this.name)) {
                    continue;
                }
                ServerOutputThread user = (ServerOutputThread)pair.getValue();
                user.setTable(table);
            }
        }
        public void authentificate() throws IOException {
            boolean success = false;
            while (!success) {
                BufferedReader reader = new BufferedReader(new FileReader("D:\\base.txt"));
                boolean mode = Boolean.parseBoolean(dataInputStream.readUTF());
                String login = dataInputStream.readUTF();
                String password = dataInputStream.readUTF();
                String name = null;
                if (mode) {
                    name = dataInputStream.readUTF();
                    String line;
                    success = true;
                    while ((line = reader.readLine()) != null) {
                        int first = 0;
                        String testName;
                        for (int i = 0; i < line.length(); i++) {
                            if (line.charAt(i) == '\t') {
                                if (first > 0) {
                                    testName = line.substring(i + 1);
                                    if (testName.equals(name)) {
                                        success = false;
                                        break;
                                    }
                                } else {
                                    first = i;
                                    String testLogin = line.substring(0, i);
                                    if (login.equals(testLogin)) {
                                        success = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (success == false) {
                            break;
                        }
                    }
                    reader.close();
                    if (success) {
                        BufferedWriter writer = new BufferedWriter(new FileWriter("D:\\base.txt", true));
                        writer.append(login + "\t" + password + "\t" + name + "\n");
                        writer.close();
                        this.name = name;
                    }
                } else {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int first = 0;
                        for (int i = 0; i < line.length(); i++) {
                            if (line.charAt(i) == '\t') {
                                if (first == 0) {
                                    String testLogin = line.substring(0, i);
                                    if (!(login.equals(testLogin))) {
                                        break;
                                    } else {
                                        first = i;
                                    }
                                } else {
                                    String testPassword = line.substring(first + 1, i);
                                    if (password.equals(testPassword)) {
                                        success = true;
                                        name = line.substring(i + 1);
                                        break;
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    reader.close();
                    this.name = name;
                }
                if (success) {
                    dataOutputStream.writeUTF(Integer.toString(0));
                    if (!mode) {
                        dataOutputStream.writeUTF(name);
                    }
                } else {
                    dataOutputStream.writeUTF(Integer.toString(1));
                }
            }
        }
        @Override
        public void run() {
            try {
                authentificate();
                System.out.println(name + " has joined");
                DataOutputStream outMessage = new DataOutputStream(client.getOutputStream());
                for (Object o: tableList.entrySet()) {
                    Map.Entry pair = (Map.Entry)o;
                    LinkedList list = (LinkedList)pair.getValue();
                    outMessage.writeUTF(Integer.toString(list.size()));
                }
                int chosen = Integer.parseInt(dataInputStream.readUTF());
                int counter = 0;
                for (Object o: tableList.entrySet()) {
                    Map.Entry pair = (Map.Entry)o;
                    if (counter == chosen) {
                        Table tab = (Table) pair.getKey();
                        LinkedList list = (LinkedList) pair.getValue();
                        table = tab;
                        list.add(name);
                        break;
                    }
                    counter++;
                }
                System.out.println(name + " has chosen Table" + (chosen + 1));
                sendTable();
                boolean first = true;
                while (!server.isClosed()) {
                    table = (Table) objectInputStream.readObject();
                    if (table != null) {
                        if (first) {
                            System.out.println("Server got table!");
                            first = false;
                        }
                        sendTable();
                    }
                }
            } catch (IOException e) {
                System.out.println("Communication with " + name + " terminated!");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private static class ServerOutputThread extends Thread {
        private DataOutputStream dataOutputStream;
        private ObjectOutputStream objectOutputStream;
        private boolean sent;
        private boolean isRunning;
        private Table table;
        public ServerOutputThread(Socket client) throws IOException {
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            isRunning = true;
            sent = true;
            start();
        }
        synchronized void setTable(Table table) {
            sent = false;
            this.table = table;
        }
        synchronized void sendTable() {
            if (!sent) {
                try {
                    objectOutputStream.writeObject(table);
                } catch (IOException e) {
                    System.out.println("Communication terminated!");
                    isRunning = false;
                }
                sent = true;
            }
        }
        @Override
        public void run() {
            while (isRunning) {
                sendTable();
            }
        }
    }
}
