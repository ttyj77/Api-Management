function updateApi(apiOne) {
    // 버튼 초기화
    document.getElementById("passwordChange").disabled = true;
    document.getElementById("name").disabled = true;
    document.getElementById("email").disabled = true;
    document.getElementById("updateRoleBtn").disabled = true;

    let div = document.getElementById("btnBox")
    div.innerHTML = ""
    let cancelBtn = document.createElement("button")
    cancelBtn.type = "button"
    cancelBtn.className = "btn btn-secondary"
    cancelBtn.innerText = "닫기"
    cancelBtn.setAttribute("data-bs-dismiss", "modal");

    let saveBtn = document.createElement("button")
    saveBtn.type = "button"
    saveBtn.className = "btn btn-dark"
    saveBtn.innerText = "수정"
    $(saveBtn).attr("onclick", "updateInfo()");
    // saveBtn.addEventListener("click", updateInfo)
    div.append(saveBtn);
    div.append(cancelBtn);

    // 모달 내용
    let id = apiOne.getAttribute("user_id");

    let d = {
        "id": id,
    }
    $.ajax({
        url: "/user/userOne/" + id,
        data: d,
        method: "get",

        success: (message) => {
            document.getElementById('userId').innerText = message.username;
            document.getElementById('name').value = message.nickname;
            document.getElementById('email').value = message.email;
            document.getElementById('saveId').value = id;
            if (message.activate) {
                document.getElementById('activate').innerText = '활성화';
            } else if (!message.activate) {
                document.getElementById('activate').innerText = '비활성화';
            }
        }
    })


    let context = document.getElementById('contextApi');
    let name = document.getElementById('nameApi');
    let explanation = document.getElementById('explanationApi');
    let buttonApi = document.getElementsByClassName("btn btn-dark btn-sm api")

    let user_id = apiOne.getAttribute("user_id");
    let data = {
        "userId": user_id,
    };
    $('#radio1').prop("checked", false);
    $('#radio2').prop("checked", false);
    $.ajax({
        url: "/user/selectOneRole", data: data, method: "get",

        success: (message) => {
            console.log(message)
            console.log(JSON.parse(message.selectedRoleList))

            let selectedRoleList = JSON.parse(message.selectedRoleList);
            let target2 = document.getElementById("target2")
            target2.innerHTML = ""
            console.log("뱃지 들어가유!!!!!!!!!!!!!!!!!!!");
            for (let i = 0; i < selectedRoleList.length; i++) {
                let h5 = document.createElement("h5")
                let badge = document.createElement("span")
                let inputHidden = document.createElement("input")

                badge.className = "badge badge-secondary";
                badge.innerText = selectedRoleList[i].name;
                badge.id = "BADGE_" + selectedRoleList[i].code;

                console.log("id = ", selectedRoleList[i].id);

                inputHidden.name = "roleId"
                inputHidden.value = selectedRoleList[i].id
                inputHidden.type = "hidden"

                h5.append(badge, inputHidden)
                target2.append(h5)
            }

            // $('#updateApi').attr("action", "/api/update/" + apiId)
        }, error: (e) => {
            console.log("출력실패", e)
        }
    })


    $('#updateModal').modal('show');
}


// 기존 Apis 역할 모달 리스트  기존 Apis 는 기존의 역할을 가지고 있어야 된다.
function apisModalRoleList() {
    console.log("apiModalRoleList")

    // 1. 전체리스트를 가져온다
    $.ajax({
        url: "/api/apiRoleList",
        method: "get",
        success: (message) => {
            $('#roleUpdateModal').modal('show')
            console.log(message);

            let array = JSON.parse(message.responseText);
            console.log("array : " + array)
            console.log(array)


            let roleBody = document.getElementById("apiRoleModalBody")
            roleBody.innerHTML = ""
            for (let i = 0; i < array.length; i++) {
                console.log(array[i].id)
                let roleBox = document.createElement("div");
                roleBox.className = "form-check"

                let roleInput = document.createElement("input")
                roleInput.className = "form-check-input"
                roleInput.type = "checkbox"
                roleInput.value = array[i].name
                roleInput.id = "API_" + array[i].code
                roleInput.name = array[i].id
                roleInput.onclick = function () {
                    is_checked(this);
                }

                let roleLabel = document.createElement("label")
                roleLabel.className = "form-check-label"
                roleLabel.htmlFor = "API_" + array[i].code
                roleLabel.innerText = array[i].name

                roleBox.append(roleInput, roleLabel);
                roleBody.append(roleBox)
            }

            // 2. api 해당하는 역할을 체크한다.
            // 뱃지 기준으로 체크
            let cnt = document.getElementById("target2").childNodes.length;
            let parent = document.getElementById("target2").childNodes


            console.log(cnt)
            console.log(parent)
            console.log(parent[0])
            console.log(parent[1])
            console.log(parent[0].childNodes[0].id)
            console.log(parent[0].childNodes[1])
            console.log(parent[0].childNodes[2])
            console.log("CNT!!!" + cnt)
            for (let j = 0; j < cnt; j++) {
                let badgeId = parent[j].childNodes[0].id

                let id = "API_" + badgeId.substring(6,)
                console.log("id===========" + id)
                if (!$('#' + id).prop("checked")) {
                    console.log("checked false")
                    $('#' + id).prop("checked", true);
                }
            }


        }

    })

}


/* 역할 모달에서 닫기를 누르면 뱃지 만들어주는용도 */
function drawRole(value) {
    console.log("drawRole")
    console.log(value.id)
    let target;
    let parent;
    if (value.id == "userRoleModal") { //서비스 등록모달
        console.log("?????????????????")
        target = document.getElementById("target2")
        parent = document.getElementById("apiRoleModalBody")
    }
    //모달의 따라서 타켓 다르게 설정하기

    target.innerHTML = ""

    // <h5><span className="badge badge-secondary">마이데이터 사업자</span></h5>

    let childNodes = parent.childNodes;

    let cnt = childNodes.length;

    for (let i = 0; i < cnt; i++) {

        let check = childNodes[i].childNodes[0];

        console.log("check : " + check)
        console.log("check : " + check.id)
        let isChecked = $('#' + check.id).is(':checked');
        console.log(isChecked)
        if (isChecked) { //체크가 되어있다면
            console.log(check)
            let h5 = document.createElement("h5")
            let badge = document.createElement("span")
            badge.className = "badge badge-secondary"
            badge.innerText = check.value
            let inputHidden = document.createElement("input")
            inputHidden.name = "roleId"
            inputHidden.value = check.name
            inputHidden.type = "hidden"

            if (value.id == "userRoleModal") {
                console.log("update Api modal span id BADGE_ 추가")
                let id = check.id.substr(4,)
                console.log(id)
                badge.id = "BADGE_" + id

                let inputHidden = document.createElement("input")

                // inputHidden.name = "roleId"
                // inputHidden.value = selectedRoleList[i].id
                // inputHidden.type = "hidden"
            }
            h5.append(badge, inputHidden)
            target.append(h5)
        }
    }
}


/* 체크박스 체크 시 input 안에 checkd 넣기*/
function is_checked(value) {
    console.log(value.id)

    console.log($('#' + value.id).prop("checked"))

    if ($('#' + value.id).prop("checked")) {
        $('#' + value.id).prop("checked", true);
    } else {
        $('#' + value.id).prop("checked", false);
    }
}
