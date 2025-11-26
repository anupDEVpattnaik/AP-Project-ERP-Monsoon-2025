package service;

import java.sql.SQLException;

import dao.AuthDAO;
import dao.CourseDAO;
import dao.SectionDAO;
import dao.UserDAO;
import model.AuthUser;
import model.Course;
import model.Instructor;
import model.Section;
import model.Student;

/**
 * AdminService - provides admin capabilities:
 *  - create auth users (admin/student/instructor)
 *  - create ERP profiles (student/instructor)
 *  - create courses
 *  - create sections
 *  - toggle maintenance via SettingsService
 *
 * All methods throw SQLException for the caller (UI) to display friendly messages.
 */
public class AdminService {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final SectionDAO sectionDAO;
    private final SettingsService settingsService;
    private final AuthService authService; // for hashing passwords

    public AdminService() throws SQLException {
        this.authDAO = new AuthDAO();
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
        this.sectionDAO = new SectionDAO();
        this.settingsService = new SettingsService();
        this.authService = new AuthService();
    }

    /**
     * Create a new auth user (in auth_db.users_auth).
     * This method hashes the password and inserts into auth_db.
     * Returns the created AuthUser (with userId populated) if successful.
     */
    public AuthUser createAuthUser(String username, String role, String plainPassword, String status) throws SQLException {
        // Hash password
        String hashed = authService.hashPassword(plainPassword);

        AuthUser user = new AuthUser();
        user.setUsername(username);
        user.setRole(role);
        user.setPasswordHash(hashed);
        user.setStatus(status);

        boolean ok = authDAO.createUser(user);
        if (!ok) {
            throw new SQLException("Failed to create auth user (username may already exist).");
        }

        // Read back the user to get the generated user_id
        AuthUser created = authDAO.getUserByUsername(username);
        if (created == null) {
            throw new SQLException("User created but could not retrieve created record.");
        }

        return created;
    }

    /**
     * Create a student ERP profile for an existing auth user.
     * Requires userId from auth_db (linking both DBs).
     */
    public boolean createStudentProfile(int authUserId, String rollNo, String program, int year) throws SQLException {
        Student s = new Student();
        s.setUser_id(authUserId);
        s.setRoll_no(rollNo);
        s.setProgram(program);
        s.setYear(year);

        return userDAO.addStudent(s);
    }

    /**
     * Create an instructor ERP profile for an existing auth user.
     */
    public boolean createInstructorProfile(int authUserId, String department) throws SQLException {
        Instructor i = new Instructor();
        i.setUser_id(authUserId);
        i.setDepartment(department);

        return userDAO.addInstructor(i);
    }

    /**
     * Create a new course in ERP DB.
     * courseId (String) is the unique course identifier (e.g., "CS101").
     */
    public boolean createCourse(String courseId, String courseName, int credits) throws SQLException {
        Course c = new Course();
        c.setCode(courseId);
        c.setTitle(courseName);
        c.setCredits(credits);

        return courseDAO.addCourse(c);
    }

    /**
     * Create a new section for a course.
     * instructorUserId should be the ERP instructor user_id (same as auth user_id).
     */
    public boolean createSection(String courseId,
                                 int instructorUserId,
                                 String dayTime,
                                 String room,
                                 int capacity,
                                 String semester,
                                 int year) throws SQLException {
        Section s = new Section();
        s.setcourse_id(courseId);
        s.setInstructor_id(instructorUserId);
        s.setday_time(dayTime);
        s.setroom(room);
        s.setcapacity(capacity);
        s.setsemester(semester);
        s.setYear(year);

        return sectionDAO.addSection(s);
    }

    /**
     * Toggle maintenance mode ON/OFF.
     * Admin actions should call these to enable/disable maintenance.
     */
    public void enableMaintenance() throws SQLException {
        settingsService.enableMaintenance();
    }

    public void disableMaintenance() throws SQLException {
        settingsService.disableMaintenance();
    }
}
