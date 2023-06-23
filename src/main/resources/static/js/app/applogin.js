// 스페이스바 금지
function checkSpacebar() {
    let kcode = event.keyCode;
    if (kcode == 32) {
        event.returnValue = false;
    }
}

let send = document.getElementById("send");
console.log(send)
send.addEventListener("click", function () {
    let form = document.getElementById("login-form");
    let id = document.getElementById("id");
    let pw = document.getElementById("pw");
    //
    // if (pw.value.trim() == "" || id.value.trim() == "") {
    //     alert("id와 비번 잘 적어라");
    //     return false;
    // }
    //
    // form.action = "http://www.naver.com";
    // form.mothod = "GET";

    // form.submit();
    console.log($('#login-form').serialize())
    $.ajax({
        url: "/login-proc",
        data: $('#login-form').serialize(),
        method: "post",

        success: (message) => {
            console.log("성공")
            window.location.href = "/app/main"
        }, error: (e) => {
            console.log("통신 실패", e)
        }
    })
});