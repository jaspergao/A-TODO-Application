package model;

import model.exceptions.NullArgumentException;

import java.util.*;

// Represents a Project, a collection of zero or more Tasks
// Class Invariant: no duplicated task; order of tasks is preserved
public class Project extends Todo implements Iterable<Todo> {
    private String description;
    private List<Todo> tasks;

    // MODIFIES: this
    // EFFECTS: constructs a project with the given description
    //     the constructed project shall have no tasks.
    //  throws EmptyStringException if description is null or empty
    public Project(String description) {
        super(description);
        this.description = description;
        tasks = new ArrayList<>();

    }

    // MODIFIES: this
    // EFFECTS: task is added to this project (if it was not already part of it)
    //   throws NullArgumentException when task is null
    public void add(Todo task) {
        if (!contains(task) && !task.equals(this)) {
            tasks.add(task);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes task from this project
    //   throws NullArgumentException when task is null
    public void remove(Todo task) {
        if (task == null) {
            throw new NullArgumentException();
        } else if (contains(task)) {
            tasks.remove(task);
        }
    }

    // EFFECTS: returns the description of this project
    public String getDescription() {
        return description;
    }

    // EFFECTS: returns the total estimated time to complete for all task and (sub projects)
    @Override
    public int getEstimatedTimeToComplete() {
        int totalTime = 0;
        for (Todo t : tasks) {
            totalTime = t.getEstimatedTimeToComplete() + totalTime;
        }
        return totalTime;
    }


    // EFFECTS: returns an unmodifiable list of tasks in this project.
    @Deprecated
    public List<Task> getTasks() {
        throw new UnsupportedOperationException();
    }

    // EFFECTS: returns an integer between 0 and 100 which represents
    //     the percentage of completion (rounded down to the nearest integer).
    //     the value returned is the average of the percentage of completion of
    //     all the tasks and sub-projects in this project.
    public int getProgress() {
        if (tasks.isEmpty()) {
            return 0;
        }
        int totalProgress = 0;
        for (Todo t : tasks) {
            totalProgress += t.getProgress();
        }
        return (int) Math.floor(totalProgress / getNumberOfTasks());
    }

    // EFFECTS: returns the number of tasks in this project
    public int getNumberOfTasks() {
        return tasks.size();
    }


    // EFFECTS: returns true if every task (and sub-project) in this project is completed, and false otherwise
    //     If this project has no tasks (or sub-projects), return false.
    public boolean isCompleted() {
        return getNumberOfTasks() != 0 && getProgress() == 100;
    }


    // EFFECTS: returns true if this project contains the task
    //   throws NullArgumentException when task is null
    public boolean contains(Todo task) {
        if (task == null) {
            throw new NullArgumentException();
        }
        return tasks.contains(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        Project project = (Project) o;
        return Objects.equals(description, project.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public Iterator<Todo> iterator() {
        return new TodoIterator();
    }

    private class TodoIterator implements Iterator<Todo> {
        private int taskIndex;
        private int priorityLevel;
        private int lastTask;


        // EFFECTS: constructs iterator
        TodoIterator() {
            taskIndex = 0;
            priorityLevel = 1;
            lastTask = tasks.size() - 1;
        }

        @Override
        public boolean hasNext() {
            return taskIndex < tasks.size();
        }

        @Override
        public Todo next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            while (priorityLevel == 1) {
                if (tasks.get(taskIndex).getPriority().equals(new Priority(1))) {
                    taskIndex++;
                    return tasks.get(taskIndex);
                } else if (taskIndex == lastTask) {
                    priorityLevel++;
                    taskIndex = 0;
                    break;
                } else {
                    taskIndex++;
                }
            }

            while (priorityLevel == 2) {
                if (tasks.get(taskIndex).getPriority().equals(new Priority(2))) {
                    taskIndex++;
                    return tasks.get(taskIndex);
                } else if (taskIndex == lastTask) {
                    priorityLevel++;
                    taskIndex = 0;
                    break;
                } else {
                    taskIndex++;
                }
            }

            while (priorityLevel == 3) {
                if (tasks.get(taskIndex).getPriority().equals(new Priority(3))) {
                    taskIndex++;
                    return tasks.get(taskIndex);
                } else if (taskIndex == lastTask) {
                    priorityLevel++;
                    taskIndex = 0;
                    break;
                } else {
                    taskIndex++;
                }
            }
            while (priorityLevel == 4) {
                if (tasks.get(taskIndex).getPriority().equals(new Priority(3))) {
                    taskIndex++;
                    return tasks.get(taskIndex);
                } else if (taskIndex == lastTask) {
                    break;
                } else {
                    taskIndex++;
                }
            }
            throw new NoSuchElementException();
        }

    }



}