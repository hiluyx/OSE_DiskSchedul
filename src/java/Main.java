import controller.MainPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new MainPane();
        root.setPadding(new Insets(20, 20, 0, 20));
        MainPane.primaryStage = primaryStage;
        primaryStage.setTitle("disk scheduled");
        primaryStage.setScene(new Scene(root, 970, 600));
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
