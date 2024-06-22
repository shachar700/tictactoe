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

    @FXML
    void pressRst(ActionEvent event) {
        initializeGame();
    }

    public void initializeGame() {
        playerXTurn = true;
        int tmp = (int) ((Math.random() * 2) + 1);
        if (tmp == 1) {
            playerXTurn = false;
            text.setText("Player O's turn");
        } else {
            text.setText("Player X's turn");
        }

        grdPane.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setText("");
                button.setFont(new Font(24));
                button.setTextFill(Color.BLACK);
                button.setDisable(false);
                button.setOnAction(this::handleButtonClick);
            }
        });
    }

    private void handleButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (playerXTurn) {
            clickedButton.setText("X");
            clickedButton.setTextFill(Color.RED);
            text.setText("Player O's turn");
        } else {
            clickedButton.setText("O");
            clickedButton.setTextFill(Color.BLUE);
            text.setText("Player X's turn");
        }
        clickedButton.setDisable(true);
        playerXTurn = !playerXTurn;

        checkForWin();
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
            text.setText("Player X wins!");
            disableAllButtons();
        } else if (isWinner(board, "O")) {
            text.setText("Player O wins!");
            disableAllButtons();
        } else if (isBoardFull(board)) {
            text.setText("It's a draw!");
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
