import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dao.CourseDAO;
import dao.SectionDAO;
import model.AuthUser;
import model.Course;
import model.Enrollment;
import model.Grade;
import model.Section;
import service.AdminService;
import service.AuthService;
import service.InstructorService;
import service.SettingsService;
import service.StudentService;

public class TestApp {

    private static AuthService authService;
    private static AdminService adminService;
    private static StudentService studentService;
    private static InstructorService instructorService;
    private static SettingsService settingsService;

    // helper DAOs for some quick listing
    private static CourseDAO courseDAO;
    private static SectionDAO sectionDAO;

    private static AuthUser currentUser = null;
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            authService = new AuthService();
            adminService = new AdminService();
            studentService = new StudentService();
            instructorService = new InstructorService();
            settingsService = new SettingsService();

            courseDAO = new CourseDAO();
            sectionDAO = new SectionDAO();

            // main loop
            while (true) {
                printMainMenu();
                String opt = sc.nextLine().trim();
                switch (opt) {
                    case "1" -> login();
                    case "2" -> logout();
                    case "3" -> adminMenu();
                    case "4" -> studentMenu();
                    case "5" -> instructorMenu();
                    case "6" -> maintenanceMenu();
                    case "7" -> scriptedFullFlowDemo();
                    case "8" -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Fatal DB init error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printMainMenu() {
        System.out.println("\n===== ERP TESTAPP =====");
        System.out.println("Logged in: " + (currentUser == null ? "NONE" : currentUser.getUsername() + " (" + currentUser.getRole() + ")"));
        System.out.println("1) Login");
        System.out.println("2) Logout");
        System.out.println("3) Admin Menu");
        System.out.println("4) Student Menu");
        System.out.println("5) Instructor Menu");
        System.out.println("6) Maintenance / Settings");
        System.out.println("7) Run Full Flow Demo (scripted)");
        System.out.println("8) Exit");
        System.out.print("Choose: ");
    }

    // ------------------------
    // Auth (login/out)
    // ------------------------
    private static void login() {
        try {
            System.out.print("Username: ");
            String u = sc.nextLine().trim();
            System.out.print("Password: ");
            String p = sc.nextLine();
            AuthUser uobj = authService.login(u, p);
            if (uobj == null) {
                System.out.println("Login failed (invalid credentials).");
            } else {
                currentUser = uobj;
                System.out.println("Login successful: " + currentUser);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out.");
    }

    // ------------------------
    // Admin menu
    // ------------------------
    private static void adminMenu() {
        if (currentUser == null || !"admin".equalsIgnoreCase(currentUser.getRole())) {
            System.out.println("Admin access required. Login as admin.");
            return;
        }
        while (true) {
            System.out.println("\n--- ADMIN MENU ---");
            System.out.println("1) Create Auth User");
            System.out.println("2) Create Student ERP Profile");
            System.out.println("3) Create Instructor ERP Profile");
            System.out.println("4) Create Course");
            System.out.println("5) Create Section");
            System.out.println("6) List Courses");
            System.out.println("7) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> adminCreateAuthUser();
                    case "2" -> adminCreateStudentProfile();
                    case "3" -> adminCreateInstructorProfile();
                    case "4" -> adminCreateCourse();
                    case "5" -> adminCreateSection();
                    case "6" -> listCourses();
                    case "7" -> { return; }
                    default -> System.out.println("Invalid.");
                }
            } catch (SQLException e) {
                System.out.println("DB error: " + e.getMessage());
            }
        }
    }

    private static void adminCreateAuthUser() throws SQLException {
        System.out.print("username: ");
        String u = sc.nextLine().trim();
        System.out.print("role (admin/student/instructor): ");
        String r = sc.nextLine().trim();
        System.out.print("password: ");
        String pw = sc.nextLine();
        System.out.print("status (active/inactive): ");
        String st = sc.nextLine().trim();
        AuthUser created = adminService.createAuthUser(u, r, pw, st);
        System.out.println("Created auth user: " + created);
    }

    private static void adminCreateStudentProfile() throws SQLException {
        System.out.print("auth user_id: ");
        int uid = Integer.parseInt(sc.nextLine().trim());
        System.out.print("roll_no: ");
        String roll = sc.nextLine().trim();
        System.out.print("program: ");
        String prog = sc.nextLine().trim();
        System.out.print("year: ");
        int year = Integer.parseInt(sc.nextLine().trim());
        boolean ok = adminService.createStudentProfile(uid, roll, prog, year);
        System.out.println("Student profile created: " + ok);
    }

    private static void adminCreateInstructorProfile() throws SQLException {
        System.out.print("auth user_id: ");
        int uid = Integer.parseInt(sc.nextLine().trim());
        System.out.print("department: ");
        String dept = sc.nextLine().trim();
        boolean ok = adminService.createInstructorProfile(uid, dept);
        System.out.println("Instructor profile created: " + ok);
    }

    private static void adminCreateCourse() throws SQLException {
        System.out.print("course code: ");
        String code = sc.nextLine().trim();
        System.out.print("title: ");
        String title = sc.nextLine().trim();
        System.out.print("credits: ");
        int credits = Integer.parseInt(sc.nextLine().trim());
        boolean ok = adminService.createCourse(code, title, credits);
        System.out.println("Course created: " + ok);
    }

    private static void adminCreateSection() throws SQLException {
        System.out.print("course code: ");
        String courseId = sc.nextLine().trim();
        System.out.print("instructor auth user_id: ");
        int iid = Integer.parseInt(sc.nextLine().trim());
        System.out.print("day_time: ");
        String dt = sc.nextLine().trim();
        System.out.print("room: ");
        String room = sc.nextLine().trim();
        System.out.print("capacity: ");
        int cap = Integer.parseInt(sc.nextLine().trim());
        System.out.print("semester: ");
        String sem = sc.nextLine().trim();
        System.out.print("year: ");
        int yr = Integer.parseInt(sc.nextLine().trim());
        boolean ok = adminService.createSection(courseId, iid, dt, room, cap, sem, yr);
        System.out.println("Section created: " + ok);
    }

    private static void listCourses() throws SQLException {
        List<Course> courses = courseDAO.getAllCourses();
        System.out.println("Courses:");
        for (Course c : courses) System.out.println(" - " + c);
    }

    // ------------------------
    // Student menu
    // ------------------------
    private static void studentMenu() {
        if (currentUser == null || !"student".equalsIgnoreCase(currentUser.getRole())) {
            System.out.println("Student access required. Login as student.");
            return;
        }
        while (true) {
            System.out.println("\n--- STUDENT MENU ---");
            System.out.println("1) Register for Section");
            System.out.println("2) Drop Section");
            System.out.println("3) View Timetable");
            System.out.println("4) View Grades");
            System.out.println("5) Export Transcript (prints CSV to console)");
            System.out.println("6) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> studentRegister();
                    case "2" -> studentDrop();
                    case "3" -> studentTimetable();
                    case "4" -> studentViewGrades();
                    case "5" -> studentExportTranscript();
                    case "6" -> { return; }
                    default -> System.out.println("Invalid.");
                }
            } catch (SQLException e) {
                System.out.println("DB error: " + e.getMessage());
            }
        }
    }

    private static void studentRegister() throws SQLException {
        System.out.print("section id: ");
        int sid = Integer.parseInt(sc.nextLine().trim());
        boolean ok = studentService.registerForSection(currentUser.getUserId(), sid, currentUser);
        System.out.println("Registered: " + ok);
    }

    private static void studentDrop() throws SQLException {
        System.out.print("section id: ");
        int sid = Integer.parseInt(sc.nextLine().trim());
        boolean ok = studentService.dropSection(currentUser.getUserId(), sid, currentUser);
        System.out.println("Dropped: " + ok);
    }

    private static void studentTimetable() throws SQLException {
        List<Section> sec = studentService.getStudentTimetable(currentUser.getUserId(), currentUser);
        System.out.println("Timetable:");
        for (Section s : sec) {
            System.out.println(" - section_id=" + s.getsection_id()
                    + " course=" + s.getcourse_id()
                    + " time=" + s.getday_time()
                    + " room=" + s.getroom());
        }
    }

    private static void studentViewGrades() throws SQLException {
        List<Grade> grades = studentService.getGrades(currentUser.getUserId(), currentUser);
        System.out.println("Grades:");
        for (Grade g : grades) {
            System.out.println(" - component=" + g.getComponent() + " score=" + g.getScore() + " final=" + g.getFinal_grade());
        }
    }

    private static void studentExportTranscript() throws SQLException {
        String csv = studentService.exportTranscriptCSV(currentUser.getUserId(), currentUser);
        System.out.println("---- Transcript CSV ----");
        System.out.println(csv);
    }

    // ------------------------
    // Instructor menu
    // ------------------------
    private static void instructorMenu() {
        if (currentUser == null || !"instructor".equalsIgnoreCase(currentUser.getRole())) {
            System.out.println("Instructor access required. Login as instructor.");
            return;
        }
        while (true) {
            System.out.println("\n--- INSTRUCTOR MENU ---");
            System.out.println("1) My Sections");
            System.out.println("2) View Enrolled Students");
            System.out.println("3) Enter Component Grade");
            System.out.println("4) Compute Final Grade");
            System.out.println("5) Export Gradebook (prints CSV)");
            System.out.println("6) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> instructorMySections();
                    case "2" -> instructorViewEnrolled();
                    case "3" -> instructorEnterGrade();
                    case "4" -> instructorComputeFinal();
                    case "5" -> instructorExportGradebook();
                    case "6" -> { return; }
                    default -> System.out.println("Invalid.");
                }
            } catch (SQLException e) {
                System.out.println("DB error: " + e.getMessage());
            }
        }
    }

    private static void instructorMySections() throws SQLException {
        List<Section> list = instructorService.getMySections(currentUser.getUserId(), currentUser);
        System.out.println("My Sections:");
        for (Section s : list) {
            System.out.println(" - id=" + s.getsection_id() + " course=" + s.getcourse_id() + " cap=" + s.getcapacity());
        }
    }

    private static void instructorViewEnrolled() throws SQLException {
        System.out.print("section id: ");
        int sid = Integer.parseInt(sc.nextLine().trim());
        List<Enrollment> en = instructorService.getEnrolledStudents(sid, currentUser);
        System.out.println("Enrolled:");
        for (Enrollment e : en) {
            System.out.println(" - enrollment_id=" + e.getEnrollment_id() + " student=" + e.getStudent_id());
        }
    }

    private static void instructorEnterGrade() throws SQLException {
        System.out.print("section id: ");
        int sid = Integer.parseInt(sc.nextLine().trim());
        System.out.print("student user id: ");
        int stu = Integer.parseInt(sc.nextLine().trim());
        System.out.print("component (e.g., Quiz1): ");
        String comp = sc.nextLine().trim();
        System.out.print("score (decimal): ");
        float score = Float.parseFloat(sc.nextLine().trim());
        System.out.print("final_grade (optional, pass blank to skip): ");
        String fg = sc.nextLine().trim();
        if (fg.isEmpty()) fg = null;

        boolean ok = instructorService.enterComponentGrade(sid, stu, comp, score, fg, currentUser);
        System.out.println("Grade entered/updated: " + ok);
    }

    private static void instructorComputeFinal() throws SQLException {
        System.out.print("section id: ");
        int sid = Integer.parseInt(sc.nextLine().trim());
        System.out.print("student user id: ");
        int stu = Integer.parseInt(sc.nextLine().trim());
        boolean ok = instructorService.computeFinalGrade(sid, stu, currentUser);
        System.out.println("Final grade computed: " + ok);
    }

    private static void instructorExportGradebook() throws SQLException {
        System.out.print("section id: ");
        int sid = Integer.parseInt(sc.nextLine().trim());
        String csv = instructorService.exportSectionGradebookCSV(sid, currentUser);
        System.out.println("---- Gradebook CSV ----");
        System.out.println(csv);
    }

    // ------------------------
    // Maintenance (Admin can also use from adminMenu)
    // ------------------------
    private static void maintenanceMenu() {
        while (true) {
            System.out.println("\n--- MAINTENANCE ---");
            System.out.println("1) Check");
            System.out.println("2) Enable (admin only)");
            System.out.println("3) Disable (admin only)");
            System.out.println("4) Back");
            System.out.print("Choose: ");
            String c = sc.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> System.out.println("Maintenance: " + settingsService.isMaintenanceOn());
                    case "2" -> {
                        if (!isAdminLogged()) { System.out.println("Admin login required."); break; }
                        settingsService.enableMaintenance();
                        System.out.println("Enabled.");
                    }
                    case "3" -> {
                        if (!isAdminLogged()) { System.out.println("Admin login required."); break; }
                        settingsService.disableMaintenance();
                        System.out.println("Disabled.");
                    }
                    case "4" -> { return; }
                    default -> System.out.println("Invalid.");
                }
            } catch (SQLException e) {
                System.out.println("DB error: " + e.getMessage());
            }
        }
    }

    private static boolean isAdminLogged() {
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }

    // ------------------------
    // Scripted Full Flow Demo (interactive option to run end-to-end)
    // ------------------------
    private static void scriptedFullFlowDemo() {
        System.out.println("\n*** Running scripted full flow demo (no prompts) ***");
        try {
            // ask for admin creds to run demo as admin (or try default admin1/admin123)
            System.out.print("Enter admin username (or press enter for 'admin1'): ");
            String adminUser = sc.nextLine().trim();
            if (adminUser.isEmpty()) adminUser = "admin1";
            System.out.print("Enter admin password (or press enter for 'admin123'): ");
            String adminPass = sc.nextLine();
            if (adminPass.isEmpty()) adminPass = "admin123";

            AuthUser admin = authService.login(adminUser, adminPass);
            if (admin == null) {
                System.out.println("Admin login failed. Aborting demo.");
                return;
            }
            System.out.println("Admin logged in: " + admin.getUsername());

            // create student & instructor auth users
            AuthUser stuAuth = adminService.createAuthUser("demo_student", "student", "stud123", "active");
            AuthUser instAuth = adminService.createAuthUser("demo_instructor", "instructor", "inst123", "active");
            System.out.println("Created demo_student id=" + stuAuth.getUserId() + ", demo_instructor id=" + instAuth.getUserId());

            // create ERP profiles
            adminService.createStudentProfile(stuAuth.getUserId(), "2025D001", "CSE", 1);
            adminService.createInstructorProfile(instAuth.getUserId(), "CSE");
            System.out.println("ERP profiles created.");

            // create course + section
            adminService.createCourse("DEMO101", "Demo Course", 3);
            boolean secOk = adminService.createSection("DEMO101",
                    instAuth.getUserId(),
                    "Fri 9AM",
                    "DemoRoom",
                    10,
                    "Monsoon",
                    2025);
            System.out.println("Section create ok=" + secOk);

            // student login & register
            AuthUser demoStudent = authService.login("demo_student", "stud123");
            System.out.println("Student logged in: " + demoStudent.getUserId());
            // find section id (get sections by instructor)
            List<Section> secList = sectionDAO.getSectionsByInstructor(instAuth.getUserId());
            if (secList.isEmpty()) {
                System.out.println("No sections found for demo instructor.");
                return;
            }
            int sectionId = secList.get(0).getsection_id();
            boolean reg = studentService.registerForSection(demoStudent.getUserId(), sectionId, demoStudent);
            System.out.println("Student registration result: " + reg);

            // instructor login & grade
            AuthUser demoInst = authService.login("demo_instructor", "inst123");
            System.out.println("Instructor logged in: " + demoInst.getUserId());

            instructorService.enterComponentGrade(sectionId, demoStudent.getUserId(), "Quiz1", 85f, null, demoInst);
            instructorService.enterComponentGrade(sectionId, demoStudent.getUserId(), "Midterm", 78f, null, demoInst);
            System.out.println("Entered two component grades.");

            // compute final
            instructorService.computeFinalGrade(sectionId, demoStudent.getUserId(), demoInst);
            System.out.println("Computed final grade.");

            // student checks grades
            List<Grade> g = studentService.getGrades(demoStudent.getUserId(), demoStudent);
            System.out.println("Student grades after compute:");
            for (Grade gg : g) {
                System.out.println(" - " + gg.getComponent() + ": " + gg.getScore() + " final=" + gg.getFinal_grade());
            }

            System.out.println("*** Demo finished successfully ***");

        } catch (SQLException e) {
            System.out.println("Demo DB error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Demo error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
