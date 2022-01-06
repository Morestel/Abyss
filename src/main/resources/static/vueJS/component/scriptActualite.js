const Actualite = {
    template:
    `
    <div class="main">

        <h2>Actualité</h2>

        <table class="tableAct">
            <thead>
                <tr>
                    <th>Nom</th>
                    <th>Heures de début</th>
                    <th>Heures de fin</th>
                    <th>Lieu</th>
                </tr>
            </thead>
            <tbody>

                <tr v-for="item in listItem">
                    <td>{{ item.nameItem }}</td>
                    <td>{{ item.hourBeginItem }}</td>
                    <td>{{ item.hourEndItem }}</td>
                    <td>{{ item.roomItem }}</td>
                </tr>


            </tbody>
        </table>

    </div>
    `,

    data: () => ({
        listItem: [],
        actualUser:"",
    }),

    mounted(){
        this.loadUsers();
        // this.loadItems();
        var video = document.getElementById("videojs");
        video.src="../videos/actualityClock.mp4"
        window.scrollTo(0, 0);
    },

    watch:{
        $route: {
            immediate: true,
            handler(to, from) {
                document.title = 'TSOR Corp. - Actualité';
            }
        },
    },

    methods:{
        async loadItems(){
            let url = '/api/items/' + this.actualUser.username;
            let req = await fetch(url);
            this.listItem = await req.json();
        },

        async loadUsers(){
            // Load the actual user of the application
            let url = '/api/users/giveUser';
            let req = await fetch(url);
            this.actualUser = await req.json();

            this.loadItems();
        },
    }

}
export { Actualite }