package com.example.android.xyztouristattractions.ui;

import java.util.ArrayList;

import com.example.android.xyztouristattractions.common.Attraction;
import com.example.android.xyztouristattractions.provider.TouristAttractions;

/**
 * Created by Dawid on 2016-12-19.
 */
public class ParentRow extends Attraction {
    private String name;
    private ArrayList<ChildRow> childList;

    public ParentRow(String name, ArrayList<ChildRow> childList) {
        this.name = name;
        this.childList = childList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChildRow> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<ChildRow> childList) {
        this.childList = childList;
    }
}
