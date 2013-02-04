package fr.free.naoj.svnlab.service.impl;

import static fr.free.naoj.svnlab.utils.StringUtils.EMPTY_STRING;

import java.lang.ref.WeakReference;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import fr.free.naoj.svnlab.security.userdetails.SvnlabUser;
import fr.free.naoj.svnlab.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private WeakReference<User> userRef;
	
	public User getUser() {
		User user = null;
		
		if (userRef == null || (user=userRef.get()) == null) {
			try {
				if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SvnlabUser) {
					user = (SvnlabUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				} else {
					user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				}
				userRef = new WeakReference<User>(user);
			} catch (Exception e) {
				user = new SvnlabUser(EMPTY_STRING, EMPTY_STRING, Collections.<GrantedAuthority> emptyList());
			}
		}
		
		return user;
	}
}
