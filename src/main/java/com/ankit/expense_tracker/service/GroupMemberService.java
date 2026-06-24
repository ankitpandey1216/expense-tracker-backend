package com.ankit.expense_tracker.service;

import com.ankit.expense_tracker.DTO.AddMemberRequest;
import com.ankit.expense_tracker.DTO.MemberDTO;
import com.ankit.expense_tracker.entities.Group;
import com.ankit.expense_tracker.entities.GroupMember;
import com.ankit.expense_tracker.entities.User;
import com.ankit.expense_tracker.repo.GroupMemberRepo;
import com.ankit.expense_tracker.repo.GroupRepo;
import com.ankit.expense_tracker.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class GroupMemberService {
    @Autowired
    private GroupMemberRepo groupMemberRepo;
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private UserRepo userRepo;

    @Transactional
    public void addMembers(Long groupId, AddMemberRequest request) {
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("Group Not Found with id: " + groupId));
        List<User> users = userRepo.findAllById(request.getUserIds());
        if(users.size() != request.getUserIds().size()){
            throw new RuntimeException("Some users not found");
        }
        List<GroupMember> groupMembers = new ArrayList<>();
        for(User user: users){
            GroupMember member = new GroupMember();
            member.setGroup(group);
            member.setUser(user);
            groupMembers.add(member);
        }
        groupMemberRepo.saveAll(groupMembers);

    }


    @Transactional
    public List<MemberDTO> getMembers(Long groupId) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        List<Long> userIds = groupMemberRepo.findAllMemberIds(groupId);
        List<User> users = userRepo.findAllById(userIds);
        if(userIds.size() != users.size()){
            throw new RuntimeException("Some members not found");
        }
        List<MemberDTO> memberDTOS = new ArrayList<>();
        for(User user: users){
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberName(user.getName());
            memberDTO.setUserId(user.getUserId());
            memberDTOS.add(memberDTO);
        }
        return memberDTOS;
    }
}
