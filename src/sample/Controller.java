package sample;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import marvin.image.MarvinImage;
import org.apache.commons.io.FilenameUtils;
import org.marvinproject.image.transform.scale.Scale;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Controller {

    static final Set<String> supportedExtensions = new HashSet<>(Arrays.asList("jpg", "jpeg", "png", "JPG", "JPEG", "PNG"));

    private File[] selectedImages;
    private File selectedDirectory;

    @FXML TextField chosenFolderText;
    @FXML TextField xScalingFactor;
    @FXML TextField yScalingFactor;

    public void initialize(){
        xScalingFactor.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d?([.]\\d{0,2})?")){
                xScalingFactor.setText(oldValue);
            }
        });
        yScalingFactor.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d?([.]\\d{0,2})?")){
                yScalingFactor.setText(oldValue);
            }
        });
    }

    public void chooseFolder(MouseEvent mouseEvent) {

        // Get the stage and show the chooser dialog
        Node node = (Node) mouseEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        DirectoryChooser chooser = new DirectoryChooser();
        selectedDirectory = chooser.showDialog(stage);

        if(selectedDirectory == null) {
            System.out.println("No directory selected!");
            return;
        }

        // Get the results
        System.out.println("Chosen folder: " + selectedDirectory.getAbsolutePath());
        selectedImages = selectedDirectory.listFiles();
        chosenFolderText.setText(selectedDirectory.getAbsolutePath());
    }

    public void scaleImages(){

        if(selectedDirectory == null && selectedImages == null) return;

        // Create directory where files will be saved
        assert selectedDirectory != null;
        String targetPath = selectedDirectory.getAbsolutePath() + System.getProperty("file.separator") + "Ridimensionate";
        File targetDirectory = new File(targetPath);
        targetDirectory.mkdir();
        
        for(File image : selectedImages) {
            String fileExtension = FilenameUtils.getExtension(image.getName());
            if(!supportedExtensions.contains(fileExtension)){
                System.out.println("File extension not supported");
                continue;
            }

            // Open the image
            BufferedImage bufferedImage;
            try {
                System.out.println(image.getAbsolutePath());
                bufferedImage = ImageIO.read(image);

                if(bufferedImage == null){
                    System.out.println("buffer null");
                    return;
                }

                // Scale the image
                double xFactor = Double.parseDouble(xScalingFactor.getText());
                double yFactor = Double.parseDouble(yScalingFactor.getText());
                BufferedImage resizedImage = resizeImage(bufferedImage, (int) (bufferedImage.getWidth() * xFactor), (int)(bufferedImage.getHeight() * yFactor));

                // Write the scaled image as a new file
                File outputFile = new File(targetPath + System.getProperty("file.separator") + image.getName());
                try {
                    ImageIO.write(resizedImage, fileExtension, outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(2);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

        }

        endJobDialog();
    }

    private void endJobDialog() {
        //aggiunta alert di fine operazione
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Tutte le immagini sono state ridimensionate!");
        alert.setHeaderText(null);
        alert.setTitle("Operazione completata");
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("ImageResizerIcon.png"))));
        alert.show();
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        MarvinImage image = new MarvinImage(originalImage);
        Scale scale = new Scale();
        scale.load();
        scale.setAttribute("newWidth", targetWidth);
        scale.setAttribute("newHeight", targetHeight);
        scale.process(image.clone(), image, null, null, false);
        return image.getBufferedImageNoAlpha();
    }
}
