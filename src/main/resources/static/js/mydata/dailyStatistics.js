// 달력
function input() {
    let tbody = document.getElementById("userNumTbody");
    tbody.innerHTML = "";
    let dday = document.getElementById("input_date").value;
    let date = dday.replaceAll("-", "");
    let data = {
        "dday": date,
    }
    $.ajax({
        url: "/mydata/statistics/calendar",
        type: "get",
        data: data,

        success: (message) => {

            let dailyList = JSON.parse(message.dailyApiList);

            for (let i = 0; i < dailyList.length; i++) {
                let tr = document.createElement("tr");
                // 리스트 컬럼 5개
                let td_1 = document.createElement("td");
                let td_2 = document.createElement("td");
                let td_3 = document.createElement("td");
                let td_4 = document.createElement("td");
                let td_5 = document.createElement("td");
                td_1.innerText = dailyList[i].date;
                td_2.innerText = dailyList[i].name;
                td_3.innerText = dailyList[i].code;
                td_4.innerText = dailyList[i].successCnt;
                td_5.innerText = dailyList[i].failCnt;

                tr.append(td_1, td_2, td_3, td_4, td_5);
                tr.onclick = function () {
                    location.href = "/mydata/statistics/" + dailyList[i].code + "/" + dailyList[i].date;
                };
                tbody.append(tr);
            }
        }
    })
}

function statisticsSearch() {
    let tbody = document.getElementById("userNumTbody");
    tbody.innerHTML = "";

    let keyword = document.getElementById("statistics").value;

    let data = {
        "keyword": keyword,
    }

    $.ajax({
        url: "/mydata/statistics/search",
        type: "get",
        data: data,

        success: (message) => {

            let dailyList = JSON.parse(message.dailyApiList);

            for (let i = 0; i < dailyList.length; i++) {
                let tr = document.createElement("tr");
                // 리스트 컬럼 5개
                let td_1 = document.createElement("td");
                let td_2 = document.createElement("td");
                let td_3 = document.createElement("td");
                let td_4 = document.createElement("td");
                let td_5 = document.createElement("td");
                td_1.innerText = dailyList[i].date;
                td_2.innerText = dailyList[i].name;
                td_3.innerText = dailyList[i].code;
                td_4.innerText = dailyList[i].successCnt;
                td_5.innerText = dailyList[i].failCnt;

                tr.append(td_1, td_2, td_3, td_4, td_5);
                tr.onclick = function () {
                    location.href = "/mydata/statistics/" + dailyList[i].code + "/" + dailyList[i].date;
                };
                tbody.append(tr);
            }
        }
    })


}

// 검색

function createElement() {
    let tbody = document.getElementById("userNumTbody");
    tbody.innerHTML = "";
    for (let i = 0; i < list.size(); i++) {
        let tr = document.createElement("tr");
        // 리스트 컬럼 5개
        let td_1 = document.createElement("td");
        let td_2 = document.createElement("td");
        let td_3 = document.createElement("td");
        let td_4 = document.createElement("td");
        let td_5 = document.createElement("td");
        td_1.innerText = "";
        td_2.innerText = "값들 하나씩";
        td_3.innerText = "값들 하나씩";
        td_4.innerText = "값들 하나씩";
        td_5.innerText = "값들 하나씩";

        tr.append(td_1, td_2, td_3, td_4, td_5);
        tr.onclick = function () {
            location.href = "/mydata/statistics/" + "기관코드";
        };
        tbody.append(tr);
    }

}
