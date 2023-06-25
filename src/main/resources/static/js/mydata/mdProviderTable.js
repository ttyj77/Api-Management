function providerModal(value) {
    let data = {
        "id": value.id,
    };
    $.ajax({
        url: "/mydata/provider/selectOne",
        type: "get",
        data: data,

        success: (message) => {
            $('#serviceInfoModal').modal('show');
            createProviderModal(message);

        },
        error: (e) => {
            console.log("통신 실패", e);
        }
    })
}

function createProviderModal(data) {
    document.getElementById("reqSEQ").innerText = data.reqSEQ
    document.getElementById("name").innerText = data.agencyName + " : " + data.serviceName
    document.getElementById("apiResources").innerText = data.apiResources
    document.getElementById("reqHeader").innerText = data.reqHeader
    document.getElementById("resMsg").innerText = data.resMsg
    document.getElementById("resData").innerText = data.resData
    document.getElementById("reqType").innerText = data.reqType
    document.getElementById("code").innerText = data.code
    document.getElementById("agencyName").innerText = data.agencyName
    document.getElementById("serviceName").innerText = data.serviceName
    document.getElementById("statusInfo").innerText = data.statusInfo
    document.getElementById("consumerNum").innerText = data.consumerNum
    document.getElementById("tokenExpiryDate").innerText = data.tokenExpiryDate
}

// 달력
function input() {
    let tbody = document.getElementById("userNumTbody");
    tbody.innerHTML = "";
    let dday = document.getElementById("input_date").value;
    let data = {
        "dday": dday,
    }
    $.ajax({
        url: "/mydata/provider/calendar",
        type: "get",
        data: data,

        success: (message) => {

            let plist = JSON.parse(message.providerList);
            let col_cnt = Object.keys(plist[0]).length;
            for (let i = 0; i < plist.length; i++) {
                let keys = Object.keys(plist[i]);
                let tr = document.createElement("tr");
                tr.className = "trContent";
                tr.id = plist[i]["id"];
                $(tr).attr({
                    "data-bs-toggle": "modal",
                    "data-bs-target": "#providerModal",
                    "onclick": "providerModal(this)"
                });
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