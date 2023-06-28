// 달력
function inputDate() {
    let tbody = document.getElementById("userNumTbody");
    tbody.innerHTML = "";
    let sday = document.getElementById("start_date").value;
    let sdate = sday.replaceAll("-", "");
    let eday = document.getElementById("end_date").value;
    let edate = eday.replaceAll("-", "");

    console.log(sdate);
    console.log(edate);
    console.log(sdate > edate);
    let data = {
        "sday": sdate, "eday": edate
    }
    $.ajax({
        url: "/mydata/periodStatistics/calendar", type: "get", data: data,

        success: (message) => {

            let periodApiList = JSON.parse(message.periodApiList);

            for (let i = 0; i < periodApiList.length; i++) {
                let tr = document.createElement("tr");
                // 리스트 컬럼 5개
                let td_1 = document.createElement("td");
                let td_2 = document.createElement("td");
                let td_3 = document.createElement("td");
                let td_4 = document.createElement("td");
                let td_5 = document.createElement("td");
                td_1.innerText = periodApiList[i].name;
                td_2.innerText = periodApiList[i].code;
                td_3.innerText = periodApiList[i].totalRequest;
                td_4.innerText = periodApiList[i].successCnt;
                td_5.innerText = periodApiList[i].failCnt;

                tr.append(td_1, td_2, td_3, td_4, td_5);
                tr.onclick = function () {
                    location.href = "/mydata/statistics/" + periodApiList[i].code + "/" + sdate + "/" + edate;
                };
                tbody.append(tr);
            }
        }
    })
}
