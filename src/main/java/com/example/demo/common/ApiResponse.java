package com.example.demo.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Data;
@Data
public class ApiResponse {
    public static ResponseEntity<Map<String, Object>> getResponse(
            boolean success, String message, Object data) {

        Map<String, Object> res = new HashMap<>();
        res.put("success", success);
        res.put("message", message);
        if (data != null) {
            res.put("data", data);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    public static ResponseEntity<Map<String, Object>> getMessage(
            boolean success, Object message) {

        Map<String, Object> res = new HashMap<>();
        res.put("success", success);
        if (message != null) {
            res.put("message", message);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

