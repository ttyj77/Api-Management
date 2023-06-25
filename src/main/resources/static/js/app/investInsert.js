window.onload = function () {
    let total_bank = document.getElementsByClassName("banks").length;
    document.getElementById("total-bank").innerText = total_bank;
    let checkboxes = document.getElementsByName('bank');
    let cnt = 0;
    checkboxes.forEach((checkbox) => {
        if (document.getElementById("checkAll").checked == true) {
            cnt = cnt - 1;
        } else {
            if (checkbox.checked) {
                cnt = cnt + 1;
            }
        }
    })

    document.getElementById("choose-bank").innerText = cnt;
};

function cntCheck() {
    let cnt = 0;
    let checkboxes = document.getElementsByName('bank');
    checkboxes.forEach((checkbox) => {
        if (document.getElementById("checkAll").checked) {
            cnt = cnt - 1;
        } else {
            if (checkbox.checked) {
                cnt = cnt + 1;
            }
        }
    })
    // 체크박스 갯수
    let chbox = document.getElementsByClassName('bankOne');
    let total = chbox.length;
    for (let i = 0; i < total; i++) {
        if (document.getElementById("checkAll").checked) {
            if (!chbox[i].checked) {
                $('#checkAll').prop("checked", false);
                cnt = total - 1;
            }
        }

    }

    document.getElementById("choose-bank").innerText = cnt;

    if (cnt > 0) {
        document.getElementById('connectProperty').disabled = false;
    } else {
        document.getElementById('connectProperty').disabled = true;
    }
}

function selectAll(selectAll) {
    const checkboxes = document.getElementsByName('bank');
    let total = document.getElementsByClassName('bankOne').length;

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAll.checked;
    })
    if (document.getElementById('checkAll').checked == true) {
        document.getElementById("choose-bank").innerText = total;
        document.getElementById('connectProperty').disabled = false;
    } else {
        document.getElementById("choose-bank").innerText = 0;
        document.getElementById('connectProperty').disabled = true;
    }
}

function addProperty() {
    let chbox = document.getElementsByClassName('bankOne');
    let total = chbox.length;
    let list = [];
    for (let i = 0; i < total; i++) {
        if (chbox[i].checked) {

            list.push(chbox[i].parentNode.children[0].children[1].innerText);
            $('#checkAll').prop("checked", false);
            cnt = total - 1;
        }
    }

    location.href = '/app/certificationSendReq/' + list;
}

function endAdd() {
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "아니요",
        confirmButtonText: "예",
        // cancelButtonColor: '#f5f5f5',
        confirmButtonColor: '#8c7ae6',
        title: "자산 연결을 종료하시겠어요?",
        text: "종료하면 처음부터 다시 시작해야해요.",
        // reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            location.href = '/app/main';
        }
    });
}
