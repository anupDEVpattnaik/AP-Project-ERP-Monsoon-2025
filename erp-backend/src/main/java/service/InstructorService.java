package service;

import java.sql.SQLException;
import java.util.List;

import dao.EnrollmentDAO;
import dao.GradesDAO;
import dao.SectionDAO;
import model.AuthUser;
import model.Enrollment;
import model.Grade;
import model.Section;

public class InstructorService {

    private SectionDAO sectionDAO;
    private EnrollmentDAO enrollmentDAO;
    private GradesDAO gradesDAO;

    private AccessService accessService;
    private SettingsService settingsService;

    public InstructorService() throws SQLException {
        this.sectionDAO = new SectionDAO();
        this.enrollmentDAO = new EnrollmentDAO();
        this.gradesDAO = new GradesDAO();

        this.accessService = new AccessService();
        this.settingsService = new SettingsService();
    }


    public List<Section> getMySections(int instructorUserId, AuthUser sessionUser) throws SQLException {
        accessService.requireInstructorSectionAccess(instructorUserId, sessionUser);
        return sectionDAO.getSectionsByInstructor(instructorUserId);
    }



    public List<Enrollment> getEnrolledStudents(int sectionId, AuthUser sessionUser) throws SQLException {


        Section sec = sectionDAO.getSectionById(sectionId);
        if (sec == null) throw new SQLException("Invalid section ID.");

        accessService.requireInstructorSectionAccess(sec.getinstructor_id(), sessionUser);

        return enrollmentDAO.getEnrollmentsBySection(sectionId);
    }



    public boolean enterComponentGrade(int sectionId, int studentId,
                                       String component, float score, String final_grade,
                                       AuthUser sessionUser) throws SQLException {

        // Instructor must own this section
        Section sec = sectionDAO.getSectionById(sectionId);
        if (sec == null) throw new SQLException("Invalid section ID.");

        accessService.requireInstructorSectionAccess(sec.getinstructor_id(), sessionUser);

        // Maintenance check
        accessService.ensureNotInMaintenance(sessionUser);

        // Student must be enrolled
        if (!enrollmentDAO.isStudentEnrolled(studentId, sectionId)) {
            throw new SQLException("Student is not enrolled in this section.");
        }

        // Get the enrollment_id for this student-section pair
        int enrollmentId = enrollmentDAO.getEnrollment_id(studentId, sectionId);

        // Insert or update the grade
        return gradesDAO.addGrade(enrollmentId, component, score, final_grade);
    }


  
    public boolean computeFinalGrade(int sectionId, int studentId, AuthUser sessionUser) throws SQLException {

        // Check instructor section ownership
        Section sec = sectionDAO.getSectionById(sectionId);
        if (sec == null) throw new SQLException("Invalid section ID.");
        accessService.requireInstructorSectionAccess(sec.getinstructor_id(), sessionUser);

        accessService.ensureNotInMaintenance(sessionUser);

        if (!enrollmentDAO.isStudentEnrolled(studentId, sectionId)) {
            throw new SQLException("Cannot compute: student not enrolled.");
        }

        int enrollmentId = enrollmentDAO.getEnrollment_id(studentId, sectionId);

        // Fetch all grade components
        List<Grade> comps = gradesDAO.getGradesByEnrollment(enrollmentId);

        if (comps.isEmpty())
            throw new SQLException("No component grades available to compute final grade.");

        // Simple average rule (you can change)
        double sum = 0;
        for (Grade g : comps) {
            sum += g.getScore();
        }
        double avg = sum / comps.size();

        // Convert numeric to letter grade
        String finalGrade = convertToLetterGrade(avg);

        return gradesDAO.updateFinalGrade(enrollmentId, finalGrade);
    }



    public String exportSectionGradebookCSV(int sectionId, AuthUser sessionUser) throws SQLException {

        // Ownership check
        Section sec = sectionDAO.getSectionById(sectionId);
        if (sec == null) throw new SQLException("Invalid section.");

        accessService.requireInstructorSectionAccess(sec.getinstructor_id(), sessionUser);

        // Fetch enrollments + grades
        List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsBySection(sectionId);

        StringBuilder sb = new StringBuilder();
        sb.append("StudentID,Component,Score,Final\n");

        for (Enrollment e : enrollments) {
            List<Grade> grades = gradesDAO.getGradesByEnrollment(e.getEnrollment_id());
            for (Grade g : grades) {
                sb.append(e.getStudent_id()).append(",");
                sb.append(g.getComponent()).append(",");
                sb.append(g.getScore()).append(",");
                sb.append(g.getFinal_grade()).append("\n");
            }
        }

        return sb.toString(); // UI saves to file
    }


    private String convertToLetterGrade(double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "A-";
        if (score >= 70) return "B";
        if (score >= 60) return "B-";
        if (score >= 50) return "C";
        if (score >= 40) return "D";
        return "F";
    }
}
