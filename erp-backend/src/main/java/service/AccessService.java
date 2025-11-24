package service;

import java.sql.SQLException;

import dao.SettingsDAO;
import dao.UserDAO;
import model.AuthUser;

public class AccessService {

    private SettingsDAO settingsDAO;
    private UserDAO userDAO;

    public AccessService() throws SQLException {
        this.settingsDAO = new SettingsDAO();
        this.userDAO = new UserDAO();
    }

    // -------------------------------
    //  MAINTENANCE MODE CHECKS
    // -------------------------------

    /** Returns true if maintenance mode is ON */
    public boolean isMaintenanceOn() throws SQLException {
        return settingsDAO.isMaintenanceOn();
    }

    /** Throw if maintenance mode is ON and user is NOT admin */
    public void ensureNotInMaintenance(AuthUser user) throws SQLException {
        if (isMaintenanceOn() && !user.getRole().equalsIgnoreCase("admin")) {
            throw new SQLException("System is under maintenance. Only admin actions allowed.");
        }
    }


    // -------------------------------
    //  ROLE CHECKS
    // -------------------------------

    public boolean isAdmin(AuthUser user) {
        return user != null && user.getRole().equalsIgnoreCase("admin");
    }

    public boolean isStudent(AuthUser user) {
        return user != null && user.getRole().equalsIgnoreCase("student");
    }

    public boolean isInstructor(AuthUser user) {
        return user != null && user.getRole().equalsIgnoreCase("instructor");
    }

    /** Require the user to be one of the roles */
    public void requireRole(AuthUser user, String... allowedRoles) throws SQLException {
        if (user == null) throw new SQLException("User not logged in.");

        for (String role : allowedRoles) {
            if (user.getRole().equalsIgnoreCase(role)) return;
        }

        throw new SQLException("Access denied: insufficient permissions.");
    }


    // -------------------------------
    //  PER-USER ACCESS VALIDATION
    // -------------------------------

    /** 
     * Student can modify only their own records.
     */
    public void requireStudentSelfAccess(int studentUserId, AuthUser sessionUser) throws SQLException {
        if (!isStudent(sessionUser))
            throw new SQLException("Only students can perform this action.");

        if (sessionUser.getUserId() != studentUserId)
            throw new SQLException("You cannot modify another student's records.");
    }

    /** 
     * Instructor can modify only their own sections.
     */
    public void requireInstructorSectionAccess(int instructorUserId, AuthUser sessionUser) throws SQLException {
        if (!isInstructor(sessionUser))
            throw new SQLException("Only instructors can modify instructor data.");

        if (sessionUser.getUserId() != instructorUserId)
            throw new SQLException("You cannot modify another instructor's section.");
    }

}
