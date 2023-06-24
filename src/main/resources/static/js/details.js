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
                console.log("=1=1=1=1=1=1=1=1 param list =1=1=1=1=1==1=1")
                console.log(parameterList[i])
                console.log("==================================")
                let tr = document.createElement("tr")
                let name = document.createElement("td");
                let transferMethod = document.createElement("td")
                transferMethod.append(transferMethod_select(parameterList[i].transferMethod))

                let explanation = document.createElement("td");
                let required = document.createElement("td");
                required.append(required_select(parameterList[i].required))

                let type = document.createElement("td");
                type.append(type_select(parameterList[i].type))

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
                    type.innerText = "application/json"
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
function transferMethod_select(param) {
    param = param.replaceAll("\"", "")
    let select = document.createElement("select")
    select.className = "form-select"
    let option = document.createElement("option")
    if (param == "queryString") {
        option.innerText = "Query String"
    } else if (param == "header") {
        option.innerText = "Header"
    } else if (param == "pathVariable") {
        option.innerText = "Path Variable"
    } else {
        option.innerText = "undefined"
    }
    select.append(option)
    return select;
}

// 필수여부 selectBody
function required_select(param) {
    let select = document.createElement("select")
    select.className = "form-select"
    let option = document.createElement("option")
    if (param == true) {
        option.innerText = "필수"
    } else if (param == false) {
        option.innerText = "필수 아님"
    } else {
        option.innerText = "undefined"
    }
    select.append(option)
    return select;
}

// type selectBody
function type_select(param) {
    console.log(param.replaceAll("\"", ""))
    param = param.replaceAll("\"", "")
    let select = document.createElement("select")
    select.className = "form-select"
    let option = document.createElement("option")
    if (param == "String") {
        option.innerText = "String"
    } else if (param == "integer") {
        option.innerText = "Integer"
    } else if (param == "boolean") {
        option.innerText = "Boolean"
    } else if (param == "Number") {
        option.innerText = "Number"
    } else {
        option.innerText = "undefined"
    }

    select.append(option)
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

    console.log("저장 버트 activate 확인ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ")
    let resourceBtn = document.getElementById("resourceBtn");
    if ($("path").val()) {
        console.log("풀림")
        resourceBtn.disabled = true;
    } else {
        console.log("잠김")
        resourceBtn.disabled = false;
    }

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

function IsJson() {
    console.log("??????????????")
    let method = ['get', 'post', 'put', 'delete']

    for (let i = 0; i < 4; i++) {
        console.log(method[i])
        if (method[i] == "post" || method[i] == "put") {
            console.log(document.getElementById(method[i]).style.display)
            console.log(document.getElementById(method[i]).style.display == "block")
            if (document.getElementById(method[i]).style.display == "block") {
                console.log("아아아")
                let reqData = document.getElementById(method[i] + "ReqData")
                console.log(reqData.value)
                console.log("str!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")

                try {
                    JSON.parse(reqData.value);
                    insertResource();
                    return true;
                } catch (e) {
                    console.log(reqData.value == "")
                    console.log("Json 타입 error")
                    if (reqData.value == "") {
                        Swal.fire({
                            icon: 'error', text: '요청데이터를 입력해주세요.'
                        }).then(() => {
                            $("#resourceModal").modal("show")
                        });
                    } else {
                        Swal.fire({
                            icon: 'error', text: 'JSON 타입으로 입력해주세요.'
                        }).then(() => {
                            console.log(reqData.value)
                            $("#resourceModal").modal("show")
                        });
                    }
                    return false;
                }
            }
        }
    }
}

function methodSwitch(value) {
    console.log(value)
    let method = value.innerText.toLowerCase()
    console.log(method)
    let visible = document.getElementById(method)
    let content = document.getElementById(method + "-tab")

    if (visible.style.display == "none") {
        visible.style.display = "block"
        document.getElementById(content.id).click()
        document.getElementById(document.getElementById(content.id).getAttribute("href").substring(1)).className = "tab-pane fade active show"

    } else if (visible.style.display == "block") {
        let methodLength = document.getElementById("myTab").children.length
        for (let i = 0; i < methodLength; i++) {
            visible.style.display = "none"
            if (document.getElementById("myTab").children[i].style.display == "block") {
                document.getElementById("myTab").children[i].children[0].click();
                break;
            } else {
                document.getElementById(document.getElementById("myTab").children[i].children[0].getAttribute("href").substring(1)).className = "tab-pane fade";
            }
        }
    }
}

var expanded = false;


function showSecondSelectBox() {
    var checkboxes = document.querySelector(".authorizationSelectBox");
    if (!expanded) {
        checkboxes.style.display = "block";
        expanded = true;
    } else {
        checkboxes.style.display = "none";
        expanded = false;
    }
}

function goTrash(details) {
    let id = details.getAttribute("detailid");
    let name = details.getAttribute("detailname");
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "\'" + name + "\' 메소드를 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공', icon: 'success'
            }).then(() => {
                location.href = "/api/goTrash/" + id;
            });
        }
    });
}

