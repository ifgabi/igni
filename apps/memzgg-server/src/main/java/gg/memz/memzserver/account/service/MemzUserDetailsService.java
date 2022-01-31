package gg.memz.memzserver.account.service;

import gg.memz.memzserver.account.model.User;
import gg.memz.memzserver.account.repositories.UserRepository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemzUserDetailsService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElse(null);
		if(user == null)
		{
			throw new UsernameNotFoundException("Username '" + username + "' cannot be resolved in IgniUserDetailsService.");
		}

		return user;
	}

}
