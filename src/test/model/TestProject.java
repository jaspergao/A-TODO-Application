package model;

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
        assertEquals(0, p1.getTasks().size());
        p1.add(t1);
        assertEquals(1, p1.getTasks().size());
        p1.add(t1);
        assertEquals(1, p1.getTasks().size());

    }

    @Test
    public void testRemove() {
        p1.remove(t1);
        assertEquals(0,p1.getTasks().size());
        p1.add(t1);
        assertTrue(p1.contains(t1));
        assertEquals(1, p1.getTasks().size());
        p1.remove(t1);
        assertFalse(p1.contains(t1));
        assertEquals(0, p1.getTasks().size());

    }

    @Test
    public void testGetDescription() {
        assertEquals("project", p1.getDescription());
    }


    @Test
    public void testGetProgress() {
        assertEquals(100, p1.getProgress());
        Task t2 = new Task("second task");
        t2.setStatus(Status.DONE);
        p1.add(t2);
        assertEquals(100, p1.getProgress());

    }

    @Test
    public void testGetProgressHalf(){
        t1.setStatus(Status.DONE);
        p1.add(t1);
        Task taskStatusDone = new Task("to finish");
        taskStatusDone.setStatus(Status.TODO);
        p1.add(taskStatusDone);

        assertEquals(50, p1.getProgress());
    }

    @Test
    public void testGetProgressAThird(){
        t1.setStatus(Status.DONE);
        p1.add(t1);
        Task notDone1 = new Task("not done");
        Task notDone2 = new Task("also not done");
        notDone1.setStatus(Status.IN_PROGRESS);
        notDone2.setStatus(Status.UP_NEXT);
        p1.add(notDone1);
        p1.add(notDone2);

        assertEquals(33,p1.getProgress());
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
    public void testIsCompleted(){
        t1.setStatus(Status.DONE);
        p1.add(t1);
        Task completedTask = new Task ("Completed Task");
        completedTask.setStatus(Status.DONE);
        p1.add(completedTask);

        assertTrue(p1.isCompleted());

    }

    @Test
    public void testIsNotCompleted(){
        t1.setStatus(Status.IN_PROGRESS);
        p1.add(t1);
        assertFalse(p1.isCompleted());
    }

    @Test
    public void testIsNotCompletedWith3Task(){
        t1.setStatus(Status.UP_NEXT);
        p1.add(t1);
        Task finish = new Task("finish");
        Task notF2 = new Task ("not finish too");
        finish.setStatus(Status.DONE);
        notF2.setStatus(Status.IN_PROGRESS);
        p1.add(finish);
        p1.add(notF2);

        assertFalse(p1.isCompleted());
    }


}