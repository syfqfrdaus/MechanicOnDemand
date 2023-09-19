package psm.mechanicondemand;

import java.io.Serializable;

public class Request implements Serializable {

    String Address, RequestDetails, ServiceType, Status, VehicleModel, documentId;
    int Latitude, Longitude, Fee;
    double Distance;

    public Request() {
    }

    public Request(String address, String requestDetails, String serviceType, String status,
                   String vehicleModel, int latitude, int longitude, int fee, double distance, String DocumentId) {
        Address = address;
        RequestDetails = requestDetails;
        ServiceType = serviceType;
        Status = status;
        VehicleModel = vehicleModel;
        Latitude = latitude;
        Longitude = longitude;
        Distance = distance;
        documentId = DocumentId;
        Fee = fee;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getRequestDetails() {
        return RequestDetails;
    }

    public void setRequestDetails(String requestDetails) {
        RequestDetails = requestDetails;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getVehicleModel() {
        return VehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        VehicleModel = vehicleModel;
    }

    public int getLatitude() {
        return Latitude;
    }

    public void setLatitude(int latitude) {
        Latitude = latitude;
    }

    public int getLongitude() {
        return Longitude;
    }

    public void setLongitude(int longitude) {
        Longitude = longitude;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String DocumentId) {
        this.documentId = DocumentId;
    }

    public int getFee() {
        return Fee;
    }

    public void setFee(int fee) {
        Fee = fee;
    }
}
