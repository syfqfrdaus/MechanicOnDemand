package psm.mechanicondemand;

import java.sql.Time;
import java.util.Date;

public class History {

    String MechanicID, UserID, Address, VehicleName, Service, Date, Time, UserName;
    Double Price;

    public History(){

    }

    public History(String mechanicID, String userID, String address, String vehicleName,
                   String service, String date, String time, String userName, Double price){
        MechanicID = mechanicID;
        UserID = userID;
        Address = address;
        VehicleName = vehicleName;
        Service = service;
        Date = date;
        Time = time;
        UserName = userName;
        Price = price;
    }

    public String getMechanicID() {
        return MechanicID;
    }

    public void setMechanicID(String mechanicID) {
        MechanicID = mechanicID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getVehicleName() {
        return VehicleName;
    }

    public void setVehicleName(String vehicleName) {
        VehicleName = vehicleName;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }
}
