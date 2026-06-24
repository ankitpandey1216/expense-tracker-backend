package com.ankit.expense_tracker.service;

import com.ankit.expense_tracker.DTO.*;
import com.ankit.expense_tracker.entities.Expense;
import com.ankit.expense_tracker.entities.Group;
import com.ankit.expense_tracker.entities.GroupMember;
import com.ankit.expense_tracker.entities.User;
import com.ankit.expense_tracker.repo.GroupMemberRepo;
import com.ankit.expense_tracker.repo.GroupRepo;
import com.ankit.expense_tracker.repo.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepo groupRepo;
    @Autowired
    private GroupMemberRepo groupMemberRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ExpenseService expenseService;

    @Transactional
    public Group createGroup(GroupCreationRequest request) {
        Group group = new Group();
        User createdBy = userRepo.findById(request.getCreatedBy()).orElseThrow(() -> new RuntimeException("User not found"));
        group.setGroupTitle(request.getGroupTitle());
        group.setCreatedBy(createdBy);
        Group createdGroup = groupRepo.save(group);
        GroupMember member = new GroupMember();
        member.setGroup(createdGroup);
        member.setUser(createdBy);
        groupMemberRepo.save(member);
        return createdGroup;
    }

    public List<Group> getAllGroups(Long id) {
        List<Group> groups = groupMemberRepo.findAllGroup(id);
        if(groups.isEmpty()){
            return new ArrayList<>();
        }
        return groups;
    }

    @Transactional
    public GroupDetailDTO getGroupDetail(Long groupId,Long userId) {
        try {
            Group group = groupRepo.findById(groupId).orElseThrow(() -> new RuntimeException("Group not fount with id: " + groupId));
            List<User> members = groupMemberRepo.findMembersByGroupId(groupId);
            List<UserDTO> userDTOS = new ArrayList<>();
            if(!members.isEmpty()){
                for(User user: members){
                    UserDTO userDTO = new UserDTO();
                    userDTO.setUserName(user.getName());
                    userDTO.setUserId(user.getUserId());
                    userDTO.setEmail(user.getEmail());
                    userDTOS.add(userDTO);
                }
            }
            List<ExpenseDTO> expenses = expenseService.getGroupExpenses(groupId);
            BalanceResponse balances = expenseService.getUserBalance(userId,groupId);
            GroupDetailDTO groupDetailDTO = new GroupDetailDTO();
            groupDetailDTO.setExpenses(expenses);
            groupDetailDTO.setMembers(userDTOS);
            groupDetailDTO.setGroupId(groupId);
            groupDetailDTO.setGroupName(group.getGroupTitle());
            groupDetailDTO.setBalances(balances);
            return groupDetailDTO;
        }catch (Exception e) {
            throw e;
        }
    }
}
