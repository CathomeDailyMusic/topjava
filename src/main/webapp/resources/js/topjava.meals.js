let filter;
// $(document).ready(function () {
$(function () {
    filter = $("#filterForm");
    makeEditable({
            ajaxUrl: "ajax/profile/meals/",
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "desc"
                    ]
                ]
            })
        }
    );
});

function updateTable() {
    const url = context.ajaxUrl + 'filter?' + filter.serialize();
    tableRefresh(url);
}

function clearFilter() {
    filter.find('.form-control').val('');
    tableRefresh(context.ajaxUrl);
}