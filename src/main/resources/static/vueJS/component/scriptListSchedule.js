const ListSchedules = {
    template: `
    <div class="main">
        <h2>Mes agenda :</h2>
        <div class="sendNewSchedule">
                <input type="text" v-model="nameSchedule" name="nameSchedule" placeholder="Nom agenda à ajouter" />
                <input type="submit" name="submit" @click.prevent="addSchedule" style="width: 100px;" value="AJOUTER" />
        </div>
        <div class="tableHome">
            <table class="tableUser">
                <thead>
                    <tr>
                        <th>Agenda admin</th>
                    </tr>
                </thead>
                <tbody>
                <tr v-for="sc in schedules">
                    <td v-if="isOwner(sc)">
                        <router-link :to="{ path: '/schedules/' + sc.id }">{{ sc.nameSchedule }}</router-link> <a @click="deleteSchedule(sc)"><img src="/images/delete.png" alt="Delete" style="height: 20px;"/></a>
                    </td>
                </tr>
                </tbody>
            </table>
            <table class="tableUser">
                <thead>
                    <tr>
                        <th>Agenda invité</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="sc in schedules">
                        <td v-if="!isOwner(sc)">
                        <router-link :to="{ path: '/schedules/' + sc.id }">{{ sc.nameSchedule }}</router-link>
                        </td>
                    </tr>
                </tbody>
            </table>

        </div>
    </div>
    
    
    `,

    data: () =>({
        schedules: [ ],
        nameSchedule: '',
        actualUser: '',
    }),
    mounted(){
        this.loadUser();
        var video = document.getElementById("videojs");
        video.src="../videos/clock.mp4";
        window.scrollTo(0, 0);
    },

    watch:{
        $route: {
            immediate: true,
            handler(to, from) {
                document.title = 'TSOR Corp. - Home';
            }
        },
    },
    
    methods: {
        loadSchedules: async function(){
            let url = '/api/users/' + this.actualUser.username + '/schedule';
            let req = await fetch(url);
            this.schedules = await req.json();
        },

        async loadUser(){
            // Load the actual user of the application
            let url = '/api/users/giveUser';
            let req = await fetch(url);
            this.actualUser = await req.json();
            this.loadSchedules();
        },

        isOwner: function(schedule){
            return (schedule.owner == this.actualUser.username)
        },

        async deleteSchedule(schedule){
            let url = '/api/schedule/delete/' + schedule.id;
            await fetch(url, { method: 'DELETE' })
            await this.loadSchedules();
        },

        addSchedule: async function(){
            if (this.nameSchedule != ""){
                let newSchedule = {nameSchedule : this.nameSchedule, owner: this.actualUser.username};
        
                let url = '/api/schedules';
                let res = await fetch(url, {
                    method: 'POST',
                    headers: {'Content-Type' : 'application/json'},
                    body: JSON.stringify(newSchedule),
                });
                // Reload the list either you can't have access to the id of the new schedule
                this.loadSchedules();   
                this.nameSchedule = "";
            }
            else{
                alert('Entré un nom de schedule');
            }
            
        }
    }
};

export { ListSchedules }