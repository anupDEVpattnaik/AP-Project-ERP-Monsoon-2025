package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;
import model.Section;

public class SectionDAO {
    private Connection conn;

    public SectionDAO() throws SQLException {
        this.conn = DatabaseConnection.getERPConnection();
    }

    public boolean addSection(Section section) throws SQLException {
        String query = "INSERT INTO sections(course_id, instructor_id, day_time, room, capacity, semester, year) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, section.getcourse_id());
        ps.setInt(2, section.getinstructor_id());
        ps.setString(3, section.getday_time());
        ps.setString(4, section.getroom());
        ps.setInt(5, section.getcapacity());
        ps.setString(6, section.getsemester());
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
            s.setsection_id(rs.getInt("section_id"));
            s.setcourse_id(rs.getString("course_id"));
            s.setInstructor_id(rs.getInt("instructor_id"));
            s.setday_time(rs.getString("day_time"));
            s.setroom(rs.getString("room"));
            s.setcapacity(rs.getInt("capacity"));
            s.setsemester(rs.getString("semester"));
            s.setYear(rs.getInt("year"));
            sections.add(s);
        }
        return sections;
    }

    public boolean updateSection(Section section) throws SQLException {
        String query = "UPDATE sections SET course_id = ?, instructor_id = ?, day_time = ?, room = ?, capacity = ?, semester = ?, year = ? WHERE section_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, section.getcourse_id());
        ps.setInt(2, section.getinstructor_id());
        ps.setString(3, section.getday_time());
        ps.setString(4, section.getroom());
        ps.setInt(5, section.getcapacity());
        ps.setString(6, section.getsemester());
        ps.setInt(7, section.getYear());
        ps.setInt(8, section.getsection_id());
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

    public Section getSectionById(int section_id) throws SQLException {
        String query = "SELECT section_id, course_id, instructor_id, day_time, room, capacity, semester, year FROM sections WHERE section_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, section_id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Section s = new Section();
            s.setsection_id(rs.getInt("section_id"));
            s.setcourse_id(rs.getString("course_id"));
            s.setInstructor_id(rs.getInt("instructor_id"));
            s.setday_time(rs.getString("day_time"));
            s.setroom(rs.getString("room"));
            s.setcapacity(rs.getInt("capacity"));
            s.setsemester(rs.getString("semester"));
            s.setYear(rs.getInt("year"));
            return s;
        }
        return null;
    }

    public List<Section> getSectionsByInstructor(int instructor_id) throws SQLException {
        String query = "SELECT section_id, course_id, instructor_id, day_time, room, capacity, semester, year FROM sections WHERE instructor_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, instructor_id);
        ResultSet rs = ps.executeQuery();
        List<Section> list = new ArrayList<>();
        while (rs.next()) {
            Section s = new Section();
            s.setsection_id(rs.getInt("section_id"));
            s.setcourse_id(rs.getString("course_id"));
            s.setInstructor_id(rs.getInt("instructor_id"));
            s.setday_time(rs.getString("day_time"));
            s.setroom(rs.getString("room"));
            s.setcapacity(rs.getInt("capacity"));
            s.setsemester(rs.getString("semester"));
            s.setYear(rs.getInt("year"));
            list.add(s);
        }
        return list;
    }
}
