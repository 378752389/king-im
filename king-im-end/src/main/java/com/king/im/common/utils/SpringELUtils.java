package com.king.im.common.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;

public class SpringELUtils {

    private static final ExpressionParser parser = new SpelExpressionParser();

    private static final DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    public static String parseSpringEL(Method method, Object[] args, String el) {
        String[] paramNames = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});
        EvaluationContext context = new StandardEvaluationContext();
        for (int i=0; i< paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        Expression expression = parser.parseExpression(el);
        return expression.getValue(context, String.class);
    }
}
