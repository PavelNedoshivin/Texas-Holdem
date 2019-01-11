package org.suai.poker.graphics;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.suai.poker.model.*;
import org.suai.poker.network.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainController implements Initializable {

	@FXML
	public RadioButton isRegister;
	@FXML
	public TextField regLogin;
	@FXML
	public PasswordField regPassword;
	@FXML
	public PasswordField regCheck;
	@FXML
	public TextField regName;
	@FXML
	public Button submitButton;
	@FXML
	public Label authLabel;
	@FXML
	public RadioButton isLogin;
	@FXML
	public TextField logLogin;
	@FXML
	public PasswordField logPassword;
	@FXML
	public VBox regBox;
	@FXML
	public VBox logBox;
	@FXML
	public HBox authBox;

	public MainController() {
	}

	@FXML
	private BorderPane border;

	@FXML
	private Label playerStats;

	@FXML
	private Label playerBet;

	@FXML
	private Slider betSlider;

	@FXML
	private Button actionButton0;

	@FXML
	private Button actionButton1;

	@FXML
	private Button actionButton2;

	@FXML
	private Button actionButton3;

	@FXML
	private HBox hboxBottom;

	@FXML
	private MenuItem statisticsMenuItem;
	
	@FXML
	private MenuItem helpMenuItem;

	@FXML
	private MenuItem aboutMenuItem;

	@FXML
	private VBox vbox;
	@FXML
	private Button button1;
	@FXML
	private Button button2;
	@FXML
	private Button button3;
	@FXML
	private Button button4;
	@FXML
	private Button button5;

	private Table table;
	private List<Image> cardImage;
	private Image icon;
	private Client client;
	private int chosenTable;
	private boolean radioEnter;
	private static String playerName;
	private static UpdateThread updateThread;
	private static boolean isStarted;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadImages();
		updateAction1();
		betSlider.setMajorTickUnit(100);
		betSlider.setMinorTickCount(0);
		betSlider.setSnapToTicks(true);
		betSlider.valueProperty().addListener((ov, old_val, new_val) -> {
			playerBet.setText("$" + new_val.intValue());
			updateAction3();
		});
		chosenTable = -1;
		isStarted = false;
		button1.setVisible(false);
		button2.setVisible(false);
		button3.setVisible(false);
		button4.setVisible(false);
		button5.setVisible(false);
		ToggleGroup group = new ToggleGroup();
		isRegister.setToggleGroup(group);
		isRegister.setSelected(true);
		isLogin.setToggleGroup(group);
		radioEnter = true;
		group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
			if (group.getSelectedToggle() != null) {
				RadioButton button = (RadioButton)group.getSelectedToggle();
				radioEnter = !button.getText().equals("Login");
			}
		});
		try {
			client = new Client();
			client.start();
			table = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void authentificate() {
		boolean label = false;
		if (radioEnter) {
			String password = regPassword.getText();
			String check = regCheck.getText();
			if (!(password.equals(check)) && (password != null)) {
				authLabel.setText("Wrong register: wrong check!");
				label = true;
			} else {
				String login = regLogin.getText();
				String name = regName.getText();
				if ((name != null) && (login != null)) {
					client.setMode(true);
					client.setRequest(login, password);
					client.setName(name);
					playerName = name;
				}
			}
		} else {
			String login = logLogin.getText();
			String password = logPassword.getText();
			if ((login != null) && (password != null)) {
				client.setMode(false);
				client.setRequest(login, password);
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int val = client.getSuccess();
		while (val < 0) {
			val = client.getSuccess();
		}
		if (val == 0) {
			if (!radioEnter) {
				playerName = client.getName();
			}
			LinkedList obj = client.getTableNumbers();
			button1.setText("Table 1 (" + obj.get(0) + "/5)");
			button1.setVisible((int)obj.get(0) < 5);
			button2.setText("Table 2 (" + obj.get(1) + "/5)");
			button2.setVisible((int)obj.get(1) < 5);
			button3.setText("Table 3 (" + obj.get(2) + "/5)");
			button3.setVisible((int)obj.get(2) < 5);
			button4.setText("Table 4 (" + obj.get(3) + "/5)");
			button4.setVisible((int)obj.get(3) < 5);
			button5.setText("Table 5 (" + obj.get(4) + "/5)");
			button5.setVisible((int)obj.get(4) < 5);
			logBox.setVisible(false);
			regBox.setVisible(false);
			authBox.setVisible(false);
		} else {
			if (radioEnter && !label) {
				authLabel.setText("Enter another name and/or login!");
			} else {
				authLabel.setText("Wrong login or password!");
			}
		}
	}

	@FXML
	public void onClickButton1() {
		chosenTable = 0;
		client.setChosen(chosenTable);
		invisibleButtons();
		initTable();
	}

	@FXML
	public void onClickButton2() {
		chosenTable = 1;
		client.setChosen(chosenTable);
		invisibleButtons();
		initTable();
	}

	@FXML
	public void onClickButton3() {
		chosenTable = 2;
		client.setChosen(chosenTable);
		invisibleButtons();
		initTable();
	}

	@FXML
	public void onClickButton4() {
		chosenTable = 3;
		client.setChosen(chosenTable);
		invisibleButtons();
		initTable();
	}

	@FXML
	public void onClickButton5() {
		chosenTable = 4;
		client.setChosen(chosenTable);
		invisibleButtons();
		initTable();
	}

	public void invisibleButtons() {
		button1.setVisible(false);
		button2.setVisible(false);
		button3.setVisible(false);
		button4.setVisible(false);
		button5.setVisible(false);
		vbox.setVisible(false);
	}

	public void loadImages(){
		icon = new Image("org/suai/poker/graphics/image/ico16.png");
		cardImage = new ArrayList<>();
		for (Integer i = 0; i < 52; i++) {
			cardImage.add(new Image("org/suai/poker/graphics/image/" + Card.getCard(i).getImagePath() + ".png"));
		}
		cardImage.add(new Image("org/suai/poker/graphics/image/b1fv.png"));
		cardImage.add(new Image("org/suai/poker/graphics/image/ec.png"));
	}

	public void statistics() throws IOException{
		if (table != null && isStarted) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/statistics.fxml"));
			Parent root = (Parent) loader.load();
			StatisticsController statControl = loader.getController();
			statControl.setStatistics(table);
			Stage statistics = new Stage();
			setupStage(statistics, new Scene(root), "Statistics");
			statisticsMenuItem.setOnAction(event ->{
				statistics.requestFocus();
			});
			statistics.setOnCloseRequest(event -> {
				statisticsMenuItem.setOnAction(event2 -> {
					try {
						statistics();
					} catch (Exception e) {
						e.printStackTrace();
					}
				});				
			});
		} else {
			MessageWindow.show("Error", "Unable to show statistics, table is not loaded.");
		}
	}

	public void help() {
		Stage help = new Stage();
		BorderPane border = new BorderPane();
		Hyperlink link = new Hyperlink("http://en.wikipedia.org/wiki/Texas_hold_'em");
		link.setOnAction(event -> {
			Stage webStage = new Stage();
			WebView web = new WebView();
			web.getEngine().load(link.getText());
			setupStage(webStage, (new Scene(web)), "Help");
		});
		VBox box = new VBox();
		box.getChildren().add(new Label("Just visit following site:"));
		box.getChildren().add(link);
		box.setAlignment(Pos.CENTER);
		border.setCenter(box);
		setupStage(help, new Scene(border), "Help");
		helpMenuItem.setOnAction(event ->{
			help.requestFocus();
		});
		help.setOnCloseRequest(event -> {
			helpMenuItem.setOnAction(event2 -> {
				help();
			});				
		});
		
	}

	public void about() {
		Stage about = new Stage();
		BorderPane border = new BorderPane();
		GridPane grid = new GridPane();
		Image aboutCard = cardImage.get(53);
		Label info = new Label("Texas Holdem \nby Pavel Nedoshivin, SUAI");
		info.setPadding(new Insets(0,10,0,20));
		info.setTextAlignment(TextAlignment.CENTER);
		ImageView imageView = new ImageView(aboutCard);
		imageView.setOnMouseClicked(event -> {
			shuffleImage(imageView, about);
			imageView.setDisable(true);
		});
		grid.add(imageView, 0, 0);
		grid.add(info, 1, 0);
		grid.setHgap(10);
		grid.setAlignment(Pos.CENTER);
		border.setCenter(grid);
		setupStage(about, new Scene(border), "About");
		aboutMenuItem.setOnAction(event ->{
			about.requestFocus();
		});
		about.setOnCloseRequest(event -> {
			aboutMenuItem.setOnAction(event2 -> {
				about();
			});				
		});
	}

	public void shuffleImage(ImageView imageView, Stage stage) {
		ExecutorService service = Executors.newFixedThreadPool(1);
		for (int i = 0; i < 1; i++) {
			service.execute(new ImageScroller(stage, imageView, cardImage));
		}
		service.shutdown();
	}

	public void updateTopPane() {
		GridPane paneTop = new GridPane();
		for (Integer i = 0; i < table.getPlayerSize(); i++) {
			paneTop.add(getCardFlowPane(table.getPlayerHand(i), table.getPlayer(i).isNotPlaying()
				|| !(table.getPlayer(i).getName().equals(playerName))), i, 1);
			String inform = table.getPlayerName(i)
					+ (table.getDealerPos() == i ? " (D)" : "")
					+ (table.getTurnPos() == i ? " (T)" : "")
					+ " - " + "$"
					+ (table.getPlayerBalance(i) - table.getPlayerList().get(i).getCurrentBet()) + "\n";
			if (table.getPlayer(i).getName().equals(playerName)) {
				inform += (table.getPlayer(i).isBustedOut() ? "" : (table.getPlayer(i).getBestHand().getId()))
						+ (table.getPlayer(i).getStatus() == PlayerStatus.PLAYER_NORMAL ? "" : " ("+ table.getPlayer(i).getStatus()+")");
			}
			Label info = newLabel(inform);
			info.setMaxWidth(Integer.MAX_VALUE);
			info.setAlignment(Pos.CENTER);
			info.setTextAlignment(TextAlignment.CENTER);
			paneTop.add(info, i, 0);
		}
		paneTop.setAlignment(Pos.TOP_CENTER);
		border.setTop(paneTop);
	}

	public void updateCenterPane() {
		VBox box = new VBox();
		FlowPane paneCenter = getCardFlowPane(table.getTableHand(), false);
		paneCenter.setAlignment(Pos.CENTER);
		box.getChildren().add(paneCenter);
		if (table.getCurrentTurn() == 5) {

			Label winnerHand = newLabel("Winner: "
					+ table.getWinner().getName() + " ("
					+ table.getWinner().getBestHand().getId() + ")");
			winnerHand.setPadding(new Insets(5, 0, 5, 0));
			box.getChildren().add(winnerHand);
			FlowPane paneWin = getCardFlowPane(table.getWinner().getBestHand(),	false);
			paneWin.setAlignment(Pos.CENTER);
			box.getChildren().add(paneWin);
		}
		box.setAlignment(Pos.CENTER);
		border.setCenter(box);
	}

	public void updateBottomPane() {
		if (table.getPlayerOnTurn().getName().equals(playerName))
		{
			FlowPane paneBottom = getCardFlowPane(table.getPlayerOnTurn().getHand(), false);
			paneBottom.setAlignment(Pos.BOTTOM_CENTER);
			border.setBottom(paneBottom);
		}
	}

	public void updatePlayerDetails() {
		String potDetails = "";
		if (!table.getPot().isEmpty()) {
			potDetails = " Pot: ";
		}
		for (Pot pot : table.getPot()) {
			potDetails += "$" + pot.getAmount().toString() + " (" + pot.getPlayerList().size() + " players)"
					+ ((table.getPot().indexOf(pot) == table.getPot().size()-1) ? " " : ", ");
		}
		playerStats.setText("Current turn: " + table.getPlayerOnTurn().getName() + potDetails);
		betSlider.setMax(table.getPlayerOnTurn().getBalance());
	}

	public void updateButtonState() {
		if (table.isTurnDone()) {
			toggleButtons(false, true, true, true, true);
		} else {
			toggleButtons(false, false, table.isPlayerUnableToCall(), false, table.isPlayerUnableToCall());
		}
	}

	public void toggleButtons(Boolean action0, Boolean action1,	Boolean action2, Boolean action3, Boolean action4) {
		actionButton0.setDisable(action0);
		actionButton1.setDisable(action1);
		actionButton2.setDisable(action2);
		actionButton3.setDisable(action3);
		betSlider.setDisable(action4);
	}

	public void viewActionButtons(boolean visible) {
		actionButton0.setVisible(visible);
		actionButton1.setVisible(visible);
		actionButton2.setVisible(visible);
		actionButton3.setVisible(visible);
	}

	/**
	 * Updates action of first button (Deal / Next / Think).
	 */
	public void updateAction0() {
		if (table.isRoundOver()) {
			actionButton0.setText("Deal");
			actionButton0.setOnAction(event -> {
				deal();
			});
		} else if (table.isNextTurnReady()) {
			actionButton0.setText("Next");
			actionButton0.setOnAction(event -> {
				nextTurn();
			});
		// Otherwise, use it as an option to think.
		} else {
			actionButton0.setText("Think");
			actionButton0.setOnAction(event -> {
				think();
			});
		}
		if (table.getPlayerOnTurn().getName().equals(playerName)) {
			viewActionButtons(true);
		} else {
			viewActionButtons(false);
		}
	}

	public void updateAction1() {
		actionButton1.setText("Fold");
		actionButton1.setOnAction(event -> {
			fold();
		});
	}

	public void updateAction2() {
		if (table.isCheckAllowed()) {
			actionButton2.setText("Check");
			actionButton2.setOnAction(event -> {
				check();
			});
		} else if (table.isCallAllowed()) {
			actionButton2.setText("Call");
			actionButton2.setOnAction(event -> {
				call();
			});
		}
	}

	public void updateAction3() {
		Double value = betSlider.getValue();
		if (table.getPlayerOnTurn().getBalance() == value.intValue()) {
			actionButton3.setText("All in");
			actionButton3.setOnAction(event -> {
				allIn();
			});
		} else if (betSlider.getValue() > table.getCurrentBet()	&& table.isRaiseAllowed()) {
			actionButton3.setText("Raise");
			actionButton3.setOnAction(event -> {
				raise();
			});
		} else if (table.isBetAllowed()) {
			actionButton3.setText("Bet");
			actionButton3.setOnAction(event -> {
				bet();
			});
		}
	}

	synchronized public void update() {
		updateThread.sendTable();
		updateTopPane();

		updateCenterPane();

		updateBottomPane();

		updatePlayerDetails();

		updateButtonState();

		updateAction0();
		updateAction1();
		updateAction2();
		updateAction3();

	}

	private static class ReceiveThread extends Thread {
		private MainController mc;
		public ReceiveThread(MainController o) {
			mc = o;
		}
		@Override
		public void run() {
			while (true) {
				boolean hasChanged = false;
				Table o = null;
				while (!hasChanged) {
					o = mc.client.getTable();
					hasChanged = !(mc.table.equals(o));
				}
				mc.table = o;
				Platform.setImplicitExit(true);
				if (isStarted) {
					Platform.runLater(() -> {
						synchronized (mc) {
							mc.updateTopPane();
							mc.updateCenterPane();
							mc.updateBottomPane();
							mc.updatePlayerDetails();
							mc.updateButtonState();
							mc.updateAction0();
							mc.updateAction1();
							mc.updateAction2();
							mc.updateAction3();
						}
					});
				}
			}
		}
	}

	private static class UpdateThread extends Thread {
	    private MainController mc;
	    private boolean sent;
	    public UpdateThread(MainController o) {
	        mc = o;
	        sent = true;
	        ReceiveThread rt = new ReceiveThread(mc);
			rt.start();
        }
        public void sendTable() {
	    	sent = false;
		}
	    @Override
        public void run() {
	        while (true) {
				while (sent) {
					;
				}
				mc.client.setTable(mc.table);
				sent = true;
            }
        }
    }

	public FlowPane getCardFlowPane(Hand hand, Boolean hide) {
		FlowPane paneCards = new FlowPane();
		for (Integer j = 0; j < hand.getSize(); j++) {
			Image card = hide ? cardImage.get(52) : cardImage.get(hand.getCard(j).getId());
			paneCards.getChildren().add(new ImageView(card));
		}
		paneCards.setAlignment(Pos.CENTER);
		paneCards.setHgap(5);
		paneCards.setVgap(5);
		return paneCards;
	}

	public Label newLabel(String string) {
		Label label = new Label(string);
		label.setTextFill(Color.WHITE);
		return label;
	}

	public void initTable() {
        table = null;
        while (table == null) {
            table = client.getTable();
        }
        updateThread = new UpdateThread(this);
        updateThread.start();
    }

	public void start() {
		if (table != null) {
			isStarted = true;
			hboxBottom.setVisible(true);
			betSlider.setDisable(false);
			if (table.getCurrentTurn() == 0)
			{
				nextTurn();
			}
			update();
			betSlider.setMin(table.getBigBlind() * 2);
			betSlider.setValue(table.getBigBlind() * 2);
		}
	}

	public void deal() {
		if (table.isGameOver()) {
			start();
		} else {
			table.resetRound();
			table.setPositions();
			nextTurn();
			update();
			betSlider.setMin(table.getBigBlind() * 2);
			betSlider.setValue(table.getBigBlind() * 2);
		}
	}

	public void nextTurn() {
		table.nextTableTurn();
		update();
		betSlider.setMin(table.getBigBlind());
		betSlider.setValue(table.getBigBlind());
	}

	public void fold() {
		table.getPlayerOnTurn().fold();
		update();
	}

	public void check() {
		table.getPlayerOnTurn().check();
		update();
	}

	public void call() {
		table.getPlayerOnTurn().call();
		update();
	}

	public void bet() {
		Double betValue = betSlider.getValue();
		table.getPlayerOnTurn().bet(betValue.intValue());
		betSlider.setMin(betSlider.getValue() * 2);
		update();
	}

	public void raise() {
		Double betValue = betSlider.getValue();
		table.getPlayerOnTurn().raise(betValue.intValue());
		betSlider.setMin(betSlider.getValue() * 2);
		update();
	}

	public void allIn() {
		table.getPlayerOnTurn().allIn();
		update();
	}

	/**
	 * Think.
	 */
	public void think() {
		table.getPlayerOnTurn().think();
		update();
	}

	public void setupStage (Stage stage, Scene scene, String title){
		stage.setResizable(false);
		stage.setTitle(title);
		stage.getIcons().add(icon);
		stage.setScene(scene);
		stage.show();
	}	
	
	public Table getTable(){
		return table;
	}
}
