package com.example.federation.GetterSetterClasses;

/**
 * Created by HP-PC on 17-04-2017.
 */

public class Participant {
    public int participantId,participantPreQuestionnair,participantFeedback,participantEvaluation,participantPostQuestionnair;
    public String participantName,participantEmail;
    public Participant(int participantId,String participantName,String participantEmail,int participantEvaluation,int participantFeedback,int participantPreQuestionnair,int participantPostQuestionnair){
        this.participantEmail=participantEmail;
        this.participantEvaluation=participantEvaluation;
        this.participantFeedback=participantFeedback;
        this.participantId=participantId;
        this.participantPreQuestionnair=participantPreQuestionnair;
        this.participantPostQuestionnair=participantPostQuestionnair;
        this.participantName=participantName;
    }

    public int getParticipantPostQuestionnair() {
        return participantPostQuestionnair;
    }

    public int getParticipantEvaluation() {
        return participantEvaluation;
    }

    public int getParticipantFeedback() {
        return participantFeedback;
    }

    public int getParticipantId() {
        return participantId;
    }

    public int getParticipantPreQuestionnair() {
        return participantPreQuestionnair;
    }

    public String getParticipantEmail() {
        return participantEmail;
    }

    public String getParticipantName() {
        return participantName;
    }
}
