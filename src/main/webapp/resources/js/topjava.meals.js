const mealsAjaxUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealsAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealsAjaxUrl, updateTableByData);
}

function formatISODateTime(value) {
    value = value.substr(0, 10) + " " + value.substr(11, 5);
    return value;
}

function populateEditForm(key, value) {
    if (key === 'dateTime') {
        value = formatISODateTime(value);
    }
    populateEditFormCommon(key, value);
}

$(function () {
    makeEditable({
        ajaxUrl: mealsAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealsAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (dateTime, type, row) {
                        if (type === "display") {
                            return formatISODateTime(dateTime);
                        }
                        return dateTime;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn

                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                },
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                data.excess ? $(row).attr("data-mealExcess", true) : $(row).attr("data-mealExcess", false);
            }
        }),
        updateTable: updateFilteredTable
    });

    // dateTimePicker
    $.datetimepicker.setLocale(locale);
    $('#datetimepicker').datetimepicker({format: 'Y-m-d H:i'});
    $('#startDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                maxDate: $('#endDate').val() ? $('#endDate').val() : false
            })
        }
    });
    $('#endDate').datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                minDate: $('#startDate').val() ? $('#startDate').val() : false
            })
        }
    });
    $('#startTime').datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                maxTime: $('#endTime').val() ? $('#endTime').val() + 1 : false
            })
        }
    });
    $('#endTime').datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                minTime: $('#startTime').val() ? $('#startTime').val() : false
            })
        }
    });
});