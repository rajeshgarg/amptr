/**
 * 
 */
package com.nyt.mpt.util.intercepter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import javax.persistence.Id;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import com.nyt.mpt.service.IValidatorService;
import com.nyt.mpt.util.enums.ErrorCodes;
import com.nyt.mpt.util.enums.ErrorMessageType;
import com.nyt.mpt.util.exception.IntegrityValidatorException;
import com.nyt.mpt.validator.Validator;

/**
 * This Class define the Integrity Validator Intercepter
 * @author surendra.singh
 *
 */
@Aspect
public class IntegrityValidatorInterceptor extends AbstractValidatorInterceptor {


	private static final Logger LOGGER = Logger.getLogger(IntegrityValidatorInterceptor.class);

	private IValidatorService validatorService;

	private Map<String, Validator> validators;

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Before("execution(@com.nyt.mpt.util.annotation.Validate * delete*(..))")
	public void checkDelete(final JoinPoint joinPoint) {
		Assert.isTrue(joinPoint.getArgs().length > 0);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Intercepting the delete request in Integrity Validator Interceptor");
		}
		final Object entity = joinPoint.getArgs()[0];
		final Validator validator = validators.get(entity.getClass().getName());
		if (validator != null) {
			final long entityId = getEntityId(entity);
			LOGGER.info("Validating association for entity: " + entity.getClass().getName() + " having id: " + entityId);
			final boolean validated = validatorService.validateAssociation(entityId, validator.getAssociationValidator());
			if (!validated) {
				throw new IntegrityValidatorException(getCustomeBusinessError(ErrorCodes.ObjectAssociationExistCanNotDelete, ErrorMessageType.ERROR));
			}
		}
	}

	@Order(Ordered.HIGHEST_PRECEDENCE)
	@Before("execution(@com.nyt.mpt.util.annotation.Validate * update*(..))")
	public void checkUpdate(final JoinPoint joinPoint) {
		Assert.isTrue(joinPoint.getArgs().length > 1);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Intercepting the update request in Integrity Validator Interceptor");
		}
		final Object entity = joinPoint.getArgs()[0];
		final boolean forceUpdate = (Boolean) joinPoint.getArgs()[1];
		final Validator validator = validators.get(entity.getClass().getName());
		if (!forceUpdate && validator != null) {
			final long entityId = getEntityId(entity);
			final boolean validated = validatorService.validateAssociation(entityId, validator.getAssociationValidator());
			LOGGER.info("Validating association for entity: " + entity.getClass().getName() + " having id: " + entityId);
			if (!validated) {
				throw new IntegrityValidatorException(getCustomeBusinessError(ErrorCodes.ObjectAssociationExistNeedConfirmation,
						ErrorMessageType.CONFIRM));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static long getEntityId(final Object object) {
		final Class clss = object.getClass();
		final Method[] methods = clss.getMethods();
		for (Method method : methods) {
			Annotation annot = method.getAnnotation(Id.class);
			if (annot != null) {
				try {
					return (Long) method.invoke(object);
				} catch (Exception e) {
					LOGGER.error("Exception has occurred: ", e);
					throw new RuntimeException(e);
				}
			}
		}
		throw new RuntimeException("Entity unique id not found - " + object);
	}

	public void setValidators(final Map<String, Validator> validators) {
		this.validators = validators;
	}

	public void setValidatorService(final IValidatorService validatorService) {
		this.validatorService = validatorService;
	}
}
