package com.springaopmongo.auditing.aspects;

import com.springaopmongo.auditing.audit.AuditJournal;
import com.springaopmongo.auditing.audit.AuditJournalRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Aspect
public class AuditAspect {

    private final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @Autowired
    private HttpServletRequest httpRequest;

    @Autowired
    private AuditJournalRepository auditJournalRepository;


    /**
     * Defined pointcut for auditing...
     */
    @Pointcut(value = "within(com.springaopmongo.auditing.controllers.*)")
    public void auditPointcut() {
        // no implementation...
    }

    /**
     * Trigger after successful execution of called method
     *
     * @param joinPoint given point (data about called method)
     */
    @AfterReturning(pointcut = "auditPointcut()")
    public void afterSuccessExecution(JoinPoint joinPoint)
    {
        try
        {
            String auditor = SecurityContextHolder.getContext().getAuthentication().getName();
            String url = getUrlFromRequestMapping(joinPoint);
            // "url" variable can be used to find, for example, API description based on given url and
            // insert that in audit journal

            AuditJournal auditJournal = new AuditJournal();
            auditJournal.setAuditor(auditor);
            auditJournal.setMethod(httpRequest.getMethod());
            auditJournal.setRoute(httpRequest.getRequestURI());
            auditJournal.setSignatureName(joinPoint.getSignature().getName());
            auditJournal.setSignatureDeclaringType(joinPoint.getSignature().getDeclaringTypeName());
            auditJournal.setActionType(targetActionType(httpRequest));
            auditJournal.setTimestamp(new Date());

            Map<String, Object> requestData = new HashMap<>();
            requestData.put("args", Arrays.toString(joinPoint.getArgs()));
            requestData.put("remoteAddress", httpRequest.getRemoteAddr());
            auditJournal.setRequestData(requestData);

            auditJournalRepository.save(auditJournal);
        }
        catch (Exception e) {
            logger.error("Audit error has been occurred. Error message: {}", e.getMessage());
        }
    }

    /**
     * Getting target URL from request mapping annotations
     *
     * @param joinPoint target join point
     * @return String value of url
     * @throws NoSuchMethodException no such class method
     */
    private String getUrlFromRequestMapping(JoinPoint joinPoint) throws NoSuchMethodException
    {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Class<?>[] parameterTypes = signature.getMethod().getParameterTypes();
        Annotation[] annotations = joinPoint.getTarget().getClass()
            .getMethod(methodName, parameterTypes)
            .getDeclaredAnnotations();

        String baseApi = getBaseApiContext(joinPoint);

        if (annotations.length > 0)
        {
            for (Annotation annotation : annotations)
            {
                String value = fetchValueFromAnnotationType(annotation, joinPoint, methodName, parameterTypes);
                String fullApiUrl = baseApi.concat(value);
                if (!fullApiUrl.trim().contentEquals("")) {
                    return fullApiUrl;
                }
            }
        }
        throw new NoSuchMethodException("No method detected.");
    }

    /**
     * Getting base API context
     *
     * @param joinPoint target join point
     * @return String value of context
     */
    private String getBaseApiContext(JoinPoint joinPoint)
    {
        Annotation[] annotations = joinPoint.getTarget().getClass().getAnnotations();
        if (annotations != null && annotations.length > 0)
        {
            for (Annotation annotation : annotations)
            {
                if (annotation.annotationType().isAssignableFrom(RequestMapping.class))
                {
                    String[] values = joinPoint.getTarget().getClass().getAnnotation(RequestMapping.class).value();
                    return values[0];
                }
            }
        }
        return "";
    }

    /**
     * Fetching value from target annotation type
     *
     * @param annotation given annotation interface
     * @param joinPoint target join point
     * @param methodName given class method
     * @param parameterTypes given method parameters
     * @return String value of annotation params
     * @throws NoSuchMethodException no such class method exception
     */
    private String fetchValueFromAnnotationType(Annotation annotation, JoinPoint joinPoint,
                                                String methodName, Class<?>[] parameterTypes)
            throws NoSuchMethodException
    {
        String[] values = null;
        if (annotation.annotationType().isAssignableFrom(RequestMapping.class))
        {
            values = joinPoint.getTarget().getClass()
                    .getMethod(methodName, parameterTypes)
                    .getAnnotation(RequestMapping.class).value();
        }

        if (annotation.annotationType().isAssignableFrom(GetMapping.class))
        {
            values = joinPoint.getTarget().getClass()
                        .getMethod(methodName, parameterTypes)
                        .getAnnotation(GetMapping.class).value();
        }

        if (annotation.annotationType().isAssignableFrom(PutMapping.class))
        {
            values = joinPoint.getTarget().getClass()
                        .getMethod(methodName, parameterTypes)
                        .getAnnotation(PutMapping.class).value();
        }

        if (annotation.annotationType().isAssignableFrom(PostMapping.class))
        {
            values = joinPoint.getTarget().getClass()
                        .getMethod(methodName, parameterTypes)
                        .getAnnotation(PostMapping.class).value();
        }

        if (annotation.annotationType().isAssignableFrom(DeleteMapping.class))
        {
            values = joinPoint.getTarget().getClass()
                        .getMethod(methodName, parameterTypes)
                        .getAnnotation(DeleteMapping.class).value();
        }
        return parseValues(values);
    }

    /**
     * Getting value of annotation
     *
     * @param values target value array
     * @return String value of url
     */
    private String parseValues(String[] values) {
        return values != null && values.length > 0 ? values[0] : "";
    }

    /**
     * Method for targeting action type depending on Http Request Method
     *
     * @param httpRequest given Http Request
     * @return string value of action type
     */
    private String targetActionType(HttpServletRequest httpRequest)
    {
        switch (httpRequest.getMethod())
        {
            case "GET":
                return "VIEW";

            case "POST":
                return "INSERT";

            case "PUT":
                return "UPDATE";

            case "DELETE":
                return "DEACTIVATION";

            default:
                return "UNKNOWN";
        }
    }
}
