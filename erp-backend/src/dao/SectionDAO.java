package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument;

import db.DatabaseConnection;

public class SectionDAO {
    private Connection conn;

    public SectionDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public boolean addSection(Section section) throws SQLException {
        String query = "INSERT INTO sections(course_id, instructor_id, day_time, room, capacity, semester, year) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, section.getCourseId());
        ps.setInt(2, section.getInstructorId());
        ps.setString(3, section.getDayTime());
        ps.setString(4, section.getRoom());
        ps.setInt(5, section.getCapacity());
        ps.setString(6, section.getSemester());
        ps.setInt(7, section.getYear());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public List<Section> getAllSections() throws SQLException {
        String query = "SELECT section_id, course_id, instructor_id, day_time, room, capacity, semester, year FROM sections";
        PreparedStatement ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<Section> sections = new ArrayList<>();
        while (rs.next()) {
            Section s = new Section();
            s.setSectionId(rs.getInt("section_id"));
            s.setCourseId(rs.getString("course_id"));
            s.setInstructorId(rs.getInt("instructor_id"));
            s.setDayTime(rs.getString("day_time"));
            s.setRoom(rs.getString("room"));
            s.setCapacity(rs.getInt("capacity"));
            s.setSemester(rs.getString("semester"));
            s.setYear(rs.getInt("year"));
            sections.add(s);
        }
        return sections;
    }

    public boolean updateSection(Section section) throws SQLException {
        String query = "UPDATE sections SET course_id = ?, instructor_id = ?, day_time = ?, room = ?, capacity = ?, semester = ?, year = ? WHERE section_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, section.getCourseId());
        ps.setInt(2, section.getInstructorId());
        ps.setString(3, section.getDayTime());
        ps.setString(4, section.getRoom());
        ps.setInt(5, section.getCapacity());
        ps.setString(6, section.getSemester());
        ps.setInt(7, section.getYear());
        ps.setInt(8, section.getSectionId());
        int rows = ps.executeUpdate();
        return rows > 0;
    }

    public boolean deleteSection(int section_id) throws SQLException{
        String query = "DELETE FROM sections WHERE section_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, section_id);
        int rows = ps.executeUpdate();
        return rows > 0;
    }
}
