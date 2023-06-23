function checkWeb() {
    if (Mobile()) {// 모바일일 경우
        console.log("mobile")
        window.location.href = "/app/login"
    } else {// 모바일 외
        console.log("web")

        window.location.href = "/user/login"
    }

}

function Mobile() {
    return /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

gsap.registerPlugin(CustomEase);

const customEaseIn = CustomEase.create('custom-ease-in', '0.52, 0.00, 0.48, 1.00');
const fourtyFrames = 1.3333333;
const fiftyFrames = 1.66666;
const twoFrames = 0.666666;
const fourFrames = 0.133333;
const sixFrames = 0.2;

const video = document.querySelector('.hero-video');
const header = document.querySelector('.header');
const book = document.querySelector('.first-desc span');
const open = document.querySelector('.second-desc span');
const copy = document.querySelector('.copyright span');
const scrollToRows = document.querySelectorAll('.scroll-to .scroll-to__row span');
const btnCircle = document.querySelector('.book-btn__circle');
const btnText = document.querySelector('.btn-text span');
const eve = document.querySelector('#eve span');
const ry = document.querySelector('#ry span');
const fo = document.querySelector('#fo span');
const ssil = document.querySelector('#ssil span');
const tells = document.querySelector('#tells span');
const a = document.querySelector('#a span');
const st = document.querySelector('#st span');
const ory = document.querySelector('#ory span');

const showElements = () => {
    const timeline = gsap.timeline();
    timeline
        .fromTo(btnCircle, {autoAlpha: 0}, {autoAlpha: 1, duration: fourtyFrames, ease: customEaseIn}, 0)
        .fromTo(btnCircle, {scale: 0.417}, {scale: 1, duration: fourtyFrames, ease: customEaseIn}, 0)
        .fromTo(header, {y: '-0.5rem'}, {y: '0rem', duration: fourtyFrames, ease: customEaseIn}, 0)
        .fromTo(eve, {x: '2.7rem'}, {x: '0rem', duration: fiftyFrames, ease: customEaseIn}, 0)
        .fromTo(book, {y: '0.5rem'}, {y: '0rem', duration: fourtyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(fo, {x: '2.1rem'}, {x: '0rem', duration: fiftyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(a, {x: '-1.2rem'}, {x: '0rem', duration: fiftyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(ory, {x: '-3.2rem'}, {x: '0rem', duration: fiftyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(open, {y: '0.3rem'}, {y: '0rem', duration: fourtyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(btnText, {y: '0.4rem'}, {y: '0rem', duration: fourtyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(ry, {x: '-2rem'}, {x: '0rem', duration: fiftyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(ssil, {x: '-3.1rem'}, {x: '0rem', duration: fiftyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(tells, {x: '4.3rem'}, {x: '0rem', duration: fiftyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(st, {x: '1.9rem'}, {x: '0rem', duration: fiftyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(copy, {y: '0.4rem'}, {y: '0rem', duration: fourtyFrames, ease: customEaseIn}, sixFrames)
        .fromTo(scrollToRows, {y: '0.5rem'}, {
            y: '0rem',
            duration: fourtyFrames,
            ease: customEaseIn
        }, sixFrames);

    return timeline;
}

const hideElements = () => {
    const timeline = gsap.timeline();

    timeline
        .fromTo(copy, {y: '0rem'}, {y: '0.4rem', duration: fourtyFrames, ease: customEaseIn}, 0)
        .fromTo(scrollToRows, {y: '0rem'}, {y: '0.5rem', duration: fourtyFrames, ease: customEaseIn}, 0)
        .fromTo(open, {y: '0rem'}, {y: '0.3rem', duration: fourtyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(btnText, {y: '0rem'}, {y: '0.4rem', duration: fourtyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(ry, {x: '0rem'}, {x: '-2rem', duration: fiftyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(ssil, {x: '0rem'}, {x: '-3.1rem', duration: fiftyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(tells, {x: '0rem'}, {x: '4.3rem', duration: fiftyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(st, {x: '0rem'}, {x: '1.9rem', duration: fiftyFrames, ease: customEaseIn}, twoFrames)
        .fromTo(book, {y: '0rem'}, {y: '0.5rem', duration: fourtyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(fo, {x: '0rem'}, {x: '2.1rem', duration: fiftyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(a, {x: '0rem'}, {x: '-1.2rem', duration: fiftyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(ory, {x: '0rem'}, {x: '-3.2rem', duration: fiftyFrames, ease: customEaseIn}, fourFrames)
        .fromTo(btnCircle, {autoAlpha: 1}, {
            autoAlpha: 0,
            duration: fourtyFrames,
            ease: customEaseIn
        }, sixFrames)
        .fromTo(btnCircle, {scale: 1}, {scale: 0.417, duration: fourtyFrames, ease: customEaseIn}, sixFrames)
        .fromTo(header, {y: '0rem'}, {y: '-100%', duration: fourtyFrames, ease: customEaseIn}, sixFrames)
        .fromTo(eve, {x: '0rem'}, {x: '2.7rem', duration: fiftyFrames, ease: customEaseIn}, sixFrames);

    return timeline;
}

document.addEventListener('DOMContentLoaded', () => {
    showElements();
});

