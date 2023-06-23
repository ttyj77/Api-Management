function removeAlert(reqId) {
    let id = reqId.getAttribute("req_id");
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "확인",
        icon: 'warning',
        text: "해당 요청을 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 완료', icon: 'success'
            }).then(() => {
                location.href = '/delete/' + id;
            })
        }
    });
}

// 승인
function approve(id) {
    Swal.fire({
        icon: 'success', title: '승인 완료'
    }).then(() => {
        location.href = '/approve/' + id;
    })
}

// 거절
function refuse(id) {
    Swal.fire({
        icon: 'success', title: '거절 완료'
    }).then(() => {
        location.href = '/refuse/' + id;
    })
}

// 요청 정보 모달에 넣을 데이터
function reqDetail(reqId) {

    let id = reqId.getAttribute("req_id");
    let data = {
        "id": id,
    }
    $.ajax({
        url: "/selectOne/" + id, data: data, method: "get",

        success: (message) => {
            console.log(message);
            document.getElementById('reqId').innerText = id;
            let title = document.getElementsByClassName('reqTitle');
            for (let i = 0; i < title.length; i++) {
                title[i].innerText = message.title;
            }
            document.getElementById('reqUsername').innerText = message.reqUsername;
            document.getElementById('reqNickname').innerText = message.reqNickname;
            document.getElementById('reqEntryDate').innerText = message.entryDate;
            document.getElementById('reqId').innerText = id;
            document.getElementById('reqContent').innerText = message.content;

            if (message.status == true) {
                document.getElementById('status').innerText = '승인';
            } else if (message.status == false) {
                document.getElementById('status').innerText = '거절';
            } else if (message.status == null) {
                document.getElementById('status').innerText = '요청';
                $('#approve').attr('onclick', 'approve(' + message.userId + ')').css("display", "block");
                $('#refuse').attr('onclick', 'refuse(' + message.userId + ')').css("display", "block");
            }
            if (message.status != null) {
                document.getElementById('statusNotNull').innerText = '성공';
                document.getElementById('reqProcDate').innerText = message.procDate;
                document.getElementById('reqProcUsername').innerText = message.procUsername;
                $('#approve').css("display", "none");
                $('#refuse').css("display", "none");
            }

        }
    })
}
