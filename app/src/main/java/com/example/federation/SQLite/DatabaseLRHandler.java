package com.example.federation.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.federation.GetterSetterClasses.Event;
import com.example.federation.GetterSetterClasses.Participant;
import com.example.federation.GetterSetterClasses.ParticipantAttendance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aakash on 14-Apr-17.
 */

public class DatabaseLRHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;

    // Database Name
    private static final String DATABASE_NAME = "Federation";

    // Contacts table name
    private static final String TABLE_INSTRUCTOR= "InstructorRegister",TABLE_PRE_QUESTIONNAIR="preQuestionnair",TABLE_POST_QUESTIONNAIR="postQuestionnair",TABLE_FEEDBACK="feedback",TABLE_EVALUATION="evaluation",TABLE_MORNING_ATTENDANCE="morningAttendance",TABLE_EVENING_ATTENDANCE="eveningAttendance";
    private static  final String PRE_QUESTIONNAIR_ID="preQuestionnairId",POST_QUESTIONNAIR_ID="postQuestionnairId",EVALUATION_REMARK="evaluationRemark",FEEDBACK_REMARK="feedbackRemark",PRE_QUESTIONNAIR="preQuestionnair",POST_QUESTIONNAIR="postQuestionnair",FEEDBACK_ID="feedbackId",FEEDBACK_DATA="feedbackData",EVALUATION_ID="evaluationId",EVALUATION_DATA="evaluationData"
            ,MORNING_ATTENDANCE_ID="morningAttendanceId",EVENING_ATTENDANCE_ID="eveningAttendanceId",NUMBER_OF_DAYS="numberOfDays",TOTAL_NUMBER_OF_DAYS="totalNumberOfDays";
    // Contacts Table Columns names
    private static  final String EVENT_ID="eventId";
    private static final String TABLE_EVENTS="events",EVENT_VENUE="eventVenue",EVENT_NAME="eventName",EVENT_CODE="eventCode",EVENT_STARTING_DATE="eventStartingDate",EVENT_ENDING_DATE="eventEndingDate",EVENT_INSTRUCTOR="eventInstructor";
    private static final String TABLE_PARTICIPANTS="participants",PARTICIPANT_ID="participantId",PARTICIPANT_NAME="" +
            "participantName",PARTICIPANT_EMAIL="participantEmail",PARTICIPANT_PHONE="participantPhone",PARTICIPANT_DATA="participantData"
            ,PARTICIPANT_EVENT_ID="participantEventId",PARTICIPANT_FEEDBACK="participantFeedback",PARTICIPANT_EVALUATION="participantEvaluation",PARTICIPANT_PRE_QUESTIONNAIR="participantPreQuestionnair",PARTICIPANT_POST_QUESTIONNAIR="participantPostQusetionnair";

    private static final String KEY_ID = "instructorId";
    private static final String KEY_NAME = "instructorName";
    private static final String KEY_EMAIL = "instructorEmail";
    private static final String KEY_PIN = "instructorPin";
    Context c;

    public DatabaseLRHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        c=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EVENTS_TABLE="CREATE TABLE "+TABLE_EVENTS+"(" +
                EVENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+EVENT_CODE+" TEXT NOT " +
                "NULL,"+EVENT_NAME+" TEXT NOT NULL,"+EVENT_VENUE+" TEXT NOT NULL,"+EVENT_INSTRUCTOR+" INTEGER NOT" +
                " NULL,"+EVENT_STARTING_DATE+" TEXT NOT NULL,"+EVENT_ENDING_DATE+" TEXT NOT NULL)";
        String CREATE_MORNING_ATTENDANCE="CREATE TABLE "+TABLE_MORNING_ATTENDANCE+"("+
                MORNING_ATTENDANCE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+EVENT_ID+" INTEGER NOT" +
                " NULL,"+PARTICIPANT_ID+" INTEGER NOT NULL,"+NUMBER_OF_DAYS+" INTEGER NOT NULL,"+TOTAL_NUMBER_OF_DAYS+" INTEGER NOT NULL)";
        String CREATE_EVENING_ATTENDANCE="CREATE TABLE "+TABLE_EVENING_ATTENDANCE+"("+
                EVENING_ATTENDANCE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+EVENT_ID+" INTEGER NOT" +
                " NULL,"+PARTICIPANT_ID+" INTEGER NOT NULL,"+NUMBER_OF_DAYS+" INTEGER NOT NULL,"+TOTAL_NUMBER_OF_DAYS+" INTEGER NOT NULL)";

        String CREATE_INSTRUCTOR_TABLE = "CREATE TABLE " + TABLE_INSTRUCTOR + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_PIN + " INTEGER NOT NULL" + ")";
        String CREATE_PARTICIPANTS_TABLE="CREATE TABLE "+TABLE_PARTICIPANTS+"(" +
                PARTICIPANT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+PARTICIPANT_NAME+" TEXT NOT NULL,"+PARTICIPANT_EMAIL+"" +
                " TEXT NOT NULL,"+PARTICIPANT_PHONE+" TEXT NOT NULL,"+PARTICIPANT_EVENT_ID+" INTEGER NOT NULL," +
                ""+PARTICIPANT_EVALUATION+" INTEGER NOT NULL,"+PARTICIPANT_DATA+" TEXT NOT NULL,"+PARTICIPANT_FEEDBACK+" " +
                "INTEGER NOT NULL,"+PARTICIPANT_PRE_QUESTIONNAIR+" INTEGER NOT NULL,"+PARTICIPANT_POST_QUESTIONNAIR+" " +
                "INTEGER NOT NULL)";
        String CREATE_PRE_QUESTIONNAIR_TABLE="CREATE TABLE "+TABLE_PRE_QUESTIONNAIR+"(" +
               PRE_QUESTIONNAIR_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+EVENT_ID+" INTEGER NOT NULL,"+PARTICIPANT_ID+" INTEGER NOT NULL,"+
                PRE_QUESTIONNAIR+" TEXT NOT NULL)";
        String CREATE_POST_QUESTIONNAIR_TABLE="CREATE TABLE "+TABLE_POST_QUESTIONNAIR+"(" +
                POST_QUESTIONNAIR_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+EVENT_ID+" INTEGER NOT NULL,"+PARTICIPANT_ID+" INTEGER NOT NULL,"+
                POST_QUESTIONNAIR+" TEXT NOT NULL)";
        String CREATE_FEEDBACK_TABLE="CREATE TABLE "+TABLE_FEEDBACK+"("+FEEDBACK_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+PARTICIPANT_ID+" INTEGER NOT NULL,"+
                EVENT_ID+" INTEGER NOT NULL,"+FEEDBACK_REMARK+" TEXT NOT NULL,"+FEEDBACK_DATA+" TEXT NOT NULL)";
        String CREATE_EVALUATION_TABLE="CREATE TABLE "+TABLE_EVALUATION+"("+EVALUATION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+PARTICIPANT_ID+" INTEGER NOT NULL,"+
                EVENT_ID+" INTEGER NOT NULL,"+EVALUATION_REMARK+" TEXT NOT NULL,"+EVALUATION_DATA+" TEXT NOT NULL)";
        db.execSQL(CREATE_PRE_QUESTIONNAIR_TABLE);
        db.execSQL(CREATE_MORNING_ATTENDANCE);
        db.execSQL(CREATE_EVENING_ATTENDANCE);
        db.execSQL(CREATE_EVALUATION_TABLE);
        db.execSQL(CREATE_FEEDBACK_TABLE);
        db.execSQL(CREATE_POST_QUESTIONNAIR_TABLE);
        db.execSQL(CREATE_PARTICIPANTS_TABLE);
        db.execSQL(CREATE_INSTRUCTOR_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUCTOR);
        onCreate(db);
    }


    // Adding new events
    public void addInstructorRegister(String name,int pin,String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,name);
        values.put(KEY_EMAIL,email);
        values.put(KEY_PIN,pin);
        Log.d("TAG:",name + email + pin);
        db.insertWithOnConflict(TABLE_INSTRUCTOR, null, values,SQLiteDatabase.CONFLICT_IGNORE);
        db.close(); // Closing database connection
    }
    public ArrayList<Event> getEventListUnderInstructor(String email){
        ArrayList<Event> list=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_INSTRUCTOR, new String[]{KEY_ID},KEY_EMAIL+"=?", new String[]{email},null,null,null);
        int id = 0;
        while(cursor.moveToNext()){
            int i=cursor.getColumnIndex(KEY_ID);
            id=cursor.getInt(i);
        }
        if(id!=0){
            SharedPreferences pref=c.getSharedPreferences("Tata",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();
            editor.putLong("instructorId",id);
            editor.commit();
        }
        cursor.close();
        Cursor cursor1=db.query(TABLE_EVENTS,new String[]{EVENT_ID,EVENT_NAME,EVENT_CODE,EVENT_VENUE,EVENT_STARTING_DATE,EVENT_ENDING_DATE,EVENT_INSTRUCTOR},EVENT_INSTRUCTOR+"=?", new String[]{String.valueOf(id)},null,null,null);
        while(cursor1.moveToNext()){
            list.add(new Event(cursor1.getInt(0),cursor1.getString(2),cursor1.getString(1),cursor1.getString(3),cursor1.getString(4),cursor1.getString(5)));
        }
        cursor1.close();
        return  list;
    }



    // Checking Availability


    public String getUserAvailability(int pin,String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_INSTRUCTOR;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
        do{

            Log.d("TAG:",cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3));

            if(String.valueOf(pin).equals(cursor.getString(3))&&email.equals(cursor.getString(2)))
            {
                return cursor.getString(1);
            }

        } while (cursor.moveToNext()) ;
    }
     return null;
    }
    public void addEvent(String eventCode,String eventName,String eventVenue,String eventStartingDate,String eventEndingDate){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EVENT_CODE,eventCode);
        values.put(EVENT_NAME,eventName);
        values.put(EVENT_VENUE,eventVenue);
        values.put(EVENT_STARTING_DATE,eventStartingDate);
        values.put(EVENT_ENDING_DATE,eventEndingDate);
        SharedPreferences pref=c.getSharedPreferences("Tata",Context.MODE_PRIVATE);
        long id=pref.getLong("instructorId",0);
        values.put(EVENT_INSTRUCTOR,id);
        Cursor cu=db.query(TABLE_EVENTS, new String[]{EVENT_CODE},EVENT_CODE+"=?", new String[]{eventCode},null,null,null);
        if(cu.moveToFirst()){
            Toast.makeText(c,"Event already exists with the given event code",Toast.LENGTH_SHORT).show();

        }
        else{
            db.insert(TABLE_EVENTS,null,values);
        }
    }

    public ArrayList<Participant> getParticipantList(int eventId) {
        ArrayList<Participant> list=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor cu=db.query(TABLE_PARTICIPANTS,new String[]{PARTICIPANT_ID,PARTICIPANT_NAME,PARTICIPANT_EMAIL,PARTICIPANT_EVENT_ID,PARTICIPANT_EVALUATION,PARTICIPANT_FEEDBACK,PARTICIPANT_PRE_QUESTIONNAIR,PARTICIPANT_POST_QUESTIONNAIR},PARTICIPANT_EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(cu.moveToNext()){
            int id=cu.getColumnIndex(PARTICIPANT_ID),name=cu.getColumnIndex(PARTICIPANT_NAME),email=cu.getColumnIndex(PARTICIPANT_EMAIL),event_id=cu.getColumnIndex(PARTICIPANT_EVENT_ID),evaluation=cu.getColumnIndex(PARTICIPANT_EVALUATION),feedback=cu.getColumnIndex(PARTICIPANT_FEEDBACK),preQuestionnair=cu.getColumnIndex(PARTICIPANT_PRE_QUESTIONNAIR),postQuestionnair=cu.getColumnIndex(PARTICIPANT_POST_QUESTIONNAIR);
            list.add(new Participant(cu.getInt(id),cu.getString(name),cu.getString(email),cu.getInt(evaluation),cu.getInt(feedback),cu.getInt(preQuestionnair),cu.getInt(postQuestionnair)));
        }
        return list;
    }
    public ArrayList<ParticipantAttendance> getParticipantListForAttendance(int eventId) {
        ArrayList<ParticipantAttendance> list=new ArrayList<>();
        SQLiteDatabase db=getReadableDatabase();
        Cursor cu=db.query(TABLE_PARTICIPANTS,new String[]{PARTICIPANT_ID,PARTICIPANT_NAME,PARTICIPANT_EMAIL,PARTICIPANT_EVENT_ID,PARTICIPANT_EVALUATION,PARTICIPANT_FEEDBACK,PARTICIPANT_PRE_QUESTIONNAIR,PARTICIPANT_POST_QUESTIONNAIR},PARTICIPANT_EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(cu.moveToNext()){
            int id=cu.getColumnIndex(PARTICIPANT_ID),name=cu.getColumnIndex(PARTICIPANT_NAME),email=cu.getColumnIndex(PARTICIPANT_EMAIL),event_id=cu.getColumnIndex(PARTICIPANT_EVENT_ID),evaluation=cu.getColumnIndex(PARTICIPANT_EVALUATION),feedback=cu.getColumnIndex(PARTICIPANT_FEEDBACK),preQuestionnair=cu.getColumnIndex(PARTICIPANT_PRE_QUESTIONNAIR),postQuestionnair=cu.getColumnIndex(PARTICIPANT_POST_QUESTIONNAIR);
            list.add(new ParticipantAttendance(cu.getInt(id),cu.getInt(event_id),cu.getString(name),cu.getString(email)));
        }
        return list;
    }

    public void addParticipant(String name, String email, String phone, int eventId,String participantData) {
        SQLiteDatabase db=this.getWritableDatabase();
        if(eventId>0){
            ContentValues values=new ContentValues();
            values.put(PARTICIPANT_EMAIL,email);
            values.put(PARTICIPANT_NAME,name);
            values.put(PARTICIPANT_PHONE,phone);
            values.put(PARTICIPANT_EVENT_ID,eventId);
            values.put(PARTICIPANT_EVALUATION,0);
            values.put(PARTICIPANT_FEEDBACK,0);
            values.put(PARTICIPANT_PRE_QUESTIONNAIR,0);
            values.put(PARTICIPANT_DATA,participantData);
            values.put(PARTICIPANT_POST_QUESTIONNAIR,0);
            ContentValues val=new ContentValues();
            Cursor c=db.query(TABLE_EVENTS,new String[]{EVENT_STARTING_DATE,EVENT_ENDING_DATE},EVENT_ID+"=?", new String[]{String.valueOf(eventId)},null,null,null);
            int startingDateIndex=c.getColumnIndex(EVENT_STARTING_DATE);
            int endingDateIndex=c.getColumnIndex(EVENT_ENDING_DATE);
            Log.d("tag",String.valueOf(startingDateIndex)+" "+String.valueOf(endingDateIndex));
            c.moveToFirst();
            String starting=c.getString(startingDateIndex);
            String ending=c.getString(endingDateIndex);
            int id= (int) db.insert(TABLE_PARTICIPANTS,null,values);
            val.put(PARTICIPANT_ID,id);
            val.put(EVENT_ID,eventId);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date startingDate = new Date();
            Date endingDate = new Date();
            try {
                startingDate = format.parse(starting);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                endingDate = format.parse(ending);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long startingMilli = startingDate.getTime();
            long endingMilli = endingDate.getTime();
            long time = endingMilli - startingMilli;
            long days=0;
            if (time == 0) {
                // eventDays.setText("1");
                days=1;
            } else {
                 days = time / (24 * 3600 * 1000);
                // eventDays.setText(String.valueOf(days));
            }
            val.put(TOTAL_NUMBER_OF_DAYS,days);
            val.put(NUMBER_OF_DAYS,0);
            db.insert(TABLE_MORNING_ATTENDANCE,null,val);
            db.insert(TABLE_EVENING_ATTENDANCE,null,val);
            db.close();
        }
        else{
            Toast.makeText(c,"Something went wrong!",Toast.LENGTH_SHORT).show();
        }

    }

    public void submitQuestionnair(int eventId, int participantId, String data,int questionnairStatus) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PARTICIPANT_ID,participantId);
        values.put(EVENT_ID,eventId);

        if(questionnairStatus==0){
            //pre
            values.put(PRE_QUESTIONNAIR,data);
            int i= (int) db.insert(TABLE_PRE_QUESTIONNAIR,null,values);
            Log.d("tag",String.valueOf(i));
            ContentValues val=new ContentValues();
            val.put(PARTICIPANT_PRE_QUESTIONNAIR,1);
            db.update(TABLE_PARTICIPANTS,val,PARTICIPANT_ID+"=?", new String[]{String.valueOf(participantId)});
        }
        else{
            //post
            values.put(POST_QUESTIONNAIR,data);
            int i= (int) db.insert(TABLE_POST_QUESTIONNAIR,null,values);
            Log.d("tag",String.valueOf(i));
            ContentValues val=new ContentValues();
            val.put(PARTICIPANT_POST_QUESTIONNAIR,1);
            db.update(TABLE_PARTICIPANTS,val,PARTICIPANT_ID+"=?", new String[]{String.valueOf(participantId)});
        }
    }

    public void submitFeedback(int participantId, int eventId, String data,String remarks) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PARTICIPANT_ID,participantId);
        values.put(FEEDBACK_DATA,data);
        values.put(EVENT_ID,eventId);
        values.put(FEEDBACK_REMARK,remarks);
        db.insert(TABLE_FEEDBACK,null,values);
        ContentValues val=new ContentValues();
        val.put(PARTICIPANT_FEEDBACK,1);
        db.update(TABLE_PARTICIPANTS,val,PARTICIPANT_ID+"=?", new String[]{String.valueOf(participantId)});
    }

    public void submitEvaluation(int participantId, int eventId, String data, String remarks) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(PARTICIPANT_ID,participantId);
        values.put(EVENT_ID,eventId);
        values.put(EVALUATION_DATA,data);
        values.put(EVALUATION_REMARK,remarks);
        db.insert(TABLE_EVALUATION,null,values);
        ContentValues val=new ContentValues();
        val.put(PARTICIPANT_EVALUATION,1);
        db.update(TABLE_PARTICIPANTS,val,PARTICIPANT_ID+"=?", new String[]{String.valueOf(participantId)});
    }

    public void submitAttendance(ArrayList<ParticipantAttendance> participantAttendanceLinkedList, String type) {
        SQLiteDatabase db=this.getWritableDatabase();

        if(type.equals("morning")){
            ContentValues values=new ContentValues();Cursor c;
            for(int i=0;i<participantAttendanceLinkedList.size();i++){
                ParticipantAttendance currentParticipant=participantAttendanceLinkedList.get(i);
                c=db.query(TABLE_MORNING_ATTENDANCE,new String[]{NUMBER_OF_DAYS},PARTICIPANT_ID+"=?",new String[]{String.valueOf(currentParticipant.getParticipantId())},null,null,null);
                if(currentParticipant.getParticipantTodaysAttendanceStatus()){
                    c.moveToFirst();
                    int attendance=c.getInt(c.getColumnIndex(NUMBER_OF_DAYS))+1;
                    values.put(NUMBER_OF_DAYS,attendance);
                    db.update(TABLE_MORNING_ATTENDANCE,values,PARTICIPANT_ID+"=?",new String[]{String.valueOf(currentParticipant.getParticipantId())});
                }
            }
        }
        else if(type.equals("evening")){
            ContentValues values=new ContentValues();Cursor c;
            for(int i=0;i<participantAttendanceLinkedList.size();i++){
                ParticipantAttendance currentParticipant=participantAttendanceLinkedList.get(i);
                c=db.query(TABLE_EVENING_ATTENDANCE,new String[]{NUMBER_OF_DAYS},PARTICIPANT_ID+"=?",new String[]{String.valueOf(currentParticipant.getParticipantId())},null,null,null);
                if(currentParticipant.getParticipantTodaysAttendanceStatus()){
                    c.moveToFirst();
                    int attendance=c.getInt(c.getColumnIndex(NUMBER_OF_DAYS))+1;
                    values.put(NUMBER_OF_DAYS,attendance);
                    db.update(TABLE_EVENING_ATTENDANCE,values,PARTICIPANT_ID+"=?",new String[]{String.valueOf(currentParticipant.getParticipantId())});
                }
            }
        }
    }

    public JSONArray getParticioantsData(int eventId) throws JSONException {
        JSONArray data=new JSONArray();

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_PARTICIPANTS,new String[]{PARTICIPANT_ID,PARTICIPANT_NAME,PARTICIPANT_EMAIL,PARTICIPANT_EVENT_ID,PARTICIPANT_PHONE,PARTICIPANT_DATA,PARTICIPANT_EVALUATION,PARTICIPANT_FEEDBACK,PARTICIPANT_PRE_QUESTIONNAIR,PARTICIPANT_POST_QUESTIONNAIR},PARTICIPANT_EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);

        while(c.moveToNext()){
            JSONObject object=new JSONObject();
            object.put(PARTICIPANT_ID,c.getInt(c.getColumnIndex(PARTICIPANT_ID)));
            object.put(PARTICIPANT_NAME,c.getString(c.getColumnIndex(PARTICIPANT_NAME)));
            object.put(PARTICIPANT_EMAIL,c.getString(c.getColumnIndex(PARTICIPANT_EMAIL)));
            object.put(PARTICIPANT_PHONE,c.getString(c.getColumnIndex(PARTICIPANT_PHONE)));
            object.put(PARTICIPANT_DATA,c.getString(c.getColumnIndex(PARTICIPANT_DATA)));
            object.put(PARTICIPANT_EVENT_ID,c.getInt(c.getColumnIndex(PARTICIPANT_EVENT_ID)));
            object.put(PARTICIPANT_FEEDBACK,c.getInt(c.getColumnIndex(PARTICIPANT_FEEDBACK)));
            object.put(PARTICIPANT_EVALUATION,c.getInt(c.getColumnIndex(PARTICIPANT_EVALUATION)));
            object.put(PARTICIPANT_PRE_QUESTIONNAIR,c.getInt(c.getColumnIndex(PARTICIPANT_PRE_QUESTIONNAIR)));
            object.put(PARTICIPANT_POST_QUESTIONNAIR,c.getInt(c.getColumnIndex(PARTICIPANT_POST_QUESTIONNAIR)));
            data.put(object);
        }
        return data;
    }

    public JSONArray getEventData(int eventId) {
        JSONArray data=new JSONArray();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_EVENTS,new String[]{EVENT_ID,EVENT_NAME,EVENT_CODE,EVENT_ENDING_DATE,EVENT_STARTING_DATE,EVENT_INSTRUCTOR,EVENT_VENUE},EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(c.moveToNext()){
            JSONObject object=new JSONObject();
            try {
                object.put(EVENT_ID,c.getInt(c.getColumnIndex(EVENT_ID)));
                object.put(EVENT_NAME,c.getString(c.getColumnIndex(EVENT_NAME)));
                object.put(EVENT_CODE,c.getString(c.getColumnIndex(EVENT_CODE)));
                object.put(EVENT_STARTING_DATE,c.getString(c.getColumnIndex(EVENT_STARTING_DATE)));
                object.put(EVENT_ENDING_DATE,c.getString(c.getColumnIndex(EVENT_ENDING_DATE)));
                object.put(EVENT_VENUE,c.getString(c.getColumnIndex(EVENT_VENUE)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.put(object);
        }
        c.close();
        return data;
    }

    public JSONArray getFeedbackData(int eventId) {
        JSONArray data=new JSONArray();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_FEEDBACK,new String[]{FEEDBACK_ID,FEEDBACK_REMARK,PARTICIPANT_ID,EVENT_ID,FEEDBACK_DATA},EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(c.moveToNext()){
            JSONObject object=new JSONObject();
            try {
                object.put(FEEDBACK_ID,c.getInt(c.getColumnIndex(FEEDBACK_ID)));
                object.put(FEEDBACK_DATA,c.getString(c.getColumnIndex(FEEDBACK_DATA)));
                object.put(FEEDBACK_REMARK,c.getString(c.getColumnIndex(FEEDBACK_REMARK)));
                object.put(PARTICIPANT_ID,c.getInt(c.getColumnIndex(PARTICIPANT_ID)));
                object.put(EVENT_ID,c.getInt(c.getColumnIndex(EVENT_ID)));
                data.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public JSONArray getEvaluationData(int eventId) {
        JSONArray data=new JSONArray();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_EVALUATION,new String[]{EVALUATION_ID,EVALUATION_REMARK,EVALUATION_DATA,PARTICIPANT_ID,EVENT_ID},EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(c.moveToNext()){

            JSONObject object=new JSONObject();
            try {
                object.put(EVALUATION_ID,c.getInt(c.getColumnIndex(EVALUATION_ID)));
                object.put(EVALUATION_DATA,c.getString(c.getColumnIndex(EVALUATION_DATA)));
                object.put(EVALUATION_REMARK,c.getString(c.getColumnIndex(EVALUATION_REMARK)));
                object.put(PARTICIPANT_ID,c.getInt(c.getColumnIndex(PARTICIPANT_ID)));
                object.put(EVENT_ID,c.getInt(c.getColumnIndex(EVENT_ID)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            data.put(object);
        }
        return data;
    }

    public JSONArray getPreQuestionnairData(int eventId)  {
        JSONArray data=new JSONArray();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_PRE_QUESTIONNAIR,new String[]{PRE_QUESTIONNAIR_ID,PRE_QUESTIONNAIR,PARTICIPANT_ID,EVENT_ID},EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(c.moveToNext()){
            JSONObject object=new JSONObject();
            try {
                object.put(PRE_QUESTIONNAIR_ID,c.getInt(c.getColumnIndex(PRE_QUESTIONNAIR_ID)));
                object.put(PRE_QUESTIONNAIR,c.getString(c.getColumnIndex(PRE_QUESTIONNAIR)));
                object.put(PARTICIPANT_ID,c.getInt(c.getColumnIndex(PARTICIPANT_ID)));
                object.put(EVENT_ID,c.getInt(c.getColumnIndex(EVENT_ID)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.put(object);
        }
        return data;

    }

    public JSONArray getPostQuestionnairData(int eventId) {
        JSONArray data=new JSONArray();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_POST_QUESTIONNAIR,new String[]{PARTICIPANT_ID,EVENT_ID,POST_QUESTIONNAIR_ID,POST_QUESTIONNAIR},EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(c.moveToNext()){
            JSONObject object=new JSONObject();
            try {
                object.put(POST_QUESTIONNAIR_ID,c.getInt(c.getColumnIndex(POST_QUESTIONNAIR_ID)));
                object.put(POST_QUESTIONNAIR,c.getString(c.getColumnIndex(POST_QUESTIONNAIR)));
                object.put(PARTICIPANT_ID,c.getInt(c.getColumnIndex(PARTICIPANT_ID)));
                object.put(EVENT_ID,c.getInt(c.getColumnIndex(EVENT_ID)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.put(object);
        }
        return data;
    }

    public JSONArray getMorningAttendance(int eventId) {
        JSONArray data=new JSONArray();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_MORNING_ATTENDANCE,new String[]{PARTICIPANT_ID,MORNING_ATTENDANCE_ID,NUMBER_OF_DAYS,TOTAL_NUMBER_OF_DAYS,EVENT_ID},EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(c.moveToNext()){
            JSONObject object=new JSONObject();
            try {
                object.put(MORNING_ATTENDANCE_ID,c.getInt(c.getColumnIndex(MORNING_ATTENDANCE_ID)));
                object.put(PARTICIPANT_ID,c.getString(c.getColumnIndex(PARTICIPANT_ID)));
                object.put(NUMBER_OF_DAYS,c.getInt(c.getColumnIndex(NUMBER_OF_DAYS)));
                object.put(TOTAL_NUMBER_OF_DAYS,c.getInt(c.getColumnIndex(TOTAL_NUMBER_OF_DAYS)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.put(object);
        }
        return data;
    }

    public JSONArray getEveningAttendance(int eventId) {
        JSONArray data=new JSONArray();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.query(TABLE_EVENING_ATTENDANCE,new String[]{PARTICIPANT_ID,EVENING_ATTENDANCE_ID,NUMBER_OF_DAYS,TOTAL_NUMBER_OF_DAYS,EVENT_ID},EVENT_ID+"=?",new String[]{String.valueOf(eventId)},null,null,null);
        while(c.moveToNext()){
            JSONObject object=new JSONObject();
            try {
                object.put(EVENING_ATTENDANCE_ID,c.getInt(c.getColumnIndex(EVENING_ATTENDANCE_ID)));
                object.put(PARTICIPANT_ID,c.getString(c.getColumnIndex(PARTICIPANT_ID)));
                object.put(NUMBER_OF_DAYS,c.getInt(c.getColumnIndex(NUMBER_OF_DAYS)));
                object.put(TOTAL_NUMBER_OF_DAYS,c.getInt(c.getColumnIndex(TOTAL_NUMBER_OF_DAYS)));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            data.put(object);
        }
        return data;
    }


}
