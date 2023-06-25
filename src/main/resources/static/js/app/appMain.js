function disconnectAcc(industry) {
    let target = document.getElementsByClassName("listHover");
    for (let i = target.length - 1; i >= 0; i--) {
        // 체크박스 생성
        let input = document.createElement("input");
        input.className = "form-check bankOne";
        input.type = "checkbox";
        input.name = "bank";
        $(input).attr("onclick", "cntCheck()");
        target[i].append(input);
    }

    // 버튼들 지우기
    let btns = document.getElementById("plusMinus");
    btns.innerHTML = "";

    //버튼 생성 ( 해제 , 취소 )
    let deleteBtn = document.createElement("button");
    deleteBtn.className = "btn btn-delete";
    deleteBtn.id = "btn-delete";
    deleteBtn.type = "button";
    deleteBtn.innerText = "연결 해제";
    deleteBtn.disabled = true;
    // 업권하고, 기관코드 넣어줘야함/ 함수 만들고
    // $(deleteBtn).attr("onclick", "deleteAcc(" + industry + ")");
    deleteBtn.onclick = function () {
        deleteAcc(industry);
    }

    let cancelBtn = document.createElement("button");
    cancelBtn.className = "btn btn-cancel";
    cancelBtn.id = "btn-cancel";
    cancelBtn.type = "button";
    cancelBtn.innerText = "취소";

    // 해당 업권 클릭하게 해도 됨
    // id 값이 industry 로 찾아가 클릭 이벤트 주면 될듯
    $(cancelBtn).attr("onclick", "window.location.reload()");

    btns.append(deleteBtn, cancelBtn);


}

// 각 카테고리 버튼

