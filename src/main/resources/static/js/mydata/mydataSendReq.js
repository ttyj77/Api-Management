// 디테일 모달 누르면
function providerDetailModal(value) {
    let tbody = document.getElementById("userNumTbody");
    tbody.innerHTML = "";
    let clientNum = value.getAttribute("client_num");
    let org_code = value.getAttribute("org_code");

    // date 조회 할 때 고객번호 param으로 사용하기 위해
    let forDate = document.getElementById("input_submit");
    $(forDate).attr("cnum", clientNum);
    $('#input_submit').attr("orgCode", org_code);

    let data = {
        "customerNum": clientNum,
        "code": org_code
    };
    $.ajax({
        url: "/mydata/provider/customerList",
        type: "get",
        data: data,

        success: (message) => {
            let plist = JSON.parse(message.providerList);
            let col_cnt = Object.keys(plist[0]).length-1;
            for (let i = 0; i < plist.length; i++) {
                let tr = document.createElement("tr");
                tr.style.fontSize = '14px';

                let keys = Object.keys(plist[i]);
                $(tr).attr({
                    "data-bs-toggle": "modal",
                    "data-bs-target": "#detailModal",
                    "onclick": "providerDetailInfo(this)"

                });
                tr.id = plist[i]["id"];
                for (let j = 1; j < col_cnt; j++) {
                    let key = keys[j];

                    let td = document.createElement("td");
                    td.innerText = plist[i][key];
                    tr.append(td);
                }
                tbody.append(tr);
            }
        }
    })
}

// 거래 이력별 만들어진 tr태그에 모달
function providerDetailInfo(value) {
    let tbody = document.getElementById("userNumTbody");
    tbody.innerHTML = "";
    let id = value.getAttribute("id");
    let data = {
        "id": id,
    }
    $.ajax({
        url: "/mydata/provider/selectOne",
        data: data,
        type: "get",

        success: (message) => {
            let cn = document.getElementsByClassName("inputClientNum");
            for (let i = 0; i < cn.length; i++) {
                $(cn).attr({
                    "client_num": message.consumerNum,
                    "org_code" : message.code
                });

                document.getElementById("reqSEQ").innerText = message.reqSEQ;
                // document.getElementById("uniqueNum").innerText = message.uniqueNum;
                document.getElementById("apiResources").innerText = message.apiResources;
                // 증권번호 ( 계좌 번호 ) resData에서 가져와야함 !!!(여기에요)
                // if (!message.apiResources.equals("/accounts")){
                //
                // }
                // document.getElementById("accountNum").innerText = "계좌번호가 들어가야함 자산목록에 뜨는"
                document.getElementById("resMsg").innerText = message.resMsg;
                document.getElementById("resdata").innerText = message.resData;

            }
        }
    })
}

// 날짜 조회 할 때 거래 이력 list 만들기
function input(value) {
    let tbody = document.getElementById("userNumTbody");
    tbody.innerHTML = "";
    let dday = document.getElementById("input_date").value;
    let org_code = value.getAttribute("orgCode");
    let cnum = value.getAttribute("cnum");
    let data = {
        "dday": dday,
        "customerNum": cnum,
        "org_code": org_code,
    }
    $.ajax({
        url: "/mydata/calendarSend",
        type: "get",
        data: data,

        success: (message) => {

            let plist = JSON.parse(message.providerList);
            let col_cnt = Object.keys(plist[0]).length;
            for (let i = 0; i < plist.length; i++) {
                let tr = document.createElement("tr");
                tr.style.fontSize = '14px';

                let keys = Object.keys(plist[i]);
                $(tr).attr({
                    "data-bs-toggle": "modal",
                    "data-bs-target": "#detailModal",
                    "onclick": "providerDetailInfo(this)"
                });
                tr.id = plist[i]["id"];
                for (let j = 1; j < col_cnt; j++) {
                    let key = keys[j];

                    let td = document.createElement("td");
                    td.innerText = plist[i][key];
                    console.log(plist[i])
                    tr.append(td);
                }
                tbody.append(tr);
            }
        }
    })


}

function showAcc(value) {
    let target = document.getElementById("pushAccount");
    target.innerHTML = "";
    let clientNum = value.getAttribute("client");
    let orgCode = value.getAttribute("code");

    let data = {
        "clientNum": clientNum,
        "orgCode": orgCode,
    }

    $.ajax({
        url: "/mydata/mdTakeAccount",
        data: data,
        type: 'post',

        success: (message) => {

            let accountList = JSON.parse(message.accountList);
            for (let i = 0; i < accountList.length; i++) {
                let tr = document.createElement("tr");
                let td_1 = document.createElement("td");
                let td_2 = document.createElement("td");
                let input = document.createElement("input");

                td_1.innerText = accountList[i];
                input.type = "checkbox";
                input.className = "form-check";
                input.disabled = true;
                input.checked = true;

                td_2.append(input);
                tr.append(td_1, td_2);
                target.append(tr);

            }
        },
        error: (e) => {
            console.log("통신 실패", e);
            let tr = document.createElement("tr");
            let td_1 = document.createElement("td");
            td_1.innerText = "데이터가 없습니다."
            tr.append(td_1);
            target.append(tr);
        }
    })


}

function selectToken(value) {
    let target = document.getElementById("tokenTarget");
    let orgCode = value.parentNode.parentNode.getAttribute("org_code");

    let data = {
        "orgCode": orgCode,
    }
    $.ajax({
        url: "/mydata/selectToken",
        data: data,
        type: 'get',

        success: (message) => {
            document.getElementById("userInfo").innerText = message.clientId;

            document.getElementById("mdServiceName").innerText = message.serviceName;
            document.getElementById("agencyName").innerText = message.agencyName;
            document.getElementById("agencyCode").innerText = message.orgCode;
            document.getElementById("accessToken").innerText = message.token;
            document.getElementById("entryTime").innerText = message.issueDate;
            document.getElementById("endTime").innerText = message.expireDate;
            $('#tokenModal').modal('show');

        },
        error: (e) => {
            console.log("통신 실패", e);
            // 값이 없을 때
            $('#noTokenModal').modal('show');
        }
    })
}
