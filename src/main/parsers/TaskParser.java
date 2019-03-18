package parsers;

import model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// Represents Task parser
public class TaskParser {
    
    // EFFECTS: iterates over every JSONObject in the JSONArray represented by the input
    // string and parses it as a task; each parsed task is added to the list of tasks.
    // Any task that cannot be parsed due to malformed JSON data is not added to the
    // list of tasks.
    // Note: input is a string representation of a JSONArray
    public List<Task> parse(String input) {
        List<Task> tasks = new ArrayList<>();
        JSONArray tasksArray = new JSONArray(input);
        for (Object object : tasksArray) {
            JSONObject jsonObject = (JSONObject) object;
            try {
                tasks.add(parseTask(jsonObject));
            } catch (JSONException e) {
                continue;
            }
        }
        return tasks;
    }


    // EFFECTS: parses each individual JSONObject in input into an task
    private Task parseTask(JSONObject taskJson) {
        String description = taskJson.getString("description");
        JSONArray jsonTags = taskJson.getJSONArray("tags");
        List<Tag> tags = parseJsonTag(jsonTags);
        JSONObject jsonPriority = taskJson.getJSONObject("priority");
        Priority priority = parseJsonPriority(jsonPriority);
        DueDate dueDate = giveRightDueDate(taskJson);
        Status status = parseJsonStatus(taskJson.getString("status"));
        Task parsedTask = new Task(description);
        for (Tag t : tags) {
            parsedTask.addTag(t);
        }
        parsedTask.setDueDate(dueDate);
        parsedTask.setPriority(priority);
        parsedTask.setStatus(status);

        return parsedTask;
    }


    //EFFECTS: parses JSONArray describing list of tags into a list
    private List<Tag> parseJsonTag(JSONArray jsonTagArray) {
        List<Tag> tags = new ArrayList<>();
        for (Object object : jsonTagArray) {
            JSONObject tagJson = (JSONObject) object;
            String name = tagJson.getString("name");
            tags.add(new Tag(name));
        }

        return tags;

    }

    //EFFECTS: parses the JSONObject that represents priority into a priority
    private Priority parseJsonPriority(JSONObject priority) {
        boolean isImportant = priority.getBoolean("important");
        boolean isUrgent = priority.getBoolean("urgent");
        Priority parsedPriority = new Priority();
        parsedPriority.setImportant(isImportant);
        parsedPriority.setUrgent(isUrgent);

        return parsedPriority;
    }

    //EFFECTS: parses Status for JSONObject
    private Status parseJsonStatus(String input) {
        if (input.equalsIgnoreCase("DONE")) {
            return Status.DONE;
        } else if (input.equalsIgnoreCase("IN_PROGRESS")) {
            return Status.IN_PROGRESS;
        } else if (input.equalsIgnoreCase("UP_NEXT")) {
            return Status.UP_NEXT;
        } else {
            return Status.TODO;
        }
    }

    //EFFECTS: parses the JSONObject for dueDate into a new DueDate
    private DueDate parseJsonDueDate(JSONObject dueDate) {
        int year = dueDate.getInt("year");
        int month = dueDate.getInt("month");
        int day = dueDate.getInt("day");
        int hour = dueDate.getInt("hour");
        int minute = dueDate.getInt("minute");
        Calendar today = Calendar.getInstance();
        today.set(year, month, day, hour, minute);

        return new DueDate(today.getTime());
    }

    private DueDate giveRightDueDate(JSONObject inputJson) {
        DueDate dueDate = null;
        Object jsonDueDate = inputJson.get("due-date");
        if (!jsonDueDate.equals(JSONObject.NULL)) {
            JSONObject jsonDue = (JSONObject) jsonDueDate;
            dueDate = parseJsonDueDate(jsonDue);
        }
        return dueDate;
    }

}
