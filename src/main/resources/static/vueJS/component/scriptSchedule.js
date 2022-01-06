const Schedules = {
    template: `
    <div class="main" id="mainSchedule">
        <h2>{{ schedule.nameSchedule }}</h2>
        <div class="blockListUser">
            <div class="blockListUserEffect">
            <ul class="listUser">    
                <li v-for="usr in listUser">
                    {{ usr.username }}
                    <span v-if="isOwner(user) && usr.username != user.username">
                        <button @click="kickUser(usr)">Exclure</button>
                    </span>
                </li>
            </ul>
            <div v-if="isAdmin(user) || isOwner(user)">
                <input type="text" v-model="nameUser" placeholder="Utilisateur à ajouter" />
                <input type="submit" name="submit" @click="addUser" value="AJOUTER" />
            </div>
        </div>
      </div>
      <div class="blockSchedule">
            <button id="buttonWorkDay" @click="switchWorkDay()">Semaine de travail</button>
            <svg id="svgBlock" width="100%" height="1500px">
                    <foreignObject v-for="j in jours" :key="j.t" v-bind:x="j.x" v-bind:y="j.y" v-bind:width="j.w" v-bind:height="j.h" class="jour">
                        <div>{{j.t}}</div>
                    </foreignObject>

                    <foreignObject v-for="h in heures" :key="h.t" v-bind:x="h.x" v-bind:y="h.y" v-bind:width="h.w" v-bind:height="h.h" class="heure">
                        <div>{{h.t}}</div>
                    </foreignObject>
 
                    <foreignObject v-if="workDayBool=='0'" v-for="item in currentItem.slice()" :x="svgX(item)" :y="svgY(item)" width="12.5%" :height="svgHeight(item)" class="itemss" @mouseup="moveItemUp(item);moveItem($event, item);modifyItem(item)" @mousedown="moveItemDown(item);moveItem($event, item)" @mousemove="moveItem($event, item)" @mouseout="moveItem($event, item);moveItemUp(item)">
                        <div><p style="display:none">{{item.id}}</p><b>{{item.nameItem}}</b><br><br>{{item.roomItem}}<br><br><p class="moreInfoItem">{{item.hourBeginItem}} à {{item.hourEndItem}}<br>{{item.descriptionItem}}</p></div>
                    </foreignObject>

                    <foreignObject v-if="workDayBool=='1'" v-for="item in currentItem.slice()" :x="svgX(item)" :y="svgY(item)-480" width="16.666666667%" :height="svgHeight(item)" class="itemss" @mouseup="moveItemUp(item);moveItem($event, item);modifyItem(item)" @mousedown="moveItemDown(item);moveItem($event, item)" @mousemove="moveItem($event, item)" @mouseout="moveItem($event, item);moveItemUp(item)">
                        <div><p style="display:none">{{item.id}}</p><b>{{item.nameItem}}</b><br><br>{{item.roomItem}}<br><br><p class="moreInfoItem">{{item.hourBeginItem}} à {{item.hourEndItem}}<br>{{item.descriptionItem}}</p></div>
                    </foreignObject>
            </svg>
            <ul class = "buttonSchedule">
                <li v-for="n in nbButton">
                    <button class="buttonScheduleOne" @load="isActif()" @click="changeBounds($event);actifDeletButton($event);">{{createButtons(n)}}</button>
                </li>
            </ul>
        </div>
        <div class="addItem" v-if="isAdmin(user) || isOwner(user)">
            <div class="addItemEffect">
                <h3>Ajouter un element a l'emploi du temps :</h3>
                <div class="scheduleBox">
                    <input type="text" v-model="nameItem" name="nameItem" placeholder="Nom de l'item" required />
                    <input type="date" v-model="day" name="day" placeholder="Jour" required />
                    <input type="time" v-model="hourBeginItem" name="hourBeginItem" required />
                    <input type="time" v-model="hourEndItem" name="hourEndItem" required />
                    <input type="text" v-model="placeItem" name="placeItem" placeholder="placeItem" required />
                    <input type="text" v-model="descriptionItem" name="descriptionItem" placeholder="descriptionItem" required />
                    <input type="submit" name="submit" @click="addItem" value="AJOUTER" />
                </div>
            </div>
        </div>
        
        <div id="chat">
            <table>
            <thead>
                <tr>
                <th v-on:click="openMessenger">
                    <svg v-show="messageBool=='0'" width="100px" height="90px">
                    <line
                        x1="20"
                        y1="40"
                        x2="80"
                        y2="40"
                        stroke="red"
                        stroke-width="5"
                    />
                    <line
                        x1="20"
                        y1="60"
                        x2="80"
                        y2="60"
                        stroke="red"
                        stroke-width="5"
                    />
                    </svg>
                    <svg v-show="messageBool=='1'" width="100px" height="90px">
                    <line
                        x1="30"
                        y1="30"
                        x2="70"
                        y2="70"
                        stroke="red"
                        stroke-width="5"
                    />
                    <line
                        x1="70"
                        y1="30"
                        x2="30"
                        y2="70"
                        stroke="red"
                        stroke-width="5"
                    />
                    </svg>
                </th>

                <th colspan="2">CHAT</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                <td colspan="3" style="text-align: center">
                    <input
                    type="text"
                    v-model="message"
                    placeholder="Ajouter un message"
                    />
                    <input type="submit" value="ENVOYER" @click.prevent="addMessage" />
                </td>
                </tr>
                <tr v-for="m in messages">
                <td style="text-align: center"><router-link :to="{ path: '/users/' + user.username + '/profile'}">{{ m.sender }}</router-link></td>
                <td>{{m.message}}</td>
                <td style="text-align: center">
                    <span v-if="isAdmin(user) || isOwner(user)" ><button @click="supprimer(m)" name="button">Supprimer</button></span>
                </td>
                </tr>
            </tbody>
            </table>
      </div>
    </div>
    `,

    data: () => ({
        //MESSAGE
        message: '',
        messagesTemp: [],
        messages: [],
        messageBool: 0,
        messageAdd: "",
        //SCHEDULE
        schedule: "",
        jours: [],
        heures: [],
        workDayBool: 0,
        appuiyer: 0,
        currentItem: [],
        listItem: [],
        lowerBound: "",
        upperBound: "",
        borneTemp: new Date(),
        nbButton: 20,
        // ITEM
        nameItem: "",
        day: "",
        hourBeginItem: "",
        hourEndItem: "",
        placeItem: "",
        descriptionItem: "",

        previousItem: "",
        dayTemp: "",
        hourBeginItemTemp: "",
        hourEndItemTemp: "",
        // USER
        nameUser: "",
        targetUser: Object,
        listUser: [],
        listAllUser: [],
        user: "",
        userAdmin: false, // Indicates if the user is admin or not, avoid to call the function isAdmin each time
    }),

    created() {
        this.loadAllUser();
        this.loadActualUser();
        this.loadUser();
    },

    mounted() {
        this.getMonday(new Date());
        this.getSunday(new Date());
        this.loadSchedule();
        // this.loadUser();
        this.loadItems();
        this.loadChat();
        var video = document.getElementById("videojs");
        video.src="../videos/Knight.mp4";
        window.scrollTo(0, 0);
    },

    watch:{
        $route: {
            immediate: true,
            handler(to, from) {
                document.title = 'TSOR Corp. - Agenda';
            }
        },
    },

    methods: {
        openMessenger: function () {
            if (this.messageBool == 0) {
                document.getElementById('chat').classList.add("open");
                this.messageBool = 1;
            } else {
                document.getElementById('chat').classList.remove("open");
                this.messageBool = 0;
            }
        },


        async loadChat() {
            let url = '/api/schedules/'  + this.$route.params.id + '/messages/';
            let req = await fetch(url);
            this.messages = await req.json();
        },

        addMessage: async function () {           
            let newMess = { sender: this.user.username, message: this.message, schedule_id: this.$route.params.id };        
            let url = '/api/messages/add/' + this.$route.params.id;
            let res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newMess),
            });
            this.message = "";
            this.loadChat();
            
        },
        async supprimer(m) {
            let url = '/api/messages/delete/' + m.id;
            await fetch(url, { method: 'DELETE' })
            await this.loadChat()
        },


        async loadItems() {
            this.workDay();
            // let url = '/api/items'
            let url = '/api/schedules/' + this.$route.params.id + '/items';
            let req = await fetch(url);
            this.listItem = await req.json();
            // We initialize it to have a display when we arrive on this page
            this.currentItem.length = 0;
            this.listItem.forEach(element => this.check(element));
        },

        async loadUser() {
            // Load all the user who are in this schedule
            let url = '/api/schedules/' + this.$route.params.id + '/users';
            let req = await fetch(url);
            this.listUser = await req.json();
        },

        async loadActualUser() {
            let url = '/api/users/giveUser';
            let req = await fetch(url);
            this.user = await req.json();
        },

        async loadAllUser() {
            // Use to load all the user in a list - Useful to know if the user you want to add exist or not
            let url = '/api/users';
            let req = await fetch(url);
            this.listAllUser = await req.json();
        },

        async loadSchedule(){
            // Load the actual schedule
            let url = '/api/schedule/' + this.$route.params.id;
            let req = await fetch(url);
            this.schedule = await req.json();
        },

        isAdmin(user){
            // Depending of the roles of the user he can delete some message or do other things that basic user can't do
            for (let i in user.role){
                if (user.role[i] == 'ADMIN'){
                    this.userAdmin = true;
                    return true;
                }
            }
            return false;
        },

        isOwner(user){
            return (this.schedule.owner == user.username)
        },

        /*listener up dand down mouse modify variable appuiyer */
        moveItemDown: function (item) {
            if (!this.isOwner(this.user)){
                return
            }
            this.appuiyer = 1;
            this.previousItem = item;
            // Case if we are sunday
            if (this.dayTemp == 0) {
                let d = new Date(this.previousItem.day);
                this.changeDay(d.getDay());
            }
        },
        moveItemUp: function (item) {
            if (!this.isOwner(this.user)){
                return
            }
            this.appuiyer = 0;
        },

        changeDay(day) {
            this.dayTemp = day;
        },

        async modifyItem(item) {
            if (!this.isOwner(this.user)){
                return
            }
            let date = new Date(this.previousItem.day);
            let diff;
            // Same day, nothing have to change
            if (this.dayTemp == date.getDay()) {
                diff = date.getDate();
            } // Case if the item was a sunday
            else if (date.getDay() == 0) {
                diff = date.getDate() - (7 - this.dayTemp);
            }
            // Case if we are sunday
            else if (this.dayTemp === 0) {
                diff = date.getDate() + (7 - date.getDay());
            }// We move forward the item, we add 
            else if (this.dayTemp > date.getDay()) {
                diff = date.getDate() + this.dayTemp - date.getDay();
            }
            // We move back the item, we substract
            else if (this.dayTemp < date.getDay()) {
                diff = date.getDate() - (date.getDay() - this.dayTemp);
            }
            let newDay = new Date(date.setDate(diff));
            let updateItem = { id: item.id, nameItem: item.nameItem, day: newDay, hourBeginItem: this.hourBeginItemTemp, hourEndItem: this.hourEndItemTemp, roomItem: this.previousItem.roomItem, descriptionItem: this.descriptionItem, idSchedule: this.id }
            let url = '/api/items/' + this.$route.params.id + '/modify';
            let res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(updateItem),
            });

            this.loadItems();

        },

        moveItem: function (event, item) {
            if (!this.isOwner(this.user)){
                return
            }
            if (event.target.parentElement.x != undefined) {
                if (this.appuiyer) {
                    event.target.parentElement.x.baseVal.value += event.movementX;
                    if (this.workDayBool) {
                        if (event.target.parentElement.y.baseVal.value > 670) {
                            event.target.parentElement.y.baseVal.value = 670;
                        } else {
                            if (event.target.parentElement.y.baseVal.value < 60) {
                                event.target.parentElement.y.baseVal.value = 60;
                            }
                            else {
                                event.target.parentElement.y.baseVal.value += event.movementY;
                            }
                        }
                    } else {
                        if (event.target.parentElement.y.baseVal.value > 1450) {
                            event.target.parentElement.y.baseVal.value = 1450;
                        } else {
                            if (event.target.parentElement.y.baseVal.value < 60) {
                                event.target.parentElement.y.baseVal.value = 60;
                            }
                            else {
                                event.target.parentElement.y.baseVal.value += event.movementY;
                            }
                        }
                    }
                } else {
                    let valuOfMouv = event.target.parentElement.x.baseVal.value;
                    let svgWidth = document.getElementById("svgBlock").width.baseVal.value;
                    if (this.workDayBool) {
                        let hourBegin = item.hourBeginItem.slice(0, 2);
                        let minutesBegin = item.hourBeginItem.slice(3, 5);
                        let hourEnd = item.hourEndItem.slice(0, 2);
                        let minutesEnd = item.hourEndItem.slice(3, 5);
                        let timeHourBegin = hourBegin * 60 + minutesBegin * 1;
                        let timeHourEnd = hourEnd * 60 + minutesEnd * 1;
                        let timeOfItem = "" + timeHourEnd - timeHourBegin;
                        /*recover beggining of items with mouse position*/
                        let deb = event.srcElement.parentElement.y.baseVal.value;
                        /*calculs end of items*/
                        let end = ((deb) + timeOfItem);
                        /*recover beggining of items write correctly 00:00*/
                        let h = 0;
                        let m = 0;
                        let res = 0;
                        h = (deb + 480) / 60 - 1;
                        m = (((h - Math.floor(h)) / 100) * 60).toFixed(2);
                        h = Math.floor(h)
                        if (h < 10) {
                            res = "0" + h + ":" + (m.substr(2, 3));
                        } else {
                            res = h + ":" + (m.substr(2, 3));
                        }
                        /*replace beggining of items write correctly 00:00*/
                        this.hourBeginItemTemp = res;
                        /*recover end of items write correctly 00:00*/
                        h = 0;
                        m = 0;
                        res = 0;
                        h = (end + 480) / 60 - 1;
                        m = (((h - Math.floor(h)) / 100) * 60).toFixed(2);
                        h = Math.floor(h)
                        if (h < 10) {
                            res = "0" + h + ":" + (m.substr(2, 3));
                        } else {
                            res = h + ":" + (m.substr(2, 3));
                        }
                        /*replace end of items write correctly 00:00*/
                        this.hourEndItemTemp = res;
                        /*x16 it's the width of one day in shcedule, in this part we check if we are on the right day for work day*/
                        let x16 = (svgWidth / 100) * 16.666666667;
                        if (valuOfMouv < x16 * 2) {
                            this.changeDay(1);
                        } else {
                            if (valuOfMouv > x16 * 2 && valuOfMouv < x16 * 3) {
                                this.changeDay(2);
                            } else {
                                if (valuOfMouv > x16 * 3 && valuOfMouv < x16 * 4) {
                                    this.changeDay(3);
                                } else {
                                    if (valuOfMouv > x16 * 4 && valuOfMouv < x16 * 5) {
                                        this.changeDay(4);
                                    } else {
                                        if (valuOfMouv > x16 * 5) {
                                            this.changeDay(5);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        let hourBegin = item.hourBeginItem.slice(0, 2);
                        let minutesBegin = item.hourBeginItem.slice(3, 5);
                        let hourEnd = item.hourEndItem.slice(0, 2);
                        let minutesEnd = item.hourEndItem.slice(3, 5);
                        let timeHourBegin = hourBegin * 60 + minutesBegin * 1;
                        let timeHourEnd = hourEnd * 60 + minutesEnd * 1;
                        let timeOfItem = "" + timeHourEnd - timeHourBegin;
                        /*recover beggining of items with mouse position*/
                        let deb = event.srcElement.parentElement.y.baseVal.value;
                        /*calculs end of items*/
                        let end = ((deb) + timeOfItem);
                        /*recover beggining of items write correctly 00:00*/
                        let h = 0;
                        let m = 0;
                        let res = 0;
                        h = deb / 60 - 1;
                        m = (((h - Math.floor(h)) / 100) * 60).toFixed(2);
                        h = Math.floor(h)
                        if (h < 10) {
                            res = "0" + h + ":" + (m.substr(2, 3));
                        } else {
                            res = h + ":" + (m.substr(2, 3));
                        }
                        /*replace beggining of items write correctly 00:00*/
                        this.hourBeginItemTemp = res;
                        /*recover end of items write correctly 00:00*/
                        h = 0;
                        m = 0;
                        res = 0;
                        h = end / 60 - 1;
                        m = (((h - Math.floor(h)) / 100) * 60).toFixed(2);
                        h = Math.floor(h)
                        if (h < 10) {
                            res = "0" + h + ":" + (m.substr(2, 3));
                        } else {
                            res = h + ":" + (m.substr(2, 3));
                        }
                        /*replace end of items write correctly 00:00*/
                        this.hourEndItemTemp = res;
                        /*x12 it's the width of one day in shcedule, in this part we check if we are on the right day for normal day*/
                        let x12 = (svgWidth / 100) * 12.5;
                        if (valuOfMouv < x12) {
                            this.changeDay(1);
                        } else {
                            if (valuOfMouv > x12 && valuOfMouv < x12 * 2) {
                                this.changeDay(2);

                            } else {
                                if (valuOfMouv > x12 * 2 && valuOfMouv < x12 * 3) {
                                    this.changeDay(3);

                                } else {
                                    if (valuOfMouv > x12 * 3 && valuOfMouv < x12 * 4) {
                                        this.changeDay(4);

                                    } else {
                                        if (valuOfMouv > x12 * 4 && valuOfMouv < x12 * 5) {
                                            this.changeDay(5);

                                        } else {
                                            if (valuOfMouv > x12 * 5 && valuOfMouv < x12 * 6) {
                                                this.changeDay(6);

                                            } else {
                                                if (valuOfMouv > x12 * 6) {
                                                    this.changeDay(0);

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        switchWorkDay: function () {
            var svgBlock = document.getElementById("svgBlock");
            var buttonWorkDay = document.getElementById("buttonWorkDay");
            if (this.workDayBool) {
                this.workDayBool = 0;
                svgBlock.style.backgroundSize = "12.5% 60px";
                buttonWorkDay.innerText = "Semaine de travail";
            } else {
                this.workDayBool = 1;
                svgBlock.style.backgroundSize = "16.666666667% 60px";
                buttonWorkDay.innerText = "Semaine normal";
            }
            this.workDay();
        },
        workDay: function () {
            var svgBlock = document.getElementById("svgBlock");
            if (this.workDayBool) {
                svgBlock.style.height = "660px";
                this.heures = [
                    { t: "08:00", x: "0", y: "60", w: "16.666666667%", h: "60px" },
                    { t: "09:00", x: "0", y: "120", w: "16.666666667%", h: "60px" },
                    { t: "10:00", x: "0", y: "180", w: "16.666666667%", h: "60px" },
                    { t: "11:00", x: "0", y: "240", w: "16.666666667%", h: "60px" },
                    { t: "12:00", x: "0", y: "300", w: "16.666666667%", h: "60px" },
                    { t: "13:00", x: "0", y: "360", w: "16.666666667%", h: "60px" },
                    { t: "14:00", x: "0", y: "420", w: "16.666666667%", h: "60px" },
                    { t: "15:00", x: "0", y: "480", w: "16.666666667%", h: "60px" },
                    { t: "16:00", x: "0", y: "540", w: "16.666666667%", h: "60px" },
                    { t: "17:00", x: "0", y: "600", w: "16.666666667%", h: "60px" },
                    { t: "18:00", x: "0", y: "660", w: "16.666666667%", h: "60px" },
                ];
                this.jours = [
                    { t: "", x: "0", y: "0", w: "16.666666667%", h: "60px" },
                    { t: "lundi", x: "16.666666667%", y: "0", w: "16.666666667%", h: "60px" },
                    { t: "mardi", x: "33.333333334%", y: "0", w: "16.666666667%", h: "60px" },
                    { t: "mercredi", x: "50.000000001%", y: "0", w: "16.666666667%", h: "60px" },
                    { t: "jeudi", x: "66.666666668%", y: "0", w: "16.666666667%", h: "60px" },
                    { t: "vendredi", x: "83.333333335%", y: "0", w: "16.666666667%", h: "60px" },
                ]
            } else {
                svgBlock.style.height = "1500px";
                this.heures = [
                    { t: "00:00", x: "0", y: "60", w: "12.5%", h: "60px" },
                    { t: "01:00", x: "0", y: "120", w: "12.5%", h: "60px" },
                    { t: "02:00", x: "0", y: "180", w: "12.5%", h: "60px" },
                    { t: "03:00", x: "0", y: "240", w: "12.5%", h: "60px" },
                    { t: "04:00", x: "0", y: "300", w: "12.5%", h: "60px" },
                    { t: "05:00", x: "0", y: "360", w: "12.5%", h: "60px" },
                    { t: "06:00", x: "0", y: "420", w: "12.5%", h: "60px" },
                    { t: "07:00", x: "0", y: "480", w: "12.5%", h: "60px" },
                    { t: "08:00", x: "0", y: "540", w: "12.5%", h: "60px" },
                    { t: "09:00", x: "0", y: "600", w: "12.5%", h: "60px" },
                    { t: "10:00", x: "0", y: "660", w: "12.5%", h: "60px" },
                    { t: "11:00", x: "0", y: "720", w: "12.5%", h: "60px" },
                    { t: "12:00", x: "0", y: "780", w: "12.5%", h: "60px" },
                    { t: "13:00", x: "0", y: "840", w: "12.5%", h: "60px" },
                    { t: "14:00", x: "0", y: "900", w: "12.5%", h: "60px" },
                    { t: "15:00", x: "0", y: "960", w: "12.5%", h: "60px" },
                    { t: "16:00", x: "0", y: "1020", w: "12.5%", h: "60px" },
                    { t: "17:00", x: "0", y: "1080", w: "12.5%", h: "60px" },
                    { t: "18:00", x: "0", y: "1140", w: "12.5%", h: "60px" },
                    { t: "19:00", x: "0", y: "1200", w: "12.5%", h: "60px" },
                    { t: "20:00", x: "0", y: "1260", w: "12.5%", h: "60px" },
                    { t: "21:00", x: "0", y: "1320", w: "12.5%", h: "60px" },
                    { t: "22:00", x: "0", y: "1380", w: "12.5%", h: "60px" },
                    { t: "23:00", x: "0", y: "1440", w: "12.5%", h: "60px" },
                ]
                this.jours = [
                    { t: "", x: "0", y: "0", w: "12.5%", h: "60px" },
                    { t: "lundi", x: "12.5%", y: "0", w: "12.5%", h: "60px" },
                    { t: "mardi", x: "25%", y: "0", w: "12.5%", h: "60px" },
                    { t: "mercredi", x: "37.5%", y: "0", w: "12.5%", h: "60px" },
                    { t: "jeudi", x: "50%", y: "0", w: "12.5%", h: "60px" },
                    { t: "vendredi", x: "62.5%", y: "0", w: "12.5%", h: "60px" },
                    { t: "samedi", x: "75%", y: "0", w: "12.5%", h: "60px" },
                    { t: "dimanche", x: "87.5%", y: "0", w: "12.5%", h: "60px" },
                ]
            }
        },
        getMonday: function (date) {
            date = new Date(date);
            var day = date.getDay();
            var diff = date.getDate() - day + (day == 0 ? -6 : 1);
            this.lowerBound = new Date(date.setDate(diff));
        },

        giveMonday: function (date) {
            date = new Date(date);
            var day = date.getDay();
            var diff = date.getDate() - day + (day == 0 ? -6 : 1);
            return new Date(date.setDate(diff));
        },

        getSunday: function (date) {
            date = new Date(date);
            var day = date.getDay();
            var diff = date.getDate() + day + (day == 0 ? -6 : 1);
            this.upperBound = new Date(date.setDate(diff));
        },

        changeBounds: function (event) {
            var currentDate = new Date(event.target.innerHTML);
            var dateUpp = new Date(event.target.innerHTML);
            // We start by the lower bound
            this.lowerBound = currentDate;
            // Now we do the same for the upper bound
            var diff = currentDate.getDate() + 6;
            dateUpp.setDate(diff);
            this.upperBound = dateUpp;
            // Before we make the list empty
            this.currentItem.length = 0;
            this.listItem.forEach(element => this.check(element));

        },
        check: function (element) {
            var monday = this.giveMonday(element.day);
            if (this.lowerBound.getFullYear() == monday.getFullYear()
                && this.lowerBound.getMonth() == monday.getMonth()
                && this.lowerBound.getDate() == monday.getDate()) {
                // We have the same monday, it means that we are in the same week
                this.currentItem.push(element);
            }
        },
        actifDeletButton: function (event) {
            var button = document.querySelectorAll(".buttonScheduleOne");
            button.forEach(element => this.delet(element));
            event.target.classList.add('actif');
        },

        delet: function (e) {
            e.classList.remove('actif');
        },

        initializeActiveButton: function () {

            var button = document.querySelectorAll(".buttonScheduleOne");
            button.forEach(element => this.isActive(element));
        },

        isActive: function (element) {
            let dateElement = new Date(element.innerHTML);
            if (dateElement.getFullYear() == this.lowerBound.getFullYear()
                && dateElement.getMonth() == this.lowerBound.getMonth()
                && dateElement.getDate() == this.lowerBound.getDate()) {

                element.classList.add('actif');
            }

        },
        createButtons: function (n) {

            if (n == 1) { // We back 4 week ago to start the display
                var date = new Date();
                var day = date.getDay();
                var diff = date.getDate() - (4 * 7) - day + (day == 0 ? -6 : 1);
                this.borneTemp = new Date(date.setDate(diff));
            }
            else {
                // Now we move forward week by week
                // BorneTemp is already a monday so we don't have to care about the day of the week
                var forward = this.borneTemp.getDate() + 7;
                this.borneTemp.setDate(forward);
            }
            var str = "" + this.borneTemp;
            return this.formatDate(str);
        },

        formatDate: function (date) {
            date = "" + date;
            var month = date.slice(4, 7);
            var monthNumber;
            switch (month) {
                case "Jan":
                    monthNumber = 1;
                    month = "January";
                    break;
                case "Feb":
                    monthNumber = 2;
                    month = "February";
                    break;
                case "Mar":
                    monthNumber = 3;
                    month = "March";
                    break;
                case "Apr":
                    monthNumber = 4;
                    month = "April";
                    break;
                case "May":
                    monthNumber = 5;
                    month = "May";
                    break;
                case "Jun":
                    monthNumber = 6;
                    month = "June";
                    break;
                case "Jul":
                    monthNumber = 7;
                    month = "July";
                    break;
                case "Aug":
                    monthNumber = 8;
                    month = "August";
                    break;
                case "Sep":
                    monthNumber = 9;
                    month = "September";
                    break;
                case "Oct":
                    monthNumber = 10;
                    month = "October";
                    break;
                case "Nov":
                    monthNumber = 11;
                    month = "November";
                    break;
                case "Dec":
                    monthNumber = 12;
                    month = "December";
                    break;
                default:
                    break;
            }
            var day = date.slice(8, 10);
            var year = date.slice(11, 15);
            return month + " " + day + "," + year;
        },

        svgX: function (item) { // In function of the day of the week
            var date = new Date(item.day);
            if (this.workDayBool) {
                switch (date.getDay()) {
                    case 1: // Monday
                        return "16.666666667%";
                    case 2: // Tuesday
                        return "33.333333334%";
                    case 3: // Wednesday
                        return "50.000000001%";
                    case 4: // Thursday
                        return "66.666666668%";
                    case 5: // Friday
                        return "83.333333335%";
                    default:
                        return "3500px"
                }
            } else {
                switch (date.getDay()) {
                    case 0: // Sunday
                        return "87.5%";
                    case 1: // Monday
                        return "12.5%";
                    case 2: // Tuesday
                        return "25%";
                    case 3: // Wednesday
                        return "37.5%";
                    case 4: // Thursday
                        return "50%";
                    case 5: // Friday
                        return "62.5%";
                    case 6: // Saturday
                        return "75%";
                }
            }
        },

        svgY: function (item) { // In function of the start of the item
            let hour = item.hourBeginItem.slice(0, 2);
            let minutes = item.hourBeginItem.slice(3, 5);
            return "" + (60 + (hour * 60) + minutes * 1);
        },

        svgHeight: function (item) {
            let hourBegin = item.hourBeginItem.slice(0, 2);
            let minutesBegin = item.hourBeginItem.slice(3, 5);
            let hourEnd = item.hourEndItem.slice(0, 2);
            let minutesEnd = item.hourEndItem.slice(3, 5);
            // Starting by convert hour in minutes then we add the minutes
            let timeHourBegin = hourBegin * 60 + minutesBegin * 1; // 600 
            let timeHourEnd = hourEnd * 60 + minutesEnd * 1;  // 720
            // Then we do the difference between them 
            return "" + timeHourEnd - timeHourBegin;
        },

        addItem: async function () {
    
            let newItem = { nameItem: this.nameItem, day: this.day, hourBeginItem: this.hourBeginItem, hourEndItem: this.hourEndItem, roomItem: this.placeItem, descriptionItem: this.descriptionItem, idSchedule: this.id }
            let url = '/api/items/' + this.$route.params.id;
            let res = await fetch(url, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(newItem),
            });
            
            // Reloading of all the item
            this.loadItems();
            // Set the input to empty, otherwise the user may think that it doesn't succeed
            this.nameItem = "";
            this.day = "";
            this.hourBeginItem = "";
            this.hourEndItem = "";
            this.placeItem = "";
            this.descriptionItem = "";
        },

        searchUser: function (user) {
            if (this.nameUser == user.username) {
                this.targetUser = user;
            }
        },

        addUser: async function () {
            let sched = this.schedule;
            // Then we search the information of the current user
            this.listAllUser.forEach(element => this.searchUser(element));
            // Finally we add him in a schedule 
            if (this.nameUser != "" && this.targetUser != ""){
                let url = '/api/users/' + this.targetUser.username + '/schedule';
                let res = await fetch(url, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(sched),
                })
                // We push the user in the list of the actual user of this schedule
                this.listUser.push(this.targetUser);
                this.nameUser = "";
                this.targetUser = "";
            }
           
        },

        checkSchedule: function (element) {
            if (element.idSchedule == this.id) {
                this.listItem.push(element);
            }
        },

        kickUser: async function(pUser){
            let url = '/api/schedule/' + this.$route.params.id + '/delete/' + pUser.username;
            let res = await fetch(url, {method: 'PUT'});
            
            // Reload the users
            this.loadUser();
        }
    }
};


export { Schedules }