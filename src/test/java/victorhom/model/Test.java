package victorhom.model;

import org.joda.time.DateTime;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class Test {

    @org.junit.jupiter.api.Test
    void addTask() {
        DailyPlanner testPlanner = new DailyPlanner();
        int poolSizeBefore = testPlanner.tasksPool.size();
        testPlanner.addTask("Test", Task.TaskType.Regular, new HoursGap(0),
                new Day(new DateTime()), "Test Description");
        int poolSizeAfter = testPlanner.tasksPool.size();
        assertEquals(1, poolSizeAfter - poolSizeBefore);

    }

    @org.junit.jupiter.api.Test
    void removeTask() {
        DailyPlanner testPlanner = new DailyPlanner();
        testPlanner.addTask("Test", Task.TaskType.Regular, new HoursGap(0),
                new Day(new DateTime()), "Test Description");
        int poolSizeBefore = testPlanner.tasksPool.size();
        testPlanner.removeTask("Test");
        int poolSizeAfter = testPlanner.tasksPool.size();
        assertEquals(-1, poolSizeAfter - poolSizeBefore);
    }

}