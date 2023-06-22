function detailModal(value) {

    // console.log(value.children())
    // $("#detailModal").modal('show')
    // let details = value.lastElementChild.lastElementChild;

    // let id = details.getAttribute("detailid")
    console.log(value)
    let data = {
        "id": value,
    }

    $.ajax({
        url: "/api/select/detail",
        data: data,
        method: "get",

        success: (message) => {
            console.log("detailModal!!!!")
            console.log(message)
            let parameterList = JSON.parse(message.parameterList)
            console.log(parameterList)
            console.log(document.getElementById("dModal_url"))

            document.getElementById("dModal_url").innerText = message.url + message.uri;
            console.log(message.url)
            console.log(message.uri)
            document.getElementById("dModal_method").innerText = message.method
            document.getElementById("dModal_summary").innerText = message.summary.replace(/\"/gi, "");

            document.getElementById("summary").innerText = message.summary.replace(/\"/gi, "");
            document.getElementById("methodUrl").innerText = message.method + " | " + message.uri;
            let operationId = document.getElementById("dModal_operationId")
            operationId.innerText = message.operationId.replace(/\"/gi, "");

            // 파라미터 정보등록
            let tbody = document.getElementById("paramTbody")
            tbody.innerHTML = ""
            // 파라미터 개수의 따라서 table 생성
            for (let i = 0; i < parameterList.length; i++) {
                let tr = document.createElement("tr")
                let name = document.createElement("td");
                let transferMethod = document.createElement("td")
                transferMethod.append(transferMethod_select())

                let explanation = document.createElement("td");
                let required = document.createElement("td");
                required.append(required_select())

                let type = document.createElement("td");
                type.append(type_select())

                let sample = document.createElement("td");
                let trash = document.createElement("td")

                let icon = document.createElement("i")
                icon.className = "fa-solid fa-trash-can"
                icon.style.color = "#797a7c"
                icon.style.fontWeight = "100"

                trash.append(icon)
                trash.onclick = function () {
                    paramterDelete(parameterList[i]);
                }
                name.innerText = parameterList[i].name.replace(/\"/gi, "");

                explanation.innerText = parameterList[i].explanation.replace(/\"/gi, "");
                sample.innerText = parameterList[i].sample.replace(/\"/gi, "");


                tr.append(name, transferMethod, explanation, required, type, sample, trash)
                tbody.append(tr)

            }
            // document.getElementById("dModal_parameter_Name").innerText = message
            let bodyReqList = JSON.parse(message.bodyReqList)
            console.log(bodyReqList)
            //  요청Body 정보
            console.log(bodyReqList.length > 0)
            // 요청 BODY 부분 시작
            if (bodyReqList.length > 0) { // 응답 데이터가 있다면
                let table = document.getElementById("resBodyTable")
                table.innerHTML = ""
                for (let i = 0; i < bodyReqList.length; i++) {
                    let tr = document.createElement("tr")
                    console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!table!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    console.log(bodyReqList[i].apiDetailsId)
                    let allBody = document.getElementsByName("allBodyParam")
                    console.log(allBody)
                    $(allBody).attr("id", bodyReqList[i].apiDetailsId)
                    // allBody.id = bodyReqList[i].apiDetailsId

                    let key = document.createElement("td")
                    let type = document.createElement("td")
                    let value = document.createElement("td")

                    let trash = document.createElement("td")
                    let icon = document.createElement("i")
                    icon.className = "fa-solid fa-trash-can"
                    icon.style.color = "#797a7c"
                    icon.style.fontWeight = "100"
                    trash.id = bodyReqList[i].id
                    trash.append(icon)
                    trash.onclick = function () {
                        resTableDeleteCheck(bodyReqList[i]);
                    }


                    key.innerText = bodyReqList[i].key
                    value.innerText = bodyReqList[i].value.replaceAll("\"", "");
                    type.innerText = "String"
                    tr.append(key, type, value, trash)
                    table.append(tr)
                }
            } else {
                let table = document.getElementById("resBodyTable")
                table.innerHTML = ""
                let tr = document.createElement("tr")
                let td = document.createElement("td")
                td.innerText = "아직 데이터가 없습니다"
                td.colSpan = "4"
                tr.append(td)
                table.append(td)
            }
            //  응답


        }, error: (e) => {
            console.log("출력실패", e)
        }
    })
    showResData(value)
}

function resTableDeleteCheck(value) {
    /* 응답 body 개별삭제 */
    let id = value.id
    let apiId = value.apiDetailsId
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "요청 Body 파라미터를 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공',
                icon: 'success'
            }).then(() => {
                resBodyDelete(id)
                setTimeout(() => detailModal(apiId), 80);
            })
        }
    });
}

function paramterDelete(value) {
    console.log("paramterDelete")
    let id = value.id
    let apiId = value.apiDetailsId

    /* 응답 body 개별삭제 */
    console.log(id)
    console.log(apiId)
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "파라미터를 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공',
                icon: 'success'
            }).then(() => {
                paramDelete(id, apiId)
                setTimeout(() => detailModal(apiId), 80);
            })
        }
    });
}


