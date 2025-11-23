package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Course;

import db.DatabaseConnection;

public class CourseDAO {
    private Connection conn;

    public CourseDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public boolean addCourse(Course course) throws SQLException {
        String query = "INSERT INTO courses(course_id, course_name, credits) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, course.getCourseId());
        ps.setString(2, course.getCourseName());
        ps.setInt(3, course.getCredits());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public Course getCourseById(String courseId) throws SQLException {
        String query = "SELECT course_id, course_name, credits FROM courses WHERE course_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, courseId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Course c = new Course();
            c.setCourseId(rs.getString("course_id"));
            c.setCourseName(rs.getString("course_name"));
            c.setCredits(rs.getInt("credits"));
            return c;
        }
        return null;
    }

    public List<Course> getAllCourses() throws SQLException {
        String query = "SELECT course_id, course_name, credits FROM courses";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Course> courses = new ArrayList<>();
        while (rs.next()) {
            Course c = new Course();
            c.setCourseId(rs.getString("course_id"));
            c.setCourseName(rs.getString("course_name"));
            c.setCredits(rs.getInt("credits"));
            courses.add(c);
        }
        return courses;
    }

    public boolean updateCourse(Course course) throws SQLException {
        String query = "UPDATE courses SET course_name = ?, credits = ? WHERE course_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, course.getCourseName());
        ps.setInt(2, course.getCredits());
        ps.setString(3, course.getCourseId());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean deleteCourse(String courseId) throws SQLException {
        String query = "DELETE FROM courses WHERE course_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, courseId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
}
