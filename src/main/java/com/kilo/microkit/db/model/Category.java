package com.kilo.microkit.db.model;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import javax.persistence.*;

/**
 * Created by kraghunathan on 9/30/16.
 */
@Entity
@Table(name = "categories")
public class Category {


    private long id;
    private String title;
    private String displayName;

    private String url;

    public Category() {
    }

    public Category(String title, String url) {
        this.title = title;
        this.url = url;

        // really !
        Function capitalizer = new Function<String,String>(){
            @Nullable
            @Override
            public String apply(String s) {
                return s.substring(0, 1).toUpperCase() + s.substring(1);
            }
        };

        this.displayName = Joiner.on(" ")
                .join(
                        Lists.transform(
                                Splitter.onPattern("_").splitToList(title), capitalizer).toArray(new String[]{}));
    }

    public Category(long id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    @Id
    @Column(name="c_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    @Column(name="title")
    public String getTitle() {
        return title;
    }

    @Column(name="url")
    public String getUrl() {
        return url;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

