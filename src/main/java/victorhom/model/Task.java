package victorhom.model;


public class Task {

    public enum TaskType {
        Regular(0),
        Short(1);

        private final int value;

        TaskType(int value) {
            this.value = value;
        }

        public static TaskType valueOf(int value) {
            if (value == 0) return Regular;
            else return Short;
        }

        public int getIntValue() {
            return value;
        }
    }

    int id;

    public int getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public HoursGap getTaskTime() {
        return taskTime;
    }

    public Day getTaskDays() {
        return taskDays;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    String taskName;
    private TaskType taskType;
    private HoursGap taskTime;
    private Day taskDays;
    private String taskDescription;

    public Task(int givenId, String givenName, TaskType givenType, HoursGap givenTime, Day givenDays, String givenDescription) {
        id = givenId;
        taskName = givenName;
        taskType = givenType;
        taskTime = givenTime;
        taskDays = givenDays;
        taskDescription = givenDescription;

    }

    @Override
    public String toString() {
        String result = "";
        result += "id: " + id + System.lineSeparator();
        result += taskName + System.lineSeparator();
        result += taskType + System.lineSeparator();
        result += taskTime.toString() + System.lineSeparator();
        result += taskDays.toString() + System.lineSeparator();
        result += taskDescription + System.lineSeparator();
        return result;
    }



}
