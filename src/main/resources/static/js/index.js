function saveApi() {
    // 입력 확인
    let context = document.querySelector('.contextApi');
    let name = document.querySelector('.nameApi');
    if (context.value === '') {
        Swal.fire({
            icon: 'error', text: '컨텍스트를 입력해주세요.'
        });
    } else if (name.value === '') {
        Swal.fire({
            icon: 'error', text: '이름을 입력해주세요.'
        });
    } else {
        Swal.fire({
            icon: 'success', text: 'API 그룹이 저장되었습니다.'
        }).then(() => {
            // location.href = '/api/insert';
            $('#registerApi').submit();
            $('#registerModal').modal('hide');
        });
    }
}

function saveApiUpdate() {
    if ($('#contextApi').val().length === 0) {
        Swal.fire({
            icon: 'error', text: '컨텍스트를 입력해주세요.'
        });
    } else if ($('#nameApi').val().length === 0) {
        Swal.fire({
            icon: 'error', text: '이름을 입력해주세요.'
        });
    } else {
        Swal.fire({
            icon: 'success', text: 'API 그룹이 저장되었습니다.'
        }).then(() => {
            $('#updateBtn').submit();
            $('#updateModal').modal('hide');
        });
    }
}

function removeApi(id) {
    Swal.fire({
        showCancelButton: true,
        cancelButtonText: "취소",
        confirmButtonText: "삭제",
        icon: 'warning',
        text: "\'XX업권\' API 그룹을 삭제하시겠습니까?"
    }).then((result) => {
        if (result.isConfirmed) {
            Swal.fire({
                title: '삭제 성공', icon: 'success'
            }).then(() => {
                location.href = "/api/delete/" + id;
            });
        }
    });
}

//
// // role 등록 part
// function checkRole() {
//
//     let admin = document.getElementById('ROLE_ADMIN');
//     let normal = document.getElementById('ROLE_NORMAL');
//     let api = document.getElementById('ROLE_PROVIDER');
//     let md = document.getElementById('ROLE_MYDATA');
//
//     let adminBadge = $(document.createElement("span")).addClass("badge badge-secondary admin").attr('style', "margin-right:2px").text(admin.value);
//     let normalBadge = $(document.createElement("span")).addClass("badge badge-secondary normal").attr('style', "margin-right:2px").text(normal.value);
//     let apiBadge = $(document.createElement("span")).addClass("badge badge-secondary api").attr('style', "margin-right:2px").text(api.value);
//     let mdBadge = $(document.createElement("span")).addClass("badge badge-secondary md").attr('style', "margin-right:2px").text(md.value);
//
//     let radimin = document.querySelector('.admin');
//     let rnormal = document.querySelector('.normal');
//     let rapi = document.querySelector('.api');
//     let rmd = document.querySelector('.md');
//
//     if (admin.checked && radimin == null) {
//         $('#target').append(adminBadge);
//     } else if (radimin != null && !admin.checked) {
//         $('.admin').remove();
//     }
//
//     if (normal.checked && rnormal == null) {
//         $('#target').append(normalBadge);
//     } else if (rnormal != null && !normal.checked) {
//         $('.normal').remove();
//     }
//
//     if (api.checked && rapi == null) {
//         $('#target').append(apiBadge);
//     } else if (rapi != null && !api.checked) {
//         $('.api').remove();
//     }
//
//     if (md.checked && rmd == null) {
//         $('#target').append(mdBadge);
//     } else if (rmd != null && !md.checked) {
//         $('.md').remove();
//     }
// }
//
// // role 수정 part
// function checkRole2() {
//     let admin = document.getElementById('admin2');
//     let normal = document.getElementById('normal2');
//     let api = document.getElementById('api2');
//     let md = document.getElementById('md2');
//
//     let adminBadge = $(document.createElement("span")).addClass("badge badge-secondary admin").attr('style', "margin-right:2px").text(admin.textContent);
//     let normalBadge = $(document.createElement("span")).addClass("badge badge-secondary normal").attr('style', "margin-right:2px").text(normal.textContent);
//     let apiBadge = $(document.createElement("span")).addClass("badge badge-secondary api").attr('style', "margin-right:2px").text(api.textContent);
//     let mdBadge = $(document.createElement("span")).addClass("badge badge-secondary md").attr('style', "margin-right:2px").text(md.textContent);
//
//     let radimin = document.querySelector('.admin');
//     let rnormal = document.querySelector('.normal');
//     let rapi = document.querySelector('.api');
//     let rmd = document.querySelector('.md');
//
//     if (admin.checked && radimin == null) {
//         $('#target2').append(adminBadge);
//     } else if (radimin != null && !admin.checked) {
//         $('.admin').remove();
//     }
//
//     if (normal.checked && rnormal == null) {
//         $('#target2').append(normalBadge);
//     } else if (rnormal != null && !normal.checked) {
//         $('.normal').remove();
//     }
//
//     if (api.checked && rapi == null) {
//         $('#target2').append(apiBadge);
//     } else if (rapi != null && !api.checked) {
//         $('.api').remove();
//     }
//
//     if (md.checked && rmd == null) {
//         $('#target2').append(mdBadge);
//     } else if (rmd != null && !md.checked) {
//         $('.md').remove();
//     }
// }
//
// modal 선택 시 안 넘어감
function insertApis(e, id) {
    if (e.target.parentNode.className == "h3 font-weight-bold text-primary mb-1" || e.target.parentNode.className == "col mr-2") {
        location.href = '/api/details/' + id;
        console.log(e.target.parentNode.className)
    } else {
        console.log("해당없음!!")
    }
}

