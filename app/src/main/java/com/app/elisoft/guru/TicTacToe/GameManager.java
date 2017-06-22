package com.app.elisoft.guru.TicTacToe;


import android.util.Log;

import com.app.elisoft.guru.Table.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.app.elisoft.guru.TicTacToe.Sign.EMPTY;
import static com.app.elisoft.guru.TicTacToe.Sign.O;
import static com.app.elisoft.guru.TicTacToe.Sign.X;

public class GameManager {

    private static final String TAG = "GameManager";
    private static GameManager instance;

    private static final int GAME_SIZE = 3;
    private int moveNumber;
    private static Item[][] matrix;

    User oppSeed, mySeed;

    //array possibilities
    int[] second_move_possibility = new int[]{1,3,7,9};

    public static GameManager getInstance(){
        if (instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        initList();

    }

    public void initList() {
        moveNumber = 0;
        matrix = new Item[GAME_SIZE][GAME_SIZE];
        int position = 1;
        for (int i=0; i<GAME_SIZE; i++){
            for (int j=0; j<GAME_SIZE; j++){
                Item item = new Item(position, EMPTY);
                matrix[i][j] = item;
                position++;
            }
        }
    }

    public void initTestList() {
        moveNumber = 0;
        matrix = new Item[GAME_SIZE][GAME_SIZE];
        int position = 1;
        for (int i=0; i<GAME_SIZE; i++){
            for (int j=0; j<GAME_SIZE; j++){
                Sign sign = EMPTY;
                if (position==1) sign = X;
                if (position==2) sign = O;
                if (position==3) sign = X;
                if (position==4) sign = O;
                if (position==5) sign = O;
                if (position==6) sign = EMPTY;
                if (position==7) sign = EMPTY;
                if (position==8) sign = X;
                if (position==9) sign = EMPTY;
                Item item = new Item(position, sign);
                matrix[i][j] = item;
                position++;
            }
        }
    }

    public void setOppSeed(User oppSeed) {
        this.oppSeed = oppSeed;
    }

    public void setMySeed(User mySeed) {
        this.mySeed = mySeed;
    }

    public int flipCoin(){
        Random r = new Random();
        int i1 = r.nextInt(2);
        return i1;
    }

    public boolean checkWin(){
        if (moveNumber<4) return false;
        else {
                 if ( matrix[0][0].getState() == matrix[0][1].getState() && matrix[0][1].getState() == matrix[0][2].getState() && (matrix[0][2].getState() == Sign.X || matrix[0][2].getState() == Sign.O)) return true;
            else if ( matrix[1][0].getState() == matrix[1][1].getState() && matrix[1][1].getState() == matrix[1][2].getState() && (matrix[1][2].getState() == Sign.X || matrix[1][2].getState() == Sign.O)) return true;
            else if ( matrix[2][0].getState() == matrix[2][1].getState() && matrix[2][1].getState() == matrix[2][2].getState() && (matrix[2][2].getState() == Sign.X || matrix[2][2].getState() == Sign.O)) return true;
            else if ( matrix[0][0].getState() == matrix[1][0].getState() && matrix[1][0].getState() == matrix[2][0].getState() && (matrix[2][0].getState() == Sign.X || matrix[2][0].getState() == Sign.O)) return true;
            else if ( matrix[0][1].getState() == matrix[1][1].getState() && matrix[1][1].getState() == matrix[2][1].getState() && (matrix[2][1].getState() == Sign.X || matrix[2][1].getState() == Sign.O)) return true;
            else if ( matrix[0][2].getState() == matrix[1][2].getState() && matrix[1][2].getState() == matrix[2][2].getState() && (matrix[2][2].getState() == Sign.X || matrix[2][2].getState() == Sign.O)) return true;
            else if ( matrix[0][0].getState() == matrix[1][1].getState() && matrix[1][1].getState() == matrix[2][2].getState() && (matrix[2][2].getState() == Sign.X || matrix[2][2].getState() == Sign.O)) return true;
            else if ( matrix[0][2].getState() == matrix[1][1].getState() && matrix[1][1].getState() == matrix[2][0].getState() && (matrix[2][0].getState() == Sign.X || matrix[2][0].getState() == Sign.O)) return true;
        }
        return false;
    }

    public boolean checkWinInBoard(Item[][] gameBoard){

             if ( gameBoard[0][0].getState() == gameBoard[0][1].getState() && gameBoard[0][1].getState() == gameBoard[0][2].getState() && (gameBoard[0][2].getState() == Sign.X || gameBoard[0][2].getState() == Sign.O)) return true;
        else if ( gameBoard[1][0].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[1][2].getState() && (gameBoard[1][2].getState() == Sign.X || gameBoard[1][2].getState() == Sign.O)) return true;
        else if ( gameBoard[2][0].getState() == gameBoard[2][1].getState() && gameBoard[2][1].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.X || gameBoard[2][2].getState() == Sign.O)) return true;
        else if ( gameBoard[0][0].getState() == gameBoard[1][0].getState() && gameBoard[1][0].getState() == gameBoard[2][0].getState() && (gameBoard[2][0].getState() == Sign.X || gameBoard[2][0].getState() == Sign.O)) return true;
        else if ( gameBoard[0][1].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][1].getState() && (gameBoard[2][1].getState() == Sign.X || gameBoard[2][1].getState() == Sign.O)) return true;
        else if ( gameBoard[0][2].getState() == gameBoard[1][2].getState() && gameBoard[1][2].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.X || gameBoard[2][2].getState() == Sign.O)) return true;
        else if ( gameBoard[0][0].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.X || gameBoard[2][2].getState() == Sign.O)) return true;
        else if ( gameBoard[0][2].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][0].getState() && (gameBoard[2][0].getState() == Sign.X || gameBoard[2][0].getState() == Sign.O)) return true;

        return false;
    }

    public Sign getWinningSign(Item[][] gameBoard){
        if      ( gameBoard[0][0].getState() == gameBoard[0][1].getState() && gameBoard[0][1].getState() == gameBoard[0][2].getState() && (gameBoard[0][2].getState() == Sign.X)) return X;
        else if ( gameBoard[1][0].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[1][2].getState() && (gameBoard[1][2].getState() == Sign.X)) return X;
        else if ( gameBoard[2][0].getState() == gameBoard[2][1].getState() && gameBoard[2][1].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.X)) return X;
        else if ( gameBoard[0][0].getState() == gameBoard[1][0].getState() && gameBoard[1][0].getState() == gameBoard[2][0].getState() && (gameBoard[2][0].getState() == Sign.X)) return X;
        else if ( gameBoard[0][1].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][1].getState() && (gameBoard[2][1].getState() == Sign.X)) return X;
        else if ( gameBoard[0][2].getState() == gameBoard[1][2].getState() && gameBoard[1][2].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.X)) return X;
        else if ( gameBoard[0][0].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.X)) return X;
        else if ( gameBoard[0][2].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][0].getState() && (gameBoard[2][0].getState() == Sign.X)) return X;
        else if ( gameBoard[0][0].getState() == gameBoard[0][1].getState() && gameBoard[0][1].getState() == gameBoard[0][2].getState() && (gameBoard[0][2].getState() == Sign.O)) return O;
        else if ( gameBoard[1][0].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[1][2].getState() && (gameBoard[1][2].getState() == Sign.O)) return O;
        else if ( gameBoard[2][0].getState() == gameBoard[2][1].getState() && gameBoard[2][1].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.O)) return O;
        else if ( gameBoard[0][0].getState() == gameBoard[1][0].getState() && gameBoard[1][0].getState() == gameBoard[2][0].getState() && (gameBoard[2][0].getState() == Sign.O)) return O;
        else if ( gameBoard[0][1].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][1].getState() && (gameBoard[2][1].getState() == Sign.O)) return O;
        else if ( gameBoard[0][2].getState() == gameBoard[1][2].getState() && gameBoard[1][2].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.O)) return O;
        else if ( gameBoard[0][0].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][2].getState() && (gameBoard[2][2].getState() == Sign.O)) return O;
        else if ( gameBoard[0][2].getState() == gameBoard[1][1].getState() && gameBoard[1][1].getState() == gameBoard[2][0].getState() && (gameBoard[2][0].getState() == Sign.O)) return O;
        return EMPTY;
    }


    public boolean canClick(int position) {
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                if (matrix[i][j].getPosition() == position) {
                    return matrix[i][j].getState() == EMPTY && (moveNumber < GAME_SIZE * GAME_SIZE);
                }
            }
        }
        return false;
    }

    public void setMove(int position, Sign type){
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                if (matrix[i][j].getPosition() == position) {
                    matrix[i][j].setState(type);
                }
            }
        }
        moveNumber++;
    }

    public Item[][] getMatrix(){
        return matrix;
    }

    public int getMoveNumber(){
        return moveNumber;
    }

    public boolean isCornerMove(){
        boolean flag = false;
        for (int i=0; i<GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                Sign itemSign= matrix[i][j].getState();
                if (itemSign != EMPTY) {
                    int movePosition = matrix[i][j].getPosition();
                    if (movePosition == 1 || movePosition == 3 || movePosition == 7 || movePosition == 9) {
                        flag = true;
                    }
                }

            }
        }
        return flag;
    }


    public boolean isMiddleMove() {
        if (matrix[1][1].getState() != EMPTY) return true;
            else return false;
    }

    private List<Integer> generateMoves(Item[][] gameBoard) {
        List<Integer> nextMoves = new ArrayList<>(); // allocate List

        // If game over, i.e., no next move
        if (checkWin()) {
            return nextMoves;   // return empty list
        }

        // Search for empty cells and add to the List
        for (int row = 0; row < GAME_SIZE; ++row) {
            for (int col = 0; col < GAME_SIZE; ++col) {
                if (gameBoard[row][col].getState() == EMPTY) {
                    nextMoves.add(gameBoard[row][col].getPosition());
                }
            }
        }
        return nextMoves;
    }

    public void printBoard(Item[][] gameBoard) {
        String line = new String();
        Log.d(TAG, "------------");
        for (int i = 0; i < GAME_SIZE; i++) {

            for (int j = 0; j < GAME_SIZE; j++) {
                if (gameBoard[i][j].getState() == EMPTY) {
                    line+=" # ";
                } else if (gameBoard[i][j].getState() == X) {
                    line+=" X ";
                } else if (gameBoard[i][j].getState() == O) {
                    line+=" O ";
                }
            }
            Log.d(TAG, line);
            line = "";
        }
        Log.d(TAG, "------------");
    }

    public Item[][] setMoveToBoard(Item[][] gameBoard, int position, Sign type){
//        Log.d(TAG, "=== Matrix ===");
//        printBoard(matrix);
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                if (gameBoard[i][j].getPosition() == position) {
                    gameBoard[i][j].setState(type);
                }
            }
        }
