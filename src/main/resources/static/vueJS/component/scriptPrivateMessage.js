const PrivateMessage = {
    template: `
        <div class="main">
            <div class="profilModifPicture">
                <div class="profile-picture">
                    <img :src="profilePicUser" alt="Photo de profil" />
                    <img :src="profilePicOther" alt="Photo de profil" />
                </div>
                <h2>{{ actualUser.username }} / {{ otherUser.username }}</h2>
                <textarea class="pmTextArea" v-model="newMessage" placeholder="Votre message ..."></textarea>
                <br><button id="submit_button" @click="addMessage">Envoyer</button>
            </div>
            
            <div class="box-private-message">
                <ul>
                    
                    <li v-for="message in listMessages">
                        <div class="messageReceiver">
                            <div class="information-pm">
                                <img v-if="message.sender == actualUser.username" class="avatar-pm" :src="profilePicUser" alt="Photo de profil" />
                                <img v-else class="avatar-pm" :src="profilePicOther" alt="Photo de profil" />
                                <span class="username-pm"><router-link :to="{ path: '/users/' + message.sender + '/profile'}">{{ message.sender }}</router-link></span> <br>
                                <span class="date-pm">{{ message.date }}</span>
                                <hr>
                            </div>
                            <div class="message-box-pm">
                                <span class="message-pm">{{ message.message }}</span>
                            </div>
                        </div>  
                    </li>
                </ul>
            </div>
            
        </div>
    `,

    data: () => ({
        newMessage: "",
        actualUser: "",
        otherUser: "",
        profilePicUser: "",
        profilePicOther: "",
        listMessages: [],
    }),


    mounted() {
        window.scrollTo(0, 0);
        this.loadUsers();
        setInterval(this.setToRead, 1000);
        
    },

    watch:{
        $route: {
            immediate: true,
            handler(to, from) {
                document.title = 'TSOR Corp. - Message privé';
            }
        },
    },

    methods: {
        async loadUsers() {
            // Load the actual user of the application
            let url = '/api/users/giveUser';
            let req = await fetch(url);
            this.actualUser = await req.json();

            // Load the other user that we want to dm
            let url2 = '/api/users/' + this.$route.params.other;
            let req2 = await fetch(url2);
            this.otherUser = await req2.json();

            // Load the profile picture for the users
            this.loadProfilePic();

            // Load the messages between the two users
            this.loadMessages();
            this.setToRead();
        },

        async setToRead(){
            let url = '/api/privateMessage/read/' + this.actualUser.username + '/' + this.otherUser.username;
            let res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
            });
            console.log("Read")
        },

        loadProfilePic: function () {
            this.profilePicUser = "/images/avatar/" + this.actualUser.profile_picture;
            this.profilePicOther = "/images/avatar/" + this.otherUser.profile_picture;
        },

        async loadMessages() {
            // Load all the message between the users, the list will be sorted
            let url = "/api/privateMessage/" + this.actualUser.username + "/" + this.otherUser.username;
            let req = await fetch(url);
            this.listMessages = await req.json();
        },

        async addMessage() {
            let mes = { sender: this.actualUser, receiver: this.otherUser, datePm: new Date(), message: this.newMessage };

            let url = '/api/privateMessage/addMessage';
            let res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(mes),
            });
            this.loadMessages();
            this.newMessage = "";
        }
    }
};

export { PrivateMessage }