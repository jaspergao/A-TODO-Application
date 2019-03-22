package model;


import model.exceptions.EmptyStringException;
import model.exceptions.NullArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parsers.Parser;
import parsers.TagParser;
import parsers.exceptions.ParsingException;



import java.util.Calendar;
import java.util.Date;



import static org.junit.jupiter.api.Assertions.*;


public class TestTask {
    private Priority priority;
    private Status status;
    private Task task;
    private String description;
    private DueDate dueDate;
    private Parser p;


    @BeforeEach
    public void runBefore() {
        task = new Task("new task");
        priority = new Priority(4);
        p = new TagParser();

    }

    @Test
    public void testConstructor(){

        assertEquals("new task",task.getDescription());
        assertEquals("TODO", task.getStatus().toString());
        assertEquals(null, task.getDueDate());
        assertEquals("DEFAULT", task.getPriority().toString());

    }

    @Test
    public void testTag(){
        Tag tag = new Tag("test");
        Tag unmodifiedTag = new Tag("final");
        Tag duplicateTag = new Tag("test");

        assertFalse(task.containsTag(tag.getName()));
        task.addTag(tag.getName());
        assertTrue(task.containsTag(tag.getName()));
        assertEquals(1,task.getTags().size());

        task.removeTag(unmodifiedTag.getName());
        assertEquals(1,task.getTags().size());
        assertTrue(task.containsTag(tag.getName()));
        task.removeTag(tag.getName());
        assertEquals(0, task.getTags().size());

        assertFalse(task.containsTag(unmodifiedTag.getName()));
        task.addTag(unmodifiedTag.getName());
        assertTrue(task.containsTag(unmodifiedTag.getName()));
        task.addTag("final");
        assertEquals(1,task.getTags().size());

    }

    @Test
    public void testGetPriority() {
        assertEquals("DEFAULT", task.getPriority().toString());
    }

    @Test
    public void testSetPriority(){
        priority = new Priority(3);
        task.setPriority(priority);
        assertEquals("URGENT", task.getPriority().toString());
    }

    @Test
    public void testStatus() {
        status = Status.UP_NEXT;
        task.setStatus(status);
        assertEquals("UP NEXT", task.getStatus().toString());
    }

    @Test
    public void testDescription(){
        description = "new description";
        task.setDescription(description);
        assertEquals("new description", task.getDescription());
    }

    @Test
    public void testDueDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(2019, Calendar.AUGUST, 12, 23, 59);
        dueDate = new DueDate(cal.getTime());
        task.setDueDate(dueDate);

