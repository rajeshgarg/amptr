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
import org.springframework.ldap.filter.NotFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;

import com.nyt.mpt.domain.User;

/**
 * Provide user search feature for TAVANT LDAP
 *  
 * @author surendra.singh
 * 
 */
public class TavantLdapSearchProvider implements ILdapSearchProvider {

	private LdapTemplate ldapTemplate;

	private static final String DISABLED = "2";

	private static final String SAMACCOUNTNAME = "samaccountname";

	private static final String SN = "sn";

	private static final String GIVEN_NAME = "givenName";

	private static final String CN = "cn";

	private static final String USER_ACCOUNT_CONTROL = "userAccountControl:1.2.840.113556.1.4.803:";

	private static final String USER = "user";

	private static final String OBJECT_CLASS = "objectClass";

	private static final String PERSON = "Person";

	private static final String OBJECT_CATEGORY = "objectCategory";

	private static final String EMPTY_STRING = "";
	
	private static final String MAIL = "mail";

	@SuppressWarnings("unchecked")
	public List<User> searchUserFromLdap(String userName) {
		List<User> list = ldapTemplate.search(DistinguishedName.EMPTY_PATH, getFilterCriteria(userName).encode(), new UserAttributesMapper());
		Collections.sort(list, new LdapSearchResultSorter());
		return list;
	}

	private AndFilter getFilterCriteria(String userName) {
		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter(OBJECT_CATEGORY, PERSON));
		andFilter.and(new EqualsFilter(OBJECT_CLASS, USER));
		andFilter.and(new NotFilter(new EqualsFilter(USER_ACCOUNT_CONTROL, DISABLED)));
		andFilter.and(new WhitespaceWildcardsFilter(CN, userName));
		return andFilter;
	}

	private static final class UserAttributesMapper implements AttributesMapper {

		public Object mapFromAttributes(Attributes attrs) throws NamingException {
			User user = new User();
			user.setFirstName(attrs.get(GIVEN_NAME) != null ? (String) attrs.get(GIVEN_NAME).get() : EMPTY_STRING);
			user.setLastName(attrs.get(SN) != null ? (String) attrs.get(SN).get() : EMPTY_STRING);
			user.setEmail(attrs.get(MAIL) != null ? (String) attrs.get(MAIL).get() : EMPTY_STRING);
			user.setLoginName(attrs.get(SAMACCOUNTNAME) != null ? (String) attrs.get(SAMACCOUNTNAME).get() : EMPTY_STRING);
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
