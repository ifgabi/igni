package gg.memz.memzserver.account.data;

import java.util.Set;

public class UserDto {
  private Long id;
  private String username;
  private String email;
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
  public String getEmail() {
      return email;
  }
  public void setEmail(String email) {
      this.email = email;
  }
  public Set<RoleDto> getRoles() {
      return roles;
  }
  public void setRoles(Set<RoleDto> roles) {
      this.roles = roles;
  }
}
