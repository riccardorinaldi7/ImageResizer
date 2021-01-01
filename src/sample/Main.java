package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(ClassLoader.getSystemResource("sample.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Ridimensiona immagini");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        primaryStage.setWidth(520);
        primaryStage.setMinWidth(500);
        primaryStage.setHeight(250);
        primaryStage.setMinHeight(250);

        primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("ImageResizerIcon.png")));

        ((Controller) fxmlLoader.getController()).initialize();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
