package model;

public class Section {
    private int section_id;
    private String course_id;
    private int instructor_id;
    private String day_time;
    private String room;
    private int capacity;
    private String semester;
    private int year;

    public int getsection_id() {return section_id;}
    public void setsection_id(int section_id) {this.section_id = section_id;}

    public String getcourse_id() {return course_id;}
    public void setcourse_id(String course_id) {this.course_id = course_id;}

    public int getinstructor_id() {return instructor_id;}
    public void setInstructor_id(int instructor_id) {this.instructor_id = instructor_id;}

    public String getday_time() {return day_time;}
    public void setday_time(String day_time) {this.day_time = day_time;}

    public String getroom() {return room;}
    public void setroom(String room) {this.room = room;}

    public int getcapacity() {return capacity;}
    public void setcapacity(int capacity) {this.capacity = capacity;}

    public String getsemester() {return semester;}
    public void setsemester(String semester) {this.semester = semester;}

    public int getYear() {return year;}
    public void setYear(int year) {this.year = year;}
}
