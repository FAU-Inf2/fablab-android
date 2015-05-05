package de.fau.cs.mad.fablab.android.ORM;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;


@DatabaseTable(tableName = "ORMObject")
public class DBObject implements Serializable{

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String title;

    @DatabaseField
    private String text;

    @DatabaseField
    private Date created;

    @DatabaseField
    private Date modified;

    public DBObject(){

    }

    public DBObject(long id, String title, String text){
        this.id = id;
        this.title = title;
        this.text = text;
        this.created = new Date();
        this.modified = new Date();
    }

    public void setId(long id){
        this.id = id;
        this.modified = new Date();
    }

    public long getId(){
        return this.id;
    }


    public void setTitle(String title){
        this.title = title;
        this.modified = new Date();
    }
    public String getTitle(){
        return this.title;
    }


    public void setText(String text){
        this.text = text;
        this.modified = new Date();
    }

    public String getText(){
        return this.text;
    }


    public String getCreated(){
        return this.created.toString();
    }

    public String getModified(){
        return this.modified.toString();
    }



    public String toString(){
        return "ID: " + this.id + " Title: " + this.title + " Text: " + this.text + " Created: " + this.created;
    }
}