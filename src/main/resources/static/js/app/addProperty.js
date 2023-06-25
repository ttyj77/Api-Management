let org_code = []

function endAdd() {
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "아니요",
        confirmButtonText: "예",
        confirmButtonColor: '#8c7ae6',
        title: "자산 연결을 종료하시겠어요?",
        text: "종료하면 처음부터 다시 시작해야해요.",
    }).then((result) => {
        if (result.isConfirmed) {
            location.href = '/app/main';
        }
    });
}


// input box 갯수 세기
function cntCheck(value) {
    let cnt = 0;
    let checkboxes = document.getElementsByName('property');
    checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
            cnt = cnt + 1;
        }
    })

    if (cnt > 0) {
        document.getElementById('nextBtn').disabled = false;
    } else {
        document.getElementById('nextBtn').disabled = true;
    }
    /*선택한 금융상품의 기관 코드 가져오기 */

    org_code.push(value.parentNode.id)


}

function connectProperty() {

    /* 코드 중복제거 */
    const set = new Set(org_code);
    const uniqueOrgArr = [...set];

    let chbox = document.getElementsByClassName('bankOne');
    let total = chbox.length;
    let list = [];
    for (let i = 0; i < total; i++) {
        if (chbox[i].checked) {
            list.push(chbox[i].parentNode.children[0].children[1].innerText);
        }
    }

    // let org_code = ;
    let data = {
        "accountList": list,
        "org_code_list": uniqueOrgArr,

    }
    // 자산 연결
    $.ajax({
        url: "/app/connectAgency",
        data: data,
        type: 'post',

        success: () => {
            location.href = '/app/main';
        }
    })
    // 스케쥴링 하는가에 따라 달라짐
    // location.href = '/app/sendReq';

}