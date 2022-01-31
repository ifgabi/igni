package gg.memz.memzserver.account.data;

import java.util.List;

import gg.memz.memzserver.account.data.AccountConstraints.EmailConstraintErrorFlag;
import gg.memz.memzserver.account.data.AccountConstraints.PasswordConstraintErrorFlag;
import gg.memz.memzserver.account.data.AccountConstraints.UsernameConstraintErrorFlag;

public class SignUpResponseDto {

    private UserDto userData;
    private List<UsernameConstraintErrorFlag> usernameErrorFlags;
    private List<PasswordConstraintErrorFlag> passwordErrorFlags;
    private List<EmailConstraintErrorFlag> emailErrorFlags;

    public UserDto getUserData() {
        return userData;
    }
    public void setUserData(UserDto userData) {
        this.userData = userData;
    }
    public List<UsernameConstraintErrorFlag> getUsernameErrorFlags() {
        return usernameErrorFlags;
    }
    public void setUsernameErrorFlags(List<UsernameConstraintErrorFlag> usernameErrorFlags) {
        this.usernameErrorFlags = usernameErrorFlags;
    }
    public List<PasswordConstraintErrorFlag> getPasswordErrorFlags() {
        return passwordErrorFlags;
    }
    public void setPasswordErrorFlags(List<PasswordConstraintErrorFlag> passwordErrorFlags) {
        this.passwordErrorFlags = passwordErrorFlags;
    }
    public List<EmailConstraintErrorFlag> getEmailErrorFlags() {
        return emailErrorFlags;
    }
    public void setEmailErrorFlags(List<EmailConstraintErrorFlag> emailErrorFlags) {
        this.emailErrorFlags = emailErrorFlags;
    }


}
