/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model1;

/**
 *
 * @author LENOVO
 */
public class SpeedViolation {

    private String stationId;
    private double speed;

    public SpeedViolation() {
    }

    public SpeedViolation(String stationId, double speed) {
        this.stationId = stationId;
        this.speed = speed;
    }

    public String getStationId() {
        return stationId;
    }

    public double getSpeed() {
        return speed;
    }

}
