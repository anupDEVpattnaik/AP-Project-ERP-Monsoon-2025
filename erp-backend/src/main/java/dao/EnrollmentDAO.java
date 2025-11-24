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

    public boolean addEnrollment(int studentUserId, int sectionId) throws SQLException {
        String checkQuery = "SELECT COUNT(*) AS count FROM enrollments WHERE student_id = ? AND section_id = ?";
        PreparedStatement checkPs = conn.prepareStatement(checkQuery);
        checkPs.setInt(1, studentUserId);
        checkPs.setInt(2, sectionId);
        ResultSet rs = checkPs.executeQuery();
        if (rs.next() && rs.getInt("count") > 0) {
            return false;
        }

        String query = "INSERT INTO enrollments(student_id, section_id) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, studentUserId);
        ps.setInt(2, sectionId);
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

    public boolean deleteEnrollment(int studentUserId, int sectionId) throws SQLException {
        String query = "DELETE FROM enrollments WHERE student_id = ? AND section_id = ? LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, studentUserId);
        ps.setInt(2, sectionId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean dropEnrollment(int studentUserId, int sectionId) throws SQLException {
        String query = "UPDATE enrollments SET status = 'dropped' WHERE student_id = ? AND section_id = ? LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, studentUserId);
        ps.setInt(2, sectionId);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
    public boolean isStudentEnrolled(int studentUserId, int sectionId) throws SQLException {
        String sql = "SELECT 1 FROM enrollments WHERE student_id = ? AND section_id = ? LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, studentUserId);
        ps.setInt(2, sectionId);

        ResultSet rs = ps.executeQuery();
        return rs.next(); // returns true if a row exists
    }

    public int getEnrollmentCount(int section_id) throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM enrollments WHERE section_id = ? AND status = 'enrolled'";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, section_id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("total");
        }
        return 0;
    }
}
