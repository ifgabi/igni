package gg.igni.igniserver.account.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;

import gg.igni.igniserver.account.data.UserDto;
import gg.igni.igniserver.account.data.AccountConstraints.*;

public interface IAccountService {
  Optional<UserDto> createAccount(String username, String password, String email);
  boolean removeAccount(String username);
  Optional<Authentication> loginAccount(String username, String password);

  List<UsernameConstraintErrorFlag> checkUsernameConstraints(String username);
  List<PasswordConstraintErrorFlag> checkPasswordConstraints(String passwordLiteral);
  List<EmailConstraintErrorFlag> checkEmailConstraints(String email);
}
