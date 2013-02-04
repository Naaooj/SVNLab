package fr.free.naoj.svnlab.security.ldap.authentication.ad;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Component;

import fr.free.naoj.svnlab.security.userdetails.SvnlabUser;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
@Component
public class ActiveDirectoryAuthenticationProvider implements AuthenticationProvider {

	private LdapAuthenticator authenticator;
	
	@Autowired
	@Qualifier("adMapper")
	private UserDetailsContextMapper contextMapper;
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		DirContextOperations context = doAuthentication(authentication);		
		
		Collection<? extends GrantedAuthority> authorities = loadAuthorities(context, authentication.getName());
		
		UserDetails temporaryDetails = contextMapper.mapUserFromContext(context, authentication.getName(), authorities);

		Collection<? extends GrantedAuthority> mappedAuthorities = temporaryDetails.getAuthorities();
		
		SvnlabUser user = new SvnlabUser(authentication.getName(), (String) authentication.getCredentials(), mappedAuthorities);
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), user.getAuthorities());
		token.setDetails(authentication.getDetails());
		
		return token;
	}
	
	private DirContextOperations doAuthentication(Authentication authentication) throws AuthenticationException {
		try {
			return authenticator.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw e;
		} catch (Exception e) {
			throw new AuthenticationServiceException("Unable to authenticate", e);
		}
	}
	
	protected Collection<? extends GrantedAuthority> loadAuthorities(DirContextOperations userData, String username) {
        String[] groups = userData.getStringAttributes("memberOf");

        if (groups == null) {
            return Collections.<GrantedAuthority> emptyList();
        }

        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(groups.length);

        for (String group : groups) {
            authorities.add(new SimpleGrantedAuthority(new DistinguishedName(group).removeLast().getValue()));
        }

        return authorities;
    }

	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public void setAuthenticator(LdapAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	public void setContextMapper(UserDetailsContextMapper contextMapper) {
		this.contextMapper = contextMapper;
	}
}
