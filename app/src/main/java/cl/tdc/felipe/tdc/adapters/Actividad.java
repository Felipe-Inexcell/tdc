package cl.tdc.felipe.tdc.adapters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Carlos on 10/05/2015.
 */
public class Actividad {
    int id;
    String activityId;
    String type;
    String durationKeyName;
    String zoneCode;
    String zoneName;
    String skillName;
    String coordX;
    String coordY;
    String status;
    String slaEnd;
    String technicianId;
    int position;

    public Actividad(){
    }

    public Actividad(int id, String activityId, String type, String durationKeyName, String zoneCode, String zoneName, String skillName, String coordX, String coordY, String slaEnd, String status, String technicianId, int position) {
        this.id = id;
        this.activityId = activityId;
        this.type = type;
        this.durationKeyName = durationKeyName;
        this.zoneCode = zoneCode;
        this.zoneName = zoneName;
        this.skillName = skillName;
        this.coordX = coordX;
        this.coordY = coordY;
        this.status = status;
        this.slaEnd = slaEnd;
        this.technicianId = technicianId;
        this.position = position;

    }

    @Override
    public String toString() {
        return id+";"+activityId+";"+type+";"+durationKeyName+";"+zoneCode+";"+zoneName+";"+skillName+";"+coordX+";"+coordY+";"+status+";"+slaEnd+";"+technicianId+";"+position;
    }

    public Actividad StringToActividad(String s){
        String[] datos = s.split(";");
        return new Actividad(Integer.parseInt(datos[0]),datos[1],datos[2],datos[3],datos[4],datos[5],datos[6],datos[7],datos[8],datos[9],datos[10],datos[11],Integer.parseInt(datos[12]));
    }

    public int getId() {
        return id;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getType() {
        return type;
    }

    public String getDurationKeyName() {
        return durationKeyName;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getCoordX() {
        return coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public String getStatus() {
        return status;
    }

    public String getSlaEnd() {
        return slaEnd;
    }

    public String getTechnicianId() {
        return technicianId;
    }

    public int getPosition() {
        return position;
    }
}
