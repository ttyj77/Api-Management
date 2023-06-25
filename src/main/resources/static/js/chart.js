$(document).ready(function () {
    getGraph();
});

function getGraph() {

    let date = document.getElementById('date').innerText;
    let day = date.replaceAll("-", "").toString();
    let orgCode = document.getElementById('orgCode').getAttribute("value");

    // 선 그래프 데이터
    let xlabel = [];
    let ylabel_1 = [];
    let ylabel_2 = [];

    // 막대 그래프 데이터
    let runChartX = [];
    let runChartY = [];

    // 파이 그래프 데이터
    let pieChartX = [];
    let pieChartY = [];

    let data = {
        "date": day, "code": orgCode,
    }

    $.ajax({
        url: "/mydata/chartData", type: "get", data: data,

        success: (message) => {
            let timeList = JSON.parse(message.timeList);
            let resourcesSeq = JSON.parse(message.resourcesSeq);
            let errorSeq = JSON.parse(message.errorSeq);

            // 선 그래프 데이터 넣기
            for (let i = 0; i < timeList.length; i++) {
                xlabel.push(timeList[i].hh);
                ylabel_1.push(timeList[i].successCnt);
                ylabel_2.push(timeList[i].failCnt);
            }

            // 막대 그래프 데이터 넣기
            for (let i = 0; i <errorSeq.length; i++) {
                runChartX.push(errorSeq[i].error);
                runChartY.push(errorSeq[i].seq);
            }

            // 파이 그래프 데이터 넣기
            for (let i = 0; i < resourcesSeq.length; i++) {
                pieChartX.push(resourcesSeq[i].apiResource);
                pieChartY.push(resourcesSeq[i].seq);
            }

            // 한시간 마다 성공 실패 호출 횟수 선차트
            new Chart(document.getElementById("line-chart"), {
                type: 'line', data: {
                    labels: xlabel,
                    datasets: [{
                        data: ylabel_1,
                        label: "API 호출 성공 ",
                        borderColor: "#3cba9f",
                        fill: false
                    }, {
                        data: ylabel_2,
                        label: "API 호출 실패 ",
                        borderColor: "#e43202",
                        fill: false
                    }]
                }, options: {
                    title: {
                        display: true, text: 'API 호출 성공 실패 수'
                    }
                }
            });

            new Chart(document.getElementById("run-chart"), {
                type: 'bar', data: {
                    labels: runChartX, datasets: [{
                        data: runChartY,
                        label: "에러코드 빈도",
                        backgroundColor: 'rgba(224, 51, 57, 0.8)',
                        borderColor: "#e4023e",
                        fill: false
                    }]
                }, options: {
                    title: {
                        display: true, text: 'API 호출 성공 실패 수'
                    }
                }
            });

            new Chart(document.getElementById("pie-chart"), {
                type: 'pie', data: {
                    labels: pieChartX,
                    datasets: [{
                        label: "API Resources 사용 빈도",
                        backgroundColor: ["#e4023e", "#eb2f06", "#e58e26", "#f6b93b", "#f8c291"],
                        data: pieChartY
                    }]
                }, options: {
                    title: {
                        display: true, text: 'API Resources (/v1/bank) 호출 수'
                    }
                }
            });

        }
    })

}
