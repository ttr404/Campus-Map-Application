package org.uwo.cs2212.model;

import java.util.List;

public class Layer {
    private String name;
    private boolean baseLayer;
    private String color;
    private String font;
    private double size;
    private List<PointOfInterest> points;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBaseLayer() {
        return baseLayer;
    }

    public void setBaseLayer(boolean baseLayer) {
        this.baseLayer = baseLayer;
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
}
