package fr.cytech.mpf.config;

import fr.cytech.mpf.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthAspect {

    @Before("@annotation(fr.cytech.mpf.config.MustBeLogged)")
    public void mustBeLogged() throws Exception {
        // TODO: TEMPORARY DURING DEV
        if(1==1) return;

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession();
        User usr = (User) session.getAttribute("account");
        if(usr == null) throw new ForbiddenException("Access Denied");
    }
}
