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
            int num = 0;
            LinkedList list = new LinkedList();
            for (Object o: tableList.entrySet()) {
                Map.Entry pair = (Map.Entry)o;
                if (num == chosen) {
                    list = (LinkedList)pair.getValue();
                    break;
                }
                num++;
            }
            for (Object o: userList.entrySet()) {
                Map.Entry pair = (Map.Entry)o;
                String name = (String)pair.getKey();
                if (name.equals(this.name)) {
                    continue;
                }
                boolean flag = false;
                ListIterator it = list.listIterator();
                while (it.hasNext()) {
                    String playerName = (String)it.next();
                    if (name.equals(playerName)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    ServerOutputThread user = (ServerOutputThread)pair.getValue();
                    user.setTable(table);
                }
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
                int id = Integer.parseInt(dataInputStream.readUTF());
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
            hand.setMaxValue(Integer.parseInt(dataInputStream.readUTF()));
            hand.setMaxValue2(Integer.parseInt(dataInputStream.readUTF()));
            int id = Integer.parseInt(dataInputStream.readUTF());
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
            int size = Integer.parseInt(dataInputStream.readUTF());
            List<Player> list = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                Player player = new Player(dataInputStream.readUTF(), Integer.parseInt(dataInputStream.readUTF()),
                        testTable, false);
                int id = Integer.parseInt(dataInputStream.readUTF());
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
                int id = Integer.parseInt(dataInputStream.readUTF());
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

            String str = dataInputStream.readUTF();
            if (str.equals("NULL")) {
                testTable.setWinner(null);
            }
            else {
                Player player = new Player(str, Integer.parseInt(dataInputStream.readUTF()),
                        testTable, false);
                int id = Integer.parseInt(dataInputStream.readUTF());
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
                player.setDealer(Boolean.parseBoolean(dataInputStream.readUTF()));
                player.setCurrentBet(Integer.parseInt(dataInputStream.readUTF()));
                player.setHand(buildHand());
                player.setBestHand(buildHand());
                player.setKicker(buildHand());
                testTable.setWinner(player);
            }

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
                        Player p = new Player(name, 20000, table, false);
                        table.addPlayer(p);
                        table.setPositions();
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
                dataOutputStream.writeUTF(Integer.toString(player.getStatus().getId()));
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
                dataOutputStream.writeUTF(Integer.toString(hand.getHand().get(i).getId()));
            }
            dataOutputStream.writeUTF(Integer.toString(hand.getMaxValue()));
            dataOutputStream.writeUTF(Integer.toString(hand.getMaxValue2()));
            dataOutputStream.writeUTF(Integer.toString(hand.getId().getId()));
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
                dataOutputStream.writeUTF(Integer.toString(deck.getDeck().get(i).getId()));
            }

            if (table.getWinner() != null) {
                dataOutputStream.writeUTF(table.getWinner().getName());
                dataOutputStream.writeUTF(Integer.toString(table.getWinner().getBalance()));
                dataOutputStream.writeUTF(Integer.toString(table.getWinner().getStatus().getId()));
                dataOutputStream.writeUTF(Boolean.toString(table.getWinner().getDealer()));
                dataOutputStream.writeUTF(Integer.toString(table.getWinner().getCurrentBet()));
                sendHand(table.getWinner().getHand());
                sendHand(table.getWinner().getBestHand());
                sendHand(table.getWinner().getKicker());
            } else {
                dataOutputStream.writeUTF("NULL");
            }
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
