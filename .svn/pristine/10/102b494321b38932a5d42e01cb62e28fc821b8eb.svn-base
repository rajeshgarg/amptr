package com.nyt.mpt.domain;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.AbstractStandardBasicType;
import org.hibernate.type.TypeResolver;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

/**
 * This class provides support for user defined enum types and is being copied from 
 * http://community.jboss.org/wiki/Java5EnumUserType
 * 
 * comments from original author
 * 
 * Look here for more info on design. 
 * http://community.jboss.org/wiki/Java5EnumUserType
 * modify to use AbstractStandardBasicType instead.
 * @author Chun ping Wang.
 *
 */
public class GenericEnumUserType implements UserType, ParameterizedType {

	private static final Logger LOGGER = Logger.getLogger(GenericEnumUserType.class);

	private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "name";
    private static final String DEFAULT_VALUE_OF_METHOD_NAME = "valueOf";
 
    @SuppressWarnings("rawtypes")
	private Class<? extends Enum> enumClass;
    private Class<?> identifierType;
    private Method identifierMethod;
    private Method valueOfMethod;
    private AbstractStandardBasicType<? extends Object> type;
    private int[] sqlTypes;
 
    @SuppressWarnings({ "unchecked"})
    public void setParameterValues(Properties parameters) {
    	final String enumClassName = parameters.getProperty("enumClass");
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException cfne) {
			LOGGER.error("Enum class not found: " + cfne);
            throw new HibernateException("Enum class not found", cfne);
        }
 
        final String methodName = parameters.getProperty("identifierMethod", DEFAULT_IDENTIFIER_METHOD_NAME);
        try {
            identifierMethod = enumClass.getMethod(methodName, new Class[0]);
            identifierType = identifierMethod.getReturnType();
        } catch (Exception e) {
			LOGGER.error("Failed to obtain identifier method: " + e);
            throw new HibernateException("Failed to obtain identifier method", e);
        }
        
        type = (AbstractSingleColumnStandardBasicType<? extends Object>) new TypeResolver().heuristicType(identifierType.getName(), parameters);
        if (type == null) {
               LOGGER.error("Unsupported identifier type");
               throw new HibernateException("Unsupported identifier type " + identifierType.getName());
        }
 
        sqlTypes = new int[] { ((AbstractSingleColumnStandardBasicType<?>)type).sqlType() };
        final String valueOfMethodName = parameters.getProperty("valueOfMethod", DEFAULT_VALUE_OF_METHOD_NAME);
        try {
            valueOfMethod = enumClass.getMethod(valueOfMethodName, new Class[] { identifierType });
        } catch (Exception e) {
			LOGGER.error("Failed to obtain valueOf method: " + e);
            throw new HibernateException("Failed to obtain valueOf method", e);
        }
    }
 
    @SuppressWarnings("rawtypes")
	public Class<? extends Enum> returnedClass() {
        return enumClass;
    }
 
    public Object nullSafeGet(final ResultSet resultSet, final String[] names, final Object owner) throws HibernateException, SQLException {  
    	final Object identifier = type.get(resultSet, names[0], null);
        if (resultSet.wasNull()) {
            return null;
        }
        
        try {
            return valueOfMethod.invoke(enumClass, new Object[] { identifier });
        } catch (Exception e) {
			LOGGER.error("Exception while invoking valueOf method: " + e);
            throw new HibernateException("Exception while invoking valueOf method '" + valueOfMethod.getName() + "' of " +
                    "enumeration class '" + enumClass + "'", e);
        }
    }
 
    public void nullSafeSet(final PreparedStatement statement, final Object value, final int index) throws HibernateException, SQLException {
        try {
            if (value == null) {
                statement.setNull(index, ((AbstractSingleColumnStandardBasicType<?>) type).sqlType());
            } else {
                final Object identifier = identifierMethod.invoke(value, new Object[0]);
                type.nullSafeSet(statement, identifier, index, null);
            }
        } catch (Exception e) {
			LOGGER.error("Exception while invoking identifierMethod: " + e);
			throw new HibernateException("Exception while invoking identifierMethod '" + identifierMethod.getName() 
					+ "' of " + "enumeration class '" + enumClass + "'", e);
        }
    }
 
    public int[] sqlTypes() {
        return sqlTypes;
    }
 
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }
 
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }
 
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }
 
    public boolean equals(final Object obj1, final Object obj2) throws HibernateException {
        return obj1 == obj2;
    }
 
    public int hashCode(final Object x) throws HibernateException {
        return x.hashCode();
    }
 
    public boolean isMutable() {
        return false;
    }
 
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}