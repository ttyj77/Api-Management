window.onload = function () {
    document.getElementById("agree").checked = false;
};

// 필수 checkbox 확인
function goCheck() {
    let checkAgree = document.getElementById("agree");
    if (checkAgree.checked == false) {
        checkAgree.checked = true;
        document.getElementById("nextBtn").disabled = false;
    } else {
        checkAgree.checked = false;
        document.getElementById("nextBtn").disabled = true;
    }
}

function goCheckInput() {
    let checkAgree = document.getElementById("agree");
    if (checkAgree.checked == false) {
        document.getElementById("nextBtn").disabled = true;
    } else {
        document.getElementById("nextBtn").disabled = false;
    }
}

function endAdd() {
    Swal.fire({
        showCancelButton: true, cancelButtonText: "아니요", confirmButtonText: "예", // cancelButtonColor: '#f5f5f5',
        confirmButtonColor: '#8c7ae6', title: "자산 연결을 종료하시겠어요?", text: "종료하면 처음부터 다시 시작해야해요.", // reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            location.href = '/app/main';
        }
    });
}

