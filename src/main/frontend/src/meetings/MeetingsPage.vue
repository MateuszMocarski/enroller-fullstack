<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0">
               Brak zaplanowanych spotkaÅ„.
           </span>
    <h3 v-else>
      Zaplanowane zajÄ™cia ({{ meetings.length }})
    </h3>

    <meetings-list :meetings="meetings"
                   :username="username"
                   @attend="addMeetingParticipant($event)"
                   @unattend="removeMeetingParticipant($event)"
                   @delete="deleteMeeting($event)"></meetings-list>
  </div>
</template>

<script>
    import NewMeetingForm from "./NewMeetingForm";
    import MeetingsList from "./MeetingsList";
    
    export default {
        components: {NewMeetingForm, MeetingsList},
        props: ['username'],
        data() {
            return {
                meetings: []
            };
        },
        methods: {
            loadMeetings() {
                this.$http.get('meetings').then(response => {
                    this.meetings = response.data;
                })
                .catch(response => {
                    Utils.notify(this, 'error', 'B³¹d', 'Nie uda³o siê za³adowaæ listy zajêc');
                });
            },
            addNewMeeting(meeting) {
                this.$http.post('meetings', meeting)
                .then(response => {
                    this.meetings.push(response.data);
                    Utils.notify(this, 'success', 'Dodano zajêcia', 'Pomyœlnie dodano zajêcia');
                    // this.loadMeetings();
                })
                .catch(response => {
                    Utils.notify(this, 'error', 'B³ad dodawania zajêæ', 'Wyst¹pi³ b³¹d w trakcie dodawania zajêæ');
                });
            },
            addMeetingParticipant(meeting) {
                let url = 'meetings/' + meeting.id + '/participants';
                this.$http.post(url)
                .then(response => {
                    this.meetings.find(m => m.id === meeting.id).participants = response.data.participants;
                    Utils.notify(this, 'success', 'Zapisano na zajêcia', 'Zapis na zajêcia przebieg³ pomyœlnie');
                })
                .catch(response => {
                    Utils.notify(this, 'error', 'B³¹d podczas zapisu na zajêcia', 'Nie uda³o siê dopisaæ Ciê do zajêæ, spróbój póŸniej');
                });
            },
            removeMeetingParticipant(meeting) {
                let url = 'meetings/' + meeting.id + '/participants/' + this.username;
                this.$http.delete(url)
                .then(response => {
                    // Modify state with response object, instead of new request for all meetings
                    this.meetings.find(m => m.id === meeting.id).participants = response.data.participants;
                    Utils.notify(this, 'success', 'Wypisano z zajêæ', 'Pomyœlnie wypisano z zajêæ');
                })
                .catch(response => {
                    console.log(response);
                    Utils.notify(this, 'error', 'B³¹d podczas wypisywania z zajêæ', 'Nieudana próba wypisania z zajêc');
                });
            },
            deleteMeeting(meeting) {
                // this.meetings.splice(this.meetings.indexOf(meeting), 1);
                let url = 'meetings/' + meeting.id;
                this.$http.delete(url)
                .then(response => {
                    this.meetings = this.meetings.filter(m => m.id !== meeting.id);
                    Utils.notify(this, 'success', 'Usuniêto zajêcia', 'Pomyœlnie usuniêto zajêcia');
                })
                .catch(response => {
                    console.log(response);
                    Utils.notify(this, 'error', 'B³¹d', 'B³¹d podczas usuwania zajêæ');
                });
            }
        },
        mounted() {
            this.loadMeetings();
        }
    }
</script>
