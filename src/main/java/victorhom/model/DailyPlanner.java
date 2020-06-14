package victorhom.model;


import Database.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DailyPlanner {

    public List<Task> tasksPool = new ArrayList<>();

    public void addTask(String givenName, Task.TaskType givenType, HoursGap givenTime, Day givenDays, String givenDescription) {

        this.tasksPool.add(new Task(calculateId(), givenName, givenType, givenTime, givenDays, givenDescription));
    }

    private int calculateId() {
        for (int i = 0; i < tasksPool.size(); i ++) {
            if (findTask(i) == null) {
                return i;
            }
        }
        return tasksPool.size();
    }

    public void removeTask(String givenName) {
        this.tasksPool.remove(findTask(givenName));

    }


    private Task findTask(String taskName) {
        for (Task task : this.tasksPool) {
            if (task.taskName.equals(taskName)) {
                return task;
            }
        }
        return null;
    }

    public List<Task> findTasksByTime(Integer timeIndex) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : this.tasksPool) {
            if (task.getTaskTime().getGapIndex() == timeIndex) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    public List<Task> findTasksByType(Task.TaskType type) {
        List<Task> tasks = new ArrayList<>();
        for (Task task : this.tasksPool) {
            if (task.getTaskType().equals(type)) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    private Task findTask(int id) {
        for (Task task : this.tasksPool) {
            if (task.id == id) {
                return task;
            }
        }
        return null;
    }

    public void uploadTasksData() throws SQLException {
        Database database = new Database();
        database.open();
        tasksPool = database.getAllTasks();
        database.clearTable();
        database.close();
    }

    public void saveTasksData() throws SQLException {
        Database database = new Database();
        database.open();
        for (Task task : tasksPool) {
            database.insert(task);
        }
        database.close();
    }

}