function goTrashResource(resource) {
    let id = resource.getAttribute("resourceid");
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "리소스를 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공', icon: 'success'
            }).then(() => {
                location.href = "/api/goTrashResource/" + id;
            });
        }
    });
}

function insertResource() {
    const searchParams = new URLSearchParams(location.search);

    let method = ['get', 'post', 'put', 'delete']

    let jsonArr = new Array();
    for (let i = 0; i < 4; i++) {
        let paramArr = new Array();
        let resCodeArr = new Array();
        let param = new Object();

        //insertOperationId
        let obj = new Object();

        let operationId = document.getElementById(method[i] + "OperationId")
        let summary = document.getElementById(method[i] + "Summary");
        let tag = document.getElementById(method[i] + "TagBox")

        let reqData = document.getElementById(method[i] + "ReqData")

        // tab display 확인 none이면 값 있어도 등록 안함
        let display = document.getElementById(method[i]).style.display

        // 파리미터 tr
        let param_tr = $('#' + method[i] + "ParameterItemBody tr");

        // 응답코드 tr
        let resCode_tr = $('#' + method[i] + "ResCodeItemBody tr");

        if (operationId.value != "" && display == "block") {

            console.log(operationId.value)
            obj.operation = operationId.value;
            obj.summary = summary.value
            console.log("==============================Tag value ==========================")

            console.log(tag == null)
            if (tag != null) { // 태그가 있다면,
                obj.tag = tag.value.toString()
            } else {
                obj.tag = ""
            }
            /* json check */
            if (method[i] == "post" || method[i] == "put") {
                obj.reqData = reqData.value;
            }

            // 파라미터 추가 시 tr 추출
            $.each(param_tr, function (index, value) {
                param = new Object();
                let tdList = $(value)
                param.name = tdList.children().eq(1).text();
                param.transferMethod = tdList.children().eq(2).text()
                param.explanation = tdList.children().eq(3).text()
                param.required = tdList.children().eq(4).text()
                param.type = tdList.children().eq(5).text()
                param.sample = tdList.children().eq(6).text()
                paramArr.push(param)
            });
            obj.param = paramArr

            // 파라미터 추가 시 tr 추출
            $.each(resCode_tr, function (index, value) {
                param = new Object();
                let tdList = $(value)
                console.log("8888888888888888888888888888")
                console.log(tdList.children())
                param.code = tdList.children().eq(1).text()
                param.explanation = tdList.children().eq(2).text()
                param.type = tdList.children().eq(3).text()
                param.paramKey = tdList.children().eq(5).text()
                param.paramValue = tdList.children().eq(6).text()
                param.paramType = tdList.children().eq(7).text()

                // 여기서 리스트 하나더 돌리자. resCode parameter list
                resCodeArr.push(param)
            });
            obj.resCode = resCodeArr
        }
        jsonArr.push(obj)
    }
    // console.log(document.getElementById("searchPathTbody").children[0])
    // [요청 json 데이터 선언]
    console.log("=====1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1=1")
    console.log(document.getElementById("resourceId"))
    console.log(document.getElementById("resourceId").value)

    let jsonData = { // Body에 첨부할 json 데이터
        "idx": document.getElementById("apiId").value, //api index
        "uriId": document.getElementById("pathid").value,
        "resourceId": document.getElementById("resourceId").value,
        "url": document.getElementById("url").value,
        "path": document.getElementById("path").value,
        "get": JSON.stringify(jsonArr[0]),
        "post": JSON.stringify(jsonArr[1]),
        "put": JSON.stringify(jsonArr[2]),
        "delete": JSON.stringify(jsonArr[3]),
    }


    $.ajax({
        url: "/api/resource/insert",
        data: JSON.stringify(jsonData), //전송 데이터
        dataType: "json",
        method: "post",
        traditional: true, //필수
        async: true, //비동기 여부
        timeout: 5000, //타임 아웃 설정
        dataType: "JSON", //응답받을 데이터 타입 (XML,JSON,TEXT,HTML,JSONP)
        contentType: "application/json; charset=utf-8", //헤더의 Content-Type을 설정

        success: (message) => {
            console.log(message)
        }
    })

    // 추가 버튼 누르면 안의 input value 삭제
    let get_input = $("#resourceModal input[type=text]");

    $.each(get_input, function (index, value) {
        $(value).val('')
    });
    // path 검색 의 내용지우기
    document.getElementById("pathSearchBar").value = '';
    // path 검색 결과 지우기
    document.getElementById("searchPathTbody").innerHTML = ""
    showAllMethod();
    document.getElementById("resourceId").value = null
    setTimeout(() => location.reload(), 150);
    // onClick="location.reload()"
}


