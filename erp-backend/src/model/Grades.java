package model;

public class Grades {
    private int grades_id;
    private int enrollment_id;
    private String component;
    private float score;
    private String final_grade;

    public int getGrades_id() {return grades_id;}
    public void setGrades_id(int grades_id) {this.grades_id = grades_id;}

    public int getEnrollment_id() {return enrollment_id;}
    public void setEnrollment_id(int enrollment_id) {this.enrollment_id = enrollment_id;}

    public String getComponent() {return component;}
    public void setComponent(String component) {this.component = component;}

    public float getScore() {return score;}
    public void setScore(float score) {this.score = score;}

    public String getFinal_grade() {return final_grade;}
    public void setFinal_grade(String final_grade) {this.final_grade = final_grade;}
}