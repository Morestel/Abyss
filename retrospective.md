# Comparé à notre objectif : ce qui a bien fonctionné, ce qui ne marche pas
Nous avons atteint notre objectif de base, et plus. Par exemple, le fait que tout soit responsive était secondaire mais nous l’avons implémenté au fur et à mesure, et le drag and drop n’était pas dans nos plans initiaux, tout comme la messagerie privée, ce qui a rendu l’application plus complète. Nous avons beaucoup apprécié les fonctionnalités offertes par VueJS, ce qui nous a poussé à réaliser cela.
Tout fonctionne.
Rien ne marche pas.

# Les difficultés que nous avons rencontré
Tout au long du projet nous avons rencontré de multiples problèmes :
Le plus conséquent d’entre eux a été l'absence de Roman qui a été hospitalisé pendant 1 mois. 
Ensuite la mise en place du routeur pour la partie VueJS nous a également posé problème. Nous avons suivi de nombreux tutoriels trouvés sur le site de Vue ou autre, ce qui nous a amenés à mélanger des fichiers en Vue2, Vue3, Vue-Router3 et Vue-Router4.
Enfin, l’accès aux données a parfois posé problème. Nous utilisions simplement les API de base, l’utilisation des RestController nous a permis de passer outre ces problèmes et d’envoyer et recevoir exactement ce que l’on désirait. La suppression dans la base de données a également été problématique, si un utilisateur possédait un agenda ou des messages privés alors il était impossible de le supprimer du aux contraintes d’intégrité de la base de données.

# Ce que vous avez appris
Au cours de ce projet, nous avons appris principalement à :
- Utiliser un framework en autonomie.
- La puissance de VueJS, c’est magique.
- Manipulation de VueJS.
- Ce que nous apporte VueJs par rapport à la partie Thymeleaf. Nous préférons tous travailler en VueJS.
- A se servir d’une API Rest.
- S’adapter aux difficultés de chacun pour finaliser le projet.
# Ce que vous feriez différemment la prochaine fois
- Si les consignes ne l'imposent pas nous travaillerons en VueJS dès le départ. Maintenant, nous sommes bien plus performants et nous pouvons réaliser des interactions avancées avec les données par rapport aux interactions de l'utilisateur. 
# Ce que vous garderiez à tout prix la prochaine fois
Responsive/Drag&Drop/VueJS/OpenSource et partageable sans aucune restriction.

auto-evaluation.md

# Le temps passé (en heures) par chaque membre du projet
- Osman Simsek : ~110 heures
- Thomas Morestel : ~115 heures [![wakatime](https://wakatime.com/badge/user/426cf0bb-dce5-47ed-8665-04765c1150f0/project/554f1c3e-56b6-44a2-8fca-94987ae9e325.svg)](https://wakatime.com/badge/user/426cf0bb-dce5-47ed-8665-04765c1150f0/project/554f1c3e-56b6-44a2-8fca-94987ae9e325)
- Soukaïna Bouziane : ~35 heures
- Roman Guirbal : ~25 heures

# Sur quelles parties chacun a travaillé
- Osman Simsek :  Thymeleaf, VueJs, Router, HTML, BDD, Controller, Modèle
- Thomas Morestel : Thymeleaf, VueJs, Router, HTML, BDD, Controller, Modèle, API REST
- Soukaïna Bouziane : CSS, HTML, Thymeleaf
- Roman Guirbal : CSS, HTML

# Bonnes Pratiques
## Au sujet de git

| Bonnes pratiques | Note /20 | Commentaire |
|:----------------:|:--------:|:-----------:|
| Commit/push souvent, pour ne pas avoir peur de faire des changements | 20/20 | Nous avons toujours commit/push après chaque modifications afin d’avoir les dernières version le plus tôt possible et d’éviter les conflits |
| utiliser des messages de commit qui décrivent le changement et surtout la raison du changement | 18/20 | Chaque commit contenait un message décrivant le mieux possible la partie travaillée du projet |
| utiliser des messages en anglais | 17/20 | Tous les messages ont été faits en anglais |
| ne pas commiter les fichiers générés (utiliser un .gitignore de façon à ce que git status soit propre) | 20/20 | .gitignore utilisé de manière adéquate |
| écrire les documents demandés en markdown (.md) pour qu’ils s’affichent correctement dans github | 20/20 | documents rédigés en markdown |

## Au sujet du code

| Bonnes pratiques | Note /20 | Commentaire |
|:----------------:|:--------:|:-----------:|
| écrire votre code en anglais | 20/20 | L’ensemble du code est en anglais. Ceci inclut le nom de variables, de fonctions..etc |
| indenter/formater votre code correctement | 20/20 | indentation automatique par VSCode, formatage correct |
| ne pas mélanger espaces et tabulations (idéalement, ne pas utiliser de tabulations) | 20/20 | Utilisation de VSCode qui indente automatiquement, aucun ajout d’espaces |
|  garder votre code propre: pas de variables globales/statiques, choisir des noms correctement (packages, classes, etc), suivre les conventions (e.g., java convention), utiliser des constantes pour les valeurs constantes, ... | 20/20 | pas de variable globale, noms corrects, conventions suivies |

## Au sujet du logiciel

| Bonnes pratiques | Note /20 | Commentaire |
|:----------------:|:--------:|:-----------:|
| tester beaucoup et souvent | 20/20 | Des tests ont été faits à chaque ajout de fonctionnalités du projet |
| automatiser les tests qui peuvent l’être | 18/20 | Génération auto de données de test (messages, users,...) |
| documenter comment utiliser, compiler, tester et lancer votre projet | 20/20 | Tout est indiqué dans le readme.md |
| documenter comment comprendre et reprendre votre projet | 18/20 | JavaDoc |
