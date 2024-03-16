/**
 * Name: Yara Most
 * Class: CIS-2572
 * Date: 11/26/2022
 * Week 14 - Weighted Graph reader GUI
 * Description: this program lets the user choose a text file representing a graph.
 *              It reads the vertices and edges, and display them in the textarea.
 *              also calculates and display the shortest path between 2 vertices
 *              that the user enter.
 * */
package weightedgraphtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author yaksh
 */
public class WeightedGraphTest extends Application {

    // textarea and weighted graph data members
    private TextArea taOutput = new TextArea();
    private WeightedGraph<String> graph1;

    @Override
    public void start(Stage primaryStage) {
        // change the font of output
        taOutput.setFont(Font.font("Verdana"));
        taOutput.setPrefSize(200, 600);
        // label, buttons and textfield
        Label lblFile = new Label("File Name");
        lblFile.setPrefWidth(60);
        Label lblVertices = new Label("Vertices");
        lblVertices.setPrefWidth(60);
        Button btnBrowse = new Button("Browse");
        btnBrowse.setPrefWidth(80);
        Button btnOk = new Button("Ok");
        btnOk.setPrefWidth(80);
        TextField tfFileName = new TextField();
        tfFileName.setEditable(false);
        TextField tfV1 = new TextField();
        tfV1.setPrefWidth(30);
        TextField tfV2 = new TextField();
        tfV2.setPrefWidth(30);
        Button btnChkShortestPath = new Button("Shortest Path");

        // filechooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        // when browse button is clicked
        btnBrowse.setOnAction(e -> {
            // open file directory window
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            // if a file is selected display the file path in the text field
            if (selectedFile != null) {
                tfFileName.setText(selectedFile.getPath());
            }
        });

        // when ok button is clicked
        btnOk.setOnAction(e -> {
            if (tfFileName.getText().endsWith("txt")) {
                // call method and pass the file path as param
                processFile(tfFileName.getText());
            }
            else {
                taOutput.setText("Please choose a txt file.");
            }
            
        });

        // when Shortest Path is clicked
        btnChkShortestPath.setOnAction(e -> {
            // first check that textfields are not empty
            if (!tfV1.getText().isEmpty() && !tfV2.getText().isEmpty() && graph1 != null) {
                // check if the vertices are inbound
                if (Integer.valueOf(tfV1.getText()) >= 0 && Integer.valueOf(tfV1.getText()) < graph1.getSize()
                        && Integer.valueOf(tfV2.getText()) >= 0 && Integer.valueOf(tfV2.getText()) < graph1.getSize()) {
                    // call method checkShortestPath with values in text fields
                    checkShortestPath(Integer.valueOf(tfV1.getText()), Integer.valueOf(tfV2.getText()));
                } else {
                    // if vertices out of bound
                    taOutput.appendText("\nVertices cannot be found.\n");
                }
            } else {
                // if textfields are empty
                taOutput.appendText("\nPlease enter vertices.\n");
            }
        });

        // hbox to hold label, textfield, browse, and ok
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.BASELINE_LEFT);
        hbox.setPadding(new Insets(20, 10, 0, 10));
        hbox.getChildren().addAll(lblFile, tfFileName, btnBrowse, btnOk);

        // hbox2 to hold label, vertices textfields and btnChkShortestPath
        HBox hbox2 = new HBox();
        hbox2.setSpacing(20);
        hbox2.setAlignment(Pos.BASELINE_LEFT);
        hbox2.setPadding(new Insets(0, 10, 0, 10));
        hbox2.getChildren().addAll(lblVertices, tfV1, tfV2, btnChkShortestPath);

        // vbox to hold the hbox, hbox2, and textarea
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(0, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(hbox, hbox2, taOutput);

        Scene scene = new Scene(vbox, 600, 500);

        primaryStage.setTitle("Weighted Graph");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void processFile(String filePath) {
        // create file object using passed filepath
        File file = new File(filePath);

        // arraylists to hol the vertices and edges
        ArrayList<String> vertices = new ArrayList<>();
        ArrayList<WeightedEdge> edges = new ArrayList<>();
        // a string to hold temporary value
        String temp;
        // variable to hold number of vertices
        int verticesNum;

        try {
            // create a scanner
            Scanner scan = new Scanner(file);

            // read number of vertices from file
            verticesNum = Integer.valueOf(scan.nextLine());

            for (int i = 0; i < verticesNum; ++i) {
                vertices.add(String.valueOf(i));
            }
            // loop till the end of file
            while (scan.hasNextLine()) {
                // store the next line in temp
                temp = scan.nextLine();
                // split temp into array where each element is going to hold 1 edge info
                String[] arr = temp.split("\\| ");

                // for loop to step through arr 
                try {
                    for (int i = 0; i < arr.length; ++i) {
                        // split into 3 ints 
                        String[] subArray = arr[i].split(", ");
                        // create a new weightedEdge and add it to edge arraylist 
                        // once as (u, v) and again as (v, u)
                        edges.add(new WeightedEdge(Integer.valueOf(subArray[0]), Integer.valueOf(subArray[1]), Double.valueOf(subArray[2])));
                        edges.add(new WeightedEdge(Integer.valueOf(subArray[1]), Integer.valueOf(subArray[0]), Double.valueOf(subArray[2])));
                    }
                } catch (Exception e) {
                    taOutput.setText("File Cannot be processed. \n Please try another file.");
                }
            }
        } catch (FileNotFoundException ex) {
            taOutput.setText("File Cannot be opened. \n Please try another file.");
        }

        // weightedGraph object with vertices, edges
        graph1 = new WeightedGraph<>(vertices, edges);

        // print to textarea
        taOutput.setText(file.getName());
        taOutput.appendText("\nNumber of vertices: " + graph1.getSize());
        taOutput.appendText("\nEdges and weight:\n" + graph1.toString());
        taOutput.appendText("\n\nMinimum Spanning Tree:\n");

        taOutput.appendText(graph1.getMinimumSpanningTree().treeToString());
        taOutput.appendText("\nTotal Weight: " + graph1.getMinimumSpanningTree().getTotalWeight());
    }

    // method to display shortest path between two vertices
    public void checkShortestPath(int v1, int v2) {
        taOutput.appendText("\n\nShortest path from " + v1 + " to " + v2
                + " is: " + graph1.getShortestPath(v1).pathToString(v2));
        taOutput.appendText("\nCost is: " + graph1.getShortestPath(v1).getCost(v2));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
