package cs211.project.models;

import java.time.LocalDateTime;

public class Team {
    private String name;
    private String quantity;
    private LocalDateTime startRecruitDate;
    private LocalDateTime endRecruitDate;
    private int peopleInTeam;

    public Team(String name, String quantity, LocalDateTime startRecruitDate, LocalDateTime endRecruitDate, int peopleInTeam) {
        this.name = name;
        this.quantity = quantity;
        this.startRecruitDate = startRecruitDate;
        this.endRecruitDate = endRecruitDate;
        this.peopleInTeam = peopleInTeam;
    }

    public void joinTeam(User user){
        if(peopleInTeam < Integer.parseInt(quantity)){
            peopleInTeam++;
        }
    }

    public void leaveTeam(User user){
        if(peopleInTeam > 0){
            peopleInTeam--;
        }
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public LocalDateTime getStartRecruitDate() {
        return startRecruitDate;
    }

    public LocalDateTime getEndRecruitDate() {
        return endRecruitDate;
    }

    public int getPeopleInTeam() {
        return peopleInTeam;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setStartRecruitDate(LocalDateTime startRecruitDate) {
        this.startRecruitDate = startRecruitDate;
    }

    public void setEndRecruitDate(LocalDateTime endRecruitDate) {
        this.endRecruitDate = endRecruitDate;
    }

    public void setPeopleInTeam(int peopleInTeam) {
        this.peopleInTeam = peopleInTeam;
    }


}
