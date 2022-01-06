const UserProfile = {
    template: `
    <div class="main">
    <div class="profilModifPicture">
        <div class="profile-picture">
            <img :src="profilePic" alt="Photo de profil" />
        </div>
        <div class="modif-profile">
            <div v-if="actualUser.username != profileUser.username"><router-link :to="{ path: '/pm/' + profileUser.username}"><button>Envoyer un message</button></router-link></div>
        </div>
    </div>
    <div class="tableHome">
        <table class="tableUser">
            <thead>
                <tr>
                    <th colspan="2">Profil</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Nom</td>
                    <td>{{ profileUser.username }}</td>
                </tr>
                <tr>
                    <td>Email</td>
                    <td>{{ profileUser.email }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
    `,

    data: () => ({
        profileUser: "",
        actualUser: "",
        profilePic: "",
    }),

    

    mounted(){
        this.loadUserProfile();
        this.loadActualUser();
        var video = document.getElementById("videojs");
        video.src="../videos/clock.mp4";
        window.scrollTo(0, 0);
    },

    watch:{
        $route: {
            immediate: true,
            handler(to, from) {
                document.title = 'TSOR Corp. - Profil ';
            }
        },
    },

    methods: {
        async loadActualUser() {
            let url = '/api/users/giveUser';
            let req = await fetch(url);
            this.actualUser = await req.json();
        },

        async loadUserProfile(){
            let url = '/api/users/' + this.$route.params.username;
            let req = await fetch(url);
            this.profileUser = await req.json();
            console.log(this.profileUser);
            this.loadProfilePic(this.profileUser);
        },

        loadProfilePic: function(user){
            this.profilePic = "/images/avatar/" + this.profileUser.profile_picture;
        }
    }
};

export { UserProfile }