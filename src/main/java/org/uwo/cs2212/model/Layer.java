package org.uwo.cs2212.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Layer {
    private String name;
    private String layerType;
    private String color;
    private String font;
    private double size;
    private boolean hideLayer;
    private List<PointOfInterest> points;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public List<PointOfInterest> getPoints() {
        return points;
    }

    public void setPoints(List<PointOfInterest> points) {
        this.points = points;
    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public boolean isHideLayer() {
        return hideLayer;
    }

    public void setHideLayer(boolean hideLayer) {
        this.hideLayer = hideLayer;
    }

}


