package cl.tdc.felipe.tdc.adapters;

public class Actividades{
    String Name;
    String Description;
    int IdActivity;

    public Actividades(String name, String desc, String id){
        this.Name = name;
        this.Description = desc;
        this.IdActivity = Integer.valueOf(id);
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public int getIdActivity() {
        return IdActivity;
    }
}
