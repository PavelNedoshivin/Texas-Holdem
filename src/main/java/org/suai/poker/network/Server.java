package org.suai.poker.network;

import org.suai.poker.model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

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
        private DataOutputStream dataOutputStream;
        private ServerOutputThread out;
        private Table table;
        private int chosen;

        public ServerInputThread(Socket client) throws IOException {
            Random rand = new Random();
            name = "Client" + rand.nextInt(100500);
            while (userList.containsKey(name)) {
                name = "Client" + rand.nextInt(100500);
            }
            this.client = client;
            dataInputStream = new DataInputStream(client.getInputStream());
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            out = new ServerOutputThread(client);
            userList.put(name, out);
            table = null;
            chosen = -1;
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

        private Hand buildHand() throws IOException {
            Hand hand = new Hand();
            int handSize = Integer.parseInt(dataInputStream.readUTF());
            List<Card> cardList = new ArrayList<>(handSize);
            for (int j = 0; j < handSize; j++) {
                cardList.add(Card.valueOf(dataInputStream.readUTF()));
            }
            hand.setHand(cardList);
            hand.setMaxValue(Integer.parseInt(dataInputStream.readUTF()));
            hand.setMaxValue2(Integer.parseInt(dataInputStream.readUTF()));
            hand.setId(HandCategory.valueOf(dataInputStream.readUTF()));
            return hand;
        }

        private List<Player> buildPlayerList(Table testTable) throws IOException {
            int size = Integer.parseInt(dataInputStream.readUTF());
            List<Player> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Player player = new Player(dataInputStream.readUTF(), Integer.parseInt(dataInputStream.readUTF()),
                        testTable, false);
                player.setStatus(PlayerStatus.valueOf(dataInputStream.readUTF()));
                player.setDealer(Boolean.parseBoolean(dataInputStream.readUTF()));
                player.setCurrentBet(Integer.parseInt(dataInputStream.readUTF()));
                Hand hand = buildHand();
                player.setHand(hand);
                hand = buildHand();
                player.setBestHand(hand);
                hand = buildHand();
                player.setKicker(hand);
                list.add(player);
            }
            return list;
        }

        public void buildTable() throws IOException {
            Table testTable = new Table();
            testTable.setPlayerList(buildPlayerList(testTable));
            testTable.setTableHand(buildHand());

            List<Pot> potList = new ArrayList<>();
            int size = Integer.parseInt(dataInputStream.readUTF());
            for (int i = 0; i < size; i++) {
                Pot pot = new Pot();
                pot.setAmount(Integer.parseInt(dataInputStream.readUTF()));
                pot.setPlayerList(buildPlayerList(testTable));
                pot.setWinnerList(buildPlayerList(testTable));
            }
            testTable.setPot(potList);

            testTable.setCurrentBet(Integer.parseInt(dataInputStream.readUTF()));
            testTable.setSmallBlind(Integer.parseInt(dataInputStream.readUTF()));

            Deck deck = new Deck();
            size = Integer.parseInt(dataInputStream.readUTF());
            List<Card> cardList = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                cardList.add(Card.valueOf(dataInputStream.readUTF()));
            }
            deck.setDeck(cardList);
            testTable.setTableDeck(deck);

            Player player = new Player(dataInputStream.readUTF(), Integer.parseInt(dataInputStream.readUTF()),
                    testTable, false);
            player.setStatus(PlayerStatus.valueOf(dataInputStream.readUTF()));
            player.setDealer(Boolean.parseBoolean(dataInputStream.readUTF()));
            player.setCurrentBet(Integer.parseInt(dataInputStream.readUTF()));
            player.setHand(buildHand());
            player.setBestHand(buildHand());
            player.setKicker(buildHand());
            testTable.setWinner(player);

            testTable.setDealerPos(Integer.parseInt(dataInputStream.readUTF()));
            testTable.setTurnPos(Integer.parseInt(dataInputStream.readUTF()));
            testTable.setBlindSmallPos(Integer.parseInt(dataInputStream.readUTF()));
            testTable.setBlindBigPos(Integer.parseInt(dataInputStream.readUTF()));
            testTable.setCurrentTurn(Integer.parseInt(dataInputStream.readUTF()));
            if (!(testTable.equals(table))) {
                table = testTable;
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
                chosen = Integer.parseInt(dataInputStream.readUTF());
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
                    buildTable();
                    if (first) {
                        System.out.println("Server got table!");
                        first = false;
                    }
                    sendTable();
                }
            } catch (IOException e) {
                System.out.println("Communication with " + name + " terminated!");
                int counter = 0;
                for (Object o: tableList.entrySet()) {
                    Map.Entry pair = (Map.Entry)o;
                    if (counter == chosen) {
                        LinkedList list = (LinkedList)pair.getValue();
                        list.remove(name);
                        break;
                    }
                    counter++;
                }
                userList.remove(name);
            }
        }
    }
    private static class ServerOutputThread extends Thread {
        private DataOutputStream dataOutputStream;
        private boolean sent;
        private boolean isRunning;
        private Table table;
        public ServerOutputThread(Socket client) throws IOException {
            dataOutputStream = new DataOutputStream(client.getOutputStream());
            isRunning = true;
            sent = true;
            start();
        }
        synchronized void setTable(Table table) {
            sent = false;
            this.table = table;
        }

        private void sendPlayerList(List<Player> list) throws IOException {
            dataOutputStream.writeUTF(Integer.toString(list.size()));
            for (int i = 0; i < list.size(); i++) {
                Player player = list.get(i);
                dataOutputStream.writeUTF(player.getName());
                dataOutputStream.writeUTF(Integer.toString(player.getBalance()));
                dataOutputStream.writeUTF(player.getStatus().toString());
                dataOutputStream.writeUTF(Boolean.toString(player.getDealer()));
                dataOutputStream.writeUTF(Integer.toString(player.getCurrentBet()));
                sendHand(player.getHand());
                sendHand(player.getBestHand());
                sendHand(player.getKicker());
            }
        }

        private void sendHand(Hand hand) throws IOException {
            dataOutputStream.writeUTF(Integer.toString(hand.getHand().size()));
            for (int i = 0; i < hand.getHand().size(); i++) {
                dataOutputStream.writeUTF(hand.getHand().get(i).getName());
            }
            dataOutputStream.writeUTF(Integer.toString(hand.getMaxValue()));
            dataOutputStream.writeUTF(Integer.toString(hand.getMaxValue2()));
            dataOutputStream.writeUTF(hand.getId().toString());
        }

        public void send() throws IOException {
            sendPlayerList(table.getPlayerList());
            sendHand(table.getTableHand());
            dataOutputStream.writeUTF(Integer.toString(table.getPot().size()));
            for (int i = 0; i < table.getPot().size(); i++) {
                Pot pot = table.getPot().get(i);
                dataOutputStream.writeUTF(Integer.toString(pot.getAmount()));
                sendPlayerList(pot.getPlayerList());
                sendPlayerList(pot.getWinnerList());
            }

            dataOutputStream.writeUTF(Integer.toString(table.getCurrentBet()));
            dataOutputStream.writeUTF(Integer.toString(table.getSmallBlind()));

            Deck deck = table.getTableDeck();
            dataOutputStream.writeUTF(Integer.toString(deck.getDeck().size()));
            for (int i = 0; i < deck.getDeck().size(); i++) {
                dataOutputStream.writeUTF(deck.getDeck().get(i).getName());
            }

            dataOutputStream.writeUTF(table.getWinner().getName());
            dataOutputStream.writeUTF(Integer.toString(table.getWinner().getBalance()));
            dataOutputStream.writeUTF(table.getWinner().getStatus().toString());
            dataOutputStream.writeUTF(Boolean.toString(table.getWinner().getDealer()));
            dataOutputStream.writeUTF(Integer.toString(table.getWinner().getCurrentBet()));
            sendHand(table.getWinner().getHand());
            sendHand(table.getWinner().getBestHand());
            sendHand(table.getWinner().getKicker());

            dataOutputStream.writeUTF(Integer.toString(table.getDealerPos()));
            dataOutputStream.writeUTF(Integer.toString(table.getTurnPos()));
            dataOutputStream.writeUTF(Integer.toString(table.getBlindSmallPos()));
            dataOutputStream.writeUTF(Integer.toString(table.getBlindBigPos()));
            dataOutputStream.writeUTF(Integer.toString(table.getCurrentTurn()));
        }

        synchronized void sendTable() {
            if (!sent) {
                try {
                    send();
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
