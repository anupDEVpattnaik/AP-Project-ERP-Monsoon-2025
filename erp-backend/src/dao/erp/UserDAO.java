package dao.erp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Student;
import model.Instructor;

import db.DatabaseConnection;

public class UserDAO {
    private Connection conn;

    public UserDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public Student getStudentByUserId(int userId) throws SQLException {
        String query = "SELECT student_id, user_id, name, email FROM students WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Student s = new Student();
            s.setStudentId(rs.getInt("student_id"));
            s.setUserId(rs.getInt("user_id"));
            s.setName(rs.getString("name"));
            s.setEmail(rs.getString("email"));
            return s;
        }
        return null;
    }

    public Instructor getInstructorByUserId(int userId) throws SQLException {
        String query = "SELECT instructor_id, user_id, name, email FROM instructors WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Instructor i = new Instructor();
            i.setInstructorId(rs.getInt("instructor_id"));
            i.setUserId(rs.getInt("user_id"));
            i.setName(rs.getString("name"));
            i.setEmail(rs.getString("email"));
            return i;
        }
        return null;
    }

    public boolean addStudent(Student student) throws SQLException {
        String query = "INSERT INTO students(user_id, name, email) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, student.getUserId());
        ps.setString(2, student.getName());
        ps.setString(3, student.getEmail());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean addInstructor(Instructor instructor) throws SQLException {
        String query = "INSERT INTO instructors(user_id, name, email) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, instructor.getUserId());
        ps.setString(2, instructor.getName());
        ps.setString(3, instructor.getEmail());
        int rows = ps.executeUpdate();
        return rows > 0;
    }
}
