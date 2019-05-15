package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

    DatabaseConnector connector;
    
    @Autowired
    private ParticipantService participantService;

    public MeetingService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Meeting> getAll() {
        String hql = "FROM Meeting";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }
    
    public Meeting findByID(long id){
    	return (Meeting) connector.getSession().get(Meeting.class, id);
    }
    
    
    public Meeting add(Meeting meeting) {
    	Transaction transaction = connector.getSession().beginTransaction();
    	connector.getSession().save(meeting);
    	transaction.commit();
    	return meeting;
    }

	public void delete(long meetingID) {
		Meeting meeting = this.findByID(meetingID);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().delete(meeting);
		transaction.commit();
		
	}
	
	public Meeting addParticipantToMeeting(Meeting meeting, Participant participant) throws Exception {
		if(meeting.hasParticipant(participant)) {
			throw new Exception();
		}
		meeting.addParticipant(participant);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(meeting);
		transaction.commit();
		return meeting;
	}
	
	public Meeting removeParticipantFromMeeting(long MeetingID, String login) {
		Meeting meeting = this.findByID(MeetingID);
		Participant participant = participantService.findByLogin(login);
		
		Transaction transaction = connector.getSession().beginTransaction();
		meeting.removeParticipant(participant);
		connector.getSession().update(meeting);
		transaction.commit();
		
		return meeting;
	}


}