function showCategory(value) {
    let target = document.getElementById('listBox-1');
    // 목록 초기화
    target.innerHTML = "";
    let category = value.getAttribute("id");
    let custnum = value.getAttribute("custnum");
    let data = {
        "clientNum": custnum, "industry": category,
    }

    // 각 버튼
    if (category == "card") {
        // 목록 이름
        document.getElementById('listName').innerText = "My 카드 목록";

        //버튼 생성 ( 추가 , 해제 )
        let deleteBtn = document.createElement("button");
        deleteBtn.className = "btn btn-minus";
        deleteBtn.id = "btn-minus";
        deleteBtn.type = "button";
        deleteBtn.innerText = "- 해제";
        // 업권하고, 기관코드 넣어줘야함/ 함수 만들고
        $(deleteBtn).attr("onclick", "disconnectAcc('card')");

        let plusBtn = document.createElement("button");
        plusBtn.className = "btn btn-plus";
        plusBtn.id = "btn-plus";
        plusBtn.type = "button";
        plusBtn.innerText = "+ 추가";
        $(plusBtn).attr("onclick", "sorry()");

        // 버튼 삭제 후 넣기

        let btns = document.getElementById('plusMinus');
        btns.innerHTML = "";
        btns.append(plusBtn, deleteBtn);

        // ajax로 목록 만들기
        $.ajax({
            url: "/app/card/myAccount", data: data, type: "post",

            success: (message) => {
                let cardList = JSON.parse(message.cardList);

                if (cardList.length == 0) {
                    let div_1 = document.createElement("div");
                    let div_2 = document.createElement("div");
                    let div_3 = document.createElement("div");
                    let span_1 = document.createElement("span");
                    let span_2 = document.createElement("span");

                    div_1.className = "oneBox";
                    div_2.className = "fontXL noList";
                    div_3.className = "fontL noList";
                    span_1.innerText = "연결된 자산이 없습니다.";
                    span_2.innerText = "자산을 추가하시려면 아래 추가 버튼을 눌러주세요.";

                    div_2.append(span_1);
                    div_3.append(span_2);
                    div_1.append(div_2, div_3);

                    target.append(div_1);

                    document.getElementById("btn-minus").disabled = true;
                }
            }
        });
    } else if (category == "bank") {
        // 목록 이름
        document.getElementById('listName').innerText = " My 은행 목록";

        //버튼 생성 ( 추가 , 해제 )
        let deleteBtn = document.createElement("button");
        deleteBtn.className = "btn btn-minus";
        deleteBtn.id = "btn-minus";
        deleteBtn.type = "button";
        deleteBtn.innerText = "- 해제";
        // 업권하고, 기관코드 넣어줘야함/ 함수 만들고
        $(deleteBtn).attr("onclick", "disconnectAcc('bank')");

        let plusBtn = document.createElement("button");
        plusBtn.className = "btn btn-plus";
        plusBtn.id = "btn-plus";
        plusBtn.type = "button";
        plusBtn.innerText = "+ 추가";
        $(plusBtn).attr("onclick", "location.href='/app/bank/insert/bank'");

        // 버튼 삭제 후 넣기

        let btns = document.getElementById('plusMinus');
        btns.innerHTML = "";
        btns.append(plusBtn, deleteBtn);

        $.ajax({
            url: "/app/bank/myAccount", data: data, type: "post",

            success: (message) => {

                // 데이터
                let agency = JSON.parse(message.agency);
                let bankList = JSON.parse(message.bankList);
                // 해당 업권에 계좌 정보가 하나도 없을 때
                if (bankList.length == 0) {
                    let div_1 = document.createElement("div");
                    let div_2 = document.createElement("div");
                    let div_3 = document.createElement("div");
                    let span_1 = document.createElement("span");
                    let span_2 = document.createElement("span");

                    div_1.className = "oneBox";
                    div_2.className = "fontXL noList";
                    div_3.className = "fontL noList";
                    span_1.innerText = "연결된 자산이 없습니다.";
                    span_2.innerText = "자산을 추가하시려면 아래 추가 버튼을 눌러주세요.";

                    div_2.append(span_1);
                    div_3.append(span_2);
                    div_1.append(div_2, div_3);

                    target.append(div_1);

                    document.getElementById("btn-minus").disabled = true;
                } else {
                    document.getElementById("btn-minus").disabled = false;
                    for (let i = 0; i < bankList.length; i += 2) {
                        let reqData = JSON.parse(message.bankList)[i];
                        let resData = JSON.parse(message.bankList)[i + 1];

                        let div_1 = document.createElement("div");

                        div_1.className = "oneBox";
                        div_1.id = reqData.org_code;
                        $(div_1).attr("industry", category);

                        $(div_1).attr("onclick", "showAccount(this)");
                        let div_2 = document.createElement("div");
                        div_2.className = "listHover";
                        div_2.id = reqData.org_code + "1";

                        let div_3 = document.createElement("div")
                        div_3.className = "d-flex align-items-center";

                        let h1 = document.createElement("h1");
                        h1.className = "bankName";

                        let span = document.createElement("span")
                        span.className = "box";
                        let img = document.createElement("img");
                        img.className = "profile";
                        // 기관 찾기
                        for (let j = 0; j < agency.length; j++) {
                            let org_code = agency[j].org_code;
                            // 은행 로고 + 이름 넣기
                            if (org_code == reqData.org_code) {

                                img.src = "/images/bank/" + agency[j].logo;
                                h1.innerText = agency[j].bankName;
                            }
                        }
                        let div_4 = document.createElement("div");
                        div_4.className = "badge badge-secondary cntAccount";
                        div_4.innerText = resData.account_cnt;

                        span.append(img);
                        div_3.append(span, h1, div_4);
                        div_2.append(div_3);
                        div_1.append(div_2);

                        target.append(div_1);
                    }
                }

            }
        });
    } else if (category == "invest") {
        // 목록 이름
        document.getElementById('listName').innerText = " My 투자 목록";

        //버튼 생성 ( 추가 , 해제 )
        let deleteBtn = document.createElement("button");
        deleteBtn.className = "btn btn-minus";
        deleteBtn.id = "btn-minus";
        deleteBtn.type = "button";
        deleteBtn.innerText = "- 해제";
        $(deleteBtn).attr("onclick", "disconnectAcc('invest')");

        let plusBtn = document.createElement("button");
        plusBtn.className = "btn btn-plus";
        plusBtn.id = "btn-plus";
        plusBtn.type = "button";
        plusBtn.innerText = "+ 추가";
        $(plusBtn).attr("onclick", "location.href='/app/invest/insert/invest'");

        // 버튼 삭제 후 넣기

        let btns = document.getElementById('plusMinus');
        btns.innerHTML = "";
        btns.append(plusBtn, deleteBtn);

        // ajax로 목록 만들기
        $.ajax({
            url: "/app/invest/myAccount", data: data, type: "post",

            success: (message) => {
                // 데이터
                let agency = JSON.parse(message.agency);
                let investList = JSON.parse(message.investList);
                // 해당 업권에 계좌 정보가 하나도 없을 때
                if (investList.length == 0) {
                    let div_1 = document.createElement("div");
                    let div_2 = document.createElement("div");
                    let div_3 = document.createElement("div");
                    let span_1 = document.createElement("span");
                    let span_2 = document.createElement("span");

                    div_1.className = "oneBox";
                    div_2.className = "fontXL noList";
                    div_3.className = "fontL noList";
                    span_1.innerText = "연결된 자산이 없습니다.";
                    span_2.innerText = "자산을 추가하시려면 아래 추가 버튼을 눌러주세요.";

                    div_2.append(span_1);
                    div_3.append(span_2);
                    div_1.append(div_2, div_3);

                    target.append(div_1);

                    document.getElementById("btn-minus").disabled = true;
                } else {
                    document.getElementById("btn-minus").disabled = false;

                    for (let i = 0; i < investList.length; i += 2) {
                        let reqData = investList[i];
                        let resData = investList[i + 1];

                        let div_1 = document.createElement("div");

                        div_1.className = "oneBox";
                        div_1.id = reqData.org_code;
                        $(div_1).attr("industry", category);

                        $(div_1).attr("onclick", "showAccount(this)");
                        let div_2 = document.createElement("div");
                        div_2.className = "listHover";
                        div_2.id = reqData.org_code + "1";

                        let div_3 = document.createElement("div")
                        div_3.className = "d-flex align-items-center";

                        let h1 = document.createElement("h1");
                        h1.className = "bankName";

                        let span = document.createElement("span")
                        span.className = "box";
                        let img = document.createElement("img");
                        img.className = "profile";
                        // 기관 찾기
                        for (let j = 0; j < agency.length; j++) {
                            let org_code = agency[j].org_code;
                            // 은행 로고 + 이름 넣기
                            if (org_code == reqData.org_code) {

                                img.src = "/images/invest/" + agency[j].logo;
                                h1.innerText = agency[j].agencyName;
                            }
                        }
                        let div_4 = document.createElement("div");
                        div_4.className = "badge badge-secondary cntAccount";
                        div_4.innerText = resData.account_cnt;

                        span.append(img);
                        div_3.append(span, h1, div_4);
                        div_2.append(div_3);
                        div_1.append(div_2);

                        target.append(div_1);
                    }
                }
            }

        });
    } else if (category == "insu") {
        // 목록 이름
        document.getElementById('listName').innerText = " My 보험 목록";
        //버튼 생성 ( 추가 , 해제 )
        let deleteBtn = document.createElement("button");
        deleteBtn.className = "btn btn-minus";
        deleteBtn.id = "btn-minus";
        deleteBtn.type = "button";
        deleteBtn.innerText = "- 해제";
        // 업권하고, 기관코드 넣어줘야함/ 함수 만들고
        $(deleteBtn).attr("onclick", "disconnectAcc('insu')");

        let plusBtn = document.createElement("button");
        plusBtn.className = "btn btn-plus";
        plusBtn.id = "btn-plus";
        plusBtn.type = "button";
        plusBtn.innerText = "+ 추가";
        $(plusBtn).attr("onclick", "sorry()");

        // 버튼 삭제 후 넣기

        let btns = document.getElementById('plusMinus');
        btns.innerHTML = "";
        btns.append(plusBtn, deleteBtn);

        // ajax로 목록 만들기
        $.ajax({
            url: "/app/insu/myAccount", data: data, type: "post",

            success: (message) => {
                let insuList = JSON.parse(message.insuList);

                if (insuList.length == 0) {
                    let div_1 = document.createElement("div");
                    let div_2 = document.createElement("div");
                    let div_3 = document.createElement("div");
                    let span_1 = document.createElement("span");
                    let span_2 = document.createElement("span");

                    div_1.className = "oneBox";
                    div_2.className = "fontXL noList";
                    div_3.className = "fontL noList";
                    span_1.innerText = "연결된 자산이 없습니다.";
                    span_2.innerText = "자산을 추가하시려면 아래 추가 버튼을 눌러주세요.";

                    div_2.append(span_1);
                    div_3.append(span_2);
                    div_1.append(div_2, div_3);

                    target.append(div_1);

                    document.getElementById("btn-minus").disabled = true;
                }
            }
        });
    }
}


