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
    var empty = true;
    filter.find('.form-control').each(function () {
        if ($(this).val().trim() !== "") {
            empty = false;
            return empty;
        }
    });
    const url = empty ? context.ajaxUrl : context.ajaxUrl + 'filter?' + filter.serialize();
    tableRefresh(url);
}

function clearFilter() {
    filter.find('.form-control').val('');
    tableRefresh(context.ajaxUrl);
}