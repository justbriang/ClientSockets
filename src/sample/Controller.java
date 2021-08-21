package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Socket s;

    public TextField portField;
    public TextArea textAreaField;
    public TextField admNo;
    public TextField name;
    public TextField course;
    public TextField faculty;

    //Initiate sending data on button click
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        System.out.println("Clicked");
    }

    //Send all the details to the server
    public void handleSendData(ActionEvent actionEvent) {
        validateData();
//        Map<String, String> DataSet = new HashMap<String, String>();
//        DataSet.put("Admission Number :\t", admNo.getText());
//        DataSet.put("Name:\t", name.getText());
//        DataSet.put("Course:\t", course.getText());
//        DataSet.put("Faculty :\t", faculty.getText());

//        String[] toServer = new String[]{admNo.getText(), name.getText(), course.getText(), faculty.getText()};

        String sent = admNo.getText() + "," + name.getText() + "," + course.getText() + "," + faculty.getText();

        String toServer = "Admission Number:\t" + admNo.getText() + ",Name:\t" + name.getText() + ",Course:\t" + course.getText() + ",Faculty:\t" + faculty.getText();

        sendData(toServer, sent);
    }

    private void sendData(String data, String echo) {
        try {
            OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
            PrintWriter out = new PrintWriter(os);
            out.write(data + "\n");
            out.flush();
            textAreaField.appendText("Data sent:\t" + echo + "\n");
        } catch (Exception ex) {
            textAreaField.appendText("\n-----\n\n" + ex + " \n\n-----\n");
        }
    }

//    private void sendData(Map<String, String> dataSet) {
//        try {
//            // using for-each loop for iteration over Map.entrySet()
//            for (Map.Entry<String, String> entry : dataSet.entrySet()) {
//                try {
//                    OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
//                    PrintWriter out = new PrintWriter(os);
//                    out.write(entry.getKey() + entry.getValue() + "\n");
//                    textAreaField.appendText(entry.getKey() + entry.getValue() + "\n");
//                    out.flush();
//                } catch (Exception ex) {
//                    textAreaField.appendText("exception " + ex + " encountered\n");
//                }
//            }
//
//
//            textAreaField.appendText("Data Send Successfully" + "\n");
//        } catch (Exception ex) {
//            textAreaField.appendText("exception " + ex + " encountered\n");
//        }
//    }

    public void validateData() {
        if (admNo.getText().isEmpty()) {
            textAreaField.appendText("Admission No is required in order to send data ");
        }
        if (name.getText().isEmpty()) {
            textAreaField.appendText("Name is required in order to send data ");
        }
        if (course.getText().isEmpty()) {
            textAreaField.appendText("Course is required in order to send data ");
        }
        if (faculty.getText().isEmpty()) {
            textAreaField.appendText("Faculty is required in order to send data ");
        }
        if (textAreaField.getText().isEmpty()) {
            textAreaField.appendText("Admission No is required in order to send data ");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    portField.setText(t1.replaceAll("[^\\d]", ""));
                }
            }
        });
        admNo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.matches("\\d*")) {
                    admNo.setText(t1.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    //Set the port number and connect to the server through the socket
    public void handleConnection(ActionEvent actionEvent) {
        if (portField.getText().isEmpty()) {
            textAreaField.appendText("A port number is required to connect\n");
        }
        if (Integer.parseInt(portField.getText()) > 65535) {
            textAreaField.appendText("Port entered is invalid\n");
            return;
        }

        connect(Integer.parseInt(portField.getText()));
    }

    //Establish the connection upon getting the port
    private void connect(int port) {
        String ip = "localhost";

        try {
            this.s = new Socket(ip, port);
            System.out.println("Socket port is " + s);
            OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
            PrintWriter out = new PrintWriter(os);
            out.write("Connected Successfully!\n");
            out.flush();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (s.isConnected()) {
                            if (s.isClosed()) {
                                textAreaField.appendText("\nServer connection lost\n");
                            }
                            while (!(s.isClosed())) {
                                //Client Protocol to handle data from server
                                ClientProtocol fromServer = new ClientProtocol();
                                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                fromServer.setFromServer(br.readLine());
                                textAreaField.appendText("Server:\t" + fromServer.getFromServer() + "\n");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception ex) {
            textAreaField.appendText("\n-----\n\n" + ex + " \n\n-----\n");
        }
    }
}
