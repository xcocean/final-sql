package top.lingkang.finalsql.g;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class ModalDialog {

    public ModalDialog(String content) {
        final Stage stage = new Stage();
        //Initialize the Stage with type of modal
        stage.initModality(Modality.APPLICATION_MODAL);
        //stage.initStyle(StageStyle.UTILITY);// 配合公共组件样式效果更好
        //Set the owner of the Stage
        stage.setTitle("Top Stage With Modality");
        Group root = new Group();
        Scene scene = new Scene(root, 200, 200, Color.web("white"));
        TextArea textArea = new TextArea(content);
        textArea.setPrefWidth(190);
        textArea.setPrefHeight(180);
        textArea.setWrapText(true);
        root.getChildren().add(textArea);
        stage.setScene(scene);
        stage.show();
    }
}