// 파라미터 추가 의 추가 버튼
function insertParameter(value) {
    let method = value.parentNode.id.split("-")[0]
    let tbody = document.getElementById(method + "ParameterItemBody")
    let name_val = document.getElementById("parameter-name").value
    let explanation_val = document.getElementById("explanation").value

    if (name_val == "") {
        document.getElementById("parameter-name").parentNode.children[0].style.color = "#1d61ff";

        document.getElementById("parameter-name").focus()
    } else if (explanation_val == "") {
        document.getElementById("explanation").parentNode.children[0].style.color = "#1d61ff";
        document.getElementById("explanation").focus()

    } else {
        let tr = document.createElement("tr")
        let th = document.createElement("th")
        let name = document.createElement("td");

        name.className = "name"
        let transferMethod = document.createElement("td");
        transferMethod.className = "transferMethod"
        let explanation = document.createElement("td");
        explanation.className = "explanation";
        let required = document.createElement("td");
        required.className = "required"
        let type = document.createElement("td");
        type.className = "type"
        let sample = document.createElement("td");
        sample.className = "sample"
        let delUpdate = document.createElement("td");
        delUpdate.className = "delUpdate"

        th.scope = "row";
        th.innerText = tbody.childElementCount + 1

        name.innerText = document.getElementById("parameter-name").value;

        transferMethod.innerText = document.getElementById("transferMethod").value;

        explanation.innerText = document.getElementById("explanation").value;

        required.innerText = document.getElementById("required").value;

        type.innerText = document.getElementById("type").value;

        sample.innerText = document.getElementById("sample").value;
        delUpdate.innerHTML = "<i class=\"fa-solid fa-pen mr-2\" style=\"color: #797a7c;\"></i><i class=\"fa-solid fa-trash-can\" style=\"color: #797a7c; cursor: pointer\"/>"
        delUpdate.onclick = function () {
            resDeleteBeforeInsert(this);
        }
        tr.append(th, name, transferMethod, explanation, required, type, sample, delUpdate)
        tbody.append(tr)

        $("#insertParameterModal").modal('hide')
        $("#resourceModal").modal('show')


        // 추가 버튼 누르면 안의 input value 삭제
        let get_input = $("#insertParameterModal input[type=text]");
        $.each(get_input, function (index, value) {
            $(value).val('')
        });

    }

}

