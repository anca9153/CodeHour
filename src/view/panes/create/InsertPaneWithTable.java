package view.panes.create;

import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import view.panes.CreatePane;

/**
 * Created by Anca on 5/1/2017.
 */
public abstract class InsertPaneWithTable extends InsertPane {
    protected TableView table;

    protected void dealWithTable(VBox finalVBox){
        boolean tableExists = false;

        if(finalVBox.getChildren().size()>4){
            for(Node n: finalVBox.getChildren()){
                if(n.getId()!=null && n.getId().equals("table")){
                    //If the table already exists we only update the data in it
                    updateTableData();
                    table.setPrefHeight((table.getFixedCellSize()+0.8) * (table.getItems().size()+1));
                    tableExists = true;
                    break;
                }
            }
        }

        if(!tableExists) {
            //If the table does not exist we create it
            finalVBox.getChildren().add(createTable());
            table.setPrefHeight((table.getFixedCellSize()+0.8) * (table.getItems().size()+1));
        }
    }

    protected abstract void updateTableData();

    protected abstract VBox createTable();

    protected void addTable(VBox finalVBox){
        if(finalVBox.getChildren().size()>4){
            for(Node n: finalVBox.getChildren()){
                if(n.getId()!=null && n.getId().equals("table")){
                    finalVBox.getChildren().remove(n);
                    break;
                }
            }
        }

        finalVBox.getChildren().add(createTable());
    }
}
