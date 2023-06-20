new Chart(document.getElementById("line-chart"), {
    type : 'line',
    data : {
        labels : [ '00:00','01:00' ,'02:00', '03:00','04:00', '05:00','06:00','07:00', '08:00','09:00', '10:00',
            '11:00' ,'12:00','13:00', '14:00','15:00', '16:00', '17:00','18:00', '19:00','20:00', '21:00','22:00','23:00' ],
        datasets : [
            {
                data : [ 19, 14, 23, 151, 210,
                    581, 313, 322, 478, 547, 399, 92 ],
                label : "API 호출 성공 ",
                borderColor : "#3cba9f",
                fill : false
            },
            {
                data : [ 2, 0, 2, 11, 54,
                    73, 13, 15, 53, 121, 22, 2 ],
                label : "API 호출 실패 ",
                borderColor : "#e43202",
                fill : false
            } ]
    },
    options : {
        title : {
            display : true,
            text : 'API 호출 성공 실패 수'
        }
    }
});


new Chart(document.getElementById("run-chart"), {
    type : 'bar',
    data : {
        labels : [ 40001, 40101, 40203, 40301, 40401, 40402,
            40403, 40405, 40501],
        datasets : [
            {
                data : [ 78, 92, 8, 51, 26,
                    11, 23, 48, 31 ],
                label : "에러코드 빈도",
                backgroundColor : 'rgba(224, 51, 57, 0.8)',
                borderColor : "#e4023e",
                fill : false
            }
            ]
    },
    options : {
        title : {
            display : true,
            text : 'API 호출 성공 실패 수'
        }
    }
});

new Chart(document.getElementById("pie-chart"), {
    type: 'pie',
    data: {
        labels: ["/accounts/deposit/detail", "/accounts/deposit/transactions", "/accounts/invest/basic", "/accounts/invest/transactions", "/irps"],
        datasets: [{
            label: "API Resources 사용 빈도",
            backgroundColor: ["#e4023e", "#eb2f06","#e58e26", "#f6b93b","#f8c291"],
            data: [678,426,388,274,130]
        }]
    },
    options: {
        title: {
            display: true,
            text: 'API Resources (/v1/bank) 호출 수'
        }
    }
});

// backgroundColor: ["#fd709f", "#9075e8","#35b7f5","#fdeb6c",
//     "#70ff77","#f19c71" , "#a1e7ff"],
//     data: [478,526,202,688,130,80,301]