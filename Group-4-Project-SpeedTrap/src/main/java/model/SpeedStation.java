/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class SpeedStation {

    private String stationId;
    private String locationName;
    private float speedLimit;

    public SpeedStation() {
    }

    public SpeedStation(String stationId, String locationName, float speedLimit) {
        this.stationId = stationId;
        this.locationName = locationName;
        this.speedLimit = speedLimit;
    }

    // --- GETTER & SETTER ---
    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public float getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(float speedLimit) {
        this.speedLimit = speedLimit;
    }
}
