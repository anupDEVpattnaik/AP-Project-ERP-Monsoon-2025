package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Grade;

public class GradesDAO {

    private Connection conn;

    public GradesDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public boolean addGrade(int enrollmentId, String component, float score, String final_grade) throws SQLException {
        String sql = "SELECT grade_id FROM grades WHERE enrollment_id = ? AND component = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, enrollmentId);
        stmt.setString(2, component);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return updateGrade(rs.getInt("grade_id"), score, final_grade);
        }
        String query = "INSERT INTO grades (enrollment_id, component, score, final_grade) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setInt(1, enrollmentId);
        ps.setString(2, component);
        ps.setFloat(3, score);
        ps.setString(4, final_grade);

        ps.executeUpdate();
        return true;
    }

    public List<Grade> getGradesByEnrollment(int enrollmentId) throws SQLException {
        String query = "SELECT * FROM grades WHERE enrollment_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, enrollmentId);

        ResultSet rs = ps.executeQuery();
        List<Grade> list = new ArrayList<>();

        while (rs.next()) {
            Grade g = new Grade();
            g.setgrade_id(rs.getInt("grade_id"));
            g.setEnrollment_id(rs.getInt("enrollment_id"));
            g.setComponent(rs.getString("component"));
            g.setScore(rs.getFloat("score"));
            g.setFinal_grade(rs.getString("final_grade"));

            list.add(g);
        }

        return list;
    }

    public boolean updateGrade(int gradeId, double newScore, String final_grade) throws SQLException {
        String query = "UPDATE grades SET score = ?, final_grade = ? WHERE grade_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setDouble(1, newScore);
        ps.setString(2, final_grade);
        ps.setInt(3, gradeId);

        ps.executeUpdate();
        return true;
    }

    public boolean updateFinalGrade(int enrollmentId, String finalGrade) throws SQLException {
        String query = "UPDATE grades SET final_grade = ? WHERE enrollment_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, finalGrade);
        ps.setInt(2, enrollmentId);

        ps.executeUpdate();
        return true;
    }

    public void deleteGrade(int gradeId) throws SQLException {
        String query = "DELETE FROM grades WHERE grade_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setInt(1, gradeId);
        ps.executeUpdate();
    }

    public void deleteGradesForEnrollment(int enrollmentId) throws SQLException {
        String query = "DELETE FROM grades WHERE enrollment_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setInt(1, enrollmentId);
        ps.executeUpdate();
    }

    public List<Grade> getGradesByStudent(int studentUserId) throws SQLException {
        String sql = "SELECT enrollment_id FROM enrollments WHERE student_id = ? AND status = 'enrolled'";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, studentUserId);
        ResultSet res = stmt.executeQuery();
        List<Grade> list = new ArrayList<>();

        while (res.next()) {
            int enrollmentId = res.getInt("enrollment_id");
            String query = "SELECT * FROM grades WHERE enrollment_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, enrollmentId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Grade g = new Grade();
                g.setgrade_id(rs.getInt("grade_id"));
                g.setEnrollment_id(rs.getInt("enrollment_id"));
                g.setComponent(rs.getString("component"));
                g.setScore(rs.getFloat("score"));
                g.setFinal_grade(rs.getString("final_grade"));

                list.add(g);
            }
        }
        return list;
    }
}