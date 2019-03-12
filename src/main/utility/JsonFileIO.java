package utility;


import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;
import parsers.TaskParser;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            System.out.println(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    
    // EFFECTS: saves the tasks to jsonDataFile
    public static void write(List<Task> tasks) {
        JSONArray taskArray = taskListToJson(tasks);
        System.out.println("?" + taskArray.toString());
        try {
//            Files.write(jsonDataFile.toPath(), taskArray.toString());
            Files.write(jsonDataFile.toPath(), Arrays.stream(taskArray.toString().split("\n")).collect(Collectors.toList()), StandardOpenOption.CREATE);

//            FileWriter file = new FileWriter(jsonDataFile);
//            file.write(taskArray.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
