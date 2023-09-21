package avantech.smartapps.team.model;

public class ProjectsModel {
    String id,title,client,type,priority,assignedTo,assistedBy,startDate,deadLine,dueDate,status,notes;

    public ProjectsModel(){

    }

    public ProjectsModel(String id, String title, String client, String type, String priority, String assignedTo, String assistedBy, String startDate, String deadLine, String dueDate, String status, String notes) {
    this.id = id;
    this.title = title;
    this.client = client;
    this.type = type;
    this.priority = priority;
    this.assignedTo = assignedTo;
    this.assistedBy = assistedBy;
    this.startDate = startDate;
    this.deadLine = deadLine;
    this.dueDate = dueDate;
    this.status = status;
    this.notes = notes;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAssistedBy() {
        return assistedBy;
    }

    public void setAssistedBy(String assistedBy) {
        this.assistedBy = assistedBy;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}
