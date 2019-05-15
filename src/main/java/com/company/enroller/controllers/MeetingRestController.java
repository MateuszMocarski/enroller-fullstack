package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
//import com.company.enroller.security.UserProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {

		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> get(@PathVariable("id") long id) {

		Meeting meeting = meetingService.findByID(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
		if (meetingService.findByID(meeting.getId()) != null) {
			return new ResponseEntity("Unable to create meeting with that name", HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting meeting = meetingService.findByID(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meetingService.delete(id);
		return new ResponseEntity<Participant>(HttpStatus.NO_CONTENT);
	}

//	@RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
//	public ResponseEntity<?> addMeetingParticipant(@PathVariable("id") long meetingID) {
//		String username = UserProvider.getUsername();
//		Participant participant = participantService.findByLogin(username);
//		Meeting meeting = meetingService.findByID(meetingID);
//
//		if (meeting == null) {
//			return new ResponseEntity<>("Meeting does not exist", HttpStatus.BAD_REQUEST);
//		}
//		try {
//			Meeting result = (Meeting) meetingService.addParticipantToMeeting(meeting, participant);
//			return new ResponseEntity<>(result, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>("Participant is in this meeting!", HttpStatus.CONFLICT);
//		}
//	}

	@RequestMapping(value = "/{id}/participants/{login}", method = RequestMethod.DELETE)
	public ResponseEntity<?> removeMeetingParticipant(@PathVariable("id") long meetingId,
			@PathVariable("login") String login) {

		Meeting meetingWithoutParticipant = meetingService.removeParticipantFromMeeting(meetingId, login);
		return new ResponseEntity<>(meetingWithoutParticipant, HttpStatus.OK);
	}
}
