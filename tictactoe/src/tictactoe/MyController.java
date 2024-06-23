package tictactoe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Random;

public class MyController {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Button rstBtn;

    @FXML
    private TextArea text;

    @FXML
    private GridPane grdPane;

    private boolean playerXTurn = true;
    private int turnCount = 0;
    private boolean gameEnded = false;
    private ObservableList<String> choices = FXCollections.observableArrayList("Play a friend", "Easy", "Medium", "Impossible");
    private String playerSymbol = "X";
    private String computerSymbol = "O";
    private boolean playerStarts = true;
    private boolean againstComputer = false;

    @FXML
    void pressRst(ActionEvent event) {
        initializeGame();
    }

    public void initConfig() {
        choiceBox.setValue("Play a friend");
        choiceBox.setItems(choices);
        choiceBox.setOnAction(this::handleChoiceSelection);
    }

    public void initializeGame() {
        turnCount = 1;
        gameEnded = false;

        String choice = choiceBox.getValue();
        againstComputer = !choice.equals("Play a friend");

        if (againstComputer) {
            // Ensure different symbols for player and computer
            if (Math.random() < 0.5) {
                playerSymbol = "X";
                computerSymbol = "O";
            } else {
                playerSymbol = "O";
                computerSymbol = "X";
            }

            // Randomly decide who starts
            playerStarts = Math.random() < 0.5;
            playerXTurn = playerStarts;
        } else {
            playerSymbol = "X";
            computerSymbol = "O";
            playerStarts = true;
            playerXTurn = true;
        }

        text.setText("Game Start!\n");
        if (playerStarts) {
            text.appendText("Player 1 starts as " + playerSymbol + "\n");
        } else {
            text.appendText("Computer starts as " + computerSymbol + "\n");
        }
        
        if(againstComputer)
        	text.appendText("Turn " + turnCount + ": " + (playerXTurn ? "Player's" : "Computer's") + " turn\n");
        else
        	text.appendText("Turn " + turnCount + ": " + (playerXTurn ? "Player 1's" : "Player 2's") + " turn\n");        

        grdPane.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText("");
                button.setFont(new Font(24));
                button.setTextFill(Color.BLACK);
                button.setDisable(false);
                button.setFocusTraversable(false);
                button.setOnAction(this::handleButtonClick);
            }
        });

        if (againstComputer && !playerStarts) {
            makeComputerMove();
        }
    }

    public void handleChoiceSelection(ActionEvent event) {
        String choice = choiceBox.getValue();
        initializeGame();
    }

    private void handleButtonClick(ActionEvent event) {
        if (gameEnded) return;

        Button clickedButton = (Button) event.getSource();

        if (clickedButton.getText().isEmpty()) {
            if (playerXTurn) {
                clickedButton.setText(playerSymbol);
                clickedButton.setTextFill(playerSymbol.equals("X") ? Color.RED : Color.BLUE);
            } else {
                clickedButton.setText(againstComputer ? computerSymbol : (playerSymbol.equals("X") ? "O" : "X"));
                clickedButton.setTextFill((againstComputer ? computerSymbol : (playerSymbol.equals("X") ? "O" : "X")).equals("X") ? Color.RED : Color.BLUE);
            }
            clickedButton.setDisable(true);

            turnCount++;
            checkForWin();

            if (!gameEnded) {
                playerXTurn = !playerXTurn;
                if(againstComputer)
                	text.appendText("Turn " + turnCount + ": " + (playerXTurn ? "Player's" : "Computer's") + " turn\n");
                else
                	text.appendText("Turn " + turnCount + ": " + (playerXTurn ? "Player 1's" : "Player 2's") + " turn\n");

                if (againstComputer && !playerXTurn) {
                    makeComputerMove();
                }
            }
        }
    }

    private void checkForWin() {
        String[][] board = new String[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = (Button) grdPane.getChildren().get(row * 3 + col);
                board[row][col] = button.getText();
            }
        }

        if (isWinner(board, playerSymbol)) {
            text.appendText("Player wins!\n");
            disableAllButtons();
            gameEnded = true;
        } else if (isWinner(board, computerSymbol)) {
            text.appendText("Computer wins!\n");
            disableAllButtons();
            gameEnded = true;
        } else if (isBoardFull(board)) {
            text.appendText("It's a draw!\n");
            gameEnded = true;
        }
    }

    private boolean isWinner(String[][] board, String player) {
        for (int row = 0; row < 3; row++) {
            if (board[row][0].equals(player) && board[row][1].equals(player) && board[row][2].equals(player)) {
                return true;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (board[0][col].equals(player) && board[1][col].equals(player) && board[2][col].equals(player)) {
                return true;
            }
        }

        if (board[0][0].equals(player) && board[1][1].equals(player) && board[2][2].equals(player)) {
            return true;
        }

        if (board[0][2].equals(player) && board[1][1].equals(player) && board[2][0].equals(player)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull(String[][] board) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void disableAllButtons() {
        grdPane.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        });
    }

    private void makeComputerMove() {
        if (gameEnded) return;

        // Logic to make computer move based on selected difficulty
        String choice = choiceBox.getValue();
        if (choice.equals("Easy")) {
            makeRandomMove();
        } else if (choice.equals("Medium")) {
            if (Math.random() < 0.5) {
                makeRandomMove();
            } else {
                makeBestMove();
            }
        } else if (choice.equals("Impossible")) {
            makeBestMove();
        }

        turnCount++;
        checkForWin();
        if (!gameEnded) {
            playerXTurn = !playerXTurn;
            text.appendText("Turn " + turnCount + ": " + (playerXTurn ? "Player's" : "Computer's") + " turn\n");
        }
    }

    private void makeRandomMove() {
        Random rand = new Random();
        int row, col;
        Button button;
        do {
            row = rand.nextInt(3);
            col = rand.nextInt(3);
            button = (Button) grdPane.getChildren().get(row * 3 + col);
        } while (!button.getText().isEmpty());
        button.setText(computerSymbol);
        button.setTextFill(computerSymbol.equals("X") ? Color.RED : Color.BLUE);
        button.setDisable(true);
    }

    private void makeBestMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        String[][] board = new String[3][3];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button button = (Button) grdPane.getChildren().get(row * 3 + col);
                board[row][col] = button.getText();
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col].isEmpty()) {
                    board[row][col] = computerSymbol;
                    int score = minimax(board, false);
                    board[row][col] = "";
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = row;
                        bestCol = col;
                    }
                }
            }
        }

        Button button = (Button) grdPane.getChildren().get(bestRow * 3 + bestCol);
        button.setText(computerSymbol);
        button.setTextFill(computerSymbol.equals("X") ? Color.RED : Color.BLUE);
        button.setDisable(true);
    }

    private int minimax(String[][] board, boolean isMaximizing) {
        if (isWinner(board, computerSymbol)) return 1;
        if (isWinner(board, playerSymbol)) return -1;
        if (isBoardFull(board)) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col].isEmpty()) {
                        board[row][col] = computerSymbol;
                        int score = minimax(board, false);
                        board[row][col] = "";
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (board[row][col].isEmpty()) {
                        board[row][col] = playerSymbol;
                        int score = minimax(board, true);
                        board[row][col] = "";
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }
}
