const ListPrivateMessages = {
    template: 
    `
    <div class="main">
        <h1>Votre messagerie :</h1>
        <table id="tableMsgPriver">
            <thead>
                <tr>
                    <th colspan="3"></th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="conv in listConversation">
                    <span style="display:block;" v-if="conv.receiver == actualUser.username">
                        <td>
                            <img class = "imgProfilMsg" :src="'/images/avatar/' + conv.sender_picture " alt="Photo de profil"/>
                        </td>
                        <td>
                            <router-link :to="{ path: '/pm/' + conv.sender }">
                                <p class="msgPrivU">{{ conv.sender }}</p>
                            </router-link>
                        </td>
                        <td class="msgPriv3">
                            <router-link :to="{ path: '/pm/' + conv.sender }">
                                <p class="msgPrivM">{{ conv.message }}</p>
                            </router-link>
                        </td>
                    </span>

                    <span v-if="conv.receiver != actualUser.username">
                        <td>
                            <img class = "imgProfilMsg" :src="'/images/avatar/' + conv.receiver_picture " alt="Photo de profil" />
                        </td>

                        <td>
                            <router-link :to="{ path: '/pm/' + conv.receiver }">
                            <p class="msgPrivU">{{ conv.receiver }}</p>
                            </router-link> 
                        </td>

                        <td class="msgPriv3">
                            <router-link :to="{ path: '/pm/' + conv.receiver }">
                            <p class="msgPrivM">{{ conv.message }}</p>
                            </router-link> 
                        </td>
                    </span>
                </tr>
            </tbody>
        </table>
    </div>
    `,

    data: ()=>({
        actualUser: "",
        listConversation: [],
    }),

    mounted(){
        this.loadUser();
        var video = document.getElementById("videojs");
        video.src="../videos/msg.mp4";
        window.scrollTo(0, 0);
    },

    watch:{
        $route: {
            immediate: true,
            handler(to, from) {
                document.title = 'TSOR Corp. - Conversation';
            }
        },
    },

    methods:{
        async loadUser(){
            // Load the actual user of the application
            let url = '/api/users/giveUser';
            let req = await fetch(url);
            this.actualUser = await req.json();
            this.loadConv();
        },

        async loadConv(){
            let url = '/api/privateMessage/getConversation/' + this.actualUser.username;
            let req = await fetch(url);
            this.listConversation = await req.json();
        },
    },
};

export { ListPrivateMessages }