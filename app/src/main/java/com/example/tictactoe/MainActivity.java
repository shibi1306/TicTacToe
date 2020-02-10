package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.util.Log;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {
    // to switch determine the player's turn
    protected boolean player1turn;
    // count of total turns by both players combined.
    protected int total_turns;

    protected int []blockIds = new int[]{R.id.imageView, R.id.imageView2, R.id.imageView3,
                            R.id.imageView4, R.id.imageView5, R.id.imageView6,
                            R.id.imageView7, R.id.imageView8, R.id.imageView9};

    // values related to these indices of each row will be compared. Check: Left to right, Top to bottom, Cross
    protected int [][]victoryIndices = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                                {0, 4, 8}, {2, 4, 6}};

    // The player moves will be recorded into this board. 0-player1, 1-player2, -1-Empty
    protected int []board;
    // Player coin images
    private int []playerCoin = new int[] {R.drawable.red, R.drawable.yellow};

    // Widgets
    TextView playerTurnDisplayer;
    ImageView []block = new ImageView[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiallizeVariables();
        initializeWidgets();
    }

    private void initializeWidgets(){
        playerTurnDisplayer = (TextView) findViewById(R.id.PlayerTurnDisplayer);
        for(int i = 0; i < 9; i += 1){
            block[i] = (ImageView) findViewById(blockIds[i]);
        }
    }
    // This function is also useful to restart the game
    private void initiallizeVariables(){
        board = new int[]{-1, -1, -1,
                            -1, -1, -1,
                            -1, -1, -1};
        player1turn = true;
        total_turns = 0;
    }

    // This function is called everytime an Image in the board is tapped.
    public void playerMove(View view){
        Log.i("Action performed", "Action: Block tapped");
        int playerIndex = player1turn? 0 : 1;
        total_turns += 1;

        // Changes to the image
        ImageView tappedBlock = (ImageView) view;
        tappedBlock.setImageResource(playerCoin[playerIndex]);
        tappedBlock.setAlpha(1f);
        tappedBlock.setEnabled(false);

        // Recording the player value in the board
        int boardIndex = getBlockIndex(tappedBlock.getId());
        board[boardIndex] = playerIndex;

        //Checking the board for potential winner
        boolean resultsDeclared = checkForWinner();
        // This if statement get failed when any player is declared winner or Draw has been declared
        if(!resultsDeclared){
            changePlayerTurn();
            changePlayerTurnDisplayer();
        }
    }

    // checks the board for a possibility of a winner
    private boolean checkForWinner(){
        if(total_turns < 5){ // It is impossible to connect 3 with less than 5 turns played by both players combined.
            return false;
        }
        // Compares the indexes according to the victory indices.
        for(int i = 0; i < 8; i += 1){
            int index1 = victoryIndices[i][0];
            int index2 = victoryIndices[i][1];
            int index3 = victoryIndices[i][2];
            if(board[index1] != -1 && board[index1] == board[index2] && board[index2] == board[index3]){
                declareResults(board[index1]); // player number is sent. 0 - player1 , 1 - player 2
                return true;
            }
        }
        if(total_turns == 9){ // Implies the game is Draw and no more turns can be done.
            declareResults(-2); // -2 is the state for Draw.
            return true;
        }
        return false; // No results are declared yet and the game must go on.
    }

    private void declareResults(int result){
        if(result == -2){ // Match is draw
            playerTurnDisplayer.setText("DRAW!!!");
            playerTurnDisplayer.setTextColor(Color.DKGRAY);
        }
        else{ // We have a winner
            playerTurnDisplayer.setText("Player " + Integer.toString(result + 1) + " wins!!!");
            playerTurnDisplayer.setTextColor(Color.DKGRAY);
            setEnableAllBlocks(false); // disables all the blocks to deny any further moves.
        }
    }
    // enables or disables all the tictactoe blocks.
    private void setEnableAllBlocks(boolean state){
        for(int i = 0; i < 9; i += 1){
            block[i].setEnabled(state);
        }
    }

    // sets one alpha value to all blocks of the tictactoe.
    private void setAlphaAllBlocks(float alpha){
        for(int i = 0; i < 9; i += 1){
            block[i].setAlpha(alpha);
        }
    }
    // Gets the block index for the imageView tapped by the player to record the player move.
    private int getBlockIndex(int blockId){
        for(int i = 0; i < 9; i += 1){
            if(blockIds[i] == blockId){
                return i;
            }
        }
        return -1;
    }
    // displays the player's turn in the textView.
    private void changePlayerTurnDisplayer(){
        String textColor;
        if (player1turn){
            playerTurnDisplayer.setText("Player 1's turn");
            textColor = "#673AB7";
            playerTurnDisplayer.setTextColor(Color.parseColor(textColor));
        }
        else{
            playerTurnDisplayer.setText("Player 2's turn");
            textColor = "#FF5722";
            playerTurnDisplayer.setTextColor(Color.parseColor(textColor));
        }
    }
    //Switches the player's turn
    private void changePlayerTurn(){
        player1turn = player1turn? false : true;
    }

    // This function is called when restart game button is tapped. It resets all the values to start a new game.
    public void restartGame(View view){
        Log.i("Event", "Restart game");
        initiallizeVariables();
        setAlphaAllBlocks(0f);
        setEnableAllBlocks(true);
        player1turn = true;
        changePlayerTurnDisplayer();
    }
}
