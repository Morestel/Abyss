let chapp = Vue.createApp({
    data: () => ({
        user: '',
        message: '',
        messagesTemp: [],
        messages: [],
        messageBool: 0,
        messageAdd: "",
    }),
    mounted() {
        this.loadData();
        this.loadActualUser();
    },

    methods: {

        async loadActualUser() {
            let url = '/api/users/giveUser';
            let req = await fetch(url);
            this.user = await req.json();
        },

        openMessenger: function () {
            if (this.messageBool == 0) {
                document.getElementById('chat').classList.add("open");
                this.messageBool = 1;
            } else {
                document.getElementById('chat').classList.remove("open");
                this.messageBool = 0;
            }
        },

        async loadData() {
            let url = '/api/messages';
            let req = await fetch(url);
            let json = await req.json();
            this.messagesTemp = json._embedded.messages;
            this.messagesTemp.forEach(element => this.organizeListMessage(element))
        },

        organizeListMessage: function (element) {
            var id_schedule = window.location.href;
            id_schedule = id_schedule.replace("http://localhost:8080/schedule/", "");
            if (element.idSchedule == id_schedule) {
                this.messages.push(element);
            }
        },

        addMessage: async function () {
            var id_schedule = window.location.href;
            id_schedule = id_schedule.replace("http://localhost:8080/schedule/", "");
            let newMess = { sender: this.user.username, message: this.message, idSchedule: id_schedule };
            let url = '/api/messages';
            let res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newMess),
            });
            let body = await res.json();
            this.messages.push(body);
        },
        async supprimer(m) {
            let url = m._links.self.href
            await fetch(url, { method: 'DELETE' })
            await this.loadData()
        },
    },
});

let vm = chapp.mount("#chat");