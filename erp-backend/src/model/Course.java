package model;

public class Course {
    private String code;
    private String title;
    private int credits;

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    @Override
    public String toString() {
        return "Course{" +
                "code='" + code + "'" +
                ", title='" + title + "'" +
                ", credits=" + credits +
                "}";
    }
}
