package persistence;


import model.DueDate;
import model.Priority;
import model.Tag;
import model.Task;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

// Converts model elements to JSON objects
public class Jsonifier {

    // EFFECTS: returns JSON representation of tag
    public static JSONObject tagToJson(Tag tag) {
        JSONObject tagToJson = new JSONObject();
        tagToJson.put("name", tag.getName());
        return tagToJson;
    }

    // EFFECTS: returns JSON representation of priority
    public static JSONObject priorityToJson(Priority priority) {

        JSONObject priorityToJson = new JSONObject();
        priorityToJson.put("important", priority.isImportant());
        priorityToJson.put("urgent", priority.isUrgent());

        return priorityToJson;
    }

    // EFFECTS: returns JSON representation of dueDate
    public static JSONObject dueDateToJson(DueDate dueDate) {
        JSONObject dueDateToJson = new JSONObject();
        if (dueDate == null) {
            return null;
        } else {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(dueDate.getDate());
            dueDateToJson.put("year", cal1.get(Calendar.YEAR));
            dueDateToJson.put("month", cal1.get(Calendar.MONTH));
            dueDateToJson.put("day", cal1.get(Calendar.DAY_OF_MONTH));
            dueDateToJson.put("hour", cal1.get(Calendar.HOUR_OF_DAY));
            dueDateToJson.put("minute", cal1.get(Calendar.MINUTE));
        }
        return dueDateToJson;
    }

    // EFFECTS: returns JSON representation of task
    public static JSONObject taskToJson(Task task) {
        JSONObject taskToJson =  new JSONObject();
        taskToJson.put("description", task.getDescription());
        JSONArray tagsArray = new JSONArray();
        for (Tag t : task.getTags()) {
            tagsArray.put(tagToJson(t));
        }
        taskToJson.put("tags", tagsArray);
        if (dueDateToJson(task.getDueDate()) != null) {
            taskToJson.put("due-date", dueDateToJson(task.getDueDate()));
        } else {
            taskToJson.put("due-date",JSONObject.NULL);
        }
        taskToJson.put("priority", priorityToJson(task.getPriority()));
        String statusWithUnderscore = task.getStatus().toString().replace(" ", "_");
        taskToJson.put("status", statusWithUnderscore);
        return taskToJson;
    }

    // EFFECTS: returns JSON array representing list of tasks
    public static JSONArray taskListToJson(List<Task> tasks) {
        JSONArray taskListToJson = new JSONArray();
        for (Task t : tasks) {
            taskListToJson.put(taskToJson(t));
        }
        return taskListToJson;
    }
}
