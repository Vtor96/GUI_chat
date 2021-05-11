package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    Socket socket;
    DataOutputStream out;

    @FXML
    TextArea textArea;
    @FXML
    TextField textField;

    @FXML
    private void onSubmit(){
        String text = textField.getText();
        textArea.appendText(text + "\n");
        textField.clear();
        try {
            out.writeUTF(text);
        } catch (IOException e) {
            textArea.appendText("Произошла ошибка");
            e.printStackTrace();
        }
    }

    @FXML
    private void connect(){
        try {
            socket = new Socket("192.168.0.106", 8188); //45.80.70.161 // 192.168.0.106

            DataInputStream in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            String response = in.readUTF(); //Ждем сообщение от сервера
            textArea.appendText(response + "\n"); //Добро пожаловать на сервер

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            String response = in.readUTF(); //ждем сообщения от сервера
                            textArea.appendText(response + "\n");
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            });
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}