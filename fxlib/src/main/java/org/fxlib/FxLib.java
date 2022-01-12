package org.fxlib;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.lang.reflect.Method;

public class FxLib {
    public static void showScene(String fxml, Image icon, Object data) {
        try {
            String path = "/fxml/" + fxml;
            final double[] xOffset = {0};
            final double[] yOffset = {0};

            FXMLLoader loader = new FXMLLoader(FxLib.class.getResource(path));
            loader.setLocation(FxLib.class.getResource(path));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));            
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setOnShown(e->{
                try {
                    Method m = loader.getController().getClass().getDeclaredMethod("onShown", Object.class);
                    if (m != null) m.invoke(loader.getController(), data);
                } catch (NoSuchMethodException ee) {
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            });
            
            if (null != icon) {
                try {
                    stage.getIcons().add(icon);    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            stage.show();

            root.setOnMousePressed(event -> {
                xOffset[0] = event.getSceneX();
                yOffset[0] = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() - xOffset[0]);
                stage.setY(event.getScreenY() - yOffset[0]);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showScene(String fxml, Image icon) {
        showScene(fxml, icon, null);
    }
    
    public static void showScene(String fxml) {
        showScene(fxml, null, null);
    }
}
