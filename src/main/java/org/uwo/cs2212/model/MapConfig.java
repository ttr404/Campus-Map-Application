package org.uwo.cs2212.model;

import java.util.List;

/**
 * The {@code MapConfig} class represents the configuration for the maps used in the application.
 * It contains a list of {@link BaseMap} objects that define the base maps available to the user.
 *
 * @author Yaopeng Xie
 * @author Tingrui Zhang
 */
public class MapConfig {

    private List<BaseMap> baseMaps;

    /**
     * Returns the list of base maps defined in this configuration.
     *
     * @return the list of base maps
     */
    public List<BaseMap> getBaseMaps() {
        return baseMaps;
    }

    /**
     * Sets the list of base maps for this configuration.
     *
     * @param baseMaps the list of base maps to set
     */
    public void setBaseMaps(List<BaseMap> baseMaps) {
        this.baseMaps = baseMaps;
    }
}
