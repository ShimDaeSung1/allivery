//package org.zerock.allivery.config;
//
//import org.springframework.core.MethodParameter;
//import org.springframework.expression.Expression;
//import org.springframework.expression.spel.support.StandardEvaluationContext;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.support.WebDataBinderFactory;
//import org.springframework.web.context.request.NativeWebRequest;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.method.support.ModelAndViewContainer;
//import org.zerock.allivery.dto.user.LoginDTO;
//
//public class CustomArgumentResolver implements HandlerMethodArgumentResolver {
//
//    @Override
//    public boolean supportsParameter(MethodParameter methodParameter) {
//        Class<?> parameterType = methodParameter.getParameterType();
//        return LoginDTO.class.equals(parameterType);
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter methodParameter,
//                                  ModelAndViewContainer modelAndViewContainer,
//                                  NativeWebRequest nativeWebRequest,
//                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
//        Object principal = null;
//        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
//        if(authentication != null){
//            principal = authentication.getPrincipal();
//        }
//        if(principal == null || principal.getClass() == String.class){
//            return null;
//        }
//        AuthenticationPrincipal authPrincipal = findMethodAnnotation(
//                AuthenticationPrincipal.class, methodParameter
//        );
//
//        String expressionToPares = authPrincipal.expression();
//        if(StringUtils.hasLength(expressionToPares)){
//            StandardEvaluationContext context = new StandardEvaluationContext();
//            context.setRootObject(principal);
//            context.setVariable("this", principal);
//            context.setBeanResolver(beanResolver);
//
//            Expression expression =  this.parser.parseExpression(expressionToPares);
//            principal = expression.getValue(context);
//        }
//
//        if(principal != null && !methodParameter.getParameterType().isAssignableFrom(principal.getClass())){
//
//            if(authPrincipal.errorOnInvalidType()){
//                throw new ClassCastException(principal + " is not assignable to" +
//                        methodParameter.getParameterType());
//            }
//            else {
//                return null;
//            }
//        }
//        return principal;
//    }
//}
