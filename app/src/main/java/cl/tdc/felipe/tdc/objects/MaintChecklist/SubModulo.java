package cl.tdc.felipe.tdc.objects.MaintChecklist;

import java.util.ArrayList;

public class SubModulo {
    public SubModulo() {
    }
    int id;
    String name;
    ArrayList<Section> sections;

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }



    public int getId() {
        return id;
    }

    public void setId(String id) {
        if (id.compareTo("") == 0)
            this.id = -1;
        else
            this.id = Integer.parseInt(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