// 응답코드 추가 의 추가 버튼
function insertResCode(value) {
    let method = value.parentNode.id.split("-")[0]
    let tbody = document.getElementById(method + "ResCodeItemBody")
    let code_val = document.getElementById("code-name").value
    let explanation_val = document.getElementById("code-explanation").value

    console.log(tbody)
    console.log(tbody.childElementCount)
    if (code_val == "") {
        document.getElementById("code-name").parentNode.children[0].style.color = "#1d61ff";
        document.getElementById("code-name").focus()
    } else if (explanation_val == "") {
        document.getElementById("code-explanation").parentNode.children[0].style.color = "#1d61ff";
        document.getElementById("code-explanation").focus()

    } else {
        let tr = document.createElement("tr")
        let th = document.createElement("th")

        let code = document.createElement("td");
        code.className = "code"
        let explanation = document.createElement("td");
        explanation.className = "explanation";
        let type = document.createElement("td");
        type.className = "type"
        let delUpdate = document.createElement("td");
        delUpdate.className = "delUpdate"

        //여기서 응답 파라미터 추가해야됨
        console.log("////////////////////////////////resParameter")
        //그냥 하낟 추가하면 안되고
        let resCodeParam_tr = $("#appendJsonResCode tr");
        let cnt = $("#appendJsonResCode tr").length // 해당 응답코드의 파라미터 갯수

        let param;

        let key = [];
        let paramValue = [];
        let paramType = [];

        $.each(resCodeParam_tr, function (index, value) {
            param = new Object();
            let tdList = $(value)
            console.log(tdList.children())
            key.push(tdList.children().eq(1).text())
            paramValue.push(tdList.children().eq(2).text())
            paramType.push(tdList.children().eq(3).text())
        });

        console.log("9999999999999999999999999")

        // let value = resCodeArr.toString()

        let resParamKey = document.createElement("td");
        resParamKey.innerText = key.toString()
        resParamKey.style.display = "none"
        resParamKey.id = "resParamKey"

        //
        let resParamValue = document.createElement("td");
        resParamValue.innerText = paramValue.toString()
        resParamValue.style.display = "none"
        resParamValue.id = "resParamValue"

        let resParamType = document.createElement("td");
        resParamType.innerText = paramType.toString()
        resParamType.style.display = "none"
        resParamType.id = "resParamType"


        //
        // let resParamType = document.createElement("td");
        // resParamType.innerText = document.getElementById("resParamType").innerText
        // resParamType.style.display = "none"
        // resParamType.id = "resParamType"


        th.scope = "row";
        th.innerText = tbody.childElementCount + 1

        code.innerText = document.getElementById("code-name").value;

        explanation.innerText = document.getElementById("code-explanation").value;

        type.innerText = document.getElementById("contentType").value;

        delUpdate.innerHTML = "<i class=\"fa-solid fa-pen mr-2\" style=\"color: #797a7c;\"></i><i class=\"fa-solid fa-trash-can\" style=\"color: #797a7c; cursor: pointer\"/>"
        delUpdate.onclick = function () {
            resDeleteBeforeInsert(this);
        }
        tr.append(th, code, explanation, type, delUpdate, resParamKey, resParamValue, resParamType)

        tbody.append(tr)

        $("#insertResCodeModal").modal('hide')
        $("#resourceModal").modal('show')

        // 추가 버튼 누르면 안의 input value 삭제
        let get_input = $("#insertResCodeModal input[type=text]");
        $.each(get_input, function (index, value) {
            $(value).val('')
        });

        // 추가로 응답 코드 테이블도 삭제해야 됨
        document.getElementById("appendJsonResCode").innerHTML = ""
    }
}

// 파라미터 추가 버튼 클릭시 이동
function paramModalShow(value) {
    let btn = document.getElementsByClassName("modal-footer param")[0]

    btn.id = value.id.split("-")[0] + "-param"
    // btn.id =
    $("#insertParameterModal").modal('show')
}

// 응답코드 추가 버튼 클릭시 이동
function resCodeModalShow(value) {
    let btn = document.getElementsByClassName("modal-footer code")[0]
    btn.id = value.id.split("-")[0] + "-resCode"
    // btn.id =
    $("#insertResCodeModal").modal('show')
}


window.onload = function () {
    let cnt = document.querySelectorAll(".tbody").length;
    let tbody = document.querySelectorAll(".tbody");
    for (let i = 0; i < cnt; i++) {
        let tr = document.createElement("tr");
        let td = document.createElement("td");
        td.innerText = "데이터가 존재하지 않습니다.";
        td.colSpan = 8;
        tr.append(td);
        if (tbody[i].childElementCount == 0) {
            tbody[i].append(tr);
        }
    }

}

function newResourceUri(keyword, defaultUri, tbody) {
    document.getElementById("resourceId").value = null
    console.log("=====================7=7=7=7=7=7=7=7=7=7=7=7=77777777=================================")
    console.log(document.getElementById("resourceId").value)
    let newUri = keyword
    let h5 = document.createElement("h5")
    h5.innerText = newUri
    let tr = document.createElement("tr")
    let td = document.createElement("td")
    td.colSpan = 4
    let newFullPath = defaultUri + '' + h5.innerText
    td.innerText = newFullPath + ' 을 새로 등록하시겠습니까?'

    tr.onclick = function () {
        newInsertPath(newFullPath);
    }
    // 신규라는 의미이다.
    tr.id = "0"


    tr.append(td)
    tbody.append(tr)
}

