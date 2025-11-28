package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.GradesDAO;
import dao.SectionDAO;
import model.AuthUser;
import model.Course;
import model.Enrollment;
import model.Grade;
import model.Section;

public class StudentService {

    private CourseDAO courseDAO;
    private SectionDAO sectionDAO;
    private EnrollmentDAO enrollmentDAO;
    private GradesDAO gradesDAO;

    private AccessService accessService;
    private SettingsService settingsService;

    public StudentService() throws SQLException {
        this.courseDAO = new CourseDAO();
        this.sectionDAO = new SectionDAO();
        this.enrollmentDAO = new EnrollmentDAO();
        this.gradesDAO = new GradesDAO();

        this.accessService = new AccessService();
        this.settingsService = new SettingsService();
    }


    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }

    public List<Section> getSectionsForCourse(String courseId) throws SQLException {
        List<Section> SectionArr = sectionDAO.getAllSections();
        List<Section> ResultArr = new ArrayList<>();

        for (Section section : SectionArr) {
            if (courseId.equalsIgnoreCase(section.getcourse_id())) {
                ResultArr.add(section);
            }
        }
        return ResultArr;
    }


    public boolean registerForSection(int studentUserId, int sectionId, AuthUser sessionUser) throws SQLException {

        // Role + ownership check
        accessService.requireStudentSelfAccess(studentUserId, sessionUser);

        // Maintenance check (students cannot write)
        accessService.ensureNotInMaintenance(sessionUser);

        // Check duplicate enrollment
        if (enrollmentDAO.isStudentEnrolled(studentUserId, sectionId)) {
            throw new SQLException("Already enrolled in this section.");
        }

        // Check section exists
        Section section = sectionDAO.getSectionById(sectionId);
        if (section == null) {
            throw new SQLException("Invalid section ID.");
        }

        // Check capacity
        int enrolledCount = enrollmentDAO.getEnrollmentCount(sectionId);
        if (enrolledCount >= section.getcapacity()) {
            throw new SQLException("Section is full.");
        }

        // Register
        return enrollmentDAO.addEnrollment(studentUserId, sectionId);
    }

    public boolean dropSection(int studentUserId, int sectionId, AuthUser sessionUser) throws SQLException {

        accessService.requireStudentSelfAccess(studentUserId, sessionUser);
        accessService.ensureNotInMaintenance(sessionUser);

        if (!enrollmentDAO.isStudentEnrolled(studentUserId, sectionId)) {
            throw new SQLException("Cannot drop: not enrolled in this section.");
        }

        return enrollmentDAO.dropEnrollment(studentUserId, sectionId);
    }


    public List<Section> getStudentTimetable(int studentUserId, AuthUser sessionUser) throws SQLException {

        accessService.requireStudentSelfAccess(studentUserId, sessionUser);

        List<Enrollment> EnrollArr = enrollmentDAO.getEnrollmentsByStudentId(studentUserId);
        List<Section> SecArr = new ArrayList<>();

        for (Enrollment e : EnrollArr) {
            if (e.getStatus().equalsIgnoreCase("enrolled")) {
                SecArr.add(sectionDAO.getSectionById(e.getSection_id()));
            }
        }
        return SecArr;
    }



    public List<Grade> getGrades(int studentUserId, AuthUser sessionUser) throws SQLException {

        accessService.requireStudentSelfAccess(studentUserId, sessionUser);

        return gradesDAO.getGradesByStudent(studentUserId);
    }


    public String exportTranscriptCSV(int studentUserId, AuthUser sessionUser) throws SQLException {

        accessService.requireStudentSelfAccess(studentUserId, sessionUser);

        List<Grade> grades = gradesDAO.getGradesByStudent(studentUserId);

        StringBuilder sb = new StringBuilder();
        sb.append("Component,Score,Final\n");

        for (Grade g : grades) {
            sb.append(g.getComponent()).append(",");
            sb.append(g.getScore()).append(",");
            sb.append(g.getFinal_grade()).append("\n");
        }

        return sb.toString(); // UI will save this to file
    }
}