//
// function showRole() {
//     $.ajax({
//         url: "/role/selectAll", method: "get", success: (result) => {
//             for (let i = 0; i < result.length; i++) {
//                 // document.getElementById("reset").addEventListener('hidden.bs.modal', function (event) {
//                 //     $('#' + result[i].code).prop("checked", false);
//                 // })
//                 console.log("추가부분"+i)
//                 console.log(document.getElementById(result[i].code))
//                 console.log(document.getElementById(result[i].code).checked)
//                 console.log(document.querySelector('.' + result[i].code) == null)
//                 console.log("==============================")
//                 console.log("삭제부분"+i)
//                 console.log(document.querySelector('.' + result[i].code)!= null)
//                 console.log(!document.getElementById(result[i].code).checked)
//                 console.log("##########################################")
//                 if (document.getElementById(result[i].code).checked && document.querySelector('.' + result[i].code) == null) {
//                     console.log("추가중")
//                     $('#target').append($(document.createElement("input")).addClass("roles " + result[i].code).attr({
//                         'value': result[i].id, 'name': 'roleId', 'type': 'hidden'
//                     }));
//                     $('#target').append($(document.createElement("span")).addClass("badge badge-secondary roles " + result[i].code).attr({
//                         'style': "margin-right:2px", 'value': result[i].id
//                     }).text(result[i].name));
//                 } else if (document.querySelector('.' + result[i].code) != null && !document.getElementById(result[i].code).checked) {
//
//                     $('.' + result[i].code).remove();
//                 }
//             }
//         }
//     })
// }
//
// function showRole2() {
//     $.ajax({
//         url: "/role/selectAll", method: "get", success: (result) => {
//             for (let i = 0; i < result.length; i++) {
//                 console.log("추가부분"+i)
//                 console.log(document.getElementById(result[i].code))
//                 console.log(document.getElementById(result[i].code).checked)
//                 console.log($('#'+result[i].code).prop("checked"))
//                 console.log(document.querySelector('.' + result[i].code))
//                 console.log(document.querySelector('.' + result[i].code) == null)
//                 console.log("==============================")
//                 console.log("삭제부분"+i)
//                 console.log(document.querySelector('.' + result[i].code)!= null)
//                 console.log(!document.getElementById(result[i].code).checked)
//                 console.log("##########################################")
//                 // document.getElementById("roleUpdateModal").addEventListener('hidden.bs.modal', function (event) {
//                 //     $('#' + result[i].code).prop("checked", false);
//                 // })
//                 if (document.getElementById(result[i].code).checked && document.querySelector('.' + result[i].code) == null) {
//                     console.log("뱃지 만드는 곳")
//                     $('#target2').append($(document.createElement("input")).addClass("roles " + result[i].code).attr({
//                         'value': result[i].id, 'name': 'roleId', 'type': 'hidden'
//                     }));
//                     $('#target2').append($(document.createElement("span")).addClass("badge badge-secondary roles " + result[i].code).attr({
//                         'style': "margin-right:2px", 'value': result[i].id
//                     }).text(result[i].name));
//                 } else if (document.querySelector('.' + result[i].code) != null && !document.getElementById(result[i].code).checked) {
//                     console.log("뱃지 삭제하는 곳")
//                     $('.' + result[i].code).remove();
//                 }
//             }
//         }
//     })
// }
//
function updateApi(apiOne) {
    console.log("update Api : 연필 모달")
    let context = document.getElementById('contextApi');
    let name = document.getElementById('nameApi');
    let explanation = document.getElementById('explanationApi');

    let apiId = apiOne.getAttribute("apiid");
    let data = {
        "apiId": apiId,
    };
    $('#radio1').prop("checked", false);
    $('#radio2').prop("checked", false);
    $.ajax({
        url: "/api/selectOne",
        data: data,
        method: "get",

        success: (message) => {

            context.value = message.apiContext;
            name.value = message.apiName;
            explanation.textContent = message.apiExplanation;


            if (message.apiDisclosure == true) {
                $('#radio1').prop("checked", true);
            } else {
                $('#radio2').prop("checked", true);
            }
            $('#updateApi').attr("action", "/api/update/" + apiId)
        }, error: (e) => {
            console.log("출력실패", e)
        }
    })
    // 모달 나오기전에 다 지우기 => 모달 공유하고 있기 때문
    let data2 = {
        "apiId": apiId
    }
    $.ajax({
        url: "/api/roleList", data: data2, method: "get", success: (message) => {
            console.log(message);
            console.log(typeof message.roleAll)
            let roleJson = JSON.parse(message)
            console.log(typeof roleJson)
            console.log(typeof roleJson.length)
            console.log(typeof roleJson.size)

            let roleBox = document.getElementById("roleBox");
            for (let i = 0; i < message.roleAll.length; i++) {
                document.g
            }
            // console.log(Object.keys(message));
            // console.log(Object.values(message));
            // console.log(Object.values(message).length);
            //
            // for (let i = 0; i < Object.values(message).length; i++) {
            //     console.log("role 뱃지 지우는 중" + i)
            //     document.getElementById("updateModal").addEventListener('hidden.bs.modal', function (event) {
            //         $('.roles').remove();
            //     })
            //     $('#target2').append($(document.createElement("input")).addClass("roles " + Object.keys(message)[i]).attr({
            //         'name': 'roleId', 'type': 'hidden'
            //     }));
            //     $('#target2').append($(document.createElement("span")).addClass("badge badge-secondary roles " + Object.keys(message)[i]).attr({'style': "margin-right:2px"}).text(Object.values(message)[i]));
            //
            // }

        }
    })


    $('#updateModal').modal('show');
    // let admin = document.getElementById('admin2');
    // let normal = document.getElementById('normal2');
    // let api = document.getElementById('api2');
    // let md = document.getElementById('md2');
}


// document.addEventListener("DOMContentLoaded", function () {
//     let myModalEls = document.getElementById("roleModal")
//     myModalEls.addEventListener('hidden.bs.modal', function (event) {
//         // do something...
//         $('#ROLE_ADMIN').prop("checked", false);
//         $('#ROLE_NORMAL').prop("checked", false);
//         $('#ROLE_PROVIDER').prop("checked", false);
//         $('#ROLE_MYDATA').prop("checked", false);
//
//     })
// })\


////////////////////////////////////////////////////////////////////////////////////////////////
//                                         juhee 🎆                                           //
////////////////////////////////////////////////////////////////////////////////////////////////

























