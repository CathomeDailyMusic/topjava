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

function populateEditForm(key, value) {
    if (key === 'dateTime') {
        value = value.substr(0, 10) + " " + value.substr(11, 5);
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
                            return dateTime.substring(0, 10) + " " + dateTime.substring(11, 16);
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
                if (data.excess) {
                    $(row).attr("data-mealExcess", true);
                } else {
                    $(row).attr("data-mealExcess", false);
                }
            }
        }),
        updateTable: updateFilteredTable
    });

    // dateTimePicker
    $.datetimepicker.setLocale(locale);
    $('#datetimepicker').datetimepicker({format: 'Y-m-d H:i'});
    $("input[id$='Date']").datetimepicker({timepicker: false, format: 'Y-m-d'});
    $("input[id$='Time']").datetimepicker({datepicker: false, format: 'H:i'});
});