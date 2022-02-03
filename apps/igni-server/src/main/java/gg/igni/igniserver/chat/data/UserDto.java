package gg.igni.igniserver.chat.data;

import java.util.Set;

public class UserDto {
  private Long id;
  private String username;
  private Set<RoleDto> roles;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public Set<RoleDto> getRoles() {
    return roles;
  }
  public void setRoles(Set<RoleDto> roles) {
    this.roles = roles;
  }


}
