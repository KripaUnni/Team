package avantech.smartapps.team.model;

public class EmployeesModel {
    String name, designation, gender, phone, email, type, id, password;

    public EmployeesModel() {
    }
    public EmployeesModel(String name, String designation, String gender, String phone, String email, String type, String id, String password) {
    this.name = name;
    this. designation = designation;
    this.gender = gender;
    this.phone = phone;
    this.email = email;
    this.type = type;
    this.id = id;
    this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}