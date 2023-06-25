
// 스페이스바 금지
function checkSpacebar() {
    let kcode = event.keyCode;
    if (kcode == 32) {
        event.returnValue = false;
    }
}

// password 일치 확인
function overlapCheck() {
    let pwd_1 = document.getElementById("password").value;
    let pwd_2 = document.getElementById("doubleCheckPwd").value;

    if (pwd_1 != pwd_2) {
        $('#doubleCheckPwd').css("border", "1px solid red");
        $('#pwdCheck').css("display", "block");
    } else {
        $('#doubleCheckPwd').css("border", "white");
        $('#pwdCheck').css("display", "none");
    }
}

// 비밀번호 변경
function updatePw(value) {
    let userId = value.getAttribute("user_id");
    $('#updatePw').attr('action', '/user/updatePw/' + userId);
}

// 수정 버튼 누를시
function updateInfo() {
    document.getElementById("passwordChange").disabled = false;
    document.getElementById("nickname").disabled = false;
    document.getElementById("email").disabled = false;

    let div = document.getElementById("btnBox")
    div.innerHTML = ""
    let cancelBtn = document.createElement("button")
    cancelBtn.type = "button"
    cancelBtn.className = "btn btn-secondary"
    cancelBtn.innerText = "취소"
    cancelBtn.addEventListener("click", changeBtn)

    let saveBtn = document.createElement("button")
    saveBtn.type = "submit"
    saveBtn.className = "btn btn-primary"
    saveBtn.innerText = "저장"
    saveBtn.id = "saveUpdate"
    saveBtn.style.marginRight = "10px"
    saveBtn.addEventListener("click", saveAlert)
    // $(saveBtn).attr("onclick", "saveAlert()");

    div.append(saveBtn)
    div.append(cancelBtn)

}

// 취소 버튼 누를 시 다시 저장 -> 수정버튼으로 변경
function changeBtn() {
    document.getElementById("passwordChange").disabled = true;
    document.getElementById("nickname").disabled = true;
    document.getElementById("email").disabled = true;
    let div = document.getElementById("btnBox")
    div.innerHTML = ""

    let updateBtn = document.createElement("button");
    updateBtn.type = "button"
    updateBtn.className = "btn btn-dark";
    updateBtn.innerText = "수정"
    updateBtn.addEventListener("click", updateInfo);

    div.append(updateBtn);

}

// 수정 확인 및 저장
function saveAlert(e) {
    let saveBtn = document.getElementById("saveUpdate");
    e.preventDefault();
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "확인",
        icon: 'warning',
        text: "변경내용을 저장하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '저장 완료',
                icon: 'success',
            }).then(() => {
                document.getElementById("updateUser").submit();
            })
        }

    });

}

