package ru.ssk.restvoting.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.ssk.restvoting.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static ru.ssk.restvoting.web.exception.GlobalControllerExceptionHandler.*;

@ControllerAdvice
public class ViewControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ViewControllerExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetExceptionView(req, e, MSG_WRONG_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
        return logAndGetExceptionView(req, e, MSG_APP_ERROR);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest req, Exception e, String messageText) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        HttpStatus status = ERRORS.get(messageText);
        log.warn("Error at request  {}: {} (status: {})", req.getRequestURL(), rootCause.toString(), status.toString());
        ModelAndView mav = new ModelAndView("exception", Map.of(
                "exception", rootCause,
                "message", ValidationUtil.getMessage(rootCause),
                "typeMessage", messageText,
                "status", status));
        mav.setStatus(status);
        return mav;
    }
}
