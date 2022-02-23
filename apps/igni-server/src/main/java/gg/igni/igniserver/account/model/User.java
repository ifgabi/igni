package gg.igni.igniserver.account.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Entity
@Table(name = "table_users")
public class User implements UserDetails, OAuth2User {

  @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String passwordHash;

	private String email;

	@Transient
	private Collection<GrantedAuthority> authorities;

	private boolean isExpired;

	private boolean isLocked;

	private boolean isCredidentialsExpired;

	private boolean isEnabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	@JsonBackReference
	private Set<Role> roles;

  @Transient
  private Map<String, Object> attributes;

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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public boolean isCredidentialsExpired() {
		return isCredidentialsExpired;
	}

	public void setCredidentialsExpired(boolean isCredidentialsExpired) {
		this.isCredidentialsExpired = isCredidentialsExpired;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return User.getAuthorities(this.getRoles());
	}

	@Override
	public String getPassword() {
		return passwordHash;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !isExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !isLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !isCredidentialsExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

  @Override
  public Map<String, Object> getAttributes() {
      if (this.attributes == null) {
          this.attributes = new HashMap<>();
          this.attributes.put("id", this.getId());
          this.attributes.put("name", this.getName());
          this.attributes.put("email", this.getEmail());
      }
      return attributes;
  }

  @Override
  public String getName() {
    return this.username;//TODO username for now
  }

  public Collection<? extends GrantedAuthority> getPrivilegesAsAuthorities() {
		return getAuthorities(this.roles);
	}

	private static Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {

		return getGrantedAuthoritiesStrings(getPrivileges(roles));
	}

	private static List<String> getPrivileges(Collection<Role> roles) {

		List<String> resultCol = new ArrayList<>();

		List<Privilege> privs = new ArrayList<>();

    if(roles == null)
      return null;

		for(Role role : roles)
			privs.addAll(role.getPrivileges());

		for(Privilege priv : privs)
			resultCol.add(priv.getName());

		return resultCol;
	}

	private static Collection<? extends GrantedAuthority> getGrantedAuthoritiesStrings(List<String> privileges) {

		List<GrantedAuthority> authorities = new ArrayList<>();

    if(privileges == null)
      return null;

		for(String priv : privileges)
			authorities.add(new SimpleGrantedAuthority(priv));

		return authorities;
	}


}
