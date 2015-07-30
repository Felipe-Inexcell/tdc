package cl.tdc.felipe.tdc.objects.Maintenance;

import java.util.ArrayList;

/**
 * Created by Felipes on 08-07-2015.
 */
public class MainSystem {
    String nameSystem;
    ArrayList<Activity> activitieList;

    public MainSystem() {
    }

    public String getNameSystem() {
        return nameSystem;
    }

    public void setNameSystem(String nameSystem) {
        this.nameSystem = nameSystem;
    }

    public ArrayList<Activity> getActivitieList() {
        return activitieList;
    }

    public void setActivitieList(ArrayList<Activity> activitieList) {
        this.activitieList = activitieList;
    }
}
