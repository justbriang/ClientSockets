package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TextField portField;
    public TextArea textAreaField;
    public TextField admNo;
    public TextField course;
    public TextField faculty;
    public TextField university;
    private Socket s;

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        System.out.println("Clicked");
    }

    public void handleSendData(ActionEvent actionEvent) {
        validateData();
        Map<String, String> DataSet= new HashMap<String,String>();;
        DataSet.put("Admission Number :" ,""+admNo.getText());
        DataSet.put("Course:" ,""+course.getText());
        DataSet.put("Faculty :" ,""+faculty.getText());
        DataSet.put("University:" ,""+university.getText());

        sendData(DataSet);


    }

    private void sendData(Map<String, String> dataSet) {
        try {
            // using for-each loop for iteration over Map.entrySet()
            for (Map.Entry<String, String> entry : dataSet.entrySet()) {
                try {
                    OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
                    PrintWriter out = new PrintWriter(os);
                    out.write("" + entry.getKey() +
                            ":" + entry.getValue()+ "\n");
                    textAreaField.appendText("" + entry.getKey() +
                            ":" + entry.getValue()+ "\n");;
                    out.flush();
                } catch (Exception ex) {
                    textAreaField.appendText("exception " + ex + " encountered\n");
                }
            }


            textAreaField.appendText("Data Send Successfully" +"\n");
        } catch (Exception ex) {
            textAreaField.appendText("exception " + ex + " encountered\n");
        }
    }

    public void validateData(){
        if(admNo.getText().isEmpty())
        {
            textAreaField.appendText("Admission No is required in order to send data ");
        }
        if(textAreaField.getText().isEmpty())
        {
            textAreaField.appendText("Admission No is required in order to send data ");
        }
        if(course.getText().isEmpty())
        {
            textAreaField.appendText("Admission No is required in order to send data ");
        }
        if(faculty.getText().isEmpty())
        {
            textAreaField.appendText("Admission No is required in order to send data ");
        }
        if(university.getText().isEmpty())
        {
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

    private void connect(int port) {
        String ip = "localhost";

        try {

            this.s = new Socket(ip, port);
            System.out.println("socket port is " + s);
            OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
            PrintWriter out = new PrintWriter(os);
            out.write("Connected to the server buddy\n");
            out.flush();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {


                        if (s.isConnected()) {
                            if(s.isClosed()){
                                textAreaField.appendText("Server connection lost\n");
                            }
                            while (!(s.isClosed())) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                String str = br.readLine();
                                textAreaField.appendText(str + "\n");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }).start();


        } catch (Exception ex) {
            textAreaField.appendText("exception encountered is " + ex);

        }
    }

}
