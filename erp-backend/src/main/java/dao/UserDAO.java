package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatabaseConnection;
import model.Instructor;
import model.Student;

public class UserDAO {
    private Connection conn;

    public UserDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public Student getStudentByUserId(int userId) throws SQLException {
        String query = "SELECT roll_no, program, year FROM students WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Student s = new Student();
            s.setRoll_no(rs.getString("roll_no"));
            s.setProgram(rs.getString("program"));
            s.setYear(rs.getInt("year"));
            return s;
        }
        return null;
    }

    public Instructor getInstructorByUserId(int userId) throws SQLException {
        String query = "SELECT user_id, department FROM instructors WHERE user_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Instructor i = new Instructor();
            i.setUser_id(rs.getInt("user_id"));
            i.setDepartment(rs.getString("department"));
            return i;
        }
        return null;
    }

    public boolean addStudent(Student student) throws SQLException {
        String query = "INSERT INTO students(user_id, roll_no, program, year) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, student.getUser_id());
        ps.setString(2, student.getRoll_no());
        ps.setString(3, student.getProgram());
        ps.setInt(4, student.getYear());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean addInstructor(Instructor instructor) throws SQLException {
        String query = "INSERT INTO instructors(user_id, department) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, instructor.getUser_id());
        ps.setString(2, instructor.getDepartment());
        int rows = ps.executeUpdate();
        return rows > 0;
    }
}
