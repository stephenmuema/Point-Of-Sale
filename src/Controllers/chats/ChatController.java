package Controllers.chats;

import Controllers.UserAccountManagementControllers.IdleMonitor;
import MasterClasses.ChatMaster;
import MasterClasses.OnlineUsersMaster;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import securityandtime.config;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    public ListView<MasterClasses.OnlineUsersMaster> userList;
    public Label onlineCountLabel;
    public ListView<MasterClasses.ChatMaster> chatPane;
    public BorderPane borderPane;
    public TextArea messageBox;
    public Button buttonSend;
    public Button recordBtn;
    public HBox onlineUsersHbox;
    public ScrollPane scrollpane;
    public VBox scrollchat;

    public ListView<OnlineUsersMaster> getUserList() {
        return userList;
    }

    public void setUserList(ListView<OnlineUsersMaster> userList) {
        this.userList = userList;
    }

    public Label getOnlineCountLabel() {
        return onlineCountLabel;
    }

    public void setOnlineCountLabel(Label onlineCountLabel) {
        this.onlineCountLabel = onlineCountLabel;
    }

    public ListView<ChatMaster> getChatPane() {
        return chatPane;
    }

    public void setChatPane(ListView<ChatMaster> chatPane) {
        this.chatPane = chatPane;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    public TextArea getMessageBox() {
        return messageBox;
    }

    public void setMessageBox(TextArea messageBox) {
        this.messageBox = messageBox;
    }

    public Button getButtonSend() {
        return buttonSend;
    }

    public void setButtonSend(Button buttonSend) {
        this.buttonSend = buttonSend;
    }

    public Button getRecordBtn() {
        return recordBtn;
    }

    public void setRecordBtn(Button recordBtn) {
        this.recordBtn = recordBtn;
    }

    public HBox getOnlineUsersHbox() {
        return onlineUsersHbox;
    }

    public void setOnlineUsersHbox(HBox onlineUsersHbox) {
        this.onlineUsersHbox = onlineUsersHbox;
    }

    public ScrollPane getScrollpane() {
        return scrollpane;
    }

    public void setScrollpane(ScrollPane scrollpane) {
        this.scrollpane = scrollpane;
    }

    public VBox getScrollchat() {
        return scrollchat;
    }

    public void setScrollchat(VBox scrollchat) {
        this.scrollchat = scrollchat;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        IdleMonitor idleMonitor = new IdleMonitor(Duration.seconds(3600),
                () -> {
                    try {
                        config.login.put("loggedout", true);
                        borderPane.getChildren().setAll(Collections.singleton(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("resourcefiles/login.fxml")))));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, true);
        idleMonitor.register(borderPane, Event.ANY);
    }

}
