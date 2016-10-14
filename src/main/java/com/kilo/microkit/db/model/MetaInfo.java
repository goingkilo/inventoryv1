package com.kilo.microkit.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by kraghunathan on 9/17/16.
 */
@Entity
@Table(name = "loadlog")
public class MetaInfo {


    private String type;
    private String loadTime;

    public MetaInfo() {

    }


    @Id
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Column(name = "load_time_s")
    public String getLoadTime() {
        return loadTime;
    }

    public void setLoadTime(String loadTime) {
        this.loadTime = loadTime;
    }
}

