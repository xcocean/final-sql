package top.lingkang.finalsql.g;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;


/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class SelectGeneratorTable {
    HashSet<String> select = new HashSet();

    public SelectGeneratorTable(List<String> allTable, Button button) {
        Stage stage = new Stage();
        stage.setTitle("选择要生成的表");
        stage.setWidth(280);
        stage.setHeight(400);
        ScrollPane scrollPane = new ScrollPane();// 使用一个滚动板面
        VBox box = new VBox(); // 滚动板面里放行垂直布局， VBox里放多个复选框

        for (String t : allTable) {
            CheckBox cb = new CheckBox(t);
            // cb.setSelected(true);
            cb.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov,
                                    Boolean old_val, Boolean new_val) {
                    if (cb.isSelected()) {
                        select.add(t);
                    } else {
                        select.remove(t);
                    }
                }
            });
            box.getChildren().add(cb);
            box.setMargin(cb, new Insets(10, 10, 0, 10));
        }
        box.getChildren().add(button);
        box.setMargin(button, new Insets(10, 10, 0, 10));
        scrollPane.setContent(box);
        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
        stage.show();
    }

    public HashSet<String> getSelect() {
        return select;
    }
}
