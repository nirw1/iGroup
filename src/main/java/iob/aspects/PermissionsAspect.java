package iob.aspects;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import iob.annotations.RolePermission;
import iob.data.UserRole;
import iob.errors.UnauthorizedAccessException;
import iob.logic.UsersServiceJpa;

@Component
@Aspect
public class PermissionsAspect {

	@Autowired
	private UsersServiceJpa userService;

	@Around("@annotation(iob.annotations.RolePermission)")
	public void permissionProxy(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
		RolePermission ann = method.getAnnotation(RolePermission.class);
		UserRole[] accessRole = ann.roles();

		Object args[] = proceedingJoinPoint.getArgs();
		String domain = "" + args[0];
		String email = "" + args[1];
		UserRole userRole = this.userService.getUserRole(domain, email);
		if (Arrays.asList(accessRole).contains(userRole)) {
			try {
				proceedingJoinPoint.proceed();
			} catch (Throwable e) {
				throw e;
			}
		} else {
			throw new UnauthorizedAccessException(domain + '/' + email + " does not have the appropriate permissions");
		}
	}
}
