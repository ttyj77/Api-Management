/* EXPANDER MENU */
const showMenu = (toggleId, navbarId, bodyId) => {
    console.log("showMenu")
    const toggle = document.getElementById(toggleId),
        navbar = document.getElementById(navbarId),
        bodypadding = document.getElementById(bodyId)

    console.log(toggle)
    if (toggle && navbar) {
        console.log("1. if")
        toggle.addEventListener('click', () => {
            navbar.classList.toggle('expander');

            bodypadding.classList.toggle('body-pd')
        })
    }
}

showMenu('nav-toggle', 'navbar', 'body-pd')

/* LINK ACTIVE */
const linkColor = document.querySelectorAll('.nav__link')

function colorLink() {
    console.log("colorLink")
    linkColor.forEach(l => l.classList.remove('active'))
    this.classList.add('active')
}

linkColor.forEach(l => l.addEventListener('click', colorLink))

/* COLLAPSE MENU */
const linkCollapse = document.getElementsByClassName('collapse__link2')
var i

for (i = 0; i < linkCollapse.length; i++) {
    console.log("for문")
    linkCollapse[i].addEventListener('click', function () {
        const collapseMenu = this.nextElementSibling
        collapseMenu.classList.toggle('showCollapse2')

        const rotate = collapseMenu.previousElementSibling
        rotate.classList.toggle('rotate')
    });
}
