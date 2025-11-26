package model;

public class Enrollment {
    private int enrollment_id;
    private int student_id;
    private int section_id;
    private String status;

    public int getEnrollment_id() {return enrollment_id;}
    public void setEnrollment_id(int enrollment_id) {this.enrollment_id = enrollment_id;}

    public int getStudent_id() {return student_id;}
    public void setStudent_id(int student_id) {this.student_id = student_id;}

    public int getSection_id() {return section_id;}
    public void setSection_id(int section_id) {this.section_id = section_id;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    @Override
    public String toString(){
        return "Enrollment{ Enrollment ID= " + enrollment_id
                + ", Student ID= " + student_id
                + ", Section ID= " + section_id
                + ", Status= " + status + "}";
    }
}
