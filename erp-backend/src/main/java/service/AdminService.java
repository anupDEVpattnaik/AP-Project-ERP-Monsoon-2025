package service;

import java.sql.SQLException;

import dao.AuthDAO;
import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.SectionDAO;
import dao.UserDAO;
import model.AuthUser;
import model.Course;
import model.Instructor;
import model.Section;
import model.Student;


public class AdminService {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final CourseDAO courseDAO;
    private final SectionDAO sectionDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final SettingsService settingsService;
    private final AuthService authService; // for hashing passwords

    public AdminService() throws SQLException {
        this.authDAO = new AuthDAO();
        this.userDAO = new UserDAO();
        this.courseDAO = new CourseDAO();
        this.sectionDAO = new SectionDAO();
        this.settingsService = new SettingsService();
        this.authService = new AuthService();
        this.enrollmentDAO = new EnrollmentDAO();
    }


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

 
    public boolean createStudentProfile(int authUserId, String rollNo, String program, int year) throws SQLException {
        Student s = new Student();
        s.setUser_id(authUserId);
        s.setRoll_no(rollNo);
        s.setProgram(program);
        s.setYear(year);

        return userDAO.addStudent(s);
    }


    public boolean createInstructorProfile(int authUserId, String department) throws SQLException {
        Instructor i = new Instructor();
        i.setUser_id(authUserId);
        i.setDepartment(department);

        return userDAO.addInstructor(i);
    }


    public boolean createCourse(String code, String title, int credits) throws SQLException {
        Course c = new Course();
        c.setCode(code);
        c.setTitle(title);
        c.setCredits(credits);

        return courseDAO.addCourse(c);
    }

  
    public boolean createSection(String code,
                                 int instructorUserId,
                                 String dayTime,
                                 String room,
                                 int capacity,
                                 String semester,
                                 int year) throws SQLException {
        Section s = new Section();
        s.setcourse_id(code);
        s.setInstructor_id(instructorUserId);
        s.setday_time(dayTime);
        s.setroom(room);
        s.setcapacity(capacity);
        s.setsemester(semester);
        s.setYear(year);

        return sectionDAO.addSection(s);
    }


    public void enableMaintenance() throws SQLException {
        settingsService.enableMaintenance();
    }

    public void disableMaintenance() throws SQLException {
        settingsService.disableMaintenance();
    }


    public boolean unlockUser(int userId) throws SQLException {
        return authDAO.unlockUser(userId);
    }

    public boolean deleteStudentProfile(int authUserId) throws SQLException {
        return userDAO.deleteStudentProfile(authUserId);
    }
    
  
    public boolean deleteInstructorProfile(int authUserId) throws SQLException {
        return userDAO.deleteInstructorProfile(authUserId);
    }

    public boolean deleteSection(int sectionId) throws SQLException {

        enrollmentDAO.deleteEnrollmentsAndGradesBySection(sectionId); 
        
        return sectionDAO.deleteSection(sectionId);
    }

    public boolean deleteCourse(String courseCode) throws SQLException {
        return courseDAO.deleteCourse(courseCode);
    }
}
