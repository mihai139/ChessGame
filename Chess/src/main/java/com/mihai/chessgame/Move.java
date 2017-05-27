package com.mihai.chessgame;

import android.graphics.Point;

/**
 * Created by Mihai on 12/24/2016.
 */

public class Move {

    private Point p1;
    private Point p2;
    private float value;

    public Move(Point _p1, Point _p2, float _value){
        this.p1 = _p1;
        this.p2 = _p2;
        this.value = _value;
    }
    public Move(){

    }

    public String toString(){
        return ""+ p1.x+" "+ p1.y+" - "+ p2.x+" "+ p2.y+" ::"+ String.valueOf(value);
    }
    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
