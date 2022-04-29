package gg.igni.igniserver.account.data;

import java.util.List;

import gg.igni.igniserver.account.data.AccountConstraints.UsernameConstraintErrorFlag;

public class SetupProfileRespDto {

  private List<UsernameConstraintErrorFlag> usernameErrorFlags;

  private UserDto user;

  public UserDto getUser() {
    return user;
  }

  public void setUser(UserDto user) {
    this.user = user;
  }

  public List<UsernameConstraintErrorFlag> getUsernameErrorFlags() {
    return usernameErrorFlags;
  }

  public void setUsernameErrorFlags(List<UsernameConstraintErrorFlag> usernameErrorFlags) {
    this.usernameErrorFlags = usernameErrorFlags;
  }



}
