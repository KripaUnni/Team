package avantech.smartapps.team.model;

public class TasksModel {
    String projectTitle,id,title,priority,startDate,dueDate,status,notes;

    public TasksModel(){

    }
    public TasksModel(String projectTitle, String id, String title, String priority, String startDate, String dueDate, String status, String notes) {
        this.projectTitle = projectTitle;
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.status = status;
        this.notes = notes;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
