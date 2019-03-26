package model;

import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestProject {
    private Project p1;
    private Task t1;
    private List<Task> listOfTasks;


    @BeforeEach
    public void runBefore() {
        p1 = new Project("project");
        t1 = new Task("task");
        listOfTasks = new ArrayList<>();
    }


    @Test
    public void testAdd() {
        assertEquals(0, p1.getNumberOfTasks());
        assertEquals(0, p1.getProgress());
        p1.add(t1);
        assertEquals(1, p1.getNumberOfTasks());
        p1.add(t1);
        assertEquals(1, p1.getNumberOfTasks());

    }

    @Test
    public void testRemove() {
        p1.remove(t1);
        assertEquals(0,p1.getNumberOfTasks());
        p1.add(t1);
        assertTrue(p1.contains(t1));
        assertEquals(1, p1.getNumberOfTasks());
        p1.remove(t1);
        assertFalse(p1.contains(t1));
        assertEquals(0, p1.getNumberOfTasks());

    }

    @Test
    public void testGetDescription() {
        assertEquals("project", p1.getDescription());
    }


    @Test
    public void testGetNumberOfTasks() {
        assertEquals(0, p1.getNumberOfTasks());
        Task task = new Task("test task");
        p1.add(task);
        assertEquals(1, p1.getNumberOfTasks());
        Task secondTask = new Task("2");
        p1.add(secondTask);
        assertEquals(2,p1.getNumberOfTasks());
    }

    @Test
    public void testContains() {
        assertFalse(p1.contains(t1));
        p1.add(t1);
        assertTrue(p1.contains(t1));
    }


    @Test
    void testEqualsForTask() {
        assertTrue(p1.equals(new Project("project")));
        assertTrue(p1.equals(p1));
    }

    @Test
    void testHashCodeForTask(){
        Project p2 = new Project("project");
        assertEquals(p1.hashCode(),p2.hashCode());
    }

    @Test
    void testDescriptionIsNull() {
        try {
            Project p2 = new Project("");
        } catch (EmptyStringException e) {
            //
        }
    }

    @Test
    void testProgressProject() {
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");

        p1.add(task1);
        p1.add(task2);
        p1.add(task3);

        assertEquals(0, p1.getProgress());

        task1.setProgress(100);

        assertEquals(33, p1.getProgress());
        task2.setProgress(50);
        task3.setProgress(25);
        assertEquals(58, p1.getProgress());


        Project p2 = new Project("project2");
        Task task4 = new Task("task4");
        p2.add(task4);
        p2.add(p1);

        assertEquals(29, p2.getProgress());

    }

    @Test
    void testGetProgressEdgeCase() {
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");

        p1.add(task1);
        p1.add(task2);
        p1.add(task3);

        assertEquals(0, p1.getProgress());

        try {
            task1.setProgress(-20);
        } catch (InvalidProgressException e) {
            //
        }
    }

    @Test
    void testGetProgressEdgeCase2() {
        Task task1 = new Task("task1");
        p1.add(task1);
        assertEquals(0, p1.getProgress());
        try {
            task1.setProgress(101);
        } catch (InvalidProgressException e) {
            //
        }
    }

    @Test
    void testGetProgress100WithSubProject() {
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");

        p1.add(task1);
        p1.add(task2);
        p1.add(task3);

        task1.setProgress(100);
        task2.setProgress(100);
        task3.setProgress(100);

        assertTrue(p1.isCompleted());
    }

    @Test
    void testGetTask() {
        try {
            p1.getTasks();
        } catch (UnsupportedOperationException e) {
            //
        }
    }

    @Test
    void testEstimateTimeToComplete() {
        Task task1 = new Task("task1");
        Task task2 = new Task("task2");
        Task task3 = new Task("task3");

        p1.add(task1);
        p1.add(task2);
        p1.add(task3);

        assertEquals(0, p1.getEstimatedTimeToComplete());
        task1.setEstimatedTimeToComplete(8);
        assertEquals(8, p1.getEstimatedTimeToComplete());
        task2.setEstimatedTimeToComplete(2);
        task3.setEstimatedTimeToComplete(10);
        assertEquals(20,p1.getEstimatedTimeToComplete());

        Project p2 = new Project("project2");
        Task task4 = new Task("task4");
        p2.add(task4);
        task4.setEstimatedTimeToComplete(4);

        p2.add(p1);
        assertEquals(24, p2.getEstimatedTimeToComplete());
    }


    @Test
    void testAddSubProject() {
        Task t1 = new Task("t1");
        p1.add(t1);
        p1.add(p1);
        assertFalse(p1.contains(p1));
    }

    @Test
    void testIsCompletedBothConditions() {
        assertFalse(p1.isCompleted());
        t1.setProgress(20);
        p1.add(t1);
        assertFalse(p1.isCompleted());
    }

    @Test
    void testContainsTask() {
        p1.add(t1);
        try {
            p1.contains((Task) null);
        } catch (NullArgumentException e) {
            //
        }
    }

    @Test
    void testAddTaskNull() {
        try {
            p1.add((Task) null);
        } catch (NullArgumentException e) {
            //
        }
    }

    @Test
    void testRemoveTaskNull() {
        try {
            p1.remove((Task) null);
        } catch (NullArgumentException e) {
            //
        }
    }


}