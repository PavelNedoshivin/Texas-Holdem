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
    private static boolean choseTable;

    public Client() throws IOException {
        client = new Socket("localhost", 8080);
        table = null;
        chosen = -1;
        name = null;
        isSuccess = -1;
        changed = true;
        tableNumbers = new LinkedList();
        choseTable = false;
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
                ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                DataInputStream inMessage = new DataInputStream(client.getInputStream());
                while (isSuccess != 0) {
                    isSuccess = Integer.parseInt(inMessage.readUTF());
                    if (!mode && (isSuccess == 0)) {
                        name = inMessage.readUTF();
                    }
                    changed = true;
                }
                for (int i = 0; i < 5; i++) {
                    tableNumbers.add(Integer.parseInt(inMessage.readUTF()));
                }
                boolean first = true;
                while (!choseTable) {
                    ;
                }
                table = (Table)in.readObject();
                while (!client.isClosed()) {
                    if (table != null) {
                        if (first) {
                            System.out.println("Client got table!");
                            first = false;
                        } else {
                            synchronized (table) { table = (Table)in.readObject(); }
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private static class ClientOutputThread extends Thread {
        private ObjectOutputStream out;
        private DataOutputStream outMessage;
        private boolean isSent;
        private boolean isRunning;
        public ClientOutputThread() throws IOException {
            out = new ObjectOutputStream(client.getOutputStream());
            outMessage = new DataOutputStream(client.getOutputStream());
            isSent = true;
            isRunning = true;
        }
        public void sendTable() {
            isSent = false;
        }
        public void requestTable() {

        }
        @Override
        public void run() {
            try {
                while (login == null) {
                    ;
                }
                while (isSuccess != 0) {
                    if (changed) {
                        outMessage.writeUTF(Boolean.toString(mode));
                        outMessage.writeUTF(login);
                        outMessage.writeUTF(password);
                        if (mode) {
                            outMessage.writeUTF(name);
                        }
                        changed = false;
                    }
                }
                while (chosen < 0) {
                    ;
                }
                outMessage.writeUTF(Integer.toString(chosen));
                choseTable = true;
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
