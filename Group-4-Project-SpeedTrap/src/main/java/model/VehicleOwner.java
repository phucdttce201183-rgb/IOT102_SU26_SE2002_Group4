/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class VehicleOwner {

    private String licensePlate;
    private String fullName;
    private String citizenId;
    private String address;

    // Hàm khởi tạo rỗng (BẮT BUỘC cho API/JSON)
    public VehicleOwner() {
    }

    // Hàm khởi tạo đầy đủ
    public VehicleOwner(String licensePlate, String fullName, String citizenId, String address) {
        this.licensePlate = licensePlate;
        this.fullName = fullName;
        this.citizenId = citizenId;
        this.address = address;
    }

    // --- GETTER & SETTER ---
    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
