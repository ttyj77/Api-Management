
/* selectOne : 서비스 클릭 시 개별 상세 모달 */
function serviceInfoModal(value) {

    let data = {
        "id": value.parentNode.id,
    };
    $.ajax({
        url: "/mydata/service/modal",
        type: "get",
        data: data,

        success: (message) => {
            $('#serviceInfoModal').modal('show');
            createServiceModal(message);


        },
        error: (e) => {
            console.log("통신 실패", e);
        }
    })
}

function createServiceModal(message) {
    console.log(message)
    document.getElementById("callbackUrlTbody").innerHTML = ""


    document.getElementById("clientId").innerText = message.clientId
    document.getElementById("mdServiceName").innerText = message.mdServiceName

    let tableTr = document.createElement("tr")
    tableTr.id = "callbackUrlTr";

    let tdHead = document.createElement("td")
    tdHead.className = "tdHead"
    tdHead.style.verticalAlign = "middle"
    tdHead.id = "callbackUrlHead"
    tdHead.innerText = "callback URL"


    tableTr.append(tdHead)
    document.getElementById("callbackUrlTbody").append(tableTr)
    let oneItem = document.createElement("td")
    oneItem.id = "callbackUrlOne"
    oneItem.className = "tdContent"

    if (message.callbackUrl.length == 0) {
        oneItem.innerText = "";
        tableTr.append(oneItem)
    }

    // callbackUrl 리스트 출력
    for (let i = 0; i < message.callbackUrl.length; i++) {
        // document.getElementById("callbackUrlHead").rowspan = message.callbackUrl.length;
        $('#callbackUrlHead').attr("rowspan", message.callbackUrl.length)

        if (i == 0) { // 1번째는 그냥 td 만들어서 append
            oneItem.innerText = message.callbackUrl[i];
            tableTr.append(oneItem)
        } else {

            let tr = document.createElement("tr")
            let td = document.createElement("td")
            td.className = "tdContent"
            td.innerText = message.callbackUrl[i]
            tr.append(td)
            document.getElementById("callbackUrlTbody").append(tr)
        }
    }

}
