package fr.free.naoj.svnlab.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public class UserAuthority implements GrantedAuthority {

	private static final long serialVersionUID = -7759991912532160393L;

	public String getAuthority() {
		return "ROLE_USER";
	}
}
