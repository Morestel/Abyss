const Administration = {
    template: `
        <div class="main">
            <h2>Liste d'utilisateurs</h2>
            <table class="adminTable">
                <thead>
                    <tr>
                        <th colspan="3">Pseudo</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="user in listUser">
                        <td style="width:80%;text-align:center;">
                            <span>{{ user.username }}</span> 
                        </td>

                        <td style="width:10%" v-if="isAdmin(actualUser)">
                            <button @click="deleteUser(user)">Supprimer</button> 
                        </td>

                        <td style="width:10%">
                            <router-link :to="{ path: '/users/' + user.username + '/profile'}"><button>Profil</button></router-link>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    `,

    data: () => ({
        listUser: [],
        actualUser: "",
        confirmDelete: "",
    }),

    mounted() {
        this.loadAllUsers();
        this.loadActualUser();
        var video = document.getElementById("videojs");
        video.src="../videos/admin.mp4"
        window.scrollTo(0, 0);
    },

    watch:{
        $route: {
            immediate: true,
            handler(to, from) {
                document.title = 'TSOR Corp. - Liste utilisateurs';
            }
        },
    },

    methods: {
        async loadAllUsers() {
            // Use to load all the user in a list - Useful to know if the user you want to add exist or not
            let url = '/api/users';
            let req = await fetch(url);
            this.listUser = await req.json();
        },

        async deleteUser(u) {
            var nom = prompt("Entrez le nom de la personne que vous vouliez suprimer :", "");
            if (nom === u.username) {
                let url = '/api/users/delete/' + u.username;
                await fetch(url, {
                    method: 'DELETE',
                });
                await this.loadAllUsers();
            } else {
                alert("vous avez fait une erreur de nom entrer supression annuler");
            }
        },
        async loadActualUser() {
            let url = '/api/users/giveUser';
            let req = await fetch(url);
            this.actualUser = await req.json();
            this.isAdmin(this.actualUser);
        },

        isAdmin(user) {
            // Depending of the roles of the user he can delete some message or do other things that basic user can't do
            for (let i in user.role) {
                if (user.role[i] == 'ADMIN') {
                    return true;
                }
            }

            return false;
        },
    }
};

export { Administration }