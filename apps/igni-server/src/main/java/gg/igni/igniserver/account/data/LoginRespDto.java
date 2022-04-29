package gg.igni.igniserver.account.data;

public class LoginRespDto {
    private String username;
    private UserDto userData;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public UserDto getUserData() {
        return userData;
    }
    public void setUserData(UserDto userData) {
        this.userData = userData;
    }


}
