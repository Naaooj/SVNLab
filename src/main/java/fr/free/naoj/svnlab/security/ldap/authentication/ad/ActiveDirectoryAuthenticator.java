package fr.free.naoj.svnlab.security.ldap.authentication.ad;

import static fr.free.naoj.svnlab.utils.StringUtils.EMPTY_STRING;

import java.util.Arrays;
import java.util.Iterator;

import javax.naming.directory.SearchControls;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.ldap.CommunicationException;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.security.ldap.authentication.LdapAuthenticator;

import fr.free.naoj.svnlab.utils.StringUtils;

/**
 * <p></p>
 *
 * @author Johann Bernez
 */
public class ActiveDirectoryAuthenticator implements LdapAuthenticator {

	private String domain;
	private DefaultSpringSecurityContextSource contextSource;
	
	public DirContextOperations authenticate(Authentication authentication) {
		String principal = authentication.getName();
		String password = EMPTY_STRING;
		
		if (authentication.getCredentials() != null) {
			password = authentication.getCredentials().toString();
		}
	
		if (!EMPTY_STRING.equals(principal) && !EMPTY_STRING.equals(password)) {
			try {
				final String adPrincipal = getPrincipalForActiveDirectory(principal);
				InitialLdapContext ldapContext = (InitialLdapContext) contextSource.getContext(adPrincipal, password);
				
				SearchControls searchCtls = new SearchControls();
		        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		        String searchFilter = "(&(objectClass=user)(userPrincipalName={0}))";
		        String searchRoot = getRoot();

		        return SpringSecurityLdapTemplate.searchForSingleEntryInternal(ldapContext, searchCtls, searchRoot, searchFilter, new Object[]{adPrincipal});
			} catch (CommunicationException e) {
				throw new AuthenticationServiceException("Connection timed out", e);
			} catch (IncorrectResultSizeDataAccessException e) {
				if (e.getActualSize() == 0) {
					UsernameNotFoundException userNameNotFoundException = new UsernameNotFoundException("User " + principal + " not found in directory.");
	                userNameNotFoundException.initCause(e);
	                throw new BadCredentialsException("Bad credentials", userNameNotFoundException);
				}
				throw e;
			} catch (Exception e) {
				throw new BadCredentialsException("Wrong login or password");
			}
		} else {
			throw new BadCredentialsException("Empty login or password");
		}
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public DefaultSpringSecurityContextSource getContextSource() {
		return contextSource;
	}

	public void setContextSource(DefaultSpringSecurityContextSource contextSource) {
		this.contextSource = contextSource;
	}
	
	private String getPrincipalForActiveDirectory(String principal) {
		if (domain == null) {
			return principal;
		}
		return principal + "@" + domain;
	}
	
	private String getRoot() {
		String root = StringUtils.EMPTY_STRING;
		
		if (domain != null) {
			StringBuffer sb = new StringBuffer();
			String dcKey = "dc=";
			String comma = ",";
			
			String[] dcs = org.springframework.util.StringUtils.tokenizeToStringArray(domain, ".");
			Iterator<String> it = Arrays.asList(dcs).iterator();
			while (it.hasNext()) {
				sb.append(dcKey).append(it.next());
				
				if (it.hasNext()) {
					sb.append(comma);
				}
			}
			root = sb.toString();
		}
		
		return root;
	}
}
