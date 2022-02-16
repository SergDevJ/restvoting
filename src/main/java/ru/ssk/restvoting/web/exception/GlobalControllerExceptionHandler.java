package ru.ssk.restvoting.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.ssk.restvoting.util.ValidationUtil;
import ru.ssk.restvoting.util.exception.*;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class GlobalControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
    private final MessageSourceAccessor messageSourceAccessor;

    public static final String EXCEPTION_DUPLICATE_USER_NAME = "exception.user.duplicateName";
    public static final String EXCEPTION_DUPLICATE_USER_EMAIL = "exception.user.duplicateEmail";
    public static final String EXCEPTION_DUPLICATE_DISH_NAME_WEIGHT = "exception.dish.duplicateNameWeight";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT_NAME = "exception.restaurant.duplicateName";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT_ADDRESS = "exception.restaurant.duplicateAddress";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT_EMAIL = "exception.restaurant.duplicateEmail";
    public static final String EXCEPTION_FKEY_MENU_DISH = "exception.menu.fkeyMenuDish";
    public static final String EXCEPTION_DUPLICATE_MENU_DISH = "exception.menu.duplicateDish";
    public static final String EXCEPTION_DATA_INTEGRITY_VIOLATION = "exception.dataIntegrityViolation";

    private static final Map<String, String> CONSTRAINTS_MSG = Map.of(
            "users_unique_name_idx", EXCEPTION_DUPLICATE_USER_NAME,
            "users_unique_email_idx", EXCEPTION_DUPLICATE_USER_EMAIL,
            "dishes_unique_name_weight_idx", EXCEPTION_DUPLICATE_DISH_NAME_WEIGHT,
            "restaurants_unique_name_idx", EXCEPTION_DUPLICATE_RESTAURANT_NAME,
            "restaurants_address_idx", EXCEPTION_DUPLICATE_RESTAURANT_ADDRESS,
            "restaurants_email_idx", EXCEPTION_DUPLICATE_RESTAURANT_EMAIL,
            "menu_dish_id_fkey", EXCEPTION_FKEY_MENU_DISH,
            "menu_unique_rest_date_dish_idx", EXCEPTION_DUPLICATE_MENU_DISH);

    @Autowired
    public GlobalControllerExceptionHandler(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<ErrorInfo> dataNotFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, ErrorType.DATA_NOT_FOUND);
    }

    @ExceptionHandler(TooLateVoteException.class)
    ResponseEntity<ErrorInfo> tooLateVote(HttpServletRequest req, TooLateVoteException e) {
        return logAndGetErrorInfo(req, e, ErrorType.VALIDATION_ERROR);
    }

    @ExceptionHandler(UserDeleteViolationException.class)
    ResponseEntity<ErrorInfo> userDeleteViolation(HttpServletRequest req, UserDeleteViolationException e) {
        return logAndGetErrorInfo(req, e, ErrorType.WRONG_REQUEST);
    }

    @ExceptionHandler(UserUpdateViolationException.class)
    ResponseEntity<ErrorInfo> userUpdateViolation(HttpServletRequest req, UserUpdateViolationException e) {
        return logAndGetErrorInfo(req, e, ErrorType.WRONG_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, PersistenceException.class})
    ResponseEntity<ErrorInfo> dataIntegrityViolation(HttpServletRequest req, Exception e) {
        String rootMessage = ValidationUtil.getRootCause(e).getMessage();
        if (rootMessage != null) {
            for (Map.Entry<String, String> entry : CONSTRAINTS_MSG.entrySet()) {
                if (rootMessage.toLowerCase().contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, ErrorType.VALIDATION_ERROR, messageSourceAccessor.getMessage(entry.getValue()));
                }
            }
        }
        if (e instanceof DataIntegrityViolationException || e.getMessage().toLowerCase().contains("constraintviolationexception")) {
            return logAndGetErrorInfo(req, e, ErrorType.VALIDATION_ERROR, messageSourceAccessor.getMessage(EXCEPTION_DATA_INTEGRITY_VIOLATION));
        }
        return logAndGetErrorInfo(req, e, ErrorType.DATA_ERROR);
    }

    @ExceptionHandler(BindException.class)
    ResponseEntity<ErrorInfo> bindError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(messageSourceAccessor::getMessage)
                .distinct()
                .toArray(String[]::new);
        return logAndGetErrorInfo(req, e, ErrorType.VALIDATION_ERROR, details);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, ErrorType.VALIDATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, ErrorType.APP_ERROR);
    }

    ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        return ResponseEntity.status(errorType.getStatus()).
                body(new ErrorInfo(req.getRequestURL(), messageSourceAccessor.getMessage(errorType.getErrorCode()),
                    details.length != 0 ? details : new String[] {ValidationUtil.getMessage(rootCause)}, errorType));
    }
}
