function infoModal(value) {

    let data = {
        "id": value.id,
    };
    $.ajax({
        url: "/mydata/agencyTable/modal",
        type: "get",
        data: data,

        success: (message) => {
            /*마이데이터 가관목록에서 우측 버튼 클릭 시 서비스 정보 모달 나오는 부분*/
            $('#etcModal').modal('show');
            document.getElementById("etcModalTbody").innerHTML = ""
            let array = JSON.parse(message.responseText);
            createEtcModal(array);


        },
        error: (e) => {
            console.log("통신 실패", e);
        }
    })
}

/* 서비스 목록 모달 & callbackUrl <br> 추가 */
function createEtcModal(array) {
    document.getElementById("etcModalTbody").innerHTML = ""
    let i = 0;

    for (i; i < array.length; i++) {
        let tr = document.createElement("tr")
        let clientId = document.createElement("td")
        let serviceName = document.createElement("td")
        let callbackUrl = document.createElement("td")
        for (let j = 0; j < array[i].callbackUrl.length; j++) {
            callbackUrl.append(array[i].callbackUrl[j] + "<br/>");
        }
        callbackUrl.innerHTML = callbackUrl.textContent
        clientId.innerText = array[i].clientId
        serviceName.innerText = array[i].mdServiceName

        tr.append(clientId)
        tr.append(serviceName)
        tr.append(callbackUrl)
        document.getElementById("etcModalTbody").append(tr);
    }
}

/* selectOne : 기관 클릭 시 개별 상세 모달 */
function agencyModal(value) {

    let data = {
        "id": value.parentNode.id,
    };
    $.ajax({
        url: "/mydata/agencyTable/selectOne",
        type: "get",
        data: data,

        success: (message) => {
            $('#agencyModal').modal('show');
            let list = JSON.parse(message.responseText);
            createAgencyModal(list);


        },
        error: (e) => {
            console.log("통신 실패", e);
        }
    })
}


function createAgencyModal(list) {
    //ipPort 초기화
    document.getElementById("ipPort").innerHTML = "";

    document.getElementById("code").innerText = list.code
    document.getElementById("division").innerText = list.division
    document.getElementById("name").innerText = list.name
    document.getElementById("industry").innerText = list.industry
    document.getElementById("address").innerText = list.address
    document.getElementById("domainName").innerText = list.domainName
    document.getElementById("publicApiIp").innerText = list.publicApiIp
    // document.getElementById("authenticationMethod").innerText = list.authenticationMethod
    // document.getElementById("TLSNum").innerText = list.TLSNum
    if (list.agencyIp == null && list.agencyPort == null) {
        document.getElementById("ipPort").innerText = "데이터가 없습니다.";
    } else {

        document.getElementById("ipPort").append(list.agencyIp);
        if (list.agencyPort != null) {
            document.getElementById("ipPort").append(" / ")
            document.getElementById("ipPort").append(list.agencyPort)
        }
    }
}

/* 우측 상단 셀렉트 박스 변경시 리스트 체인지 */
function selectChange(value) {

    let data = {
        "division": value,
    };
    $.ajax({
        url: "/mydata/agencyTable/selectBox",
        type: "get",
        data: data,

        success: (message) => {
            document.getElementById("agencyListTbody").innerHTML = ""
            let array = JSON.parse(message.responseText);
            createAgencyList(array);

        },
        error: (e) => {
            console.log("통신 실패", e);
        }
    })
}

function createAgencyList(array) {

    let i = 0;
    for (i; i < array.length; i++) {
        let tr = document.createElement("tr")
        let agCode = document.createElement("td")
        let name = document.createElement("td")
        let division = document.createElement("td")
        let industry = document.createElement("td")
        let note = document.createElement("td")
        // let deleteTd = document.createElement("td")
        let btn = document.createElement("button")


        agCode.append(array[i].code)
        agCode.onclick = function () {
            agencyModal(this)
        }
        division.append(array[i].division)
        division.onclick = function () {
            agencyModal(this)
        }
        name.append(array[i].name)
        name.onclick = function () {
            agencyModal(this)
        }
        industry.append(array[i].industry)
        industry.onclick = function () {
            agencyModal(this)
        }
        // <td onClick="infoModal(this)" th:id="${agencyList.id}"><i className="fa-solid fa-braille"
        // style="cursor: pointer"></i></td>

        // btn.className = "btn-sm btn-danger rounded-pill";
        // $(btn).attr("onclick", "deleteRow(this)");
        // btn.innerText = "삭제"
        //
        // deleteTd.append(btn);

        note.onclick = function () {
            infoModal(this);
        };
        note.id = array[i].id
        let icon = document.createElement("i")
        icon.className = "fa-solid fa-braille";
        icon.style.cursor = "pointer"
        note.append(icon)

        tr.id = array[i].id
        tr.append(agCode, division, name, industry, note);

        document.getElementById("agencyListTbody").append(tr)
    }
}

/* 기관 관리 삭제 */
function deleteRow(value) {
    let data = {
        "id": value.parentNode.parentNode.id,
    };

    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "확인",
        icon: 'warning',
        text: "메소드를 삭제하시겠습니까??"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공',
                icon: 'success'
            }).then(() => {
                $.ajax({
                    url: "/mydata/agencyTable/delete",
                    type: "get",
                    data: data,

                    success: (message) => {
                        // 1. 리다이렉트
                        location.href = '/mydata/agencyTable';
                    },
                    error: (e) => {
                        console.log("통신 실패", e);
                    }
                })
            })
        }
    });
}

