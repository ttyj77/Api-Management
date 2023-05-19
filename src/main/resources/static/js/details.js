function detailModal(value) {
    // console.log(value.children())
    $("#detailModal").modal('show')
    let details = document.getElementById(value.id).lastElementChild.lastElementChild;
    console.log(details.getAttribute("detailid")) // 선택한 디테일 아이디
    let id = details.getAttribute("detailid")

    let data = {
        "id": id,
    }

    $.ajax({
        url: "/api/select/detail",
        data: data,
        method: "get",

        success: (message) => {


        }, error: (e) => {
            console.log("출력실패", e)
        }
    })
}