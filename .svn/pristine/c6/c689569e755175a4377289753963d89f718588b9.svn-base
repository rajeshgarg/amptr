/**
 * 
 */
package com.nyt.mpt.util.intercepter;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StopWatch;

import com.nyt.mpt.domain.User;
import com.nyt.mpt.util.ConstantStrings;
import com.nyt.mpt.util.DateUtil;

/**
 * Simple AOP Alliance MethodInterceptor for performance monitoring.
 * This interceptor has no effect on the intercepted method call.
 *
 * Uses a StopWatch for the actual performance measuring.
 * 
 * @author amandeep.singh
 *
 */
public class AMPTPerformanceMonitorInterceptor implements MethodInterceptor {

	private static final Logger LOGGER = Logger.getLogger("AMPTHealthCheckLogger");
	
	/**
	 * Create a new AMPTPerformanceMonitorInterceptor with a static logger.
	 */
	public AMPTPerformanceMonitorInterceptor() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Class clazz = method.getDeclaringClass();
		StringBuilder name = new StringBuilder(150);
		name.append(clazz.getName()+ "." + method.getName());
		StopWatch stopWatch = new StopWatch(name.toString());
		long startTime = System.currentTimeMillis();
		stopWatch.start(name.toString());
		try {
			return invocation.proceed();
		}
		finally {
			long endTime = System.currentTimeMillis();
			stopWatch.stop();
			StringBuilder sb = new StringBuilder(150);
			sb.append(DateUtil.getGuiDateTimeString(startTime)).append(ConstantStrings.COMMA);
			sb.append(clazz.getSimpleName()).append(ConstantStrings.COMMA);
			sb.append(method.getName()).append(ConstantStrings.COMMA);
			sb.append(convertToSeconds(stopWatch.getTotalTimeMillis())).append(ConstantStrings.COMMA);
			sb.append(DateUtil.getGuiTimeString(startTime)).append(ConstantStrings.COMMA);
			sb.append(DateUtil.getGuiTimeString(endTime)).append(ConstantStrings.COMMA);
			sb.append(getUser().getFullName());
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug(sb.toString());
			}
		}
	}
		
	/**
	 * @param totalTimeMillis
	 * @return
	 */
	private String convertToSeconds(long totalTimeMillis) {
		long seconds = 0;
		long milliSeconds = 0;
		if(totalTimeMillis > 0){ 
			seconds = (totalTimeMillis/1000);
			milliSeconds = (totalTimeMillis%1000);
		}
		return seconds + " sec : " + milliSeconds + " ms";
	}

	/**
	 * Method returns the user associated with current session
	 * @return
	 */
	private User getUser() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			User user = new User();
			user.setFirstName("System");
			user.setLastName(ConstantStrings.EMPTY_STRING);
			return user;
		} else {
			Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (object.getClass().getName().equals(String.class.getName())) {
				User user = new User();
				user.setFirstName("sos");
				user.setLastName(ConstantStrings.EMPTY_STRING);
				return user;
			} else {
				return (User) object;
			}
		}
	}
}
