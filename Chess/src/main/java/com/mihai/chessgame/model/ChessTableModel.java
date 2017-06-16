package com.mihai.chessgame.model;

import android.graphics.Point;
import android.util.Log;
import android.widget.ImageButton;

import com.mihai.chessgame.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihai on 11/12/2016.
 */

public class ChessTableModel{


    private ChessPieceModel [][] chessTableModel = new ChessPieceModel[8][8];


    public ChessTableModel(){
        
        arrangeDefaultPieces();
        
    }
    public ChessTableModel(int x){

        arrangeNexusPieces();

    }

    public ChessPieceModel [][] cloneTable(){
        ChessPieceModel [][] chessTableModelNew = new ChessPieceModel[8][8];
        for(int i = 0; i<8;i++){
            for(int j=0;j<8;j++){
                if(chessTableModel[i][j]!= null) {
                    ChessPieceModel piece = new ChessPieceModel(chessTableModel[i][j].getPieceColor(),
                            chessTableModel[i][j].getPieceType(), chessTableModel[i][j].getPosition());
                    chessTableModelNew[i][j] = piece;
                }
                else{
                    chessTableModelNew[i][j] = null;

                }

            }
        }
        return chessTableModelNew;
    }


    public ChessPieceModel getChessPieceModelAtPoint (Point p){
        if(p.x <=7 && p.y <=7 && p.x >=0 && p.y >=0 ) {
            return chessTableModel[p.y][p.x];
        }
        else return null;
    }

    public boolean isPointOnTable(Point p){
        if(p.x < 0 ) return  false;
        if(p.y < 0) return false;
        if(p.x>7) return false;
        if(p.y>7) return false;
        return  true;


    }

    public ChessPieceModel[][] getChessTableModel() {
        return chessTableModel;
    }

    public void setChessTableModel(ChessPieceModel[][] chessTableModel) {
        this.chessTableModel = chessTableModel;
    }

    public void performMove(Point p1, Point p2){
        ChessPieceModel piece = getChessPieceModelAtPoint(p1);


        getChessTableModel()[p2.y][p2.x] = getChessTableModel()[p1.y][p1.x];
        getChessTableModel()[p1.y][p1.x] = null;
        if(piece != null) {

            piece.setPosition(p2);

        }



//  Rocada
            if(piece!=null) {
                if (piece.getPieceType() == ChessPieceModel.PieceType.PIECE_KING && piece.getPosition().x == p1.x -3 ) {
                    Log.d("Big castling", "this");

                    ChessPieceModel rook = this.getChessPieceModelAtPoint(new Point(0, p1.y));
                    // chessTableModel.getChessTableModel()[y][x] = chessTableModel.getChessTableModel()[y][x-2];


                    getChessTableModel()[p2.y][2] = getChessTableModel()[p1.y][0];
                    getChessTableModel()[p1.y][0] = null;
                    if(piece != null) {

                        rook.setPosition(new Point (2,p1.y));

                    }
                }

                if (piece.getPieceType() == ChessPieceModel.PieceType.PIECE_KING && piece.getPosition().x == p1.x +2) {
                    Log.d("Small castling", "this");

                    ChessPieceModel rook = this.getChessPieceModelAtPoint(new Point(7, p1.y));
                    // chessTableModel.getChessTableModel()[y][x] = chessTableModel.getChessTableModel()[y][x-2];


                    getChessTableModel()[p2.y][5] = getChessTableModel()[p1.y][7];
                    getChessTableModel()[p1.y][7] = null;
                    if(piece != null) {

                        rook.setPosition(new Point (5,p1.y));

                    }
                }
            }

    }

    public int tableValue(ChessPieceModel.PieceColor color){
        int total = 0;
        int possibleMoves = 0;
        ChessPieceModel king =null;

        for (int i =0;i<8;i++){
            for(int j = 0;j<8;j++){
                ChessPieceModel piece = getChessPieceModelAtPoint(new Point(i,j));
               if(piece!= null && piece.getPieceType() == ChessPieceModel.PieceType.PIECE_KING){
                   king = piece;
               }

                if(piece!=null && piece.getPieceColor() == color){
                    total += piece.pieceValue();
                    possibleMoves+= piece.listAllPossibleMovesForThisPiece(this).size();

                }
            }
        }

        if( possibleMoves== 0){
            total = 0;
        }

        if(possibleMoves ==1){
            total = 1;
        }
        if(possibleMoves ==2){
            total = 2;
        }
        return total;
    }

    public boolean isCheckMate(ChessPieceModel.PieceColor color){

        List<Move> moveList = new ArrayList<Move>();

        for(int i = 0;i<8;i++) {
            for (int j = 0; j < 8; j++) {
                ChessPieceModel pieceModel = getChessPieceModelAtPoint(new Point(i,j));
                if(pieceModel!= null && pieceModel.getPieceColor() == color) {
                    List<Point> points = pieceModel.listAllPossibleMovesForThisPiece(this);
                    for (Point point : points) {
                        boolean moveAllowed = true;


                        if (pieceModel != null) {
                            ChessTableModel table2 = new ChessTableModel();
                            table2.setChessTableModel(this.cloneTable());


                            table2.performMove(pieceModel.getPosition(), new Point(point.x, point.y));
                            if (table2.isCheck(pieceModel.getPieceColor())) {
                                moveAllowed = false;
                            }
                            if (moveAllowed) {
                                moveList.add(new Move(pieceModel.getPosition(), point, 0));
                            }
                        }
                    }
                }


            }
        }
        if(moveList.size() > 0){
            return false;
        }
        return true;
    }


