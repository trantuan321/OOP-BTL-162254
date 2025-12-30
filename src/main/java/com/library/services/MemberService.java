package com.library.services;

import com.library.models.Member;
import java.util.*;
import java.io.*;

public class MemberService {
    private List<Member> members;
    private static final String FILE_NAME = "data/members.txt";
    
    public MemberService() {
        members = new ArrayList<>();
        loadFromFile();
        // Removed initializeSampleData() - chỉ load từ file
    }
    
    public boolean addMember(Member member) {
        // Validate member ID format
        if (!member.getId().matches("M\\d{3}")) {
            System.err.println("Invalid member ID format: " + member.getId());
            return false;
        }
        
        // Check if ID already exists
        for (Member m : members) {
            if (m.getId().equals(member.getId())) {
                System.err.println("Member ID already exists: " + member.getId());
                return false;
            }
        }
        
        System.out.println("Adding new member: " + member.getId() + " - " + member.getName());
        members.add(member);
        boolean saved = saveToFile();
        if (saved) {
            System.out.println("Member saved successfully to file");
        }
        return saved;
    }
    
    public boolean updateMember(String memberId, Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId().equals(memberId)) {
                members.set(i, updatedMember);
                return saveToFile();
            }
        }
        return false;
    }
    
    public boolean deleteMember(String memberId) {
        Iterator<Member> iterator = members.iterator();
        while (iterator.hasNext()) {
            Member member = iterator.next();
            if (member.getId().equals(memberId)) {
                // Check if member has borrowed items
                if (member.getBorrowedCount() > 0) {
                    System.err.println("Cannot delete member with borrowed items: " + memberId);
                    return false;
                }
                iterator.remove();
                boolean saved = saveToFile();
                if (saved) {
                    System.out.println("Member deleted successfully: " + memberId);
                }
                return saved;
            }
        }
        System.err.println("Member not found for deletion: " + memberId);
        return false;
    }
    
    public Member findMemberById(String memberId) {
        for (Member member : members) {
            if (member.getId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }
    
    public List<Member> searchByName(String name) {
        List<Member> result = new ArrayList<>();
        for (Member member : members) {
            if (member.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(member);
            }
        }
        return result;
    }
    
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }
    
    public List<Member> getActiveMembers() {
        List<Member> result = new ArrayList<>();
        for (Member member : members) {
            if (member.isActive()) {
                result.add(member);
            }
        }
        return result;
    }
    
    public List<Member> getMembersWithBorrowedItems() {
        List<Member> result = new ArrayList<>();
        for (Member member : members) {
            if (member.getBorrowedCount() > 0) {
                result.add(member);
            }
        }
        return result;
    }
    
    public int getTotalMembers() {
        return members.size();
    }
    
    public int getActiveMembersCount() {
        int count = 0;
        for (Member member : members) {
            if (member.isActive()) {
                count++;
            }
        }
        return count;
    }
    
    private boolean saveToFile() {
        try {
            // Ensure directory exists
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
                for (Member member : members) {
                    writer.println(String.format("%s,%s,%s,%s,%s,%s", 
                            member.getId(), member.getName(), member.getEmail(), 
                            member.getPhone(), member.isActive(), member.getJoinDate()));
                }
                System.out.println("Members saved to file: " + FILE_NAME + ", count: " + members.size());
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error saving members to " + FILE_NAME + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Members file not found: " + FILE_NAME + ", starting with empty list");
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int loadedCount = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Member member = new Member(parts[0], parts[1], parts[2], parts[3]);
                    member.setActive(Boolean.parseBoolean(parts[4]));
                    if (parts.length >= 6) {
                        member.setJoinDate(parts[5]);
                    }
                    members.add(member);
                    loadedCount++;
                }
            }
            System.out.println("Loaded " + loadedCount + " members from " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error loading members from " + FILE_NAME + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