//     url 실시간 검색
function searchPath(value) {
    let keyword = value.value
    let apisId = document.getElementById("apiId").value
    let defaultUri = document.getElementById("path").name + "/"
    if (keyword.charAt(0) == "/") {
        console.log("첫글자 슬래시")
        keyword = keyword.slice(1)
    }
    console.log("keyword " + keyword)
    let data = {
        "defaultUri": defaultUri,
        "keyword": keyword,
        "apisId": apisId,
    };

    $.ajax({
        url: "/api/search/path",
        type: "get",
        data: data,

        success: (message) => {
            let array = JSON.parse(message.responseText);
            let exactMatchUri = JSON.parse(message.exactMatchUri);
            let tbody = document.getElementById("searchPathTbody")
            tbody.innerHTML = ""
            if (array.length == 0) {
                // DB에 없는 경우
                // 해당 글자를 그대로 받고 + 새로 등록하시겠습니까?
                newResourceUri(keyword, defaultUri, tbody);

            } else {
                if (exactMatchUri.length == 0) {
                    console.log("비슷한건 있어도 정확하게 일치하는건 없다.")
                    console.log(exactMatchUri)
                    newResourceUri(keyword, defaultUri, tbody);
                }
                let arr = []
                for (let i = 0; i < array.length; i++) {
                    arr[i] = array[i].uri
                }

                let result1 = [...new Set(arr)];

                // console.log(result1); //중복 제거됨
                let mt = []
                for (let i = 0; i < result1.length; i++) {
                    let tr = document.createElement("tr")
                    let id = document.createElement("td");
                    let path = document.createElement("td");

                    let possibleMethod = document.createElement("td");

                    let impossibleMethod = document.createElement("td");
                    impossibleMethod.id = 'impossibleMethod'
                    path.innerText = result1[i]


                    tr.id = array[i].id
                    tr.name = array[i].resourceId
                    mt.splice(0, mt.length);
                    let k = 0;
                    for (let j = 0; j < array.length; j++) {
                        if (result1[i] == array[j].uri) {
                            mt[k] = array[j].method
                            // method.innerHTML = array[j].method;
                            k++
                        }
                        impossibleMethod.innerHTML = mt;
                    }

                    mt = mt.sort();

                    let http = ["GET", "POST", "PUT", "DELETE"]

                    http = http.sort();

                    let http2 = ["GET", "POST", "PUT", "DELETE"]

                    http2 = http2.sort();
                    // console.log(http.length)
                    // console.log(mt.length)
                    for (let j = http.length; j >= 0; j--) {
                        for (let l = mt.length; l >= 0; l--) {
                            if (mt[l] == http[j]) {
                                http2.splice(j, 1)

                            }
                        }
                    }
                    possibleMethod.innerHTML = http2
                    impossibleMethod.style.backgroundColor = "rgb(224 0 0 / 22%)"
                    possibleMethod.style.backgroundColor = "rgb(37 224 0 / 22%)"
                    id.innerText = array[i].id
                    tr.onclick = function () {
                        insertPath(event);
                    }
                    console.log("==========1=2=2=2=2=2=2=2=2=2=2=2=2=2=2=2")
                    console.log(array[i])
                    console.log(array[i].resourceId)
                    document.getElementById("resourceId").value = array[i].resourceId
                    console.log("" +
                        "333333333333333333333333333")
                    console.log(document.getElementById("resourceId").value)
                    tr.append(id, path, possibleMethod, impossibleMethod)
                    tbody.append(tr)

                }


            }
        },
        error: (e) => {
            console.log("통신 실패", e);
        }
    })
}

// path 입력하기
function showPathSearchModal() {
    $("#resourceModal").modal('hide')
    $("#searchPathModal").modal('show')
    document.getElementById("defaultUri").innerText = document.getElementById("path").name + " /" // 기본 uri
    showAllMethod()
}


// path 신규 등록 시
function newInsertPath(uri) {

    $("#searchPathModal").modal('hide')

    document.getElementById("path").value = uri
    showAllMethod()

    let pathid = document.getElementById("pathid")
    pathid.value = "0" // 선택한 uri id , 새로 생성 시 0

    // 라라라ㅏ
    console.log("저장 버트 activate 확인ㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹㄹ")
    let resourceBtn = document.getElementById("resourceBtn");
    if ($("path").val()) {
        console.log("잠김")
        resourceBtn.disabled = true;
    } else {
        console.log("풀림")
        resourceBtn.disabled = false;
    }

    $("#resourceModal").modal("show")

}

function checkPath() {
    let btnActivate = document.getElementById("")
}

function tabHidden() {
    let tabList = document.getElementById("myTabContent").children
    let tabHeaderList = document.getElementById("myTab").children

    for (let k = 0; k < tabList.length; k++) {
        tabList[k].className = "tab-pane fade"
        tabHeaderList[k].style.display = "none"
    }
}

// 모든 method 버튼 show all
function showAllMethod() {
    let buttonList = $("#nav-tab button[type=button]");

    for (let i = 0; i < buttonList.length; i++) {
        buttonList[i].style.display = 'block'
    }
}
