/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author LENOVO
 */
public class TrafficTicket {

    private int ticketId;
    private String licensePlate; // Khóa ngoại
    private String stationId;    // Khóa ngoại
    private float recordedSpeed;
    private Timestamp violationTime;
    private int status;

    public TrafficTicket() {
    }

    public TrafficTicket(String licensePlate, String stationId, float recordedSpeed) {
        this.licensePlate = licensePlate;
        this.stationId = stationId;
        this.recordedSpeed = recordedSpeed;
        this.status = 0;
    }

    public TrafficTicket(int ticketId, String licensePlate, String stationId, float recordedSpeed, Timestamp violationTime, int status) {
        this.ticketId = ticketId;
        this.licensePlate = licensePlate;
        this.stationId = stationId;
        this.recordedSpeed = recordedSpeed;
        this.violationTime = violationTime;
        this.status = status;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public float getRecordedSpeed() {
        return recordedSpeed;
    }

    public void setRecordedSpeed(float recordedSpeed) {
        this.recordedSpeed = recordedSpeed;
    }

    public Timestamp getViolationTime() {
        return violationTime;
    }

    public void setViolationTime(Timestamp violationTime) {
        this.violationTime = violationTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Thêm hàm toString để debug in ra Console cho dễ nhìn
    @Override
    public String toString() {
        return "GIAY PHAT [" + ticketId + "] Xe: " + licensePlate + " | Toc do: " + recordedSpeed + " km/h | Tram: " + stationId;
    }
}
