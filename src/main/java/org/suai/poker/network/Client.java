package org.suai.poker.network;

import org.suai.poker.model.Table;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

public class Client {
    private static Socket client;
    private static Table table;
    private static int chosen;
    private static String name;
    private static String login;
    private static String password;
    private static boolean mode;
    private static int isSuccess;
    private static boolean changed;
    private static LinkedList tableNumbers;
    private static ClientInputThread cit;
    private static ClientOutputThread cot;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public Client() throws IOException {
        client = new Socket("localhost", 8080);
        in = new ObjectInputStream(client.getInputStream());
        out = new ObjectOutputStream(client.getOutputStream());
        table = null;
        chosen = -1;
        name = null;
        isSuccess = -1;
        changed = true;
        tableNumbers = new LinkedList();
    }
    public void setMode(boolean mode) {
        this.mode = mode;
    }
    public void setRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public void setName(String name) {
        if (mode) {
            this.name = name;
        }
    }
    public void start() throws IOException {
        cit = new ClientInputThread();
        cot = new ClientOutputThread();
        cit.start();
        cot.start();
    }
    public void setChosen(int c) {
        chosen = c;
    }
    public String getName() {
        return name;
    }
    synchronized public Table getTable() {
        return table;
    }
    synchronized public void setTable(Table t) {
        table = t;
        cot.sendTable();
    }
    public int getSuccess() {
        return isSuccess;
    }
    public LinkedList getTableNumbers() {
        return tableNumbers;
    }
    private static class ClientInputThread extends Thread {
        @Override
        public void run() {
            try {
                in = new ObjectInputStream(client.getInputStream());
                while (login == null) {
                    ;
                }
                while (isSuccess != 0) {
                    if (changed) {
                        out.writeObject(mode);
                        out.writeObject(login);
                        out.writeObject(password);
                        if (mode) {
                            out.writeObject(name);
                        }
                        changed = false;
                    }
                    isSuccess = (int)in.readObject();
                    if (!mode && (isSuccess == 0)) {
                        name = (String)in.readObject();
                    }
                    changed = true;
                }
                for (int i = 0; i < 5; i++) {
                    tableNumbers.add(in.readObject());
                }
                while (chosen < 0) {
                    ;
                }
                out.writeObject(chosen);
                boolean first = true;
                table = (Table)in.readObject();
                while (!client.isClosed()) {
                    if (table != null) {
                        if (first) {
                            System.out.println("Client got table!");
                            first = false;
                        } else {
                            table = (Table)in.readObject();
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private static class ClientOutputThread extends Thread {
        private boolean isSent;
        private boolean isRunning;
        public ClientOutputThread() {
            isSent = true;
            isRunning = true;
        }
        public void sendTable() {
            isSent = false;
        }
        @Override
        public void run() {
            try {
                while (isRunning) {
                    if (table != null) {
                        synchronized (table) {
                            if (!isSent) {
                                out.writeObject(table);
                                isSent = true;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Communication terminated!");
                isRunning = false;
            }
        }
    }
}
