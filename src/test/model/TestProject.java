package model;

import model.exceptions.EmptyStringException;
import model.exceptions.InvalidProgressException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestProject {
    private Project p1, project1,project2,project3,subProject;
    private Task t1, task1, task2,task3;
    private List<Task> listOfTasks;




    @BeforeEach
    public void runBefore() {
        p1 = new Project("project");
        t1 = new Task("task");
        listOfTasks = new ArrayList<>();



        project1 = new Project("p1");
        project2 = new Project("p2");
        project3 = new Project("p3");
        task1 = new Task("task1");
        task2 = new Task("task2");
        task3 = new Task("task3");
        subProject = new Project("sub-project");
    }

    @Test
    void testIteratorWithSubProjectsAndTask() {
        Priority p1 = new Priority(1);
        Priority p3 = new Priority(3);
        subProject.add(task1);
        task2.setPriority(p3);
        task3.setPriority(p3);
        subProject.setPriority(p1);
        project3.setPriority(p1);
        project2.add(subProject);
        project2.add(task2);
        project2.add(task3);
        project2.add(project3);
        for (Todo todo : project2) {
            System.out.println(todo.getPriority());
        }
    }

    @Test
    void testIteratorNotUrgentAndImportant() {
        project1.add(task1);
        project1.add(task2);
        for (Todo todo : project1) {
            assertFalse(todo.getPriority().isImportant());
            assertFalse(todo.getPriority().isUrgent());
        }
    }


    @Test
    void testIteratorImportantAndUrgent() {
        Priority firstPriority = new Priority(1);
        task1.setPriority(firstPriority);
        task2.setPriority(firstPriority);
        project1.add(task1);
        project1.add(task2);
        for (Todo todo : project1) {
            assertTrue(todo.getPriority().isImportant());
            assertTrue(todo.getPriority().isUrgent());
        }
    }

    @Test
    void testIteratorNotImportantAndUrgent() {
        Priority p = new Priority(3);
        task2.setPriority(p);
        task3.setPriority(p);
        project1.add(task2);
        project1.add(task3);
        Iterator<Todo> iterator = project1.iterator();
        Priority p1 = iterator.next().getPriority();
        assertFalse(p1.isImportant());
        assertTrue(p1.isUrgent());
        assertEquals(new Priority(3), iterator.next().priority);

    }

    @Test
    void testIteratorImportantAndNotUrgent() {
        Priority p = new Priority(2);
        task1.setPriority(p);
        task2.setPriority(p);
        project1.add(task1);
        project1.add(task2);
        for (Todo todo : project1) {
            assertTrue(todo.getPriority().isImportant());
            assertFalse(todo.getPriority().isUrgent());
        }
    }

    @Test
    void testIteratorDifferentPriority() {
        Priority p1 = new Priority(3);
        Priority p2 = new Priority(4);
        Priority p3 = new Priority(3);
        task1.setPriority(p1);
        task2.setPriority(p2);
        task3.setPriority(p3);
        project1.add(task1);
        project1.add(task2);
        project1.add(task3);
        for (Todo todo : project1) {
            System.out.println(todo.getPriority());
        }
    }

    @Test
    void testIteratorReverseOrderOfPriority() {
        Priority p1 = new Priority(4);
        Priority p2 = new Priority(3);
        task1.setPriority(p1);
        task2.setPriority(p2);
        project1.add(task1);
        project1.add(task2);
        for (Todo todo : project1) {
            assertFalse(todo.getPriority().isImportant());
            System.out.println(todo.getPriority());
        }
    }

    @Test
    void testIteratorOnlyDefault() {
        Priority p1 = new Priority(4);
        task1.setPriority(p1);
        project1.add(t1);
        for (Todo todo : project1) {
            System.out.println(todo.getPriority());
        }
    }

    @Test
    public void testIteratorEmpty() {
        Project emptyProject = new Project("Empty");
        Iterator<Todo> itr = emptyProject.iterator();
        assertFalse(itr.hasNext());

        try {
            itr.next();
            fail();
        } catch (NoSuchElementException e) {
            assertEquals(0, 0);
        }
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
        assertEquals(0, p1.getNumberOfTasks());
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
        assertEquals(2, p1.getNumberOfTasks());
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
    void testHashCodeForTask() {
        Project p2 = new Project("project");
        assertEquals(p1.hashCode(), p2.hashCode());
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
        assertEquals(20, p1.getEstimatedTimeToComplete());

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