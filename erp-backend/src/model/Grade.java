package model;

public class Grade {
    private int grade_id;
    private int enrollment_id;
    private String component;
    private float score;
    private String final_grade;

    public int getgrade_id() {return grade_id;}
    public void setgrade_id(int grade_id) {this.grade_id = grade_id;}

    public int getEnrollment_id() {return enrollment_id;}
    public void setEnrollment_id(int enrollment_id) {this.enrollment_id = enrollment_id;}

    public String getComponent() {return component;}
    public void setComponent(String component) {this.component = component;}

    public float getScore() {return score;}
    public void setScore(float score) {this.score = score;}

    public String getFinal_grade() {return final_grade;}
    public void setFinal_grade(String final_grade) {this.final_grade = final_grade;}
}