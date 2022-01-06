const Foot = {
    template: 
    `
    <footer id="footer">
    <table>
        <tr id="footerTete">
            <th>Plan du site :</th>
            <th></th>
        </tr>
        <tr id="liFooterTete">
            <td>
                <router-link to="/">Accueil</router-link><br>
                <router-link to="/actualite">Actualité</router-link><br>
                <router-link to="/aide">Aide</router-link><br>
            </td>
            <td>
                <img id="logoFooter" src="/images/tsor.png" alt="logoFooter">
            </td>
        </tr>
    </table>
    <p style="text-align : center;font-size: 150%;color:black;">Copyright © 2021 TSOR Corp. Tous droits réservés.</p>
</footer>
    `
};

export { Foot }