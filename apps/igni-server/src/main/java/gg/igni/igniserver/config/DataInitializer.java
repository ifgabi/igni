package gg.igni.igniserver.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import gg.igni.igniserver.account.repositories.PrivilegeRepository;
import gg.igni.igniserver.account.repositories.RoleRepository;
import gg.igni.igniserver.account.repositories.UserRepository;
import gg.igni.igniserver.model.Embed;
import gg.igni.igniserver.model.EmbedSite;
import gg.igni.igniserver.model.Privilege;
import gg.igni.igniserver.model.Role;
import gg.igni.igniserver.model.User;
import gg.igni.igniserver.watch.repository.EmbedRepository;
import gg.igni.igniserver.watch.repository.EmbedSiteRepository;

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
  private EmbedRepository embedRepository;

  @Autowired
  private EmbedSiteRepository embedSiteRepository;


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
      Role userRole = roleRepository.findByName("ROLE_USER").orElse(null);

      User admin = new User();
      admin.setUsername("admin");
      admin.setPasswordHash(passwordEncoder.encode("admin"));
      admin.setEmail("aresgab@live.ca");
      admin.setRoles(new HashSet<>(Arrays.asList(adminRole, userRole)));
      admin.setEnabled(true);
      userRepository.save(admin);

      User user1 = new User();
      user1.setUsername("user1");
      user1.setPasswordHash(passwordEncoder.encode("user1"));
      user1.setEmail("user1@gmail.com");
      user1.setRoles(new HashSet<>(Arrays.asList(userRole)));
      user1.setEnabled(true);
      userRepository.save(user1);

      EmbedSite esYoutube = new EmbedSite();
      esYoutube.setName("YouTube");
      esYoutube.setCode("YOUTUBE_TOKEN");
      esYoutube.setEmbeds(new HashSet<Embed>());

      EmbedSite esTwitch = new EmbedSite();
      esTwitch.setName("TwitchTV");
      esTwitch.setCode("TWITCH_TOKEN");
      esTwitch.setEmbeds(new HashSet<Embed>());

      Embed ytEmbed = new Embed();
      esYoutube.getEmbeds().add(ytEmbed);
      ytEmbed.setToken("GlV5sXdXPu4");
      ytEmbed.setMemo("This is a youtube embed!");
      for(int i = 0; i < 120; i++)
      {
        Embed emb = new Embed();
        emb.setEmbedSite(esYoutube);
        emb.setToken("dummy_data" + i);
        emb.setMemo("dummy_data" + i);
        esYoutube.getEmbeds().add(emb);
      }

      Embed ttvEmbed = new Embed();
      esTwitch.getEmbeds().add(ttvEmbed);
      ttvEmbed.setToken("gypsy93");
      ttvEmbed.setMemo("This is a TTV embed!");

      embedSiteRepository.save(esYoutube);
      embedSiteRepository.save(esTwitch);

      ytEmbed.setEmbedSite(esYoutube);
      embedRepository.save(ytEmbed);

      ttvEmbed.setEmbedSite(esTwitch);
      embedRepository.save(ttvEmbed);

      embedRepository.saveAll(esYoutube.getEmbeds());

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
