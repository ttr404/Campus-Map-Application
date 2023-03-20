package org.uwo.cs2212.model;

import java.util.List;

public class FloorMap {
    private  String name;
    private String mapFileName;
    private boolean showLegend;
    private List<Double> legendPosition;
    private List<Layer> layers;
    private String configFileName;

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
}
