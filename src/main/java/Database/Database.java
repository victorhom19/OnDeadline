package Database;

import victorhom.model.Day;
import victorhom.model.HoursGap;
import victorhom.model.Task;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private Connection connection;
    public static String dbdir;

    public void open() {
        try {
            File dir = new File(dbdir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String dbName = "tasks.db";
            String dbPath = dbdir + "/" + dbName;
            File dbFile = new File(dbPath);
            if (!dbFile.exists()){
                InputStream iStream = Database.class.getResourceAsStream("/" + dbName);
                Files.copy(iStream, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        } catch (Exception ignored) {
        }
    }

    public void insert(Task taskToInsert) throws SQLException {

        String query;
        if (taskToInsert.getTaskType().equals(Task.TaskType.Regular)) {
            query = "INSERT INTO tasks (id, name, type, time, day, description) " +
                    "VALUES ("  + taskToInsert.getId() + ", '"
                    + taskToInsert.getTaskName() + "', "
                    + taskToInsert.getTaskType().getIntValue() +  ", '"
                    + taskToInsert.getTaskTime().toString() + "', '"
                    + stringFromIntList(taskToInsert.getTaskDays().getWeekdays()) + "', '"
                    + taskToInsert.getTaskDescription() +"')";
        } else {
            query = "INSERT INTO tasks (id, name, type, time, day, description) " +
                    "VALUES (" + taskToInsert.getId() + ", '"
                    + taskToInsert.getTaskName() + "', "
                    + taskToInsert.getTaskType().getIntValue() + ", '"
                    + taskToInsert.getTaskTime().toString() + "', '"
                    + taskToInsert.getTaskDays().getDate() + "', '"
                    + taskToInsert.getTaskDescription() + "')";
        }
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);


        statement.close();
    }

    private String stringFromIntList(List<Integer> list) {
        StringBuilder result = new StringBuilder();
        for (int i : list) {
            result.append(i);
        }
        return result.toString();
    }


    public List<Task> getAllTasks() {
        List<Task> result = new ArrayList<>();
        try {
            String query = "SELECT id, name, type, time, day, description " +
                           "FROM tasks ORDER BY id";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int type = rs.getInt("type");
                int time = rs.getInt("time");
                String day = rs.getString("day");
                String description = rs.getString("description");

                Task task;
                if (type == 0) {
                    List<Integer> weekdays = new ArrayList<>();
                    for (int i = 0; i < day.length(); i++){
                        char c = day.charAt(i);
                        weekdays.add(Character.getNumericValue(c));
                    }
                    task = new Task(id, name, Task.TaskType.valueOf(type), new HoursGap(time), new Day(weekdays), description);
                } else {
                    task = new Task(id, name, Task.TaskType.valueOf(type), new HoursGap(time), new Day(day), description);
                }
                result.add(task);
            }
            rs.close();
            statement.close();


        } catch (Exception ignored) {

        }
        return result;
    }


    public void clearTable() {
        try {
            String query = "DELETE FROM tasks";

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            statement.close();
        } catch (Exception ignored) {

        }
    }

    public void close() {
        try {
            connection.close();
        } catch (Exception ignored) {

        }
    }




}
