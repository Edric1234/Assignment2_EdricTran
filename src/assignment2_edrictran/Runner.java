/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignment2_edrictran;

import javafx.scene.paint.Color;

/**
 *
 * @author Edric
 */
public class Runner {

    private String name;
    private int number;
    private Color color;
    private double speed;


    public Runner(String name, int number, Color color, double speed) {
        this.name = name;
        this.number = number;
        this.color = color;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
