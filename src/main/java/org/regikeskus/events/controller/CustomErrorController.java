package org.regikeskus.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class CustomErrorController implements ErrorController {
    private final ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(WebRequest webRequest) {
        Map<String, Object> error = this.errorAttributes.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        HttpStatus status = HttpStatus.valueOf((Integer) error.get("status"));
        return new ResponseEntity<>(error, status);
    }

}
