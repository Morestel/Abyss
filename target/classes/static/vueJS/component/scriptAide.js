const Aide = {


    watch:{
        $route: {
            immediate: true,
            handler(to, from) {
                document.title = 'TSOR Corp. - Aide';
            }
        },
    },

    mounted() {
        var video = document.getElementById("videojs");
        video.src="../videos/handshake.mp4";
        window.scrollTo(0, 0);
    },

    template:`
    <div class="main">
      <h2>Contactez-nous</h2>
      <div class="contact-box">
        <form method="POST" th:action="@{contactUs}">
          <div class="input-box">
            <input type="text" name="pSubject" placeholder="Sujet" required />
          </div>

          <div class="input-box">
            <textarea name="pBody" placeholder="Description"></textarea>
          </div>

          <input type="submit" id="submit_button" value="Envoyer" />
        </form>
      </div>
    </div>
    `
}
export { Aide }