function sorry() {
    Swal.fire({
        title: "자산 연결은 은행 업권과 투자 업권에서만 가능합니다.",
    })
}

// 해제 누르면 체크박스 생성 및 버튼 교환

// 자산 해제 마지막 단계 (delete)
function deleteAcc(industry) {

    let chbox = document.getElementsByClassName('bankOne');
    let total = chbox.length;
    let list = [];
    for (let i = 0; i < total; i++) {
        if (chbox[i].checked) {

            list.push(chbox[i].parentNode.parentNode.id);

        }
    }
    // let arraylist = []
    let data = {
        "choiceAgency": list, "industry": industry,
    }

    Swal.fire({
        title: "정말 연결해제 하시겠습니까?",
        cancelButtonText: "취소",
        showCancelButton: true,
        confirmButtonText: "예",
        icon: "warning",

    }).then((result) => {
        if (result.isConfirmed) {
            // arraylist.push(data)
            $.ajax({
                url: "/app/deleteAgency", data: data, type: "post",

                success: (message) => {
                    console.log("삭제 성공");
                    Swal.fire({
                        title: "삭제 성공",
                    }).then(() => {
                        // 여기 클릭하게
                        // location.href = '/app/main';
                        $('#' + industry).click();
                    })

                }
            });
        }
    });


}

// 체크박스 체크되면 버튼 disabled 비활성화
function cntCheck() {
    let cnt = 0;
    let checkboxes = document.getElementsByName('bank');
    checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
            cnt = cnt + 1;
        }
    })

    if (cnt > 0) {
        document.getElementById('btn-delete').disabled = false;
    } else {
        document.getElementById('btn-delete').disabled = true;
    }
}
