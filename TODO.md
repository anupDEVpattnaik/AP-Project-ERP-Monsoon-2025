# TODO: Implement ChangePasswordFrame and Security Features

## Database Schema Updates
- [ ] Update auth_schema.sql to add failed_login_attempts column to users_auth table
- [ ] Update status enum to include 'locked'

## Backend Model Updates
- [ ] Update AuthUser.java to include failedLoginAttempts field

## DAO Updates
- [ ] Update AuthDAO.java to handle failed login attempts increment and reset
- [ ] Add methods to lock/unlock user accounts

## Service Updates
- [ ] Update AuthService.java login method to track failed attempts and lock after 5
- [ ] Ensure changePassword method works (already exists)

## UI Updates
- [ ] Create ChangePasswordFrame.java UI class
- [ ] Add "Change Password" button to StudentDashboard.java
- [ ] Add "Change Password" button to InstructorDashboard.java
- [ ] Add "Change Password" button to AdminDashboard.java
- [ ] Add "Unlock User" functionality to AdminDashboard.java

## Testing
- [ ] Test login locking after 5 failed attempts
- [ ] Test password change functionality
- [ ] Test admin unlock
