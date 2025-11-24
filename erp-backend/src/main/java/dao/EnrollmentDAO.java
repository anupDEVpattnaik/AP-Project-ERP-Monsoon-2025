package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Enrollment;

public class EnrollmentDAO {
    private Connection conn;

    public EnrollmentDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public boolean addEnrollment(Enrollment enrollment) throws SQLException {
        String checkQuery = "SELECT COUNT(*) AS count FROM enrollments WHERE student_id = ? AND section_id = ?";
        PreparedStatement checkPs = conn.prepareStatement(checkQuery);
        checkPs.setInt(1, enrollment.getStudent_id());
        checkPs.setInt(2, enrollment.getSection_id());
        ResultSet rs = checkPs.executeQuery();
        if (rs.next() && rs.getInt("count") > 0) {
            return false;
        }

        String query = "INSERT INTO enrollments(student_id, section_id, status) VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, enrollment.getStudent_id());
        ps.setInt(2, enrollment.getSection_id());
        ps.setString(3, enrollment.getStatus());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public List<Enrollment> getEnrollmentsByStudentId(int studentId) throws SQLException {
        String query = "SELECT enrollment_id, student_id, section_id, status FROM enrollments WHERE student_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, studentId);
        ResultSet rs = ps.executeQuery();
        List<Enrollment> list = new ArrayList<>();
        while (rs.next()) {
            Enrollment e = new Enrollment();
            e.setEnrollment_id(rs.getInt("enrollment_id"));
            e.setStudent_id(rs.getInt("student_id"));
            e.setSection_id(rs.getInt("section_id"));
            e.setStatus(rs.getString("status"));
            list.add(e);
        }
        return list;
    }

    public boolean updateEnrollmentStatus(int enrollmentId, String status) throws SQLException {
        String query = "UPDATE enrollments SET status = ? WHERE enrollment_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, status);
        ps.setInt(2, enrollmentId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean deleteEnrollment(int enrollmentId) throws SQLException {
        String query = "DELETE FROM enrollments WHERE enrollment_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, enrollmentId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
}
