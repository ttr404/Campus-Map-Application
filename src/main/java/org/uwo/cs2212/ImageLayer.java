package org.uwo.cs2212;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.uwo.cs2212.model.Layer;
import org.uwo.cs2212.model.PointOfInterest;



public class ImageLayer extends Canvas {

    public ImageLayer(double width, double height, double zoom, Layer layer) {
        super(width * zoom, height * zoom);
        GraphicsContext graphic = getGraphicsContext2D();
        Color color = Color.valueOf(layer.getColor());
        graphic.setStroke(color);
        graphic.setFill(color);
        Font normalFont = Font.font(layer.getFont(), FontWeight.NORMAL, layer.getSize());
        Font boldFont = Font.font(layer.getFont(), FontWeight.BOLD, layer.getSize()*1.5);

        boolean isBaseLayer = ImageLayer.isBaseLayer(layer);
        boolean isHideLayer = layer.isHideLayer();
        if(isHideLayer){
            layer.getPoints().get(0).setSelected(true);
        }
        for(PointOfInterest poi: layer.getPoints()){
            if(isBaseLayer || !isHideLayer || poi.isSelected()){
                if(poi.isSelected()){
                    graphic.setFont(boldFont);
                    graphic.fillOval(poi.getX()*zoom - 8, poi.getY()*zoom - 8, 16, 16);
                    graphic.strokeText(poi.getName(), poi.getX()*zoom - 16, poi.getY()*zoom - 16);
                }
                else{
                    graphic.setFont(normalFont);
                    graphic.fillOval(poi.getX()*zoom - 6, poi.getY()*zoom - 6, 12, 12);
                    graphic.strokeText(poi.getName(), poi.getX()*zoom - 10, poi.getY()*zoom - 10);
                }
            }
        }
    }
    private static boolean isBaseLayer(Layer layer){
        if (layer != null && layer.getLayerType() != null && "base".equals(layer.getLayerType().trim().toLowerCase())){
            return true;
        }
        return false;
    }
}
