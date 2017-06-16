package com.mihai.chessgame.model;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mihai on 11/12/2016.
 */

public class ChessPieceModel {

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public enum PieceType  {
        PIECE_PAWN, PIECE_BISHOP , PIECE_ROOK, PIECE_KNIGHT,PIECE_QUEEN ,PIECE_KING
    }
    public enum PieceColor {
        PIECE_WHITE, PIECE_BLACK
    }
    private Point position ;

    private PieceType pieceType;
    private PieceColor pieceColor;

    public String imageName(){
        String result = "piece_";
        if(pieceType == PieceType.PIECE_PAWN){
            result+="pawn_";
        }
        if(pieceType == PieceType.PIECE_BISHOP){
            result+="bishop_";
        }
        if(pieceType == PieceType.PIECE_ROOK){
            result+="rook_";
        }
        if(pieceType == PieceType.PIECE_KNIGHT){
            result+="knight_";
        } if(pieceType == PieceType.PIECE_QUEEN){
            result+="queen_";
        }
        if(pieceType == PieceType.PIECE_KING){
            result+="king_";
        }
        if(pieceColor == PieceColor.PIECE_BLACK){
            result +="black";
        }
        if(pieceColor == PieceColor.PIECE_WHITE){
            result +="white";
        }



        return result;
    }

    public boolean canCaptureAnotherPiece(ChessPieceModel otherChessPieceModel){
        if(otherChessPieceModel == null){
            return false;
        }
        else{
            if(this.pieceColor!= otherChessPieceModel.pieceColor){
                return true;
            }
            return false;

        }
    }

    final String TAG = "ChessPieceModel";
    public ChessPieceModel(PieceColor pieceColor, PieceType pieceType, Point position){

        this.position = position;
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;



    }

    public int pieceValue(){
        if(this.getPieceType() == PieceType.PIECE_PAWN){
            return 1;
        }
        if(this.getPieceType() == PieceType.PIECE_KNIGHT){
            return 3;
        }
        if(this.getPieceType() == PieceType.PIECE_BISHOP){
            return 3;
        }
        if(this.getPieceType() == PieceType.PIECE_ROOK){
            return 5;
        }
        if(this.getPieceType() == PieceType.PIECE_QUEEN){
            return 9;
        }
        if(this.getPieceType() == PieceType.PIECE_KING){
            return 13;
        }
        return 0;
    }

