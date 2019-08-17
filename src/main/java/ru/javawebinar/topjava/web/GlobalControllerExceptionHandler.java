package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;

import static ru.javawebinar.topjava.util.ValidationUtil.getDataConflictMessage;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        ModelAndView mav = logAndPrepareError(req, e);
        mav.addObject("exception", rootCause);
        mav.addObject("message", rootCause.toString());
        return mav;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView dataConflictHandler(HttpServletRequest req, DataIntegrityViolationException e) {
        String message = getDataConflictMessage(messageSource, e);
        ModelAndView mav = logAndPrepareError(req, e);
        mav.addObject("message", message);
        return mav;
    }

    private static ModelAndView logAndPrepareError(HttpServletRequest req, Exception e) {
        log.error("Exception at request " + req.getRequestURL(), e);
        ModelAndView mav = new ModelAndView("exception/exception");
        // Interceptor is not invoked, put userTo
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            mav.addObject("userTo", authorizedUser.getUserTo());
        }
        return mav;
    }
}