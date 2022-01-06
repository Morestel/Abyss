var navPorfile = document.getElementById('nav_profile');
var action = document.getElementById('btn');
var menuGrand = document.getElementById("menuGrand");
var menuPetit = document.getElementById("menuPetit");
//var span = document.querySelectorAll("span");
//var posScroll = null;

function chargementFini() {
    window.addEventListener('resize', redimentionne);
    //window.addEventListener('scroll', fonduScroll);
    action.addEventListener('click', getVisibel);
    redimentionne();
    document.body.classList.add("charger");
}

function fonduScroll() {
    posScroll = window.scrollY;
    span.forEach(element => affiche(element));
}

function affiche(element) {
    if (element.offsetTop + element.offsetHeight < posScroll + ((hauteur / 100) * 20)) {
        element.classList.remove("afficher");
        element.classList.add("fermer");
    } else {
        if (element.offsetTop < (posScroll + (hauteur - ((hauteur / 100) * 20)))) {
            element.classList.remove("fermer");
            element.classList.add("afficher");
        } else {
            element.classList.remove("afficher");
            element.classList.add("fermer");
        }
    }
}

function redimentionne() {
    largeur = (window.innerWidth);
    hauteur = (window.innerHeight);
    if (hauteur < largeur || largeur > 900) {
        menuGrand.style.display = "block";
        menuPetit.style.display = "none";
    } else {
        menuGrand.style.display = "none";
        menuPetit.style.display = "block";
    }
    //fonduScroll();
}

function validation(f) {
    if (f.password.value !== f.repassword.value) {
        alert('Mot de passe non concordant !')
        f.password.focus()
        return false;
    }
    else if (f.password.value === f.repassword.value) {
        return true;
    }
    else {
        f.password.focus();
        return false;
    }
}

function getVisibel() {
    if (navPorfile.style.display == "none") {
        navPorfile.style.display = "block";
    }
    else {
        navPorfile.style.display = "none";
    }
}


let appMenuPetit = Vue.createApp({ template: `<menuPetit-app></menuPetit-app>` });

appMenuPetit.component("menuPetit-app", {
    data: () => ({
        menuBool: 0,
    }),
    methods: {
        openMenuPetit: function () {
            if (this.menuBool == 0) {
                document.getElementById('menuPetit').classList.add("open");
                this.menuBool = 1;
            } else {
                document.getElementById('menuPetit').classList.remove("open");
                this.menuBool = 0;
            }
        },
    },
    template: `
            <div v-on:click="openMenuPetit">
                <svg v-show="menuBool=='0' "width="100px" height="100px">
                    <line x1="20" y1="40" x2="80" y2="40" stroke="red" stroke-width="5"/>
                    <line x1="20" y1="60" x2="80" y2="60" stroke="red" stroke-width="5"/>
                </svg>
                <svg v-show="menuBool=='1' "width="100px" height="100px">
                    <line x1="30" y1="30" x2="70" y2="70" stroke="red" stroke-width="5"/>
                    <line x1="70" y1="30" x2="30" y2="70" stroke="red" stroke-width="5"/>
                </svg>
            </div>
      `,
});

let menuPetitOuverture = appMenuPetit.mount("#menuPetitOuverture");