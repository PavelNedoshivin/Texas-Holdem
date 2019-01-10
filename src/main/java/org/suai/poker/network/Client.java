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

    public Client() throws IOException {
        client = new Socket("localhost", 8080);
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
                int id = Integer.parseInt(inMessage.readUTF());
                switch (id) {
                    case -1:
                        cardList.add(Card.E0);
                        break;
                    case 0:
                        cardList.add(Card.C2);
                        break;
                    case 1:
                        cardList.add(Card.C3);
                        break;
                    case 2:
                        cardList.add(Card.C4);
                        break;
                    case 3:
                        cardList.add(Card.C5);
                        break;
                    case 4:
                        cardList.add(Card.C6);
                        break;
                    case 5:
                        cardList.add(Card.C7);
                        break;
                    case 6:
                        cardList.add(Card.C8);
                        break;
                    case 7:
                        cardList.add(Card.C9);
                        break;
                    case 8:
                        cardList.add(Card.C10);
                        break;
                    case 9:
                        cardList.add(Card.CJ);
                        break;
                    case 10:
                        cardList.add(Card.CQ);
                        break;
                    case 11:
                        cardList.add(Card.CK);
                        break;
                    case 12:
                        cardList.add(Card.CA);
                        break;
                    case 13:
                        cardList.add(Card.D2);
                        break;
                    case 14:
                        cardList.add(Card.D3);
                        break;
                    case 15:
                        cardList.add(Card.D4);
                        break;
                    case 16:
                        cardList.add(Card.D5);
                        break;
                    case 17:
                        cardList.add(Card.D6);
                        break;
                    case 18:
                        cardList.add(Card.D7);
                        break;
                    case 19:
                        cardList.add(Card.D8);
                        break;
                    case 20:
                        cardList.add(Card.D9);
                        break;
                    case 21:
                        cardList.add(Card.D10);
                        break;
                    case 22:
                        cardList.add(Card.DJ);
                        break;
                    case 23:
                        cardList.add(Card.DQ);
                        break;
                    case 24:
                        cardList.add(Card.DK);
                        break;
                    case 25:
                        cardList.add(Card.DA);
                        break;
                    case 26:
                        cardList.add(Card.S2);
                        break;
                    case 27:
                        cardList.add(Card.S3);
                        break;
                    case 28:
                        cardList.add(Card.S4);
                        break;
                    case 29:
                        cardList.add(Card.S5);
                        break;
                    case 30:
                        cardList.add(Card.S6);
                        break;
                    case 31:
                        cardList.add(Card.S7);
                        break;
                    case 32:
                        cardList.add(Card.S8);
                        break;
                    case 33:
                        cardList.add(Card.S9);
                        break;
                    case 34:
                        cardList.add(Card.S10);
                        break;
                    case 35:
                        cardList.add(Card.SJ);
                        break;
                    case 36:
                        cardList.add(Card.SQ);
                        break;
                    case 37:
                        cardList.add(Card.SK);
                        break;
                    case 38:
                        cardList.add(Card.SA);
                        break;
                    case 39:
                        cardList.add(Card.H2);
                        break;
                    case 40:
                        cardList.add(Card.H3);
                        break;
                    case 41:
                        cardList.add(Card.H4);
                        break;
                    case 42:
                        cardList.add(Card.H5);
                        break;
                    case 43:
                        cardList.add(Card.H6);
                        break;
                    case 44:
                        cardList.add(Card.H7);
                        break;
                    case 45:
                        cardList.add(Card.H8);
                        break;
                    case 46:
                        cardList.add(Card.H9);
                        break;
                    case 47:
                        cardList.add(Card.H10);
                        break;
                    case 48:
                        cardList.add(Card.HJ);
                        break;
                    case 49:
                        cardList.add(Card.HQ);
                        break;
                    case 50:
                        cardList.add(Card.HK);
                        break;
                    case 51:
                        cardList.add(Card.HA);
                        break;
                }
            }
            hand.setHand(cardList);
            hand.setMaxValue(Integer.parseInt(inMessage.readUTF()));
            hand.setMaxValue2(Integer.parseInt(inMessage.readUTF()));
            int id = Integer.parseInt(inMessage.readUTF());
            switch (id) {
                case 0:
                    hand.setId(HandCategory.UNKNOWN);
                    break;
                case 1:
                    hand.setId(HandCategory.HIGH_CARD);
                    break;
                case 2:
                    hand.setId(HandCategory.PAIR);
                    break;
                case 3:
                    hand.setId(HandCategory.TWO_PAIR);
                    break;
                case 4:
                    hand.setId(HandCategory.THREE_OF_A_KIND);
                    break;
                case 5:
                    hand.setId(HandCategory.STRAIGHT);
                    break;
                case 6:
                    hand.setId(HandCategory.FLUSH);
                    break;
                case 7:
                    hand.setId(HandCategory.FULL_HOUSE);
                    break;
                case 8:
                    hand.setId(HandCategory.FOUR_OF_A_KIND);
                    break;
                case 9:
                    hand.setId(HandCategory.STRAIGHT_FLUSH);
                    break;
                case 10:
                    hand.setId(HandCategory.ROYAL_FLUSH);
                    break;
            }
            return hand;
        }

        private List<Player> buildPlayerList(Table testTable) throws IOException {
            int size = Integer.parseInt(inMessage.readUTF());
            List<Player> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Player player = new Player(inMessage.readUTF(), Integer.parseInt(inMessage.readUTF()),
                        testTable, false);
                int id = Integer.parseInt(inMessage.readUTF());
                switch (id) {
                    case 0:
                        player.setStatus(PlayerStatus.PLAYER_NORMAL);
                        break;
                    case 1:
                        player.setStatus(PlayerStatus.PLAYER_CHECK);
                        break;
                    case 2:
                        player.setStatus(PlayerStatus.PLAYER_CALL);
                        break;
                    case 3:
                        player.setStatus(PlayerStatus.PLAYER_BET);
                        break;
                    case 4:
                        player.setStatus(PlayerStatus.PLAYER_RAISE);
                        break;
                    case 5:
                        player.setStatus(PlayerStatus.PLAYER_FOLD);
                        break;
                    case 6:
                        player.setStatus(PlayerStatus.PLAYER_ALLIN);
                        break;
                    case 7:
                        player.setStatus(PlayerStatus.PLAYER_WINNER);
                        break;
                    case 8:
                        player.setStatus(PlayerStatus.PLAYER_LOST);
                        break;
                    case 9:
                        player.setStatus(PlayerStatus.PLAYER_BUSTED_OUT);
                        break;
                }
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
                int id = Integer.parseInt(inMessage.readUTF());
                switch (id) {
                    case -1:
                        cardList.add(Card.E0);
                        break;
                    case 0:
                        cardList.add(Card.C2);
                        break;
                    case 1:
                        cardList.add(Card.C3);
                        break;
                    case 2:
                        cardList.add(Card.C4);
                        break;
                    case 3:
                        cardList.add(Card.C5);
                        break;
                    case 4:
                        cardList.add(Card.C6);
                        break;
                    case 5:
                        cardList.add(Card.C7);
                        break;
                    case 6:
                        cardList.add(Card.C8);
                        break;
                    case 7:
                        cardList.add(Card.C9);
                        break;
                    case 8:
                        cardList.add(Card.C10);
                        break;
                    case 9:
                        cardList.add(Card.CJ);
                        break;
                    case 10:
                        cardList.add(Card.CQ);
                        break;
                    case 11:
                        cardList.add(Card.CK);
                        break;
                    case 12:
                        cardList.add(Card.CA);
                        break;
                    case 13:
                        cardList.add(Card.D2);
                        break;
                    case 14:
                        cardList.add(Card.D3);
                        break;
                    case 15:
                        cardList.add(Card.D4);
                        break;
                    case 16:
                        cardList.add(Card.D5);
                        break;
                    case 17:
                        cardList.add(Card.D6);
                        break;
                    case 18:
                        cardList.add(Card.D7);
                        break;
                    case 19:
                        cardList.add(Card.D8);
                        break;
                    case 20:
                        cardList.add(Card.D9);
                        break;
                    case 21:
                        cardList.add(Card.D10);
                        break;
                    case 22:
                        cardList.add(Card.DJ);
                        break;
                    case 23:
                        cardList.add(Card.DQ);
                        break;
                    case 24:
                        cardList.add(Card.DK);
                        break;
                    case 25:
                        cardList.add(Card.DA);
                        break;
                    case 26:
                        cardList.add(Card.S2);
                        break;
                    case 27:
                        cardList.add(Card.S3);
                        break;
                    case 28:
                        cardList.add(Card.S4);
                        break;
                    case 29:
                        cardList.add(Card.S5);
                        break;
                    case 30:
                        cardList.add(Card.S6);
                        break;
                    case 31:
                        cardList.add(Card.S7);
                        break;
                    case 32:
                        cardList.add(Card.S8);
                        break;
                    case 33:
                        cardList.add(Card.S9);
                        break;
                    case 34:
                        cardList.add(Card.S10);
                        break;
                    case 35:
                        cardList.add(Card.SJ);
                        break;
                    case 36:
                        cardList.add(Card.SQ);
                        break;
                    case 37:
                        cardList.add(Card.SK);
                        break;
                    case 38:
                        cardList.add(Card.SA);
                        break;
                    case 39:
                        cardList.add(Card.H2);
                        break;
                    case 40:
                        cardList.add(Card.H3);
                        break;
                    case 41:
                        cardList.add(Card.H4);
                        break;
                    case 42:
                        cardList.add(Card.H5);
                        break;
                    case 43:
                        cardList.add(Card.H6);
                        break;
                    case 44:
                        cardList.add(Card.H7);
                        break;
                    case 45:
                        cardList.add(Card.H8);
                        break;
                    case 46:
                        cardList.add(Card.H9);
                        break;
                    case 47:
                        cardList.add(Card.H10);
                        break;
                    case 48:
                        cardList.add(Card.HJ);
                        break;
                    case 49:
                        cardList.add(Card.HQ);
                        break;
                    case 50:
                        cardList.add(Card.HK);
                        break;
                    case 51:
                        cardList.add(Card.HA);
                        break;
                }
            }
            deck.setDeck(cardList);
            testTable.setTableDeck(deck);

            String str = inMessage.readUTF();
            if (str.equals("NULL")) {
                testTable.setWinner(null);
            }
            else {
                Player player = new Player(str, Integer.parseInt(inMessage.readUTF()),
                        testTable, false);
                int id = Integer.parseInt(inMessage.readUTF());
                switch (id) {
                    case 0:
                        player.setStatus(PlayerStatus.PLAYER_NORMAL);
                        break;
                    case 1:
                        player.setStatus(PlayerStatus.PLAYER_CHECK);
                        break;
                    case 2:
                        player.setStatus(PlayerStatus.PLAYER_CALL);
                        break;
                    case 3:
                        player.setStatus(PlayerStatus.PLAYER_BET);
                        break;
                    case 4:
                        player.setStatus(PlayerStatus.PLAYER_RAISE);
                        break;
                    case 5:
                        player.setStatus(PlayerStatus.PLAYER_FOLD);
                        break;
                    case 6:
                        player.setStatus(PlayerStatus.PLAYER_ALLIN);
                        break;
                    case 7:
                        player.setStatus(PlayerStatus.PLAYER_WINNER);
                        break;
                    case 8:
                        player.setStatus(PlayerStatus.PLAYER_LOST);
                        break;
                    case 9:
                        player.setStatus(PlayerStatus.PLAYER_BUSTED_OUT);
                        break;
                }
                player.setDealer(Boolean.parseBoolean(inMessage.readUTF()));
                player.setCurrentBet(Integer.parseInt(inMessage.readUTF()));
                player.setHand(buildHand());
                player.setBestHand(buildHand());
                player.setKicker(buildHand());
                testTable.setWinner(player);
            }

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
                out.writeUTF(Integer.toString(player.getStatus().getId()));
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
                out.writeUTF(Integer.toString(hand.getHand().get(i).getId()));
            }
            out.writeUTF(Integer.toString(hand.getMaxValue()));
            out.writeUTF(Integer.toString(hand.getMaxValue2()));
            out.writeUTF(Integer.toString(hand.getId().getId()));
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
                out.writeUTF(Integer.toString(deck.getDeck().get(i).getId()));
            }

            if (table.getWinner() != null) {
                out.writeUTF(table.getWinner().getName());
                out.writeUTF(Integer.toString(table.getWinner().getBalance()));
                out.writeUTF(Integer.toString(table.getWinner().getStatus().getId()));
                out.writeUTF(Boolean.toString(table.getWinner().getDealer()));
                out.writeUTF(Integer.toString(table.getWinner().getCurrentBet()));
                sendHand(table.getWinner().getHand());
                sendHand(table.getWinner().getBestHand());
                sendHand(table.getWinner().getKicker());
            } else {
                out.writeUTF("NULL");
            }

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
