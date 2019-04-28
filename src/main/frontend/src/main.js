import Vue from 'vue';
import VueResource from "vue-resource";
Vue.use(VueResource);
Vue.http.options.root = '/api';
import App from './App.vue';

new Vue({
    el: '#app',
    render: h => h(App)
});
