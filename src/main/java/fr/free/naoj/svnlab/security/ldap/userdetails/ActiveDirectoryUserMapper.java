package fr.free.naoj.svnlab.security.ldap.userdetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

import fr.free.naoj.svnlab.security.UnknownAuthority;
import fr.free.naoj.svnlab.security.UserAuthority;

/**
 * <p></p>
 * 
 * @author Johann Bernez
 */
@Component(value="adMapper")
public class ActiveDirectoryUserMapper implements UserDetailsContextMapper, Serializable {

	private static final long serialVersionUID = -8729096603590872072L;

	private static final GrantedAuthority USER_AUTHORITY = new UserAuthority();
	private static final GrantedAuthority UNKNOWN_AUTHORITY = new UnknownAuthority();
	
	@Value("${ldap.userrole}")
	private String userRole;
	
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
		List<GrantedAuthority> mappedAuthorities = new ArrayList<GrantedAuthority>(1);
		
		for (GrantedAuthority auth : authorities) {
			if (auth.getAuthority().contains(userRole)) {
				mappedAuthorities.add(USER_AUTHORITY);
			}
		}
		
		if (mappedAuthorities.isEmpty()) {
			mappedAuthorities.add(UNKNOWN_AUTHORITY);
		}
		
		return new User(username, "", mappedAuthorities);
	}

	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}	
}
