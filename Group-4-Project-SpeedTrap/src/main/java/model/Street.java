/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Street {
    private int id;
    private String name;
    private int speedLimit;

    public Street() {
    }

    public Street(int id, String name, int speedLimit) {
        this.id = id;
        this.name = name;
        this.speedLimit = speedLimit;
    }

    // Các hàm Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getSpeedLimit() { return speedLimit; }
    public void setSpeedLimit(int speedLimit) { this.speedLimit = speedLimit; }
}
