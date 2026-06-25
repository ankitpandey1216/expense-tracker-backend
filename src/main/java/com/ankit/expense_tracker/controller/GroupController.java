package com.ankit.expense_tracker.controller;

import com.ankit.expense_tracker.DTO.GroupCreationRequest;
import com.ankit.expense_tracker.DTO.GroupDTO;
import com.ankit.expense_tracker.DTO.GroupDetailDTO;
import com.ankit.expense_tracker.entities.Group;
import com.ankit.expense_tracker.service.GroupService;
import com.ankit.expense_tracker.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/groups")
@RestController
public class GroupController {
    @Autowired
    private GroupService groupService;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupCreationRequest request){
        Group group = groupService.createGroup(request);
        GroupDTO response = new GroupDTO(
                group.getGroupId(),
                group.getGroupTitle(),
                group.getCreatedBy().getUserId(),
                group.getCreatedBy().getName(),
                group.getCreatedAt()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> getAllGroups(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        System.out.println("The token sent is : " + token);
        Long userId = jwtService.extractUserId(token);
        System.out.println("Inside getAllgroups: " + userId);
        List<Group> groups = groupService.getAllGroups(userId);
        if(groups.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<GroupDTO> response = new ArrayList<>();
        for(Group group: groups){
            GroupDTO groupDTO = new GroupDTO(
                    group.getGroupId(),
                    group.getGroupTitle(),
                    group.getCreatedBy().getUserId(),
                    group.getCreatedBy().getName(),
                    group.getCreatedAt()
            );
            response.add(groupDTO);
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailDTO> getGroupDetail(@PathVariable Long groupId,HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);
        Long userId = jwtService.extractUserId(token);
        return ResponseEntity.status(200).body(groupService.getGroupDetail(groupId,userId));
    }
}
