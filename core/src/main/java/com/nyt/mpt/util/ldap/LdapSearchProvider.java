/**
 * 
 */
package com.nyt.mpt.util.ldap;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;

import com.nyt.mpt.domain.User;

/**
 * This class is used to provide ldap search
 * 
 * @author surendra.singh
 * 
 */
public class LdapSearchProvider implements ILdapSearchProvider {

	private LdapTemplate ldapTemplate;

	private static final String SN = "sn";

	private static final String GIVEN_NAME = "givenName";

	private static final String CN = "cn";

	private static final String OBJECT_CLASS = "objectClass";

	private static final String PERSON = "Person";

	private static final String UID = "uid";

	private static final String MAIL = "mail";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<User> searchUserFromLdap(String userName) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter(OBJECT_CLASS, PERSON));
		filter.and(new WhitespaceWildcardsFilter(CN, userName));
		List result = ldapTemplate.search(DistinguishedName.EMPTY_PATH, filter.encode(), new UserAttributesMapper());
		Collections.sort(result, new LdapSearchResultSorter());
		return result;
	}

	private static class UserAttributesMapper implements AttributesMapper {
		public Object mapFromAttributes(Attributes attrs) throws NamingException {
			User user = new User();

			if (attrs.get(UID) != null) {
				user.setLoginName((String) attrs.get(UID).get());
			}
			if (attrs.get(GIVEN_NAME) != null) {
				user.setFirstName((String) attrs.get(GIVEN_NAME).get());
			}
			if (attrs.get(SN) != null) {
				user.setLastName((String) attrs.get(SN).get());
			}
			if (attrs.get(MAIL) != null) {
				user.setEmail((String) attrs.get(MAIL).get());
			}
			return user;
		}
	}

	private static final class LdapSearchResultSorter implements Comparator<User> {
		@Override
		public int compare(User u1, User u2) {
			return new CompareToBuilder().append(u1.getFirstName().toLowerCase(), u2.getFirstName().toLowerCase()).toComparison();
		}
	}

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
}
