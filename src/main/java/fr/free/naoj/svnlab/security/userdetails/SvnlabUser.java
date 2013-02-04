package fr.free.naoj.svnlab.security.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SvnlabUser extends User {

	private static final long serialVersionUID = -7573623845665822789L;
	
	private String username;
	private String password;
	
	public SvnlabUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, true, true, true, true, authorities);
		
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