    public List<Point> listAllPossibleMovesForThisPiece (ChessTableModel chessTableModel){
        List<Point> points = new ArrayList<Point>();
        if( pieceType ==   PieceType.PIECE_ROOK ){

            for(int i = position.x +1;i<8;i++){
                Point p =new Point(i, position.y);
                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if(piece == null && chessTableModel.isPointOnTable(p)){
                    points.add(p);
                }
                else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    }
                    else break;
                }


            }
            for (int i = position.x-1 ; i>=0;i--){

                Point p =new Point(i, position.y);
                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if(piece == null && chessTableModel.isPointOnTable(p)){
                    points.add(p);
                }
                else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    }
                    else break;
                }
            }
            for (int j = position.y-1;j>=0;j--){

                Point p =new Point(position.x,j);
                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if(piece == null && chessTableModel.isPointOnTable(p)){
                    points.add(p);
                }
                else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    }
                    else break;
                }
            }
            for(int j = position.y+1;j<8;j++){

                Point p =new Point(position.x, j);
                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if(piece == null && chessTableModel.isPointOnTable(p)){
                    points.add(p);
                }
                else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    }
                    else break;
                }
            }


        }

        if(pieceType == PieceType.PIECE_PAWN){
            if(pieceColor == PieceColor.PIECE_WHITE){


                Point capturePointLeft = new Point(position.x-1, position.y-1);
                Point capturePointRight = new Point(position.x +1, position.y -1);


                if(this.canCaptureAnotherPiece(chessTableModel.getChessPieceModelAtPoint(capturePointLeft))){
                    points.add(capturePointLeft);
                }
                if(this.canCaptureAnotherPiece(chessTableModel.getChessPieceModelAtPoint(capturePointRight))){
                    points.add(capturePointRight);
                }

                if(position.y == 6){
                    Point p = new Point(position.x , position.y -2);
                    Point p2 = new Point(position.x , position.y -1);

                    if(chessTableModel.getChessPieceModelAtPoint(p)== null && (chessTableModel.getChessPieceModelAtPoint(p2)== null)){
                        points.add(p);
                    }

                    if(chessTableModel.getChessPieceModelAtPoint(p2)== null){
                        points.add(p2);
                    }

                }
                else {

                    Point p = new Point(position.x , position.y -1);
                    if(chessTableModel.getChessPieceModelAtPoint(p)== null && chessTableModel.isPointOnTable(p)){
                        points.add(p);
                    }

                }

            }

            else if (pieceColor == PieceColor.PIECE_BLACK) {

                Point capturePointLeft = new Point(position.x-1, position.y+1);
                Point capturePointRight = new Point(position.x +1, position.y +1);


                if(this.canCaptureAnotherPiece(chessTableModel.getChessPieceModelAtPoint(capturePointLeft))){
                    points.add(capturePointLeft);
                }
                if(this.canCaptureAnotherPiece(chessTableModel.getChessPieceModelAtPoint(capturePointRight))){
                    points.add(capturePointRight);
                }

                if (position.y == 1) {

                    Point p = new Point(position.x, position.y + 2);
                    Point p2 = new Point(position.x, position.y + 1);
                    if (chessTableModel.getChessPieceModelAtPoint(p) == null && (chessTableModel.getChessPieceModelAtPoint(p2) == null)) {
                        points.add(p);
                    }


                    if (chessTableModel.getChessPieceModelAtPoint(p2) == null) {
                        points.add(p2);
                    }
                }
                else if(position.y == 6){
                    Point p = new Point(position.x , position.y -2);
                    Point p2 = new Point(position.x , position.y -1);
                    if (chessTableModel.getChessPieceModelAtPoint(p) == null && (chessTableModel.getChessPieceModelAtPoint(p2) == null)) {
                        points.add(p);
                    }
                    if (chessTableModel.getChessPieceModelAtPoint(p2) == null) {
                        points.add(p2);
                    }
                }
                else{

                    Point p = new Point(position.x, position.y - 1);
                    if (chessTableModel.getChessPieceModelAtPoint(p) == null && chessTableModel.isPointOnTable(p)) {
                        points.add(p);
                    }


                }


                }
                else{

                    Point p = new Point(position.x, position.y - 1);
                    if (chessTableModel.getChessPieceModelAtPoint(p) == null && chessTableModel.isPointOnTable(p)) {
                        points.add(p);
                    }


                }



        }

        if(pieceType == PieceType.PIECE_BISHOP){

            for(int i = 0; i<8;i++) {

                Point p = new Point(position.x + 1 + i, position.y + i + 1);

                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {

                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }


            for(int i = 0; i<8;i++) {

                Point p = new Point(position.x - 1 - i, position.y - i - 1);

                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {

                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }

            for(int i = 0; i<8;i++) {

                Point p = new Point(position.x + 1 + i, position.y - i - 1);

                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {

                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }


            for(int i = 0; i<8;i++) {

                Point p = new Point(position.x - 1 - i, position.y + i + 1);

                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {

                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }

        }







        if(pieceType == PieceType.PIECE_KNIGHT){

            Point p = new Point(position.x-1, position.y -2);
            ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

            p = new Point(position.x-2, position.y -1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

            p = new Point(position.x+1, position.y -2);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

            p = new Point(position.x-1, position.y +2);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

            p = new Point(position.x+1, position.y +2);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

            p = new Point(position.x-2, position.y +1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

            p = new Point(position.x+2, position.y -1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

            p = new Point(position.x+2, position.y +1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

        }



        if(pieceType == PieceType.PIECE_KING){
            Point p = new Point(position.x-1, position.y -1);
            ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }

            p = new Point(position.x-1, position.y);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }
            p = new Point(position.x-1, position.y+1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }
            p = new Point(position.x, position.y+1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }
            p = new Point(position.x, position.y-1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }
            p = new Point(position.x+1, position.y-1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }
            p = new Point(position.x+1, position.y);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }
            p = new Point(position.x+1, position.y+1);
            piece = chessTableModel.getChessPieceModelAtPoint(p);
            if(piece == null && chessTableModel.isPointOnTable(p)){
                points.add(p);
            }
            else{
                if(this.canCaptureAnotherPiece(piece)) {
                    points.add(p);
                }
            }




            Point p1 = new Point(position.x+2, position.y );

            if (isSmallCastlingAllowedOnKing(chessTableModel)) {


                points.add(p1);

            }
            Point p2 = new Point(position.x-3, position.y );

            if (isBigCastlingAllowedOnKing(chessTableModel)) {


                points.add(p2);

            }



        }
        if(pieceType == PieceType.PIECE_QUEEN) {

            for (int i = position.x + 1; i < 8; i++) {
                Point p = new Point(i, position.y);
                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {
                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }
            for (int i = position.x - 1; i >= 0; i--) {

                Point p = new Point(i, position.y);
                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {
                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }
            }
            for (int j = position.y - 1; j >= 0; j--) {

                Point p = new Point(position.x, j);
                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {
                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }
            }
            for (int j = position.y + 1; j < 8; j++) {

                Point p = new Point(position.x, j);
                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {
                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }
            }


            for (int i = 0; i < 8; i++) {

                Point p = new Point(position.x + 1 + i, position.y + i + 1);

                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {

                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }


            for (int i = 0; i < 8; i++) {

                Point p = new Point(position.x - 1 - i, position.y - i - 1);

                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {

                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }

            for (int i = 0; i < 8; i++) {

                Point p = new Point(position.x + 1 + i, position.y - i - 1);

                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {

                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }


            for (int i = 0; i < 8; i++) {

                Point p = new Point(position.x - 1 - i, position.y + i + 1);

                ChessPieceModel piece = chessTableModel.getChessPieceModelAtPoint(p);
                if (piece == null && chessTableModel.isPointOnTable(p)) {

                    points.add(p);
                } else {
                    if (this.canCaptureAnotherPiece(piece)) {
                        points.add(p);
                        break;
                    } else break;
                }


            }

        }

        return points;
    }

    public boolean isSmallCastlingAllowedOnKing(ChessTableModel chessTableModel){
        int c = 7;

        if(this.getPieceColor() == PieceColor.PIECE_BLACK) {
            c=0;

        }
        ChessPieceModel p1 = chessTableModel.getChessPieceModelAtPoint(new Point(6,c));
        ChessPieceModel p2 = chessTableModel.getChessPieceModelAtPoint(new Point(5,c));

        ChessPieceModel p3 = chessTableModel.getChessPieceModelAtPoint(new Point(7,c));
        ChessPieceModel p4 = chessTableModel.getChessPieceModelAtPoint(new Point(4,c));
        if(p3!= null && p4!= null && p3.getPieceType() == PieceType.PIECE_ROOK && p4.getPieceType() == PieceType.PIECE_KING && p1 == null && p2 == null){
            return true;

        }

        return false;

    }


    public boolean isBigCastlingAllowedOnKing(ChessTableModel chessTableModel){
        int c = 7;

        if(this.getPieceColor() == PieceColor.PIECE_BLACK) {
            c=0;

        }
        ChessPieceModel p1 = chessTableModel.getChessPieceModelAtPoint(new Point(1,c));
        ChessPieceModel p2 = chessTableModel.getChessPieceModelAtPoint(new Point(2,c));
        ChessPieceModel p3 = chessTableModel.getChessPieceModelAtPoint(new Point(3,c));

        ChessPieceModel p4 = chessTableModel.getChessPieceModelAtPoint(new Point(0,c));
        ChessPieceModel p5 = chessTableModel.getChessPieceModelAtPoint(new Point(4,c));
        if(p4!= null && p5!= null &&p4.getPieceType() == PieceType.PIECE_ROOK && p5.getPieceType() == PieceType.PIECE_KING && p1 == null && p2 == null && p3 == null){
            return true;

        }

        return false;

    }


}
