package gg.igni.igniserver.account.service;

import gg.igni.igniserver.account.model.User;
import gg.igni.igniserver.account.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class IgniUserDetailsService implements UserDetailsService {

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
