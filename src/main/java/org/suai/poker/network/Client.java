package org.suai.poker.network;

import org.suai.poker.model.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        private static DataInputStream inMessage;

        static {
            try {
                inMessage = new DataInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private static DataOutputStream outMessage;

        static {
            try {
                outMessage = new DataOutputStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Hand buildHand() throws IOException {
            Hand hand = new Hand();
            int handSize = Integer.parseInt(inMessage.readUTF());
            List<Card> cardList = new ArrayList<>(handSize);
            for (int j = 0; j < handSize; j++) {
                cardList.add(Card.valueOf(inMessage.readUTF()));
            }
            hand.setHand(cardList);
            hand.setMaxValue(Integer.parseInt(inMessage.readUTF()));
            hand.setMaxValue2(Integer.parseInt(inMessage.readUTF()));
            hand.setId(HandCategory.valueOf(inMessage.readUTF()));
            return hand;
        }

        private List<Player> buildPlayerList(Table testTable) throws IOException {
            int size = Integer.parseInt(inMessage.readUTF());
            List<Player> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Player player = new Player(inMessage.readUTF(), Integer.parseInt(inMessage.readUTF()),
                        testTable, false);
                player.setStatus(PlayerStatus.valueOf(inMessage.readUTF()));
                player.setDealer(Boolean.parseBoolean(inMessage.readUTF()));
                player.setCurrentBet(Integer.parseInt(inMessage.readUTF()));
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
            int size = Integer.parseInt(inMessage.readUTF());
            for (int i = 0; i < size; i++) {
                Pot pot = new Pot();
                pot.setAmount(Integer.parseInt(inMessage.readUTF()));
                pot.setPlayerList(buildPlayerList(testTable));
                pot.setWinnerList(buildPlayerList(testTable));
            }
            testTable.setPot(potList);

            testTable.setCurrentBet(Integer.parseInt(inMessage.readUTF()));
            testTable.setSmallBlind(Integer.parseInt(inMessage.readUTF()));

            Deck deck = new Deck();
            size = Integer.parseInt(inMessage.readUTF());
            List<Card> cardList = new ArrayList<>(size);
            for (int j = 0; j < size; j++) {
                cardList.add(Card.valueOf(inMessage.readUTF()));
            }
            deck.setDeck(cardList);
            testTable.setTableDeck(deck);

            Player player = new Player(inMessage.readUTF(), Integer.parseInt(inMessage.readUTF()),
                    testTable, false);
            player.setStatus(PlayerStatus.valueOf(inMessage.readUTF()));
            player.setDealer(Boolean.parseBoolean(inMessage.readUTF()));
            player.setCurrentBet(Integer.parseInt(inMessage.readUTF()));
            player.setHand(buildHand());
            player.setBestHand(buildHand());
            player.setKicker(buildHand());
            testTable.setWinner(player);

            testTable.setDealerPos(Integer.parseInt(inMessage.readUTF()));
            testTable.setTurnPos(Integer.parseInt(inMessage.readUTF()));
            testTable.setBlindSmallPos(Integer.parseInt(inMessage.readUTF()));
            testTable.setBlindBigPos(Integer.parseInt(inMessage.readUTF()));
            testTable.setCurrentTurn(Integer.parseInt(inMessage.readUTF()));
            if (!(testTable.equals(table))) {
                table = testTable;
            }
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
                    isSuccess = Integer.parseInt(inMessage.readUTF());
                    if (!mode && (isSuccess == 0)) {
                        name = inMessage.readUTF();
                    }
                    changed = true;
                }
                for (int i = 0; i < 5; i++) {
                    tableNumbers.add(Integer.parseInt(inMessage.readUTF()));
                }
                while (chosen < 0) {
                    ;
                }
                outMessage.writeUTF(Integer.toString(chosen));
                choseTable = true;
                boolean first = true;
                buildTable();
                while (!client.isClosed()) {
                    if (table != null) {
                        if (first) {
                            System.out.println("Client got table!");
                            first = false;
                        } else {
                            buildTable();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static class ClientOutputThread extends Thread {
        private DataOutputStream out;
        private boolean isSent;
        private boolean isRunning;
        public ClientOutputThread() throws IOException {
            out = new DataOutputStream(client.getOutputStream());
            isSent = true;
            isRunning = true;
        }
        public void sendTable() {
            isSent = false;
        }

        private void sendPlayerList(List<Player> list) throws IOException {
            out.writeUTF(Integer.toString(list.size()));
            for (int i = 0; i < list.size(); i++) {
                Player player = list.get(i);
                out.writeUTF(player.getName());
                out.writeUTF(Integer.toString(player.getBalance()));
                out.writeUTF(player.getStatus().getName());
                out.writeUTF(Boolean.toString(player.getDealer()));
                out.writeUTF(Integer.toString(player.getCurrentBet()));
                sendHand(player.getHand());
                sendHand(player.getBestHand());
                sendHand(player.getKicker());
            }
        }

        private void sendHand(Hand hand) throws IOException {
            out.writeUTF(Integer.toString(hand.getHand().size()));
            for (int i = 0; i < hand.getHand().size(); i++) {
                out.writeUTF(hand.getHand().get(i).getName());
            }
            out.writeUTF(Integer.toString(hand.getMaxValue()));
            out.writeUTF(Integer.toString(hand.getMaxValue2()));
            out.writeUTF(hand.getId().getName());
        }

        public void send() throws IOException {
            sendPlayerList(table.getPlayerList());
            sendHand(table.getTableHand());
            out.writeUTF(Integer.toString(table.getPot().size()));
            for (int i = 0; i < table.getPot().size(); i++) {
                Pot pot = table.getPot().get(i);
                out.writeUTF(Integer.toString(pot.getAmount()));
                sendPlayerList(pot.getPlayerList());
                sendPlayerList(pot.getWinnerList());
            }

            out.writeUTF(Integer.toString(table.getCurrentBet()));
            out.writeUTF(Integer.toString(table.getSmallBlind()));

            Deck deck = table.getTableDeck();
            out.writeUTF(Integer.toString(deck.getDeck().size()));
            for (int i = 0; i < deck.getDeck().size(); i++) {
                out.writeUTF(deck.getDeck().get(i).getName());
            }

            out.writeUTF(table.getWinner().getName());
            out.writeUTF(Integer.toString(table.getWinner().getBalance()));
            out.writeUTF(table.getWinner().getStatus().getName());
            out.writeUTF(Boolean.toString(table.getWinner().getDealer()));
            out.writeUTF(Integer.toString(table.getWinner().getCurrentBet()));
            sendHand(table.getWinner().getHand());
            sendHand(table.getWinner().getBestHand());
            sendHand(table.getWinner().getKicker());

            out.writeUTF(Integer.toString(table.getDealerPos()));
            out.writeUTF(Integer.toString(table.getTurnPos()));
            out.writeUTF(Integer.toString(table.getBlindSmallPos()));
            out.writeUTF(Integer.toString(table.getBlindBigPos()));
            out.writeUTF(Integer.toString(table.getCurrentTurn()));
        }

        @Override
        public void run() {
            try {
                while (isRunning) {
                    if (table != null) {
                        synchronized (table) {
                            if (!isSent) {
                                send();
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
