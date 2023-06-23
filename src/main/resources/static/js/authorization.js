function eraseWarning(value) {
    let name = value.getAttribute("role_name");
    let id = value.getAttribute("role_id");
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "확인",
        text: name + " 역할을 삭제하시겠습니까?",
        icon: "warning",
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공', icon: 'success'
            }).then(() => {
                location.href = "/roleDelete/" + id;
            });
        }
    });
}

function changeColor() {
    $('#dataTable tbody tr').mouseover(function () {
        $(this).addClass('changeColor');
    }).mouseout(function () {
        $(this).removeClass('changeColor');
    })
}

function changeConfirm() {
    let name = document.querySelector("#authName");
    let description = document.querySelector("#authDesc");
    let code = document.querySelector("#authCode");

    if (name.value === '' || code.value === '') {
        swal({
            title: "권한 추가 처리에 실패하였습니다.", text: "다시 입력해주세요.", icon: "error"
        });
    } else if (description.value === '') {
        swal({
            title: "아무런 설명이 추가되지 않았습니다, 그래도 추가하시겠습니까?", icon: "warning", buttons: true, dangerMode: true,
        })
            .then((willDelete) => {
                if (willDelete) {
                    swal("추가되었습니다.", {
                        icon: "success",
                    });
                } else {
                    swal("취소되었습니다.");
                }
            });
    }
}

function mydataAuthCorrection() {
    $('#mydataAuthCheck').attr("disabled", false);
}

function adminAuthCorrection() {
    $('#adminAuthCheck').attr("disabled", false);
}

function commonAuthCorrection() {
}

function providerAuthCorrection() {
    $('#providerAuthCheck').attr("disabled", false);
}