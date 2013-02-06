# SVNLab
=======

Application aiming at browsing a SVN repository and linking additional 
informations to commits. It includes a search engine based on Lucene 
in order to retrieve tasks as fast as possible.

## Configuration

*ldap.properties* contains properties to authenticate users with an active 
directory. If you don't want to use an AD, modify *spring-security.xml* with
such values :   
```xml
<s:authentication-manager>
	<!-- <s:authentication-provider ref="ldapAuthProvider" /> -->
	<s:authentication-provider>
		<s:user-service>
			<s:user name="anyName" password="anyPassword" authorities="ROLE_USER " />
		</s:user-service>
	</s:authentication-provider>
</s:authentication-manager>
```

*svn.properties* contains the url of the repository.