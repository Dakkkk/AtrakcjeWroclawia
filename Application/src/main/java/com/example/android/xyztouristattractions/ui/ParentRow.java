package com.example.android.xyztouristattractions.ui;

import java.util.ArrayList;
import com.example.android.xyztouristattractions.provider.TouristAttractions;

/**
 * Created by Dawid on 2016-12-19.
 */
public class ParentRow {
    private String name;
    private ArrayList<TouristAttractions> childList;

    public ParentRow(String name, ArrayList<TouristAttractions> childList) {
        this.name = name;
        this.childList = childList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TouristAttractions> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<TouristAttractions> childList) {
        this.childList = childList;
    }
}
