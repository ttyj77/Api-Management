function saveApi() {
    // 입력 확인
    let context = document.querySelector('.contextApi');
    let name = document.querySelector('.nameApi');
    if (context.value === '') {
        Swal.fire({
            icon: 'error', text: '컨텍스트를 입력해주세요.'
        });
    } else if (name.value === '') {
        Swal.fire({
            icon: 'error', text: '이름을 입력해주세요.'
        });
    } else {
        Swal.fire({
            icon: 'success', text: 'API 그룹이 저장되었습니다.'
        }).then(() => {
            // location.href = '/api/insert';
            $('#registerApi').submit();
            $('#registerModal').modal('hide');
        });
    }
}

function saveApiUpdate() {
    if ($('#contextApi').val().length === 0) {
        Swal.fire({
            icon: 'error', text: '컨텍스트를 입력해주세요.'
        });
    } else if ($('#nameApi').val().length === 0) {
        Swal.fire({
            icon: 'error', text: '이름을 입력해주세요.'
        });
    } else {
        Swal.fire({
            icon: 'success', text: 'API 그룹이 저장되었습니다.'
        }).then(() => {
            $('#updateBtn').submit();
            $('#updateModal').modal('hide');
        });
    }
}

function removeApi(apiOne) {
    let id = apiOne.getAttribute('apiid');
    let name = apiOne.getAttribute('apiname');
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "\'"+name+"업권\' API 그룹을 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공', icon: 'success'
            }).then(() => {
                location.href = "/api/delete/" + id;
            });
        }
    });
}

// modal 선택 시 안 넘어감
function insertApis(e, id) {
    if (e.target.parentNode.className == "h3 font-weight-bold text-primary mb-1" || e.target.parentNode.className == "col mr-2") {
        location.href = '/api/details/' + id;
        console.log(e.target.parentNode.className)
    } else {
        console.log("해당없음!!")
    }
}

function updateApi(apiOne) {
    console.log("update Api : 연필 모달")
    let context = document.getElementById('contextApi');
    let name = document.getElementById('nameApi');
    let explanation = document.getElementById('explanationApi');
    let buttonApi = document.getElementsByClassName("btn btn-dark btn-sm api")

    let apiId = apiOne.getAttribute("apiid");
    let data = {
        "apiId": apiId,
    };
    $('#radio1').prop("checked", false);
    $('#radio2').prop("checked", false);
    $.ajax({
        url: "/api/selectOne", data: data, method: "get",

        success: (message) => {

            context.value = message.apiContext;
            name.value = message.apiName;
            explanation.textContent = message.apiExplanation;
            buttonApi.id = message.apiId;
            console.log("message !!! " + message)
            let selectedRoleList = JSON.parse(message.selectedRoleList);
            console.log(selectedRoleList)
            let target2 = document.getElementById("target2")
            target2.innerHTML = ""
            for (let i = 0; i < selectedRoleList.length; i++) {
                let h5 = document.createElement("h5")
                let badge = document.createElement("span")
                let inputHidden = document.createElement("input")

                badge.className = "badge badge-secondary"
                badge.innerText = selectedRoleList[i].name
                badge.id = "BADGE_" + selectedRoleList[i].code

                inputHidden.name = "roleId"
                inputHidden.value = selectedRoleList[i].id
                inputHidden.type = "hidden"

                h5.append(badge, inputHidden)
                target2.append(h5)
            }

            $('#updateApi').attr("action", "/api/update/" + apiId)
        }, error: (e) => {
            console.log("출력실패", e)
        }
    })


    $('#updateModal').modal('show');
}

function roleList() {
    if ($("#roleModalBody").children().length > 0) { // 한번이라도 클릭 했다면
        $('#roleModal').modal('show')
    } else {
        $.ajax({
            url: "/api/roleList",
            method: "get",
            success: (message) => {
                $('#roleModal').modal('show')
                console.log(message);

                let array = JSON.parse(message.responseText);

                let roleBody = document.getElementById("roleModalBody")
                roleBody.innerHTML = ""
                roleBody.id

                for (let i = 0; i < array.length; i++) {
                    let roleBox = document.createElement("div")
                    roleBox.className = "form-check"

                    let roleInput = document.createElement("input")
                    roleInput.className = "form-check-input"
                    roleInput.type = "checkbox"
                    roleInput.value = array[i].name
                    roleInput.id = array[i].code
                    roleInput.name = array[i].id
                    roleInput.onclick = function () {
                        is_checked(this);
                    }

                    let roleLabel = document.createElement("label")
                    roleLabel.className = "form-check-label"
                    roleLabel.htmlFor = array[i].code
                    roleLabel.innerText = array[i].name

                    roleBox.append(roleInput, roleLabel);
                    roleBody.append(roleBox)
                }
            }
        })
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

/* 역할 모달에서 닫기를 누르면 뱃지 만들어주는용도 */
function drawRole(value) {
    console.log("drawRole")
    console.log(value.id)
    let target;
    let parent;
    if (value.id == "insertServiceModal") { //서비스 등록모달
        target = document.getElementById("target")
        parent = document.getElementById("roleModalBody")
    } else if (value.id == "updateApisModal") { // apis update모달
        target = document.getElementById("target2")
        parent = document.getElementById("apiRoleModalBody")
    }
    //모달의 따라서 타켓 다르게 설정하기

    target.innerHTML = ""

    // <h5><span className="badge badge-secondary">마이데이터 사업자</span></h5>

    let childNodes = parent.childNodes;

    console.log(parent)
    console.log(parent.length)
    console.log()
    console.log(childNodes)

    console.log(childNodes.length)
    let cnt = childNodes.length;

    for (let i = 0; i < cnt; i++) {

        let check = childNodes[i].childNodes[0];
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
            if (value.id == "updateApisModal") {
                console.log("update Api modal span id BADGE_ 추가")
                let id = check.id.substr(4,)
                console.log(id)
                badge.id = "BADGE_" + id

            }
            h5.append(badge, inputHidden)
            target.append(h5)
        }
    }
}

// 기존 Apis 역할 모달 리스트  기존 Apis 는 기존의 역할을 가지고 있어야 된다.
function apisModalRoleList() {
    let api = document.getElementsByClassName("btn btn-dark btn-sm api")
    console.log(api.id)

    let data = {
        "apiId": api.id,
    };

    // 1. 전체리스트를 가져온다
    $.ajax({
        url: "/api/apiRoleList",
        method: "get",
        data: data,
        success: (message) => {
            $('#roleUpdateModal').modal('show')
            console.log(message);

            let array = JSON.parse(message.responseText);
            console.log("array : " + array)

            let roleBody = document.getElementById("apiRoleModalBody")
            roleBody.innerHTML = ""
            // roleBody.id
            for (let i = 0; i < array.length; i++) {
                let roleBox = document.createElement("div")
                roleBox.className = "form-check"

                let roleInput = document.createElement("input")
                roleInput.className = "form-check-input"
                roleInput.type = "checkbox"
                roleInput.value = array[i].name
                roleInput.id = "API_" + array[i].code
                roleInput.name = 'roleId'
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