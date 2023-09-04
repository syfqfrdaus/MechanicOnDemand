package psm.mechanicondemand;

public class Vehicle {

    String VehicleType, VehicleBrand, VehicleModel, VehiclePlate;

    public Vehicle() {
    }

    public Vehicle(String vehicleType, String vehicleBrand, String vehicleModel, String vehiclePlate) {
        VehicleType = vehicleType;
        VehicleBrand = vehicleBrand;
        VehicleModel = vehicleModel;
        VehiclePlate = vehiclePlate;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getVehicleBrand() {
        return VehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        VehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return VehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        VehicleModel = vehicleModel;
    }

    public String getVehiclePlate() {
        return VehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        VehiclePlate = vehiclePlate;
    }
}
