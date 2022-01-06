const Navigation = {
  template:
    `
    <div id="menuGrand">
      <ul>

        <li>
          <router-link to="/"><img id="logo" alt="logo" src="../images/tsor_corp.png" /></router-link>
        </li>

        <li>
          <router-link to="/actualite">Actualité</router-link>
        </li>

        <li>
          <router-link to="/admin">Liste utilisateurs</router-link>
        </li>

        <li>
          <router-link to="/pm">Messagerie <b id="msgMenuNumber">{{ numberNotif.number }}</b></router-link>
        </li>

        <li>
          <router-link to="/aide">Aide</router-link>
        </li>

        <li><a href="http://localhost:8080">Thymeleaf</a></li>

        <li>
          <div class="avatar"><img id="btn" :src="'/images/avatar/' + profilPicture " alt="Photo de profil"/></div>
          <div class="box arrow" id="nav_profile">
            <router-link v-on:click="getVisibel" :to="{ path: '/users/' + actualUser.username + '/profile'}">Profil</router-link>
            <br>
            <a href="http://localhost:8080/logout">Déconnexion</a>
          </div>
        </li>

      </ul>
    </div>

    <div id="menuPetit">
      <div id="menuPetitOuverture" v-on:click="switchStateMenu">
            <svg v-show="menuBool=='0' "width="100px" height="100px">
              <line x1="20" y1="40" x2="80" y2="40" stroke="red" stroke-width="5"/>
              <line x1="20" y1="60" x2="80" y2="60" stroke="red" stroke-width="5"/>
            </svg>
            <svg v-show="menuBool=='1' "width="100px" height="100px">
                <line x1="30" y1="30" x2="70" y2="70" stroke="red" stroke-width="5"/>
                <line x1="70" y1="30" x2="30" y2="70" stroke="red" stroke-width="5"/>
            </svg>
      </div>
          <router-link id = "logo" to="/"><img id="logo" alt="logo" src="../images/tsor_corp.png" /></router-link> 
      <ul>
          <li>
            <div class="blockProfilMenuPetit">
                <img class="avatarMenuPetit" id="btn" src="/images/avatar/default.png" alt="Photo de profil"/>
                <p> 
                <router-link v-on:click="switchStateMenu" :to="{ path: '/users/' + actualUser.username + '/profile'}">Profil</router-link>
                - 
                <a href="http://localhost:8080/logout">Déconnexion</a>
                </p>
            </div>
          </li>
          <li>
            <router-link v-on:click="switchStateMenu" to="/actualite">Actualité</router-link>
          </li>
          <li>
            <router-link to="/admin" v-on:click="switchStateMenu">Liste utilisateurs</router-link>
          </li>
          <li>
            <router-link to="/pm" v-on:click="switchStateMenu">Messagerie</router-link>
          </li>
          <li>
            <router-link to="/aide" v-on:click="switchStateMenu">Aide</router-link>
          </li>
      </ul>
    </div>
    `,

  data: () => ({
    actualUser: "",
    menuBool: 0,
    numberNotif: 0,
  }),

  created() {
    this.loadUser();
    setInterval(this.checkNotification, 1000)
  },

  mounted() {
    window.addEventListener('resize', this.redimentionne);
    window.addEventListener('load', this.redimentionne);
    document.getElementById('btn').addEventListener('click', this.getVisibel);
  },

  computed:{
    profilPicture: function(){
      return this.actualUser.profile_picture;
    }
  },

  methods: {
    async loadUser() {
      let url = '/api/users/giveUser';
      let req = await fetch(url);
      this.actualUser = await req.json();
      this.checkNotification();
    },

    switchStateMenu: function () {
      if (this.menuBool == 0) {
        document.getElementById('menuPetit').classList.add("open");
        this.menuBool = 1;
      } else {
        document.getElementById('menuPetit').classList.remove("open");
        this.menuBool = 0;
      }
    },

    redimentionne: function () {
      var menuGrand = document.getElementById("menuGrand");
      var menuPetit = document.getElementById("menuPetit");
      var largeur = (window.innerWidth);
      var hauteur = (window.innerHeight);
      if ((largeur > hauteur)) {
        menuGrand.style.display = "block";
        menuPetit.style.display = "none";
      } else {
        menuGrand.style.display = "none";
        menuPetit.style.display = "block";
      }
    },

    getVisibel: function () {
      var navPorfile = document.getElementById('nav_profile');
      if (navPorfile.style.display == "none") {
        navPorfile.style.display = "block";
      }
      else {
        navPorfile.style.display = "none";
      }
    },

    async checkNotification(){
      let url = '/api/privateMessage/notif/' + this.actualUser.username;
      let req = await fetch(url);
      this.numberNotif = await req.json();
    },  
  }
};

export { Navigation }