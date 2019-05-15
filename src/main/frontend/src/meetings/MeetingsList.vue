<template>
  <table v-if="meetings.length > 0">
    <thead>
    <tr>
      <th>Nazwa spotkania</th>
      <th>Data</th>
      <th>Opis</th>
      <th>Uczestnicy</th>
      <td></td>
    </tr>
    </thead>
    <tbody>
    <tr v-for="meeting in meetings" :key="meeting.id">
      <td>{{ meeting.title }}</td>
      <td>{{ meeting.date }}</td>
      <td>{{ meeting.description }}</td>
      <td>
      	<tr v-for="participant in meeting.participants" :key="participant.login">
      		<td> {{participant.login}}
      		</td>
      	</tr>
      </td>
      <td class="controls">
        <button class="button" @click="$emit('delete', meeting)">
          Usuñ spotkanie
        </button>
        
      </td>
    </tr>
    </tbody>
  </table>
</template>

<script>
    export default {
        props: ['meetings', 'username']
    }
</script>
