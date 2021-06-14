package com.epam.esm.web.handler;

import com.epam.esm.repository.exception.AbsenceOfNewlyCreatedException;
import com.epam.esm.repository.exception.GiftCertificateDuplicateException;
import com.epam.esm.repository.exception.TagDuplicateException;
import com.epam.esm.service.exception.IllegalDurationException;
import com.epam.esm.service.exception.IllegalPriceException;
import com.epam.esm.service.exception.IncorrectCertificateDescriptionException;
import com.epam.esm.service.exception.IncorrectCertificateNameException;
import com.epam.esm.service.exception.NoSuchCertificateException;
import com.epam.esm.service.exception.NoSuchTagException;
import com.epam.esm.service.exception.NotExistentUpdateException;
import com.epam.esm.service.validation.ValidationMessageManager;
import com.epam.esm.web.model.ErrorCode;
import com.epam.esm.web.model.ErrorInfo;
import com.epam.esm.web.model.ErrorMessageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Exception handler class. Constructs a response depending on thrown exception.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    private static final String TAG_NOT_FOUND_PROPERTY = "tag_not_found";
    private static final String CERTIFICATE_NOT_FOUND_PROPERTY = "certificate_not_found";
    private static final String NEWLY_CREATED_ABSENCE_PROPERTY = "newly_created_absence";
    private static final String INVALID_ARGUMENT_PROPERTY = "invalid_argument";
    private static final String NON_EXISTING_UPDATE_PROPERTY = "non_existing_update";
    private static final String TAG_DUPLICATE_PROPERTY = "tag_duplicate";
    private static final String CERTIFICATE_DUPLICATE_PROPERTY = "certificate_duplicate";
    private static final String INCORRECT_CERTIFICATE_NAME_PROPERTY = "incorrect_certificate_name";
    private static final String INCORRECT_CERTIFICATE_DESCRIPTION_PROPERTY = "incorrect_certificate_description";
    private static final String INCORRECT_CERTIFICATE_PRICE_PROPERTY = "incorrect_certificate_price";
    private static final String INCORRECT_CERTIFICATE_DURATION_PROPERTY = "incorrect_certificate_duration";
    private static final String BLANK_TAG_PROPERTY = "blank_tag";
    private static final String TAG_WRONG_SIZE_PROPERTY = "tag_wrong_size";
    private static final String CERTIFICATE_NAME_BLANK_PROPERTY = "certificate_name_blank";
    private static final String CERTIFICATE_DESCRIPTION_BLANK_PROPERTY = "certificate_description_blank";
    private static final String CERTIFICATE_NAME_WRONG_SIZE_PROPERTY = "certificate_name_wrong_size";
    private static final String CERTIFICATE_DESCRIPTION_WRONG_SIZE_PROPERTY = "certificate_description_wrong_size";
    private static final String INVALID_PRICE_PROPERTY = "invalid_price";
    private static final String DURATION_INVALID_PROPERTY = "duration_invalid";
    private ErrorMessageManager messageManager;

    @Autowired
    public void setMessageManager(ErrorMessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @ExceptionHandler(NoSuchTagException.class)
    public ResponseEntity<?> tagNotFoundHandle(NoSuchTagException e, Locale locale) {
        String message = messageManager.receiveMessage(TAG_NOT_FOUND_PROPERTY, locale);
        ErrorInfo errorInfo = new ErrorInfo(message + " (id = " + e.getId() + ")", ErrorCode.NOT_FOUND_TAG);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchCertificateException.class)
    public ResponseEntity<ErrorInfo> certificateNotFoundHandle(NoSuchCertificateException e, Locale locale) {
        String message = messageManager.receiveMessage(CERTIFICATE_NOT_FOUND_PROPERTY, locale);
        String errorMessage = message + " (id = " + e.getId() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.NOT_FOUND_CERTIFICATE);
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({AbsenceOfNewlyCreatedException.class, SQLException.class, FileNotFoundException.class})
    public ResponseEntity<ErrorInfo> newlyCreatedAbsenceHandle(Locale locale) {
        String message = messageManager.receiveMessage(NEWLY_CREATED_ABSENCE_PROPERTY, locale);
        ErrorInfo errorInfo = new ErrorInfo(message, ErrorCode.NEWLY_CREATED_ABSENCE);
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorInfo> illegalArgumentsHandle(IllegalArgumentException e, Locale locale) {
        String message = messageManager.receiveMessage(INVALID_ARGUMENT_PROPERTY, locale);
        String errorMessage = message + " (" + e.getMessage() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.ILLEGAL_ARGUMENT);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotExistentUpdateException.class})
    public ResponseEntity<ErrorInfo> notExistingUpdateHandle(NotExistentUpdateException e, Locale locale) {
        String message = messageManager.receiveMessage(NON_EXISTING_UPDATE_PROPERTY, locale);
        String errorMessage = message + " (id = " + e.getId() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.NOT_EXISTENT_UPDATE);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> unsupportedMethodHandling() {
        HttpHeaders headers = new HttpHeaders();
        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.PUT);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.POST);
        headers.setAllow(allowedMethods);
        return new ResponseEntity<>(headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(TagDuplicateException.class)
    public ResponseEntity<ErrorInfo> tagDuplicateCreatingHandle(TagDuplicateException e, Locale locale) {
        String message = messageManager.receiveMessage(TAG_DUPLICATE_PROPERTY, locale);
        String errorMessage = message + " (" + e.getMessage() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.CREATE_TAG_DUPLICATE);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GiftCertificateDuplicateException.class)
    public ResponseEntity<ErrorInfo> certificateDuplicateCreatingHandle(GiftCertificateDuplicateException e,
                                                                        Locale locale) {
        String message = messageManager.receiveMessage(CERTIFICATE_DUPLICATE_PROPERTY, locale);
        String errorMessage = message + " (" + e.getMessage() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.CREATE_CERTIFICATE_DUPLICATE);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> invalidDataHandle(MethodArgumentNotValidException e, Locale locale) {
        StringBuilder responseMessage = new StringBuilder();
        for (ObjectError err : e.getBindingResult().getAllErrors()) {
            String errorMessage = err.getDefaultMessage();
            if (errorMessage != null) {
                switch (errorMessage) {
                    case ValidationMessageManager.BLANK_TAG_NAME: {
                        responseMessage.append(" ")
                                .append(messageManager.receiveMessage(BLANK_TAG_PROPERTY, locale));
                        break;
                    }
                    case ValidationMessageManager.TAG_NAME_WRONG_SIZE: {
                        responseMessage.append(" ")
                                .append(messageManager.receiveMessage(TAG_WRONG_SIZE_PROPERTY, locale));
                        break;
                    }
                    case ValidationMessageManager.BLANK_CERTIFICATE_NAME: {
                        responseMessage.append(" ")
                                .append(messageManager.receiveMessage(CERTIFICATE_NAME_BLANK_PROPERTY, locale));
                        break;
                    }
                    case ValidationMessageManager.BLANK_CERTIFICATE_DESCRIPTION: {
                        responseMessage.append(" ")
                                .append(messageManager.receiveMessage(CERTIFICATE_DESCRIPTION_BLANK_PROPERTY, locale));
                        break;
                    }
                    case ValidationMessageManager.CERTIFICATE_NAME_WRONG_SIZE: {
                        responseMessage.append(" ")
                                .append(messageManager.receiveMessage(CERTIFICATE_NAME_WRONG_SIZE_PROPERTY, locale));
                        break;
                    }
                    case ValidationMessageManager.CERTIFICATE_DESCRIPTION_WRONG_SIZE: {
                        responseMessage.append(" ")
                                .append(messageManager
                                        .receiveMessage(CERTIFICATE_DESCRIPTION_WRONG_SIZE_PROPERTY, locale));
                        break;
                    }
                    case ValidationMessageManager.CERTIFICATE_PRICE_INVALID: {
                        responseMessage.append(" ")
                                .append(messageManager.receiveMessage(INVALID_PRICE_PROPERTY, locale));
                        break;
                    }
                    case ValidationMessageManager.CERTIFICATE_DURATION_INVALID: {
                        responseMessage.append(" ")
                                .append(messageManager.receiveMessage(DURATION_INVALID_PROPERTY, locale));
                        break;
                    }
                }
            }
        }
        ErrorInfo errorInfo = new ErrorInfo(responseMessage.toString().trim(), ErrorCode.INVALID_DATA);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectCertificateNameException.class)
    public ResponseEntity<ErrorInfo> handleInvalidCertificateName(IncorrectCertificateNameException e, Locale locale) {
        String message = messageManager.receiveMessage(INCORRECT_CERTIFICATE_NAME_PROPERTY, locale);
        String errorMessage = message + "(" + e.getMessage() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.INVALID_DATA);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectCertificateDescriptionException.class)
    public ResponseEntity<ErrorInfo> handleInvalidCertificateDescription(IncorrectCertificateDescriptionException e,
                                                                         Locale locale) {
        String message = messageManager.receiveMessage(INCORRECT_CERTIFICATE_DESCRIPTION_PROPERTY, locale);
        String errorMessage = message + "(" + e.getMessage() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.INVALID_DATA);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalPriceException.class)
    public ResponseEntity<ErrorInfo> handleInvalidCertificatePrice(IllegalPriceException e, Locale locale) {
        String message = messageManager.receiveMessage(INCORRECT_CERTIFICATE_PRICE_PROPERTY, locale);
        String errorMessage = message + " (" + e.getMessage() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.INVALID_DATA);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalDurationException.class)
    public ResponseEntity<ErrorInfo> handleInvalidCertificateDuration(IllegalDurationException e, Locale locale) {
        String message = messageManager.receiveMessage(INCORRECT_CERTIFICATE_DURATION_PROPERTY, locale);
        String errorMessage = message + " (" + e.getMessage() + ")";
        ErrorInfo errorInfo = new ErrorInfo(errorMessage, ErrorCode.INVALID_DATA);
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFoundError(Exception ex) {
        return ResponseEntity.badRequest().build();
    }
}
