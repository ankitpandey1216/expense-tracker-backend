package com.ankit.expense_tracker.controller;

import com.ankit.expense_tracker.DTO.DashboardResponse.DashboardResponse;
import com.ankit.expense_tracker.service.DashboardService;
import com.ankit.expense_tracker.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    private final JwtService jwtService;

    @Autowired
    public DashboardController(DashboardService dashboardService,JwtService jwtService){
        this.dashboardService = dashboardService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardData(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        System.out.println("The token sent is : " + token);
        Long userId = jwtService.extractUserId(token);
        DashboardResponse response = dashboardService.getDashboardData(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
