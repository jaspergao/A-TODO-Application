package utility;


import model.Task;
import org.json.JSONArray;
import parsers.TaskParser;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static persistence.Jsonifier.taskListToJson;

// File input/output operations
public class JsonFileIO {
    public static final File jsonDataFile = new File("./resources/json/tasks.json");

    // EFFECTS: attempts to read jsonDataFile and parse it
    //           returns a list of tasks from the content of jsonDataFile
    public static List<Task> read() {
        List<Task> tasks = new ArrayList<>();
        try {
            String input = new String(Files.readAllBytes(Paths.get(jsonDataFile.toURI())));
            TaskParser parser = new TaskParser();
            tasks = parser.parse(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {
        JSONArray taskArray = taskListToJson(tasks);
        try {
            FileWriter fileWriter = new FileWriter(jsonDataFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(taskArray.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
