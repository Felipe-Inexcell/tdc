package cl.tdc.felipe.tdc.objects.Maintenance;

import java.util.ArrayList;

/**
 * Created by Felipes on 08-07-2015.
 */
public class Maintenance {

    String date;
    String latitude;
    String longitude;
    String address;
    String station;
    String status;
    String idMaintenance;
    String type;
    ArrayList<MainSystem> systemList;

    public Maintenance() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdMaintenance() {
        return idMaintenance;
    }

    public void setIdMaintenance(String idMaintenance) {
        this.idMaintenance = idMaintenance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<MainSystem> getSystemList() {
        return systemList;
    }

    public void setSystemList(ArrayList<MainSystem> systemList) {
        this.systemList = systemList;
    }
}
