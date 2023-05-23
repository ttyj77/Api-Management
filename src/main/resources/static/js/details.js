function detailModal(value) {
    // console.log(value.children())
    $("#detailModal").modal('show')
    let details = value.lastElementChild.lastElementChild;

    let id = details.getAttribute("detailid")
    let data = {
        "id": id,
    }

    $.ajax({
        url: "/api/select/detail",
        data: data,
        method: "get",

        success: (message) => {
            console.log(message)
            let parameterList = JSON.parse(message.parameterList)
            console.log(parameterList)
            console.log(document.getElementById("dModal_url"))

            document.getElementById("dModal_url").innerText = message.url + message.uri;
            console.log(message.url)
            console.log(message.uri)
            document.getElementById("dModal_method").innerText = message.method
            document.getElementById("dModal_summary").innerText = message.summary.replace(/\"/gi, "");
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

                name.innerText = parameterList[i].name.replace(/\"/gi, "");

                explanation.innerText = parameterList[i].explanation.replace(/\"/gi, "");
                sample.innerText = parameterList[i].sample.replace(/\"/gi, "");


                tr.append(name, transferMethod, explanation, required, type, sample)
                tbody.append(tr)

            }
            // document.getElementById("dModal_parameter_Name").innerText = message

            //  요청Body 정보

            //  응답


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
