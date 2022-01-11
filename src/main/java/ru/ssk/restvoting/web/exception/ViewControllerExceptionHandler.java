package ru.ssk.restvoting.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.ssk.restvoting.util.ValidationUtil;
import ru.ssk.restvoting.util.exception.ErrorType;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class ViewControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ViewControllerExceptionHandler.class);

    private final MessageSourceAccessor messageSourceAccessor;

    public ViewControllerExceptionHandler(@Autowired MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetExceptionView(req, e, ErrorType.WRONG_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
        return logAndGetExceptionView(req, e, ErrorType.APP_ERROR);
    }


    private ModelAndView logAndGetExceptionView(HttpServletRequest req, Exception e, ErrorType errorType) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        ModelAndView mav = new ModelAndView("exception", Map.of(
                "exception", rootCause,
                "message", ValidationUtil.getMessage(rootCause),
                "typeMessage", messageSourceAccessor.getMessage(errorType.getErrorCode()),
                "status", errorType.getStatus()));
        mav.setStatus(errorType.getStatus());
        return mav;
    }
}
