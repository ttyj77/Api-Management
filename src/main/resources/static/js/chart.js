new Chart(document.getElementById("line-chart"), {
    type : 'line',
    data : {
        labels : [ '00:00', '02:00', '04:00', '06:00', '08:00', '10:00',
            '12:00', '14:00', '16:00', '18:00', '20:00', '22:00' ],
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

