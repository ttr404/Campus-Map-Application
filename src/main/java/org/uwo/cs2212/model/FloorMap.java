package org.uwo.cs2212.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

/**
 * This class is used to store data on the floor map for a base map. This includes the name, file name, if the legend
 * is shown, and it's position, the list of layers, the config (json) file name, and the corresponding userLayer.
 *
 * @author Yaopeng Xie
 */
public class FloorMap {
    private String name;
    private String mapFileName;
    private boolean showLegend;
    private List<Double> legendPosition;
    private List<Layer> layers;
    private String configFileName;
    @JsonIgnore
    private UserLayer userLayer;

    public String getConfigFileName() {
        return configFileName;
    }

    public void setConfigFileName(String configFileName) {
        this.configFileName = configFileName;
    }

    public String getMapFileName() {
        return mapFileName;
    }

    public void setMapFileName(String mapFileName) {
        this.mapFileName = mapFileName;
    }

    public boolean isShowLegend() {
        return showLegend;
    }

    public void setShowLegend(boolean showLegend) {
        this.showLegend = showLegend;
    }

    public List<Double> getLegendPosition() {
        return legendPosition;
    }

    public void setLegendPosition(List<Double> legendPosition) {
        this.legendPosition = legendPosition;
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserLayer getUserLayer() {
        return userLayer;
    }

    public void setUserLayer(UserLayer userLayer) {
        this.userLayer = userLayer;
    }

    /**
     * Check if the 2 FloorMap are equal to each other
     *
     * @param other The other FloorMap to compare to
     * @return Returns true if they match, otherwise, it returns false
     */
    public boolean equals(FloorMap other) {
        return this.getName().equals(other.getName()) && this.getConfigFileName().equals(other.getConfigFileName())
                && this.getMapFileName().equals(other.getMapFileName());
    }
}
