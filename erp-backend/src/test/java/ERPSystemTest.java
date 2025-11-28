import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions; 
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import dao.SectionDAO;
import model.AuthUser;
import model.Grade;
import model.Section;
import service.AdminService;
import service.AuthService;
import service.InstructorService;
import service.StudentService;

/**
 * Comprehensive integration test for the entire ERP system flow, 
 * simulating an admin, student, and instructor interaction.
 * The tests are ordered to ensure the correct data setup and flow.
 * * NOTE: All DAO methods used in the Services and this test MUST be 
 * using the 'try-with-resources' pattern to prevent connection leaks.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ERPSystemTest {

    // Static Initialization of Services (for the whole test class)
    private static AuthService authService;
    private static AdminService adminService;
    private static StudentService studentService;
    private static InstructorService instructorService;
    private static SectionDAO sectionDAO; 
    
    // Test Data Identifiers
    private static final String STUDENT_USERNAME = "junit_student";
    private static final String INSTRUCTOR_USERNAME = "junit_instructor";
    private static final String COURSE_CODE = "JUT101";

    // IDs populated during the test flow
    private static AuthUser adminUser;
    private static AuthUser demoStudentAuth;
    private static AuthUser demoInstructorAuth;
    private static int demoSectionId;

    // --- SETUP and TEARDOWN ---

    @BeforeAll
    static void setup() throws SQLException {
        // 1. Initialize all services
        authService = new AuthService();
        adminService = new AdminService();
        studentService = new StudentService();
        instructorService = new InstructorService();
        sectionDAO = new SectionDAO();
        
        // 2. Log in the administrator needed for test setup
        adminUser = authService.login("admin", "admin123"); 
        
        // CRITICAL ASSERTION: The test cannot proceed without admin access
        Assertions.assertNotNull(adminUser, "FATAL: Admin user login failed. Check DB connection/credentials.");
        System.out.println("Setup complete. Admin logged in: " + adminUser.getUsername());
    }
    
    // -------------------------------------------------------------------------
    // --- TEST A: DATA CREATION (Admin Flow) ---
    // -------------------------------------------------------------------------
    @Test
    @Order(1) 
    void test_A_DataCreation() throws SQLException {
        System.out.println("\n--- Test A: Data Creation (Admin Flow) ---");
        
        // 1. Create Auth Users
        demoStudentAuth = adminService.createAuthUser(STUDENT_USERNAME, "student", "jstud123", "active");
        demoInstructorAuth = adminService.createAuthUser(INSTRUCTOR_USERNAME, "instructor", "jinst123", "active");
        
        Assertions.assertNotNull(demoStudentAuth, "1. Student Auth User creation failed.");
        Assertions.assertNotNull(demoInstructorAuth, "2. Instructor Auth User creation failed.");
        
        // 2. Create ERP Profiles
        Assertions.assertTrue(
            adminService.createStudentProfile(demoStudentAuth.getUserId(), "JUNIT001", "B.Tech", 1), 
            "3. Student ERP profile creation failed."
        );
        Assertions.assertTrue(
            adminService.createInstructorProfile(demoInstructorAuth.getUserId(), "CSE"), 
            "4. Instructor ERP profile creation failed."
        );

        // 3. Create Course + Section
        Assertions.assertTrue(
            adminService.createCourse(COURSE_CODE, "JUnit Testing Course", 3), 
            "5. Course creation failed."
        );
        boolean sectionCreated = adminService.createSection(COURSE_CODE, demoInstructorAuth.getUserId(),
            "Mon 10AM", "T101", 5, "Spring", 2026);
        Assertions.assertTrue(sectionCreated, "6. Section creation failed.");

        // 4. Retrieve Section ID for subsequent tests
        List<Section> secList = sectionDAO.getSectionsByInstructor(demoInstructorAuth.getUserId());
        Assertions.assertFalse(secList.isEmpty(), "7. Created section not found in DAO.");
        demoSectionId = secList.get(0).getsection_id();
        System.out.println("Test A success. Created Section ID: " + demoSectionId);
    }

    // -------------------------------------------------------------------------
    // --- TEST B: ENROLLMENT AND GRADING FLOW (Student/Instructor) ---
    // -------------------------------------------------------------------------
    @Test
    @Order(2) 
    void test_B_EnrollmentAndGradingFlow() throws SQLException {
        System.out.println("\n--- Test B: Enrollment & Grading Flow ---");
        
        Assumptions.assumeTrue(demoStudentAuth != null && demoSectionId != 0, "Prerequisite data missing from Test A.");

        // 1. Student registers for section
        AuthUser studentSession = authService.login(STUDENT_USERNAME, "jstud123");
        Assertions.assertNotNull(studentSession, "1. Student login failed.");
        
        boolean registered = studentService.registerForSection(studentSession.getUserId(), demoSectionId, studentSession);
        Assertions.assertTrue(registered, "2. Student registration failed.");

        // 2. Instructor enters grades
        AuthUser instructorSession = authService.login(INSTRUCTOR_USERNAME, "jinst123");
        Assertions.assertNotNull(instructorSession, "3. Instructor login failed.");
        
        Assertions.assertTrue(
            instructorService.enterComponentGrade(demoSectionId, studentSession.getUserId(), "Quiz1", 85.0f, null, instructorSession),
            "4. Entering Quiz1 grade failed."
        );
        Assertions.assertTrue(
            instructorService.enterComponentGrade(demoSectionId, studentSession.getUserId(), "Midterm", 78.0f, null, instructorSession),
            "5. Entering Midterm grade failed."
        );

        // 3. Instructor computes final grade
        Assertions.assertTrue(
            instructorService.computeFinalGrade(demoSectionId, studentSession.getUserId(), instructorSession),
            "6. Final grade computation failed."
        );

        // 4. Student verifies final grade
        List<Grade> grades = studentService.getGrades(studentSession.getUserId(), studentSession);
        
        String computedFinalGrade = grades.stream()
            .filter(g -> g.getFinal_grade() != null && !g.getFinal_grade().isEmpty())
            .findFirst()
            .map(Grade::getFinal_grade)
            .orElse(null);
            
        // Expected average (85 + 78) / 2 = 81.5 -> A-
        Assertions.assertEquals("A-", computedFinalGrade, "7. Computed letter grade is incorrect (Expected A-).");
        System.out.println("Test B success. Enrollment and Grading verified.");
    }
    
    // -------------------------------------------------------------------------
    // --- TEST C: BOUNDARY CONDITIONS (Drop/Duplicate) ---
    // -------------------------------------------------------------------------
    @Test
    @Order(3) 
    void test_C_BoundaryConditions() throws SQLException {
        System.out.println("\n--- Test C: Boundary Conditions (Drop/Duplicate) ---");
        
        Assumptions.assumeTrue(demoStudentAuth != null && demoSectionId != 0, "Prerequisite data missing from Test A.");
        AuthUser studentSession = authService.login(STUDENT_USERNAME, "jstud123");
        
        // 1. Test Duplicate Enrollment (Should fail)
        SQLException enrollException = Assertions.assertThrows(SQLException.class, () -> {
            studentService.registerForSection(studentSession.getUserId(), demoSectionId, studentSession);
        }, "1. Duplicate enrollment must throw an SQLException.");
        
        Assertions.assertTrue(enrollException.getMessage().contains("Already enrolled"), 
                              "2. Exception message for duplicate enrollment was incorrect.");

        // 2. Test Drop Section
        Assertions.assertTrue(
            studentService.dropSection(studentSession.getUserId(), demoSectionId, studentSession),
            "3. Dropping the section failed."
        );

        // 3. Verify drop succeeded (timetable should be empty)
        List<Section> timetable = studentService.getStudentTimetable(studentSession.getUserId(), studentSession);
        Assertions.assertTrue(timetable.isEmpty(), "4. Timetable should be empty after dropping the section.");
        
        System.out.println("Test C success. Boundary conditions verified.");
    }


    // -------------------------------------------------------------------------
    // --- CLEANUP ---
    // -------------------------------------------------------------------------
    @AfterAll
    static void cleanup() {
        System.out.println("\n--- Cleanup Step: Deleting all test data ---");
        
        // Ensure all IDs were successfully retrieved before attempting deletion
        Assumptions.assumeTrue(demoStudentAuth != null && demoInstructorAuth != null && demoSectionId != 0);
        
        try {
            // Deletion order is critical due to foreign key constraints:
            // 1. Delete Student grades (if any remained) and Enrollment
            // 2. Delete Section
            // 3. Delete Course
            // 4. Delete ERP Profiles (Student/Instructor)
            // 5. Delete Auth Users

            // We rely on service methods that internally call DAO delete methods (e.g., AdminService)
            
            // 1 & 2: Delete Section, which should cascade or require deleting grades/enrollments first.
            // Assuming AdminService has a deleteSection method that handles grades/enrollments:
            adminService.deleteSection(demoSectionId);
            System.out.println("Section " + demoSectionId + " deleted.");

            // 3. Delete Course
            adminService.deleteCourse(COURSE_CODE);
            System.out.println("Course " + COURSE_CODE + " deleted.");
            
            // 4. Delete ERP Profiles (Student/Instructor)
            adminService.deleteStudentProfile(demoStudentAuth.getUserId());
            adminService.deleteInstructorProfile(demoInstructorAuth.getUserId());
            System.out.println("ERP Profiles deleted.");

            // 5. Delete Auth Users
            authService.deleteUser(demoStudentAuth.getUserId());
            authService.deleteUser(demoInstructorAuth.getUserId());
            System.out.println("Auth Users deleted.");
            
            System.out.println("Cleanup successful. Database state restored.");

        } catch (SQLException e) {
            System.err.println("CRITICAL: Cleanup failed. Manual database cleanup may be required.");
            e.printStackTrace();
        }
    }
}