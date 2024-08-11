package com.king.im.common.log;


import com.king.im.common.interceptor.RequestInfoHolder;
import com.king.im.common.interceptor.UserInfo;
import com.king.im.common.log.mapper.LogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LogAspect {

    @Resource
    private LogMapper logMapper;

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    @Around("@annotation(com.king.im.common.log.SysLog)")
    public Object doLogProcess(ProceedingJoinPoint pjp) throws Throwable {
        Object result = pjp.proceed();
        UserInfo userInfo = RequestInfoHolder.getUserInfo();

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = method.getAnnotation(SysLog.class);
        String description = sysLog.description();

        Object[] args = pjp.getArgs();
        String[] argNames = discoverer.getParameterNames(method);
        String logStr = description;

        StandardEvaluationContext context = new StandardEvaluationContext();
        // 处理参数解析
        if (argNames != null) {
            for (int i = 0; i < argNames.length; i++) {
                context.setVariable(argNames[i], args[i]);
            }
        }
        // 处理返回值解析
        if (result != null) {
            context.setVariable("ret", result);
        }

        Expression expression = parser.parseExpression(description);
        logStr = expression.getValue(context, String.class);

        Log log = new Log();
        log.setOperator(userInfo.getUsername());
        log.setLog(logStr);
        log.setCreateTime(new Date());
        log.setIp("");

        logMapper.insert(log);
        return result;
    }
}
