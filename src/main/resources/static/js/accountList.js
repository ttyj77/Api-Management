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

function removeAlert(user) {
    let id = user.getAttribute("id");
    let name = user.getAttribute("nickname");
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "확인",
        icon: 'warning',
        text: name + " 계정을 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 완료', icon: 'success'
            }).then(() => {
                location.href = '/user/delete/' + id;
            })
        }
    });
}


//  수정버튼 클릭시
//  1. 수정 버튼 -> 취소 / 저장 버튼으로
function updateInfo() {

    document.getElementById("passwordChange").disabled = false;
    // document.getElementById("userId").disabled = false;
    // document.getElementById("userId").style.borderColor = "#0982f0";
    document.getElementById("name").disabled = false;
    document.getElementById("email").disabled = false;
    document.getElementById("updateRoleBtn").disabled = false;


    let div = document.getElementById("btnBox")
    div.innerHTML = ""
    let cancelBtn = document.createElement("button")
    cancelBtn.type = "button"
    cancelBtn.className = "btn btn-secondary"
    cancelBtn.innerText = "취소"
    cancelBtn.setAttribute("data-bs-dismiss", "modal");

    let saveBtn = document.createElement("button")
    saveBtn.type = "submit"
    saveBtn.className = "btn btn-primary"
    saveBtn.innerText = "저장"
    saveBtn.id = "saveUpdate"
    saveBtn.addEventListener("click", saveAlert)
    // $(saveBtn).attr("onclick", "saveAlert()");

    div.append(saveBtn)
    div.append(cancelBtn)


}

//
// 수정 확인 SA(Sweet Alert)
// 변경내용 저장 확인
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
                title: '저장 완료', icon: 'success',
            }).then(() => {
                document.getElementById("updateAccount").submit();
            })
        }

    });

}

function activate(value) {
    let id = value.getAttribute("userid");
    $.ajax({
        url: "/turnActivate/" + id, type: "post",
    })
    if (value.className == "btn btn-primary") { //활성화 상태 - > 비활성화로 변경
        value.className = "btn btn-danger";
        value.innerText = "비활성화"

    } else if (value.className = "btn btn-danger") {//비활성화 상태 - > 활성화로 변경
        value.className = "btn btn-primary";
        value.innerText = "활성화"
    }
}

function makeAction() {
    let id = document.getElementById('saveId').value;
    $('#makeAction').attr('action', '/user/updatePwd/' + id);
}

// 활성화 선택
function showActivate(value) {

    let on = document.getElementById("true");
    let off = document.getElementById("false");
    let whole = document.getElementById("whole");

    // 버튼 초기화
    on.className = "btn btn-outline-primary mr-2";
    off.className = "btn btn-outline-danger mr-2";
    whole.className = "btn btn-outline-secondary mr-2";

    if (value.id == "whole") {
        whole.className = "btn btn-secondary mr-2";
    } else if (value.id == "true") {
        on.className = "btn btn-primary mr-2";
    } else if (value.id == "false") {
        off.className = "btn btn-danger mr-2";
    }

    let tbody = document.getElementById("tbody");
    tbody.innerHTML = "";

    let activate = value.id;
    let data = {
        "activate": activate,
    }
    $.ajax({
        url: "/showActivate", data: data, type: "get", success: (message) => {
            let userList = JSON.parse(message.userList);
            for (let i = 0; i < userList.length; i++) {

                //tr
                let tr = document.createElement("tr");
                $(tr).attr({"user_id": userList[i].id, "onclick": "updateApi(this)"})

                // ID td
                let tdUsername = document.createElement("td");
                tdUsername.id = "tableId";
                tdUsername.innerText = userList[i].username;
                $(tdUsername).attr({"data-bs-toggle": "modal", "data-bs-target": "#exampleModal"})

                // 이름 td
                let tdNickname = document.createElement("td");
                tdNickname.id = "tableName";
                tdNickname.innerText = userList[i].nickname;
                $(tdNickname).attr({"data-bs-toggle": "modal", "data-bs-target": "#exampleModal"})

                // 활성 비활성 td
                let tdAct = document.createElement("td");
                let onBtn = document.createElement("button");
                onBtn.type = "button";
                $(onBtn).attr({"userid": userList[i].id, "onclick": "activate(this)"});
                if (userList[i].activate) {
                    onBtn.className = "btn btn-primary";
                    onBtn.innerText = "활성화";
                } else if (!userList[i].activate) {
                    onBtn.className = "btn btn-danger";
                    onBtn.innerText = "비활성화";
                }

                tdAct.append(onBtn);

                // 비고 td
                let tdI = document.createElement("td");
                let iTag = document.createElement("i");
                iTag.className = "fa-solid fa-trash-can";
                iTag.style = "color: #797a7c; cursor: pointer";
                $(iTag).attr({
                    "id": userList[i].id, "nickname": userList[i].nickname, "onclick": "removeAlert(this)"
                })

                tdI.append(iTag);

                // 합치기
                tr.append(tdUsername);
                tr.append(tdNickname);
                tr.append(tdAct);
                tr.append(tdI);

                tbody.append(tr);

            }


        }
    })


}

function showSearch() {

    let on = document.getElementById("true");
    let off = document.getElementById("false");
    let whole = document.getElementById("whole");

    let keyword = document.getElementById("keyword").value;

    // 버튼 초기화
    on.className = "btn btn-outline-primary mr-2";
    off.className = "btn btn-outline-danger mr-2";
    whole.className = "btn btn-outline-secondary mr-2";

    let tbody = document.getElementById("tbody");
    tbody.innerHTML = "";

    let data = {
        "keyword": keyword,
    }
    $.ajax({
        url: "/accountSearch", data: data, type: "get", success: (message) => {
            let userList = JSON.parse(message.userList);
            for (let i = 0; i < userList.length; i++) {

                //tr
                let tr = document.createElement("tr");
                $(tr).attr({"user_id": userList[i].id, "onclick": "updateApi(this)"})

                // ID td
                let tdUsername = document.createElement("td");
                tdUsername.id = "tableId";
                tdUsername.innerText = userList[i].username;
                $(tdUsername).attr({"data-bs-toggle": "modal", "data-bs-target": "#exampleModal"})

                // 이름 td
                let tdNickname = document.createElement("td");
                tdNickname.id = "tableName";
                tdNickname.innerText = userList[i].nickname;
                $(tdNickname).attr({"data-bs-toggle": "modal", "data-bs-target": "#exampleModal"})

                // 활성 비활성 td
                let tdAct = document.createElement("td");
                let onBtn = document.createElement("button");
                onBtn.type = "button";
                $(onBtn).attr({"userid": userList[i].id, "onclick": "activate(this)"});
                if (userList[i].activate) {
                    onBtn.className = "btn btn-primary";
                    onBtn.innerText = "활성화";
                } else if (!userList[i].activate) {
                    onBtn.className = "btn btn-danger";
                    onBtn.innerText = "비활성화";
                }

                tdAct.append(onBtn);

                // 비고 td
                let tdI = document.createElement("td");
                let iTag = document.createElement("i");
                iTag.className = "fa-solid fa-trash-can";
                iTag.style = "color: #797a7c; cursor: pointer";
                $(iTag).attr({
                    "id": userList[i].id, "nickname": userList[i].nickname, "onclick": "removeAlert(this)"
                })

                tdI.append(iTag);

                // 합치기
                tr.append(tdUsername);
                tr.append(tdNickname);
                tr.append(tdAct);
                tr.append(tdI);

                tbody.append(tr);

            }


        }
    })


}