        assertEquals("Mon Aug 12 2019 11:59 PM", task.getDueDate().toString());
    }



    @Test
    public void testToString(){
        Calendar cal = Calendar.getInstance();
        DueDate d1 = new DueDate(cal.getTime());
        task.setDueDate(d1);
        task.addTag("ComputerScience210");
        System.out.println(task.toString());

    }

    @Test
    public void testNullTask() {
        try {
            Task nullTask = new Task("");
        } catch (EmptyStringException e) {
            //
        }
    }

    @Test
    public void testNullStatus() {
        try {
            Status nullStatus = null;
            task.setStatus(nullStatus);
        } catch (NullArgumentException e) {
            //
        }
    }


    @Test
    void testEqualsForTask() {
        Task T1 = new Task("Standard Task");
        assertTrue(T1.equals(T1));
        assertFalse(T1.equals(T1.getPriority()));
        assertTrue(T1.equals(new Task("Standard Task")));
        Priority p3 = new Priority(3);
        T1.setPriority(p3);
        assertFalse(T1.equals(new Task("Standard Task")));
    }

    @Test
    void testTaskPriorityIsNull() {
        try {
            Priority pNull = null;
            task.setPriority(pNull);
        } catch (NullArgumentException e) {
            //
        }

    }

    @Test
    void testDescriptionNull() {
        try {
            String nullDescription = null;
            task.setDescription(nullDescription);
        } catch (EmptyStringException e) {
            //
        }
    }

    @Test
    void testContainsTagsStringNull() {
        try {
            String tagName = null;
            task.containsTag(tagName);
        } catch (EmptyStringException e) {
            //
        }
    }

    public void testTagParserJustDoubleHashWithNoTags(){
        String description = "CS210  ##";
        Task T1 = new Task(description);
        try {
            p.parse(description,T1);
            assertEquals("CS210  ",T1.getDescription());
            assertEquals(0,T1.getTags().size());
        } catch (ParsingException p) {
            fail();
        }
    }


    @Test
    public void testTagParserJustDoubleHash(){
        String description = " ## tag1";
        Task T1 = new Task(description);
        try {
            p.parse(description, T1);
            assertEquals(" ", T1.getDescription());
            assertNull(T1.getDueDate());
            assertEquals(Status.TODO,T1.getStatus());
            assertFalse(T1.getPriority().isUrgent());
            assertFalse(T1.getPriority().isImportant());
            assertEquals(1,T1.getTags().size());
            assertTrue(T1.containsTag("tag1"));
        } catch (ParsingException p) {
            fail();
        }
    }


    @Test
    public void testTagParserWithoutDoubleHash() {
        String description = "Register for the course. CS210; tomorrow; important; urgent; in progress";
        Task T1 = new Task(description);
        try {
            p.parse(description, T1);
            assertEquals(description, T1.getDescription());
            fail();
        } catch (ParsingException p) {
        }
    }


    @Test
    public void testTagParser() {
        String description = "Register for the course. ## CS210; tomorrow; important; urgent; in progress";
        Task T1 = new Task(description);
        DueDate D1 = new DueDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(D1.getDate());
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date tmr = cal.getTime();
        D1.setDueDate(tmr);
        try {
            p.parse(description, T1);
            assertEquals("Register for the course. ", T1.getDescription());
            assertEquals(D1.toString(), T1.getDueDate().toString());
            assertEquals(Status.IN_PROGRESS, T1.getStatus());
            assertTrue(T1.getPriority().isImportant());
            assertTrue(T1.getPriority().isUrgent());
            assertEquals(1, T1.getTags().size());
        } catch (ParsingException e) {
            fail();
        }
    }

    @Test
    public void testTagParser2() {
        String description = "Register for the course. ## CS210; tomorrow; urgent; important; in progress";
        Task T1 = new Task(description);
        DueDate D1 = new DueDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(D1.getDate());
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date tmr = cal.getTime();
        D1.setDueDate(tmr);
        try {
            p.parse(description, T1);
            assertEquals("Register for the course. ", T1.getDescription());
            assertEquals(D1.toString(), T1.getDueDate().toString());
            assertEquals(Status.IN_PROGRESS, T1.getStatus());
            assertTrue(T1.getPriority().isImportant());
            assertTrue(T1.getPriority().isUrgent());
            assertEquals(1, T1.getTags().size());
        } catch (ParsingException e) {
            fail();
        }
    }

    @Test
    public void testTagParserWithTodayAndTomorrow(){
        String description = "   Test Description. ## CS210; today; TOMORROW; ToMorrow; Urgent";
        Task T1 = new Task(description);
        DueDate D1 = new DueDate();
        try {
            p.parse(description,T1);
            assertEquals("   Test Description. ", T1.getDescription());
            assertEquals(D1.toString(), T1.getDueDate().toString());
            assertEquals(Status.TODO, T1.getStatus());
            assertFalse(T1.getPriority().isImportant());
            assertTrue(T1.getPriority().isUrgent());
            assertEquals(2, T1.getTags().size());
        } catch (ParsingException e) {
            fail();
        }
    }


    @Test
    public void testTagParserIfInputDescriptionIsEmpty() {
        String description = "";
        Task T1 = new Task("t1");
        try {
            p.parse(description, T1);
            fail();
        } catch (ParsingException p) {

        } catch (EmptyStringException e){

        }
    }

    @Test
    public void testTagParserChangeStatus() {
        String description = "description .## cS210; done; todo; tAg1; tag1";
        Task T1 = new Task (description);
        try {
            p.parse(description, T1);
            assertEquals("description .", T1.getDescription());
            assertNull(T1.getDueDate());
            assertEquals(Status.DONE, T1.getStatus());
            assertFalse(T1.getPriority().isImportant());
            assertFalse(T1.getPriority().isUrgent());
            assertEquals(3, T1.getTags().size());
            assertTrue(T1.containsTag("cS210"));
            assertTrue(T1.containsTag("todo"));
            assertTrue(T1.containsTag("tAg1"));
        } catch (ParsingException p) {
            fail();
        }
    }
    @Test
    public void testTagParserChangeStatus2(){
        String description = "description .## cS210; up next; todo; tAg1; tag1";
        Task T1 = new Task (description);
        try {
            p.parse(description, T1);
            assertEquals("description .", T1.getDescription());
            assertNull(T1.getDueDate());
            assertEquals(Status.UP_NEXT, T1.getStatus());
            assertFalse(T1.getPriority().isImportant());
            assertFalse(T1.getPriority().isUrgent());
            assertEquals(3, T1.getTags().size());
            assertTrue(T1.containsTag("cS210"));
            assertTrue(T1.containsTag("todo"));
            assertTrue(T1.containsTag("tAg1"));
        } catch (ParsingException p) {
            fail();
        }
    }
    @Test
    public void testTagParserChangeStatus3(){
        String description = "description .## cS210; to do; tAg1; tag1";
        Task T1 = new Task (description);
        try {
            p.parse(description, T1);
            assertEquals("description .", T1.getDescription());
            assertNull(T1.getDueDate());
            assertEquals(Status.TODO, T1.getStatus());
            assertFalse(T1.getPriority().isImportant());
            assertFalse(T1.getPriority().isUrgent());
            assertEquals(2, T1.getTags().size());
            assertTrue(T1.containsTag("cS210"));
            assertTrue(T1.containsTag("tAg1"));
            System.out.println(T1);
        } catch (ParsingException p) {
            fail();
        }

    }

    @Test
    public void testOnlyImp() {
        String testDescription = "Only priority ## IMpOrTaNt";
        Task parseTask = new Task(testDescription);
        try {
            p.parse(testDescription, parseTask);
            assertTrue(parseTask.getPriority().isImportant());
            assertFalse(parseTask.getPriority().isUrgent());

            System.out.println(parseTask);
        } catch (ParsingException p) {
            fail();
        }
    }

    @Test
    public void testOnlyUrg(){
        String description = "Only priority## uRgEnT; urgent; too urgent;";
        Task T1 = new Task("test");
        try {
            p.parse(description, T1);
            assertTrue(T1.getPriority().isUrgent());
            assertFalse(T1.getPriority().isImportant());
            assertEquals(1,T1.getTags().size());
            assertTrue(T1.containsTag("too urgent"));
            System.out.println(T1);
        } catch (ParsingException p) {
            fail();
        }
    }


    @Test
    public void testTodayTags() {
        String testDescription = "Test description ## tomorrow; tOdAy   ; today ; tOmoRroW; ;important";
        Task t = new Task(testDescription);
        Parser p = new TagParser();
        try {
            p.parse(testDescription, t);
        } catch (ParsingException e) {
            fail("Caught parsing exception");
        }
        assertEquals(1, t.getTags().size());
        assertEquals("IMPORTANT", t.getPriority().toString());
        System.out.println(t.toString());
    }


    @Test
    void testTagToString() {
        Task t1 = new Task("no tags");
        System.out.println(t1.toString());
        Tag tag1 = new Tag("first tag");
        t1.addTag(tag1);
        System.out.println(t1.toString());
    }


}