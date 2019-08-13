package ru.javawebinar.topjava.web;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.StringJoiner;

public class AjaxUtil {
    public static ResponseEntity<String> getStringResponseEntity(BindingResult result) {
        ResponseEntity<String> responseEntity = null;
        if (result.hasErrors()) {
            StringJoiner joiner = new StringJoiner("<br>");
            result.getFieldErrors().forEach(
                    fe -> {
                        String msg = fe.getDefaultMessage();
                        if (msg != null) {
                            if (!msg.startsWith(fe.getField())) {
                                msg = fe.getField() + ' ' + msg;
                            }
                            joiner.add(msg);
                        }
                    });
            responseEntity = ResponseEntity.unprocessableEntity().body(joiner.toString());
        } else {
            responseEntity = ResponseEntity.ok().build();
        }
        return responseEntity;
    }
}