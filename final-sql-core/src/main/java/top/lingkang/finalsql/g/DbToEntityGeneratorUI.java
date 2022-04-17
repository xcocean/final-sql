package top.lingkang.finalsql.g;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

/**
 * @author lingkang
 * Created by 2022/4/17
 */
public class DbToEntityGeneratorUI extends Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        URL resource = getClass().getResource("/ui.fxml");
        if (resource == null) {
            throw new RuntimeException("未找到fxml资源");
        }
        Pane pane = FXMLLoader.load(resource);

        stage.setTitle("final-sql 数据库映射实体类UI");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        // 将 AnchorPane 加入到场景
        stage.setScene(new Scene(pane));
        stage.show();
        this.stage=stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
