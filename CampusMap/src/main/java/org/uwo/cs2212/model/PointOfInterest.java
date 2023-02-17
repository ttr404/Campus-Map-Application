package org.uwo.cs2212.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PointOfInterest {
    private double x;
    private double y;
    private String name;
    private String roomNumber;
    private String description;
    private String type;
    @JsonIgnore
    private boolean selected;
    private boolean favorite;
}
