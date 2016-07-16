$.fn.serializeObject = function()
{
    var result = {};
    var serializeArray = this.serializeArray();
    $.each(serializeArray, function() {
        if (result[this.name] !== undefined) {
            if (!result[this.name].push) {
            	result[this.name] = [result[this.name]];
            }
            result[this.name].push(this.value || '');
        } else {
        	result[this.name] = this.value || '';
        }
    });
    return result;
};
$(document).ready(function () {
    var Table = function (id, url) {
        var me = this;
        this.__url = url;
        this.__oldData = '';
        this.__table = $(id).DataTable(
            {
                bFilter: false,
                bPaginate: false,
                bSort: false,
                columnDefs: [
                    {
                        width: '50px',
                        targets: 0,
                        render: function (data, type, row) {
                            return row.status === 'done' ? '<div class=details-control></div>' : '';
                        }
                    },
                    {
                        render: function (data, type, row) {
                            return '<div class=table-icon-' + data + '></div>';
                        },
                        width: '50px',
                        targets: 2
                    }
                ],
                columns: [
                    {
                        data: null,
                        defaultContent: '<div class=details-control></div>'
                    },
                    {data: 'url'},
                    {data: 'status'}
                ]
            }
        );
        $(id + ' tbody').on('click', 'div.details-control', function () {
            var tr = $(this).closest('tr');
            var row = me.__table.row(tr);
            if (row.child.isShown()) {
                row.child.hide();
                tr.removeClass('shown');
            }
            else {
                row.child(me.format(row.data())).show();
                tr.addClass('shown');
            }
        });
    };
    Table.prototype.format = function (originalData) {
        var makeRow = function (key, value) {
            return '<tr>' +
                '<td>' + key + ':</td>' +
                '<td>' + value + '</td>' +
                '</tr>';
        };
        var result = '';
        if (originalData.info) {
            var info = originalData.info;
            result =
                makeRow('Title', info.title) +
                makeRow('Html Version', info.htmlVersion);
        }
        return result.length === 0
            ? ''
            : '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">' +
        result +
        '</table>';
    };
    Table.prototype.render = function (data) {
        if (JSON.stringify(data) !== this.__oldData) {
            this.__oldData = JSON.stringify(data);
            this.__table.rows().remove();
            this.__table.rows.add(data).draw();
        }
    };
    Table.prototype.update = function () {
//        this.render([{
//            "url": "http://ya.ru",
//            "status": "done",
//            "info": {"title": "test", "htmlVersion": "11"}
//        }, {
//            "url": "http://ya.ru3",
//            "status": "done",
//            "info": {"title": "test3", "htmlVersion": "113"}
//        }, {"url": "http://ya1.ru", "status": "done", "info": {"title": "test1", "htmlVersion": "121"}}]);
        $.ajax({
            dataType: "json",
            url: this.__url,
            success: this.render.bind(this)
        });
    };

    var resultsTable = new Table('#results', './api/tasks');
    resultsTable.update();
    setInterval(function () {
        resultsTable.update();
    }, 3000);

    var onAfterUrlSent = function (result) {
        resultsTable.update();
    };
    $('input#process').click(function () {
        $.ajax({
            url: './api/tasks',
            type: 'post',
            contentType: 'application/json; charset=utf-8',
            dataType: 'json',
            data: JSON.stringify($('form#main').serializeObject()),
            success: onAfterUrlSent
        });
    });

});