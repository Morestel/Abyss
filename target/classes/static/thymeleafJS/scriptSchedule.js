let app = Vue.createApp({

    data: () => ({
        jours: [],
        heures: [],
        workDayBool: 0,
        listCurrentItem: [],
        lowerBound: "",
        upperBound: "",
        borneTemp: new Date(),
        nbButton: 20,
        appuiyer: 0,
    }),
    template: `
    <button @click="switchWorkDay()">Semaine de travail</button>
    <div class="scheduleAndButton">
        <svg id="svgBlock" width="100%" height="1500px">

                <foreignObject v-for="j in jours" :key="j.t" v-bind:x="j.x" v-bind:y="j.y" v-bind:width="j.w" v-bind:height="j.h" class="jour">
                    <div>{{j.t}}</div>
                </foreignObject>

                <foreignObject v-for="h in heures" :key="h.t" v-bind:x="h.x" v-bind:y="h.y" v-bind:width="h.w" v-bind:height="h.h" class="heure">
                    <div>{{h.t}}</div>
                </foreignObject>
                
                <foreignObject v-if="workDayBool=='0'" v-for="item in listCurrentItem" :x="svgX(item)" :y="svgY(item)" width="12.5%" :height="svgHeight(item)" class="itemss" @mouseup="moveItemUp();moveItem($event)" @mousedown="moveItemDown()" @mousemove="moveItem($event)" @mouseout="moveItemUp();moveItem($event)">
                    <div><b>{{item.nameItem}}</b><br><br>{{item.roomItem}}<br><br><p class="moreInfoItem">{{item.hourBeginItem}} à {{item.hourEndItem}}<br>{{item.descriptionItem}}</p></div>
                </foreignObject>

                <foreignObject v-if="workDayBool=='1'" v-for="item in listCurrentItem" :x="svgX(item)" :y="svgY(item)-480" width="16.666666667%" :height="svgHeight(item)" class="itemss" @mouseup="moveItemUp();moveItem($event)" @mousedown="moveItemDown()" @mousemove="moveItem($event)" @mouseout="moveItemUp();moveItem($event)">
                    <div><b>{{item.nameItem}}</b><br><br>{{item.roomItem}}<br><br><p class="moreInfoItem">{{item.hourBeginItem}} à {{item.hourEndItem}}<br>{{item.descriptionItem}}</p></div>
                </foreignObject>
        </svg>
        <ul class = "buttonSchedule">
            <li v-for="n in nbButton">
                <button class="buttonScheduleOne" @load="isActif()" @click="changeBounds($event);actifDeletButton($event);">{{createButtons(n)}}</button>
            </li>
        </ul>
    </div>
    `,
    mounted() {
        this.loadItems();
        this.getMonday(new Date());
        this.getSunday(new Date());
        this.initializeActiveButton();
    },

    methods: {
        async loadItems() {
            var id_schedule = window.location.href;
            id_schedule = id_schedule.replace("http://localhost:8080/schedule/", "");
            let url = '/api/schedules/' + id_schedule + '/items';
            let req = await fetch(url);
            this.listItem = await req.json();
            this.workDay();
            // We initialize it to have a display when we arrive on this page
            this.listCurrentItem.length = 0;
            this.listItem.forEach(element => this.check(element));
            console.log(this.listCurrentItem);
            this.listCurrentItem.forEach(function (element) {
                console.log("Item courant : " + element.nameItem);
            })
        },
        moveItemDown: function () {
            this.appuiyer = 1;
        },
        moveItemUp: function () {
            this.appuiyer = 0;
        },
        moveItem: function (event) {
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
                    var valuOfMouv = event.target.parentElement.x.baseVal.value;
                    var svgWidth = document.getElementById("svgBlock").width.baseVal.value;
                    if (this.workDayBool) {
                        let x16 = (svgWidth / 100) * 16.666666667;
                        if (valuOfMouv < x16 * 2) {
                            event.target.parentElement.x.baseVal.value = x16;
                        } else {
                            if (valuOfMouv > x16 * 2 && valuOfMouv < x16 * 3) {
                                event.target.parentElement.x.baseVal.value = x16 * 2;
                            } else {
                                if (valuOfMouv > x16 * 3 && valuOfMouv < x16 * 4) {
                                    event.target.parentElement.x.baseVal.value = x16 * 3;
                                } else {
                                    if (valuOfMouv > x16 * 4 && valuOfMouv < x16 * 5) {
                                        event.target.parentElement.x.baseVal.value = x16 * 4;
                                    } else {
                                        if (valuOfMouv > x16 * 5) {
                                            event.target.parentElement.x.baseVal.value = x16 * 5;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        let x12 = (svgWidth / 100) * 12.5;
                        if (valuOfMouv < x12) {
                            event.target.parentElement.x.baseVal.value = x12;
                        } else {
                            if (valuOfMouv > x12 && valuOfMouv < x12 * 2) {
                                event.target.parentElement.x.baseVal.value = x12 * 2;
                            } else {
                                if (valuOfMouv > x12 * 2 && valuOfMouv < x12 * 3) {
                                    event.target.parentElement.x.baseVal.value = x12 * 3;
                                } else {
                                    if (valuOfMouv > x12 * 3 && valuOfMouv < x12 * 4) {
                                        event.target.parentElement.x.baseVal.value = x12 * 4;
                                    } else {
                                        if (valuOfMouv > x12 * 4 && valuOfMouv < x12 * 5) {
                                            event.target.parentElement.x.baseVal.value = x12 * 5;
                                        } else {
                                            if (valuOfMouv > x12 * 5 && valuOfMouv < x12 * 6) {
                                                event.target.parentElement.x.baseVal.value = x12 * 6;
                                            } else {
                                                if (valuOfMouv > x12 * 6) {
                                                    event.target.parentElement.x.baseVal.value = x12 * 7;
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
            if (this.workDayBool) {
                this.workDayBool = 0;
                svgBlock.style.backgroundSize = "12.5% 60px"
            } else {
                this.workDayBool = 1;
                svgBlock.style.backgroundSize = "16.666666667% 60px"
            }
            this.workDay();
        },
        workDay: function () {
            var svgBlock = document.getElementById("svgBlock");
            if (this.workDayBool) {
                svgBlock.style.height = "720px";
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
                ],
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
            this.listCurrentItem.length = 0;
            this.listItem.forEach(element => this.check(element));
        },

        check: function (element) {
            var monday = this.giveMonday(element.day);
            if (this.lowerBound.getFullYear() == monday.getFullYear()
                && this.lowerBound.getMonth() == monday.getMonth()
                && this.lowerBound.getDate() == monday.getDate()) {
                // We have the same monday, it means that we are in the same week
                this.listCurrentItem.push(element);
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
            //console.log(str);
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
        }
    }
});


let sts = app.mount('#schedule-vue');