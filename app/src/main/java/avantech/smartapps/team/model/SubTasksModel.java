package avantech.smartapps.team.model;

public class SubTasksModel {
    String projectTitle,taskTitle,id,title,priority,startDate,dueDate,status,notes;

    public SubTasksModel(){

    }
    public SubTasksModel(String projectTitle, String taskTitle, String id, String title, String priority, String startDate, String dueDate, String status, String notes) {
        this.projectTitle = projectTitle;
        this.taskTitle = taskTitle;
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
