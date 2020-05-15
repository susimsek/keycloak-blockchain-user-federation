package org.keycloak.fabric.error;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class ErrorHandler implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ApiError handleError(WebRequest webRequest){
        Map<String, Object> attributes = this.errorAttributes.getErrorAttributes(webRequest, true);
        String message = (String) attributes.get("message");
        String path = (String) attributes.get("path");
        int status = (Integer) attributes.get("status");
        long timestamp = ((Date) attributes.get("timestamp")).getTime();
        ApiError error = new ApiError(status,message,path,timestamp);
        if(attributes.containsKey("errors")){
            List<FieldError> fieldErrors = (List<FieldError>) attributes.get("errors");
            Map<String, String> errors = new HashMap<>();
            for(FieldError fieldError: fieldErrors){
                errors.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            error.setErrors(errors);
        }
        return error;
    }

}
