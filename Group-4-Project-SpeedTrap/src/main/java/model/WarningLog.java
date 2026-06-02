/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

public class WarningLog {
    private int id;
    private int speedLimit;     // Thuộc tính lấy từ bảng Streets để đối chiếu
    private int recordedSpeed;
    private Timestamp timestamp; // Dùng Timestamp để lưu cả ngày lẫn giờ

    public WarningLog() {
    }

    public WarningLog(int id, int speedLimit, int recordedSpeed, Timestamp timestamp) {
        this.id = id;
        this.speedLimit = speedLimit;
        this.recordedSpeed = recordedSpeed;
        this.timestamp = timestamp;
    }

    // Các hàm Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSpeedLimit() { return speedLimit; }
    public void setSpeedLimit(int speedLimit) { this.speedLimit = speedLimit; }

    public int getRecordedSpeed() { return recordedSpeed; }
    public void setRecordedSpeed(int recordedSpeed) { this.recordedSpeed = recordedSpeed; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}