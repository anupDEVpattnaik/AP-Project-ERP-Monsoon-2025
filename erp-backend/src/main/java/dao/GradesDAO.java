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

    public void addGrade(Grade grade) throws SQLException {
        String query = "INSERT INTO grades (enrollment_id, component, score, final_grade) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setInt(1, grade.getEnrollment_id());
        ps.setString(2, grade.getComponent());
        ps.setFloat(3, grade.getScore());
        ps.setString(4, grade.getFinal_grade());

        ps.executeUpdate();
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

    public void updateScore(int gradeId, double newScore) throws SQLException {
        String query = "UPDATE grades SET score = ? WHERE grade_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setDouble(1, newScore);
        ps.setInt(2, gradeId);

        ps.executeUpdate();
    }

    public void updateFinalGrade(int enrollmentId, String finalGrade) throws SQLException {
        String query = "UPDATE grades SET final_grade = ? WHERE enrollment_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);

        ps.setString(1, finalGrade);
        ps.setInt(2, enrollmentId);

        ps.executeUpdate();
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

    public List<Grade> getGradesByStudent(int studentUserId) {
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
}