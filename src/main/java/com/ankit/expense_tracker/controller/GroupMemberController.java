package com.ankit.expense_tracker.controller;

import com.ankit.expense_tracker.DTO.AddMemberRequest;
import com.ankit.expense_tracker.DTO.MemberDTO;
import com.ankit.expense_tracker.service.GroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("group/{groupId}/members")
public class GroupMemberController {
    @Autowired
    private GroupMemberService groupMemberService;
    @PostMapping
    public ResponseEntity<Void> addMembers(@PathVariable Long groupId, @RequestBody AddMemberRequest request){
        groupMemberService.addMembers(groupId,request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getMembers(@PathVariable Long groupId){
        List<MemberDTO>  response = groupMemberService.getMembers(groupId);
        return ResponseEntity.ok(response);
    }
}
