package com.springboot.starling.adapter.controller;

import com.springboot.starling.adapter.api.response.SavingGoalsResponse;
import com.springboot.starling.adapter.services.SavingGoalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saving-goals")
public class SavingGoalsController {

    @Autowired
    private SavingGoalsService savingGoalsService;

    @PostMapping("/process")
    public SavingGoalsResponse processSavingGoals(@RequestParam String minTimestamp,
                                   @RequestHeader("Authorization") String authorizationHeader) {
        return savingGoalsService.processSavingGoals(minTimestamp, authorizationHeader);
    }
}