    public boolean isCheck(ChessPieceModel.PieceColor color){
        ChessPieceModel kingOppositePiece = null;

            for(int i =0; i<8;i++){
                for(int j=0 ; j<8;j++){
                    ChessPieceModel piece = getChessPieceModelAtPoint(new Point(i,j));
                    if(piece == null){

                    }
                    else{
                        if (piece.getPieceType()== ChessPieceModel.PieceType.PIECE_KING){
                            if(piece.getPieceColor() == color){
                                kingOppositePiece = piece;
                                break;
                            }
                        }
                    }
                }
            }

            for(int i = 0; i<8;i++){
                for(int j=0;j<8;j++){
                    ChessPieceModel myPiece = getChessPieceModelAtPoint(new Point(i,j));
                    if(myPiece!= null && myPiece.getPieceColor() != color){
                       if( myPiece.listAllPossibleMovesForThisPiece(this).contains(kingOppositePiece.getPosition())){
                            return true;

                        }
                    }


                }
            }



        return false;

    }

    public void arrangeDefaultPieces (){
        for(int i = 0;i<8;i++){
            for(int j=0;j<8;j++){
                getChessTableModel()[i][j] = null;
            }
        }
   // PAWNS
        getChessTableModel()[6][0] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(0,6));
        getChessTableModel()[6][1] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(1,6));
        getChessTableModel()[6][2] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(2,6));
        getChessTableModel()[6][3] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(3,6));
        getChessTableModel()[6][4] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(4,6));
        getChessTableModel()[6][5] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(5,6));
        getChessTableModel()[6][6] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(6,6));
        getChessTableModel()[6][7] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(7,6));

        getChessTableModel()[1][0] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(0,1));
        getChessTableModel()[1][1] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(1,1));
        getChessTableModel()[1][2] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(2,1));
        getChessTableModel()[1][3] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(3,1));
        getChessTableModel()[1][4] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(4,1));
        getChessTableModel()[1][5] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(5,1));
        getChessTableModel()[1][6] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(6,1));
        getChessTableModel()[1][7] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(7,1));


        //OTHERS

        getChessTableModel()[0][0] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_ROOK, new Point(0,0));
        getChessTableModel()[0][7] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_ROOK, new Point(7,0));
        getChessTableModel()[0][1] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_KNIGHT, new Point(1,0));
        getChessTableModel()[0][6] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_KNIGHT, new Point(6,0));
        getChessTableModel()[0][2] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_BISHOP, new Point(2,0));
        getChessTableModel()[0][5] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_BISHOP, new Point(5,0));
        getChessTableModel()[0][3] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_QUEEN, new Point(3,0));
        getChessTableModel()[0][4] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_KING, new Point(4,0));


        getChessTableModel()[7][0] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_ROOK, new Point(0,7));
        getChessTableModel()[7][1] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_KNIGHT, new Point(1,7));
        getChessTableModel()[7][2] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_BISHOP, new Point(2,7));
        getChessTableModel()[7][3] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_QUEEN, new Point(3,7));
        getChessTableModel()[7][4] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_KING, new Point(4,7));
        getChessTableModel()[7][5] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_BISHOP, new Point(5,7));
        getChessTableModel()[7][6] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_KNIGHT, new Point(6,7));
        getChessTableModel()[7][7] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_ROOK, new Point(7,7));






    }



    public void arrangeNexusPieces (){
        for(int i = 0;i<8;i++){
            for(int j=0;j<8;j++){
                getChessTableModel()[i][j] = null;
            }
        }
        // PAWNS
        getChessTableModel()[6][0] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(0,6));
        getChessTableModel()[6][1] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(1,6));
        getChessTableModel()[6][2] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(2,6));
        getChessTableModel()[6][3] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(3,6));
        getChessTableModel()[6][4] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(4,6));
        getChessTableModel()[6][5] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(5,6));
        getChessTableModel()[6][6] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(6,6));
        getChessTableModel()[6][7] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_PAWN, new Point(7,6));

        getChessTableModel()[1][0] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(0,1));
        getChessTableModel()[1][1] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(1,1));
        getChessTableModel()[1][2] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(2,1));
        getChessTableModel()[1][3] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(3,1));
        getChessTableModel()[1][4] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(4,1));
        getChessTableModel()[1][5] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(5,1));
        getChessTableModel()[1][6] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(6,1));
        getChessTableModel()[1][7] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_PAWN, new Point(7,1));


        //OTHERS

        getChessTableModel()[0][0] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_ROOK, new Point(0,0));
        getChessTableModel()[0][7] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_ROOK, new Point(7,0));
        getChessTableModel()[0][1] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_KNIGHT, new Point(1,0));
        getChessTableModel()[0][6] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_KNIGHT, new Point(6,0));
        getChessTableModel()[0][2] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_BISHOP, new Point(2,0));
        getChessTableModel()[0][5] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_BISHOP, new Point(5,0));
        getChessTableModel()[0][4] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_QUEEN, new Point(4,0));
        getChessTableModel()[0][3] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_WHITE, ChessPieceModel.PieceType.PIECE_KING, new Point(3,0));


        getChessTableModel()[7][0] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_ROOK, new Point(0,7));
        getChessTableModel()[7][1] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_KNIGHT, new Point(1,7));
        getChessTableModel()[7][2] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_BISHOP, new Point(2,7));
        getChessTableModel()[7][4] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_QUEEN, new Point(4,7));
        getChessTableModel()[7][3] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_KING, new Point(3,7));
        getChessTableModel()[7][5] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_BISHOP, new Point(5,7));
        getChessTableModel()[7][6] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_KNIGHT, new Point(6,7));
        getChessTableModel()[7][7] = new ChessPieceModel(ChessPieceModel.PieceColor.PIECE_BLACK, ChessPieceModel.PieceType.PIECE_ROOK, new Point(7,7));






    }
}
