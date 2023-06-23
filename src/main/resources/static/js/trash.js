function goReturn(one) {
    let id = one.getAttribute('id');
    let resource = one.getAttribute('resource');
    let detail = one.getAttribute('detail');
    if (resource == null) {
        Swal.fire({
            showCancelButton: true,
            cancelButtonText: "취소",
            confirmButtonText: "확인",
            icon: 'warning',
            text: "메소드를 복구하시겠습니까??"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: '복구 성공',
                    icon: 'success'
                }).then(() => {
                    location.href = "/api/return/" + id;
                })
            }
        });
    } else {
        Swal.fire({
            showCancelButton: true,
            cancelButtonText: "취소",
            confirmButtonText: "확인",
            icon: 'warning',
            text: "리소스를 복구하시겠습니까??"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: '복구 성공',
                    icon: 'success'
                }).then(() => {
                    location.href = "/api/returnResource/" + id;
                })
            }
        });
    }

}

function completeDelete(one) {
    console.log(one)
    let id = one.getAttribute('id');
    let resource = one.getAttribute('resource');
    let detail = one.getAttribute('detail');
    console.log(resource);
    console.log(detail);
    console.log(id);
    if (resource == null) {
        Swal.fire({
            showCancelButton: true,
            cancelButtonText: "취소",
            confirmButtonText: "확인",
            icon: 'warning',
            text: "메소드를 삭제하시겠습니까??"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: '삭제 성공',
                    icon: 'success'
                }).then(() => {
                    location.href = "/api/completeDelete/" + id;
                })
            }
        });
    } else {
        Swal.fire({
            showCancelButton: true,
            cancelButtonText: "취소",
            confirmButtonText: "확인",
            icon: 'warning',
            text: "리소스를 삭제하시겠습니까??"
        }).then((result) => {
            if (result.isConfirmed) {
                Swal.fire({
                    title: '삭제 성공',
                    icon: 'success'
                }).then(() => {
                    location.href = "/api/resourceDelete/" + id;
                })
            }
        });
    }
}

window.onload = function () {
    let target = document.querySelectorAll('.apiContext');
    console.log(target)
    let h3 = document.createElement('h3').innerText = "# APIs";
    target[0].append(h3);
}