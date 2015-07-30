package cl.tdc.felipe.tdc.adapters;

import java.util.ArrayList;

/**
 * Created by Carlos on 10/05/2015.
 */
public class Maintenance {
    int IdMaintenance;
    String Date;
    String System;
    Double Latitude;
    Double Longitude;
    String Address;
    String Station;
    String Status;
    String Type;
    ArrayList<Actividades> Activities;



    public Maintenance (String date, String system, String latitude, String longitude, String address, String station, String status, String id, String type){
        IdMaintenance = Integer.valueOf(id);
        Date = date;
        System = system;
        Latitude = Double.valueOf(latitude);
        Longitude = Double.valueOf(longitude);
        Address = address;
        Station = station;
        Status = status;
        Type = type;
        Activities = new ArrayList<>();
    }


    public void addActivity(Actividades actividades){
        Activities.add(actividades);
    }

    public int getIdMaintenance() {
        return IdMaintenance;
    }

    public String getDate() {
        return Date;
    }

    public String getSystem() {
        return System;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getAddress() {
        return Address;
    }

    public String getStation() {
        return Station;
    }

    public String getStatus() {
        return Status;
    }

    public String getType() {
        return Type;
    }

    public ArrayList<Actividades> getActivities() {
        return Activities;
    }
}
