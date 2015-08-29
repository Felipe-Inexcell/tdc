package cl.tdc.felipe.tdc.objects.Averia;

/**
 * Created by felip on 20/08/2015.
 */
public class Item {
    int id;
    String name;

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
