package cl.tdc.felipe.tdc.objects;

import android.graphics.Bitmap;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by felip on 19/08/2015.
 */
public class FormImage {
    int id;
    int idSystem;
    int idSubSystem;
    String comment;
    String name;
    Bitmap image;
    boolean send = false;
    String type;
    String description;

    public FormImage(int idSystem, int idSubSystem, String comment, String name) {
        this.idSystem = idSystem;
        this.idSubSystem = idSubSystem;
        this.comment = comment;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FormImage(){}

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }

    public void newName(int id){
        DateFormat f = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
        this.name = id+"_"+f.format(new Date())+".png";
    }

    public void newNameRelevo(int id, String elemento){
        DateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
        this.name = f.format(new Date())+"_"+id+"_"+elemento+".png";
    }
    public void newNameRF(int id, String elemento){
        DateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
        this.name = f.format(new Date())+"_"+this.id+"_"+elemento+".png";
    }
    public void newNameRFAerial(int id, String elemento, int numero){
        DateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
        this.name = f.format(new Date())+"_"+this.id+"_"+elemento+"_"+numero+".png";
    }
    public int getIdSystem() {
        return idSystem;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setIdSystem(int idSystem) {
        this.idSystem = idSystem;
    }

    public int getIdSubSystem() {
        return idSubSystem;
    }

    public void setIdSubSystem(int idSubSystem) {
        this.idSubSystem = idSubSystem;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void newNameRelevoRecom(int id, int idRelevo, String comment) {
        DateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
        this.name = f.format(new Date())+"_"+id+"_"+idRelevo+"_"+comment+".png";
    }
}
