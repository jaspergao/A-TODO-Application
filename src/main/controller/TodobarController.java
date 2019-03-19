package controller;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import model.Task;
import ui.EditTask;
import ui.ListView;
import ui.PomoTodoApp;
import utility.JsonFileIO;
import utility.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static ui.PomoTodoApp.getTasks;
import static ui.PomoTodoApp.setScene;

// Controller class for Todobar UI
public class TodobarController implements Initializable {
    private static final String todoOptionsPopUpFXML = "resources/fxml/TodoOptionsPopUp.fxml";
    private static final String todoActionsPopUpFXML = "resources/fxml/TodoActionsPopUp.fxml";
    private File todoOptionsPopUpFxmlFile = new File(todoOptionsPopUpFXML);
    private File todoActionsPopUpFxmlFile = new File(todoActionsPopUpFXML);
    
    @FXML
    private Label descriptionLabel;
    @FXML
    private JFXHamburger todoActionsPopUpBurger;
    @FXML
    private StackPane todoActionsPopUpContainer;
    @FXML
    private JFXRippler todoOptionsPopUpRippler;
    @FXML
    private StackPane todoOptionsPopUpBurger;
    
    private Task task;
    private JFXPopup todoBarActionPopUp;
    private JFXPopup viewOptionPopUp;
    
    // REQUIRES: task != null
    // MODIFIES: this
    // EFFECTS: sets the task in this Todobar
    //          updates the Todobar UI label to task's description
    public void setTask(Task task) {
        this.task = task;
        descriptionLabel.setText(task.getDescription());
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadTodoActionsPopUp();
        loadTodoBarPopUpActionListener();
        loadTodoOptionsPopUp();
        loadTodoOptionsPopUpActionListener();
    }

//     EFFECTS: load options pop up (to do, up next, in progress, done, pomodoro!)
    private void loadTodoActionsPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoActionsPopUpFxmlFile.toURI().toURL());
            fxmlLoader.setController(new TodoActionsPopUpController());
            todoBarActionPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // EFFECTS: load actions pop up (edit, delete)
    private void loadTodoOptionsPopUp() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(todoOptionsPopUpFxmlFile.toURI().toURL());
            fxmlLoader.setController(new TodoBarOptionsPopUpController());
            viewOptionPopUp = new JFXPopup(fxmlLoader.load());
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    // EFFECTS: show option selector pop up when its icon is clicked
    private void loadTodoOptionsPopUpActionListener() {
        todoOptionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                viewOptionPopUp.show(todoOptionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.RIGHT,
                        -12,
                        15);
            }
        });
    }

    // EFFECTS: show actions pop up when its icon is clicked
    private void loadTodoBarPopUpActionListener() {
        todoActionsPopUpBurger.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                todoBarActionPopUp.show(todoActionsPopUpBurger,
                        JFXPopup.PopupVPosition.TOP,
                        JFXPopup.PopupHPosition.LEFT,
                        12,
                        15);
            }
        });
    }

    // Inner class: view option pop up controller
    class TodoBarOptionsPopUpController {
        @FXML
        private JFXListView<?> optionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = optionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    setScene(new EditTask(task));

                    Logger.log("TodobarOptionsPopUpController", "Edit is not supported in this version");
                    break;
                case 1:
                    List<Task> tasks = getTasks();
                    tasks.remove(task);
                    JsonFileIO.write(tasks);
                    setScene(new ListView(tasks));
                    Logger.log("TodobarOptionsPopUpController", "Task is successfully deleted");
                    break;
                default:
                    Logger.log("TodobarOptionsPopUpController", "No action is implemented for the selected option");
            }
            viewOptionPopUp.hide();
        }
    }

    // Inner class: action pop up controller
    class TodoActionsPopUpController {
        @FXML
        private JFXListView<?> actionPopUpList;

        @FXML
        private void submit() {
            int selectedIndex = actionPopUpList.getSelectionModel().getSelectedIndex();
            switch (selectedIndex) {
                case 0:
                    Logger.log("TodobarActionsPopUpController", "To do is not supported in this version");
                    break;
                case 1:
                    Logger.log("TodobarActionsPopUpController", "Up next is not supported in this version");
                    break;
                case 2:
                    Logger.log("TodobarActionsPopUpController", "In progress is not supported in this version");
                    break;
                case 3:
                    Logger.log("TodobarActionsPopUpController", "Done is not supported in this version");
                    break;
                default:
                    Logger.log("TodobarActionsPopUpController", "No action is implemented for the selected option");
            }
            todoBarActionPopUp.hide();
        }
    }

}