//        Log.d(TAG, "=== Matrix ===");
//        printBoard(matrix);
        return gameBoard;
    }

    public Item[][] undoMoveToBoard(Item[][] gameBoard, int position){
//        Log.d(TAG, "=== Matrix ===");
//        printBoard(matrix);
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                if (gameBoard[i][j].getPosition() == position) {
                    gameBoard[i][j].setState(EMPTY);
                }
            }
        }
//        Log.d(TAG, "=== Matrix ===");
//        printBoard(matrix);
        return gameBoard;
    }


    public int[] getXYFromPosition(int position) {
        if (position == 1) return new int[]{0,0};
        if (position == 2) return new int[]{0,1};
        if (position == 3) return new int[]{0,2};
        if (position == 4) return new int[]{1,0};
        if (position == 5) return new int[]{1,1};
        if (position == 6) return new int[]{1,2};
        if (position == 7) return new int[]{2,0};
        if (position == 8) return new int[]{2,1};
        if (position == 9) return new int[]{2,2};
        return new int[]{-1,-1};
    }

    private RecItem minimax(int depth, User player, Item[][] gameBoard) {
        // Generate possible next moves in a List of int[2] of {row, col}.
//        Log.d(TAG, "=== gameBoard ===");
//        printBoard(gameBoard);
        List<Integer> nextMoves = generateMoves(gameBoard);
//        Log.d(TAG, "Possible moves for: " + player.getSign() + " | List: " + nextMoves.toString());
        printBoard(gameBoard);
        // mySeed is maximizing; while oppSeed is minimizing
//        int bestScore;
//                = (player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        RecItem currentScore =  new RecItem(0, 0);
        ArrayList<RecItem> moveList = new ArrayList<>();
        RecItem bestMove = new RecItem(0, Integer.MIN_VALUE);
//        Log.d(TAG, " depth = "+ depth);
//        Log.d(TAG, "=== Matrix ===");
//        printBoard(matrix);

//        if (nextMoves.isEmpty() || depth <= 0) {
//            // Gameover or depth reached, evaluate score
////            if (moveNumber ==9 && !checkWin()) bestScore = 0;
////                else bestScore = (getWinningSign() == mySeed.getSign()) ? 10 : -10;
//            if (depth == 0 && !checkWin()) return new RecItem(0, 0);
//            else return new RecItem(0, (getWinningSign() == oppSeed.getSign()) ? 10 : -10);
//        }
        if (checkWinInBoard(gameBoard)) {
            Sign winSign = getWinningSign(gameBoard);
            if (winSign == oppSeed.getSign()) return new RecItem(0, 10);
            else return new RecItem(0, -10);
        } else {
            if (depth == 0) {
                return new RecItem(0, 0);
            } else {
                for (int move : nextMoves) {
                    if (player.getEmail().equals(mySeed.getEmail())) {
                        currentScore.score += minimax(depth - 1, oppSeed, setMoveToBoard(gameBoard, move, player.getSign())).score;
                        undoMoveToBoard(gameBoard, move);
                    } else {
                        currentScore.score += minimax(depth - 1, mySeed, setMoveToBoard(gameBoard, move, player.getSign())).score;
                        undoMoveToBoard(gameBoard, move);
                    }
                    currentScore.position = move;
                    Log.d(TAG, "Adding to list: " + currentScore.position + " score: " + currentScore.score);
                    moveList.add(currentScore);
                    printBoard(gameBoard);

                    //                if (player.getEmail().equals(mySeed.getEmail())) {  // mySeed (computer) is maximizing player
                    //                    currentScore = minimax(depth - 1, oppSeed, setMoveToBoard(gameBoard, move, player.getSign()));
                    //                    if (currentScore.score > bestMove.score) {
                    //                        bestMove = currentScore;
                    //                        bestMove.position = move;
                    //
                    //                    }
                    //                } else {  // oppSeed is minimizing player
                    //                    currentScore = minimax(depth - 1, mySeed, setMoveToBoard(gameBoard, move, player.getSign()));
                    //                    if (currentScore.score < bestMove.score) {
                    //                        bestMove = currentScore;
                    //                        bestMove.position = move;
                    //                    }
                    //                }
                    // Undo move
                    //                cells[move[0]][move[1]].content = Seed.EMPTY;
                }
//                Log.d(TAG, "Getting the best score ===");
//                RecItem temp = new RecItem(0, -3000);

//                Log.d(TAG, "Best Move - move" + bestMove.position + " score: " + bestMove.score);
//                bestMove.position = temp.position;
//                bestMove.score = temp.score;
            }
        }
        for (RecItem rec : moveList) {
//            Log.d(TAG, "move: " + rec.position + " score: " + rec.score);
            if (rec.score > bestMove.score) {
                bestMove.score = rec.score;
                bestMove.position = rec.position;
            }
        }
        return bestMove;
    }


    public int calMove(User playerTurn){
        Item[][] gameBoard;
        Item item;
        gameBoard = new Item[GAME_SIZE][GAME_SIZE];
//        gameBoard = Arrays.copyOf(matrix, matrix.length);
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                item = new Item(matrix[i][j].position, matrix[i][j].state);
                gameBoard[i][j] = item;
                }
            }

        Log.d(TAG, "=== Matrix ===");
        printBoard(matrix);
        Log.d(TAG, "=== gameBoard ===");
        printBoard(gameBoard);
        RecItem positionBest = minimax(9 - moveNumber, playerTurn, gameBoard);
        Log.d(TAG, "The best move is:" + positionBest.position + " score: " + positionBest.score);
//        return positionBest.position;
        return positionBest.position;

//        if (moveNumber == 9) return new RecItem(playerTurn, playerSign, depth, 0);
//        if (checkWin()) {
//            if (getWinningSign() == playerSign) return new RecItem(playerTurn, playerSign, depth, 10);
//            else  return new RecItem(playerTurn, playerSign, depth, -10);
//        }
//
//        for (int i = 0; i < GAME_SIZE; i++) {
//            for (int j = 0; j < GAME_SIZE; j++) {
//                if (matrix[i][j].getPosition() == position) {
//                    matrix[i][j].setState(type);
//                }
//            }
//        }
//        moveNumber++;
//        calMove()


    }

    public class RecItem{
        User player;
        Sign playerSign;
        int position;
        int depth;
        int score;

        public RecItem(int position, int score) {
            this.position = position;
            this.score = score;
        }

        public RecItem(User player, Sign mSign, int depth, int score) {
            this.player = player;
            this.playerSign = mSign;
            this.depth = depth;
            this.score = score;
        }
        public void addScore(int mScore){
            score = score + mScore;
        }
        public void setPositionMove(int mPosition) {
            position = mPosition;
        }
    }
}