function resBodyDelete(id) {
    console.log("==============resBodyDelete=============")
    let data = {
        "id": id
    }
    $.ajax({
        url: "/api/delete/resBody",
        data: data,
        method: "get",

        success: (message) => {
            console.log("삭제성공")
        }, error: (e) => {
            console.log("출력실패", e)
        }
    })

}

function showResData(id) {

    let data = {
        "id": id
    }

    $.ajax({
        url: "/api/resCode/selectOne",
        data: data,
        method: "get",

        success: (message) => {
            console.log("================message===================")
            console.log(message)
            console.log("================message===================")
            console.log(message.responseList)

            let resList = JSON.parse(message.responseList)
            let temp = null;
            console.log("길이 : " + resList.length);
            for (let i = 0; i < resList.length; i++) {
                let id = resList[i].id;
                console.log("id = " + id);
            }

            let resParamList = JSON.parse(message.resParamList)
            console.log(resList)
            console.log()
            console.log(resParamList)

            // 요청 BODY 부분 끝

            document.getElementById("accordionExample").innerHTML = ""
            for (let d = 0; d < resList.length; d++) {
                $("#accordionExample").append(`
                    <div class="accordion-item m-3">
                        <h2 class="accordion-header" id="heading` + resList[d].id + `">
                            <button class="accordion-button collapsed" type="button"
                                data-bs-toggle="collapse" data-bs-target="#collapse` + resList[d].id + `"
                                aria-expanded="false" aria-controls="collapse` + resList[d].id + `">
                                ` + resList[d].respCode.replace(/\"/gi, "") +
                    `</button>
                        </h2>
                        <div id="collapse` + resList[d].id + `" class="accordion-collapse collapse"
                            aria-labelledby="heading` + resList[d].id + `">
                            <div class="accordion-body">
                                <table class="table table-borderless">
                                    <tbody>
                                    <tr class="borderTop">
                                        <td>
                                            설명
                                        </td>
                                        <td class="tdContent" id="s">
                                           ` + resList[d].respMsg.replace(/\"/gi, "") + `
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <table class="table">
                                    <thead>
                                     <tr style="text-align: -webkit-center;">
                                        <th scope="col">Content-type</th>
                                        <th scope="col">Body</th>
                                        <th scope="col">비고</th>
                                    </tr>
                                    </thead>
                                    <tbody class="table-group-divider">
                                    <tr class="detailModal_ResouceId" style="text-align: center" id=` + resList[d].apiDetailsId + `>
                                        <td style="vertical-align: middle;">` + resList[d].type + `</td>
                                        <td>
                                            <table class="table">
                                                <thead style="background-color: #1c294e21">
                                                <tr>
                                                    <th scope="col">키</th>
                                                    <th scope="col">타입</th>
                                                    <th scope="col">샘플</th>
                                                    <th scope="col">비고</th>
                                                </tr>
                                                </thead>
                                                <tbody class="table-group-divider" id="resParamTbody` + resList[d].id + `">
                                                <!-- res paramter for문 시작  -->
                                                <!-- res의 id 와 resparam 의 resId 와 같으면 출력 -->
                                                <!-- res paramter for문 끝 -->
                                                </tbody>
                                            </table>
                                        </td>
                                        <td onclick="removeResCode(this)" id=` + resList[d].id + `><i class="fa-solid fa-trash-can"
                                               style="color: #797a7c;font-weight: 100;"></i></td>
                                    </tr>
        
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>`
                )

                //      <!-- res paramter for문 시작  -->
                //                                                 <!-- res의 id 와 resparam 의 resId 와 같으면 출력 -->
                for (let i = 0; i < resParamList.length; i++) {

                    if (resList[d].id == resParamList[i].resId) {
                        console.log(resParamList[i].resId)
                        console.log(resList[d].id)
                        let id = resList[d].id
                        console.log(document.getElementById("resParamTbody" + id))
                        $('#' + 'resParamTbody' + id).append(`
                         <tr id=` + resParamList[i].id + `>
                            <td>` + resParamList[i].key.replace(/\"/gi, "") + `</td>
                            <td>
                                <select class="form-select"
                                        aria-label="Default select example">
                                    <option value="1" selected>` + resParamList[i].type.replace(/\"/gi, "") + `</option>
                                </select>
                            </td>
                            <td>
                                
</td>
  <td onclick="removeResCodeParam(this)"><i class="fa-solid fa-trash-can"
                                               style="color: #797a7c;font-weight: 100;"></i></td>
                        </tr>

                `)
                    }
                }
            }


        }, error: (e) => {
            console.log("출력실패", e)
        }


    })
}

// 응답파라미터 삭제
function removeResCodeParam(value) {
    console.log("removeResCode")
    console.log(value)
    console.log(value.closest(".detailModal_ResouceId").id)
    let apiDetailsId = value.closest(".detailModal_ResouceId").id

    console.log(value.parentNode.parentNode.parentNode.parentNode.parentNode)

    let id = value.parentNode.id


    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "응답파라미터를 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공',
                icon: 'success'
            }).then(() => {
                resParamDelete(id, apiDetailsId)
                setTimeout(() => detailModal(apiDetailsId), 80);
            })
        }
    });
}

// 응답 삭제
function removeResCode(value) {
    console.log(value.id)
    console.log(value.parentNode.id)
    let id = value.id
    let apiDetailsId = value.parentNode.id
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "응답데이터를 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공',
                icon: 'success'
            }).then(() => {
                resDelete(id)
                setTimeout(() => detailModal(apiDetailsId), 80);
            })
        }
    });
}

function resParamDelete(id, apiDetailsId) {
    let data = {
        "id": id
    }
    $.ajax({
        url: "/api/remove/resParam",
        data: data,
        method: "get",

        success: (message) => {
        }, error: (e) => {
            console.log("출력실패", e)
        }
    })


}

function paramDelete(id, apiDetailsId) {
    let data = {
        "id": id
    }
    $.ajax({
        url: "/api/remove/param",
        data: data,
        method: "get",

        success: (message) => {
        }, error: (e) => {
            console.log("출력실패", e)
        }
    })


}


function resDelete(id) {
    let data = {
        "id": id
    }
    $.ajax({
        url: "/api/remove/resCode",
        data: data,
        method: "get",

        success: (message) => {
        }, error: (e) => {
            console.log("출력실패", e)
        }
    })

}


// 전송방법 selectBox
function transferMethod_select() {
    let select = document.createElement("select")
    select.className = "form-select"
    let option1 = document.createElement("option")
    option1.value = "1"
    option1.innerText = "Header"

    let option2 = document.createElement("option")
    option2.value = "2"
    option2.innerText = "Path Variable"

    let option3 = document.createElement("option")
    option3.value = "3"
    option3.innerText = "Query String"

    select.append(option1, option2, option3)
    return select;
}

// 필수여부 selectBody
function required_select() {
    let select = document.createElement("select")
    select.className = "form-select"
    let option1 = document.createElement("option")
    option1.value = "1"
    option1.innerText = "필수"

    let option2 = document.createElement("option")
    option2.value = "2"
    option2.innerText = "필수 아님"

    select.append(option1, option2)
    return select;
}

// type selectBody
function type_select() {
    let select = document.createElement("select")
    select.className = "form-select"
    let option1 = document.createElement("option")
    option1.value = "1"
    option1.innerText = "String"

    let option2 = document.createElement("option")
    option2.value = "2"
    option2.innerText = "Integer"

    let option3 = document.createElement("option")
    option3.value = "3"
    option3.innerText = "Boolean"

    let option4 = document.createElement("option")
    option4.value = "4"
    option4.innerText = "Number"

    select.append(option1, option2, option3, option4)
    return select;
}

function insertResJson() {
    let tbody = document.getElementById("appendJsonResCode")

    let key = document.getElementById("json_key").value
    let value = document.getElementById("json_value").value
    let type = document.getElementById("json_type").value


    if (key == "") {
        document.getElementById("json_key").parentNode.children[0].style.color = "#1d61ff";
        document.getElementById("json_key").focus()
    } else if (value == "") {
        document.getElementById("json_value").parentNode.children[0].style.color = "#1d61ff";
        document.getElementById("json_value").focus()
    } else {

        let tr = document.createElement("tr")
        let index = document.createElement("td")
        index.innerText = tbody.childElementCount + 1

        let keyTd = document.createElement("td")
        keyTd.innerText = document.getElementById("json_key").value
        keyTd.classname = "resParamKey"

        let valueTd = document.createElement("td")
        valueTd.innerText = document.getElementById("json_value").value
        valueTd.classname = "resParamValue"

        let typeTd = document.createElement("td")
        typeTd.innerText = document.getElementById("json_type").value
        typeTd.classname = "resParamType"

        tr.append(index, keyTd, valueTd, typeTd)
        tbody.append(tr)

        console.log(tbody.parentNode)
        console.log(tbody.childElementCount)

        $("#insertResCodeJson").modal('hide')

        $("#insertResCodeModal").modal('show')

        // 추가 버튼 누르면 모달안의 input value 삭제
        let get_input = $("#insertResCodeJson input[type=text]");
        $.each(get_input, function (index, value) {
            $(value).val('')
        });
    }
}

// 검색 결과 표를 클릭하면 input 박스에 들어감
function insertPath(e) {
    console.log("INSERT +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    console.log(e.target) // 클릭한 uri
    console.log(e.target.parentNode.id) // 클릭한 uri
    console.log(e.target.parentNode.name) // 클릭한 resourceId

    // console.log(e.target.parentNode.childNodes[0].textContent) // 클릭한 uri
    console.log(e.target.parentNode.childNodes[1].textContent) // 클릭한 uri
    console.log(e.target.parentNode.childNodes[3].textContent) // 클릭한 uri의 impossibleMethod

    $("#searchPathModal").modal('hide')
    $("#resourceModal").modal('show')

    let input = document.getElementById("path")
    let pathid = document.getElementById("pathid")
    pathid.value = e.target.parentNode.id // 선택한 uri id , 새로 생성 시 0
    let defaultUri = document.getElementById("path").name

    document.getElementById("resourceId").value = e.target.parentNode.name


    // resouce modal path 값 넣음
    input.value = e.target.parentNode.childNodes[1].textContent
    // hidden으로 기존 리소스 값 넣음
    // pathid.value

    // + input 박스에 해당 uri가 들어가면서 자동으로 작성가능한 method만 보이도록 하는 부분
    let buttonList = $("#nav-tab button[type=button]");


    let impossibleMethod = e.target.parentNode.childNodes[3].textContent

    const arr = impossibleMethod.split(',');
    console.log(arr) // 작성 불가능한 메서드 리스트

    // 작성 불가능한 메서드들은 disabled 처리
    for (let i = 0; i < buttonList.length; i++) {
        for (let j = 0; j < arr.length; j++) {
            console.log("===================")
            console.log(arr[j])
            let trim = buttonList[i].innerText.trim()
            console.log(trim)
            console.log("===================")
            if (trim == arr[j]) {
                console.log("일치 = 작성불가능")
                buttonList[i].style.display = 'none'

                let visible = document.getElementById(trim.toLowerCase())
                console.log(visible)
                visible.style.display = "none"
                //     tab hidden
                tabHidden()
            }
        }
        // 버튼 누르면 모달안의 input value 삭제
        let id = '#' + buttonList[i].innerText.trim().toLowerCase() + '-content'
        let get_input = $(id + ' input[type=text]');
        console.log("====================get_input11122222222222222=======================")
        console.log(get_input)
        $.each(get_input, function (index, value) {
            console.log(index)
            console.log(value)
            $(value).val('')
        });

    }

}

/* resBody 큼직한 삭제 */
function removeResBody(value) {
    console.log("응답 바디 삭제 ")
    console.log(value)
    console.log(value.parentNode)
    console.log(value.parentNode.parentNode.id)
    let id = value.parentNode.parentNode.id
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공',
                icon: 'success'
            }).then(() => {
                allResParamDelete(id)
                setTimeout(() => detailModal(id), 80);
            })
        }
    });
}

function allResParamDelete(id) {
    console.log("==============resBodyDelete=============")
    let data = {
        "id": id
    }
    $.ajax({
        url: "/api/delete/allResParamDelete",
        data: data,
        method: "get",

        success: (message) => {
            console.log("삭제성공")
        }, error: (e) => {
            console.log("출력실패", e)
        }
    })

}

function resDeleteBeforeInsert(value) {
    console.log("resDeleteBeforeInsert")
    console.log(value)
    console.log(value.parentNode)
    value.parentNode.remove()

}