package org.fxlib.table;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.lang.reflect.Field;

public class TableViewController {
    public static void init(TableView<Object> table, TableViewModel model) {
        if (null == table) {
            return;
        }

        Class clazz = model.getClass();
        table.getColumns().clear();
        
        for (Field field : clazz.getFields()) {
            String columnName = model.getColumnTitle(field.getName()) == null ? field.getName() : model.getColumnTitle(field.getName());
            TableColumn<Object, Object> column = new TableColumn<>(columnName);
            column.setCellValueFactory(param -> {
                try {
                    Object object = model.getClass().getField(field.getName()).get(param.getValue());
                    if (object instanceof Double || object instanceof Float) {
                        return new SimpleObjectProperty<>(String.format("%.2f", object));
                    } else {
                        return new SimpleObjectProperty<>(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return new SimpleObjectProperty<>("-");
            });

            table.getColumns().add(column);
        }

        for (int i = 0; i < table.getColumns().size(); i++) {
            table.getColumns().get(i).prefWidthProperty().bind(table.widthProperty().divide(table.getColumns().size()));
        }
    }

    public static void init(TableView<Object> table, TableViewModel model, ObservableList list) {
        TableViewController.init(table, model);
        table.setItems(list);
    }
}