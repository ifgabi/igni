package gg.memz.memzserver.account.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gg.memz.memzserver.account.model.Privilege;
import gg.memz.memzserver.account.model.Role;
import gg.memz.memzserver.account.model.User;
import gg.memz.memzserver.account.repositories.PrivilegeRepository;
import gg.memz.memzserver.account.repositories.RoleRepository;
import gg.memz.memzserver.account.repositories.UserRepository;

@Component
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
  private boolean alreadySetup = false;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PrivilegeRepository privilegeRepository;


  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent event) {
      if (alreadySetup)
          return;

      //if admin exists, we already ran this
      if(userRepository.findByUsername("admin").orElse(null) != null)
        return;

      Privilege readAccountsPrivilege
        = createPrivilegeIfNotFound("PRIVILEGE_ACCOUNTS_READ");
      Privilege manageAccountsPrivilege
        = createPrivilegeIfNotFound("PRIVILEGE_ACCOUNTS_MANAGE");

      Privilege chatUsagePrivilege
        = createPrivilegeIfNotFound("PRIVILEGE_CHAT_USAGE");

      createRoleIfNotFound("ROLE_ADMIN",  new HashSet<>(Arrays.asList(readAccountsPrivilege, manageAccountsPrivilege)));
      createRoleIfNotFound("ROLE_USER", new HashSet<>(Arrays.asList(chatUsagePrivilege)));

      createRoleIfNotFound("ROLE_GUEST", null);

      Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElse(null);
      User admin = new User();
      admin.setUsername("admin");
      admin.setPasswordHash(passwordEncoder.encode("admin"));
      admin.setEmail("aresgab@live.ca");
      admin.setRoles(new HashSet<>(Arrays.asList(adminRole)));
      admin.setEnabled(true);
      userRepository.save(admin);

      Role userRole = roleRepository.findByName("ROLE_USER").orElse(null);
      User user1 = new User();
      user1.setUsername("user1");
      user1.setPasswordHash(passwordEncoder.encode("user1"));
      user1.setEmail("sawpdawge@gmail.com");
      user1.setRoles(new HashSet<>(Arrays.asList(userRole)));
      user1.setEnabled(true);
      userRepository.save(user1);

      alreadySetup = true;
  }

  @Transactional
  Privilege createPrivilegeIfNotFound(String name) {

      Privilege privilege = privilegeRepository.findByName(name).orElse(null);
      if (privilege == null) {
          privilege = new Privilege();
          privilege.setName(name);
          privilegeRepository.save(privilege);
      }
      return privilege;
  }

  @Transactional
  Role createRoleIfNotFound( String name, Set<Privilege> privileges) {

      Role role = roleRepository.findByName(name).orElse(null);
      if (role == null) {
          role = new Role();
          role.setName(name);
          role.setPrivileges(privileges);
          roleRepository.save(role);
      }
      return role;
  }
}
