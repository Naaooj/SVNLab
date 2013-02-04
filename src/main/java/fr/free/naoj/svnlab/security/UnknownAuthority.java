package fr.free.naoj.svnlab.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public class UnknownAuthority implements GrantedAuthority {
	private static final long serialVersionUID = 4448321395100194707L;

	public String getAuthority() {
		return "ROLE_UNKNOWN";
	}
}
