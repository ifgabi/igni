package gg.igni.igniserver.account.data;

import java.util.Set;

public class RoleDto {
    private Long id;
    private String name;
    private Set<PrivilegeDto> privileges;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Set<PrivilegeDto> getPrivileges() {
        return privileges;
    }
    public void setPrivileges(Set<PrivilegeDto> privileges) {
        this.privileges = privileges;
    }


}
