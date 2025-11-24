package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Course;

public class CourseDAO {
    private Connection conn;

    public CourseDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public boolean addCourse(Course course) throws SQLException {
        String query = "INSERT INTO courses(code, title, credits) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, course.getCode());
        ps.setString(2, course.getTitle());
        ps.setInt(3, course.getCredits());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public Course getCourseById(String courseId) throws SQLException {
        String query = "SELECT code, title, credits FROM courses WHERE code = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, courseId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Course c = new Course();
            c.setCode(rs.getString("code"));
            c.setTitle(rs.getString("title"));
            c.setCredits(rs.getInt("credits"));
            return c;
        }
        return null;
    }

    public List<Course> getAllCourses() throws SQLException {
        String query = "SELECT code, title, credits FROM courses";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Course> courses = new ArrayList<>();
        while (rs.next()) {
            Course c = new Course();
            c.setCode(rs.getString("code"));
            c.setTitle(rs.getString("title"));
            c.setCredits(rs.getInt("credits"));
            courses.add(c);
        }
        return courses;
    }

    public boolean updateCourse(Course course) throws SQLException {
        String query = "UPDATE courses SET title = ?, credits = ? WHERE code = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, course.getTitle());
        ps.setInt(2, course.getCredits());
        ps.setString(3, course.getCode());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean deleteCourse(String courseId) throws SQLException {
        String query = "DELETE FROM courses WHERE code = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, courseId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
}
