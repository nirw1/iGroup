package iob.aspects;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import iob.annotations.RolePermission;
import iob.boundaries.InstanceBoundary;
import iob.data.UserRole;
import iob.errors.NotFoundException;
import iob.errors.UnauthorizedAccessException;
import iob.logic.UsersServiceJpa;

@Component
@Aspect
public class PermissionsAspect {

	@Autowired
	private UsersServiceJpa userService;

	public static List<?> convertObjectToList(Object obj) {
		List<?> list = new ArrayList<>();
		if (obj instanceof Collection) {
			list = new ArrayList<>((Collection<?>) obj);
		}
		return list;
	}

	@Around("@annotation(iob.annotations.RolePermission)")
	public Object permissionProxy(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Method method = ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod();
		RolePermission ann = method.getAnnotation(RolePermission.class);
		UserRole[] accessRole = ann.roles();

		Object args[] = proceedingJoinPoint.getArgs();
		String domain = "", email = "";

		if (args.length >= 2) {
			domain = "" + args[0];
			email = "" + args[1];
		}

		UserRole userRole = this.userService.getUserRole(domain, email);
		if (Arrays.asList(accessRole).contains(userRole)) {
			try {
				Object retVal = proceedingJoinPoint.proceed();

				if (retVal instanceof Collection) {
					List<?> list = convertObjectToList(retVal);
					try {
						if (userRole == UserRole.PLAYER && list.get(0) instanceof InstanceBoundary) {
							retVal = list.stream().filter(instance -> ((InstanceBoundary) instance).getActive() == true)
									.collect(Collectors.toList());
						} else {

						}
					} catch (NullPointerException e) {
						return retVal;
					} catch (Exception e) {

					}
				} else {
					if (retVal instanceof InstanceBoundary) {
						if (userRole == UserRole.PLAYER && !((InstanceBoundary) retVal).getActive()) {
							new NotFoundException("Instance is inactive, user can't access it");
						}
					}
				}
				return retVal;
			} catch (Throwable e) {
				throw e;
			}
		} else {
			throw new UnauthorizedAccessException(domain + '/' + email + " does not have the appropriate permissions");
		}
	}
}
