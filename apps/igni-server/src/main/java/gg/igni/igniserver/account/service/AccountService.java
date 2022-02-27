package gg.igni.igniserver.account.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.sanctionco.jmail.JMail;

import org.modelmapper.ModelMapper;
import org.passay.CharacterCharacteristicsRule;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gg.igni.igniserver.account.data.AccountConstraints;
import gg.igni.igniserver.account.data.AccountConstraints.PasswordConstraintErrorFlag;
import gg.igni.igniserver.account.data.AccountConstraints.UsernameConstraintErrorFlag;
import gg.igni.igniserver.account.data.UserDto;
import gg.igni.igniserver.account.repositories.PrivilegeRepository;
import gg.igni.igniserver.account.repositories.RoleRepository;
import gg.igni.igniserver.account.repositories.UserRepository;
import gg.igni.igniserver.model.Privilege;
import gg.igni.igniserver.model.Role;
import gg.igni.igniserver.model.User;

@Service
public class AccountService implements IAccountService {

  @Autowired
	IgniAuthProvider igniAuthProvider;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PrivilegeRepository privilegeRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Optional<UserDto> createAccount(String username, String password, String email) {
    User toCreate = new User();
    toCreate.setUsername(username);
    toCreate.setPasswordHash(passwordEncoder.encode(password));
    toCreate.setEmail(email);
    toCreate.setEnabled(true);
    toCreate.setRoles(new HashSet<>(Arrays.asList(getcreateRole("ROLE_USER", new HashSet<>(Arrays.asList(getcreatePrivilege("PRIVILEGE_CHAT_USAGE")))))));
    userRepository.save(toCreate);

    ModelMapper mm = new ModelMapper();
    UserDto userDto = mm.map(toCreate, UserDto.class);

    return Optional.of(userDto);
  }

  @Override
  public boolean removeAccount(String username) {
    Long id = userRepository.deleteByUsername(username);
    if(id > -1)
      return true;

    return false;
  }

  @Override
  public Optional<Authentication> loginAccount(String username, String password) {
    UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(username, password);
		Authentication newauth = igniAuthProvider.authenticate(authReq);

    return Optional.of(newauth);
  }

  @Override
  public List<UsernameConstraintErrorFlag> checkUsernameConstraints(String username) {
      List<UsernameConstraintErrorFlag> errs = new ArrayList<UsernameConstraintErrorFlag>();

      if(username == null)
      {
          errs.add(UsernameConstraintErrorFlag.ERROR);
          return errs;
      }

      if(username.length() > AccountConstraints.MAX_SIZE_USERNAME)
          errs.add(UsernameConstraintErrorFlag.TOOLONG);

      if(username.length() < AccountConstraints.MIN_SIZE_USERNAME)
          errs.add(UsernameConstraintErrorFlag.TOOSHORT);

      if(username.trim().length() != username.length())
          errs.add(UsernameConstraintErrorFlag.INVALID_CHARACTERS);

      for(int i = 0; i < username.length(); i++)
      {
          if(!(Character.isAlphabetic(username.charAt(i)) || Character.isDigit(username.charAt(i))))
              errs.add(UsernameConstraintErrorFlag.INVALID_CHARACTERS);
      }

      User exists = userRepository.findByUsernameIgnoreCase(username).orElse(null);
      if(exists != null)
          errs.add(UsernameConstraintErrorFlag.ALREADYEXISTS);

      return errs;
  }

  @Override
  public List<PasswordConstraintErrorFlag> checkPasswordConstraints(String passwordLiteral) {

      List<AccountConstraints.PasswordConstraintErrorFlag> errs = new ArrayList<AccountConstraints.PasswordConstraintErrorFlag>();

      if(passwordLiteral == null)
      {
          errs.add(PasswordConstraintErrorFlag.ERROR);
          return errs;
      }

      if(passwordLiteral.trim().length() < passwordLiteral.length())
          errs.add(PasswordConstraintErrorFlag.CONTAINS_SPACES);

      LengthRule lcr = new LengthRule(AccountConstraints.MIN_SIZE_PASSWORD, AccountConstraints.MAX_SIZE_PASSWORD);
      CharacterCharacteristicsRule charrule = new CharacterCharacteristicsRule();
      charrule.setRules(List.of(
          new CharacterRule(EnglishCharacterData.LowerCase, 5),
          new CharacterRule(EnglishCharacterData.UpperCase, 5),
          new CharacterRule(EnglishCharacterData.Digit),
          new CharacterRule(EnglishCharacterData.Special)));
      PasswordValidator validator = new PasswordValidator(lcr, charrule);
      PasswordData pwdata = new PasswordData(passwordLiteral);
      RuleResult result = validator.validate(pwdata);

      if(result.getDetails() != null)
          if(result.getDetails().size() > 0)
              errs.add(PasswordConstraintErrorFlag.INVALID);

      return errs;
  }

  public List<AccountConstraints.EmailConstraintErrorFlag> checkEmailConstraints(String email)
  {
      List<AccountConstraints.EmailConstraintErrorFlag> errs = new ArrayList<AccountConstraints.EmailConstraintErrorFlag>();

      if(email == null)
      {
          errs.add(AccountConstraints.EmailConstraintErrorFlag.ERROR);
          return errs;
      }

      if(!JMail.isValid(email))
          errs.add(AccountConstraints.EmailConstraintErrorFlag.INVALID);

      User exists = userRepository.findByEmailIgnoreCase(email).orElse(null);
      if(exists != null)
          errs.add(AccountConstraints.EmailConstraintErrorFlag.ALREADYEXISTS);

      return errs;
  }

  private Privilege getcreatePrivilege(String name) {

    Privilege privilege = privilegeRepository.findByName(name).orElse(null);
    if (privilege == null) {
        privilege = new Privilege();
        privilege.setName(name);
        privilegeRepository.save(privilege);
    }
    return privilege;
  }

  private Role getcreateRole( String name, Set<Privilege> privileges) {

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
