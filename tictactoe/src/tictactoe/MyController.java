package tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class MyController {

    @FXML
    private GridPane grdPane;

    @FXML
    private Button rstBtn;

    @FXML
    private TextArea text;

    private boolean playerXTurn = true;
    private int turnCount = 0;
    private boolean gameEnded = false;

    @FXML
    void pressRst(ActionEvent event) {
        initializeGame();
    }

    public void initializeGame() {
        playerXTurn = true;
        turnCount = 1;
        gameEnded = false;
        int tmp = (int) ((Math.random() * 2) + 1);
        text.setText("Game Start!\n");
        if (tmp == 1) {
            playerXTurn = false;
            text.appendText("Turn 1: Player O's turn\n");
        } else {
            text.appendText("Turn 1: Player X's turn\n");
        }

        grdPane.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText("");
                button.setFont(new Font(24));
                button.setTextFill(Color.BLACK);
                button.setDisable(false);
                button.setFocusTraversable(false); // Ensure buttons are not focusable by default
                button.setOnAction(this::handleButtonClick);
            }
        });
    }

    private void handleButtonClick(ActionEvent event) {
    	if (gameEnded) return;
    	
        Button clickedButton = (Button) event.getSource();
        
        turnCount++;    
        
        if (playerXTurn) {
            clickedButton.setText("X");
            clickedButton.setTextFill(Color.RED);    
        } else {
            clickedButton.setText("O");
            clickedButton.setTextFill(Color.BLUE);
        }        
        clickedButton.setDisable(true);
        
        checkForWin();
        
        if (!gameEnded) {
            playerXTurn = !playerXTurn;
            text.appendText("Turn " + turnCount + ": " + (playerXTurn ? "Player X's" : "Player O's") + " turn\n");
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

        if (isWinner(board, "X")) {
            text.appendText("Player X wins!\n");
            disableAllButtons();
            gameEnded = true;
        } else if (isWinner(board, "O")) {
            text.appendText("Player O wins!\n");
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

}
