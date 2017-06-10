package com.example.federation.GetterSetterClasses;

/**
 * Created by Aakash on 19-Apr-17.
 */

public class ParticipantAttendance {

        String participant_name,participant_email,participant_reason_for_absent="";
        int participant_id,participant_id_in_event;
        boolean participant_todays_attendance_status=false;
        boolean participant_evaluation_status=false,participant_feedback_status=false;
        public ParticipantAttendance(int participantId,int eventId,String participantName,String participantEmail){

            participant_id=participantId;
            participant_email=participantEmail;
            participant_name=participantName;

        }



        public void setParticipantTodaysAttendanceStatus(boolean checked){
            participant_todays_attendance_status=!participant_todays_attendance_status;
        }
        public int getParticipantId(){
            return participant_id;
        }
        public String getParticipantName(){
            return  participant_name;
        }
        public String getParticipantEmail(){
            return participant_email;
        }
        public int getParticipantIdInEvent(){
            return participant_id_in_event;
        }
        public boolean getParticipantTodaysAttendanceStatus(){
            return participant_todays_attendance_status;
        }
        public void setParticipantReasonForAbsent(String reason){
            participant_reason_for_absent=reason;
        }
        public String getParticipantReasonForAbsent(){
            return participant_reason_for_absent;
        }
        public boolean getParticipantEvaluationStatus(){
            return participant_evaluation_status;
        }
        public boolean getParticipantFeedbackStatus(){
            return participant_feedback_status;
        }

        public void setParticipantId(int participantId) {
            this.participant_id = participantId;
        }

        public void setParticipantIDEvent(int participantIDEvent) {
            this.participant_id_in_event = participantIDEvent;
        }

        public void setParticipantName(String participantName) {
            this.participant_name = participantName;
        }

        public void setParticipantEmail(String participantEmail) {
            this.participant_email = participantEmail;
        }

        public void setAttendanceStatus(Boolean attendanceStatus) {
            this.participant_todays_attendance_status = attendanceStatus;
        }

        public void setEvaluationStatus(Boolean evaluationStatus) {
            this.participant_evaluation_status = evaluationStatus;
        }

        public void setFeedbackStatus(Boolean feedbackStatus) {
            this.participant_feedback_status = feedbackStatus;
        }
    }


