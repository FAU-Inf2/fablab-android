package de.fau.cs.mad.fablab.android.model.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="AutoCompleteHelper")
public class AutoCompleteWords implements Serializable {
    static final int HOURSTILLUPDATE = 24;

    //there is only one entry for the words --> ID always 0
    @Id
    private long id = 0;

    @Column(name="words")
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private String[] possibleAutoCompleteWords;

    @Column(name="last_refresh")
    private Date lastRefresh;

    public  AutoCompleteWords(){}

    public AutoCompleteWords(String[] possibleAutoCompleteWords) {
        this.possibleAutoCompleteWords = possibleAutoCompleteWords;
        this.lastRefresh = new Date();
    }

    public String[] getPossibleAutoCompleteWords() {
        return possibleAutoCompleteWords;
    }

    public boolean needsRefresh(){
        if(((lastRefresh.getTime()- new Date().getTime() ) / 60*60*1000) >= HOURSTILLUPDATE)
            return true;
        return false;
    }
}