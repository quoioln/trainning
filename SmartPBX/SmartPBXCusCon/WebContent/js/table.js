(function($) {
    $.fn.initTable = function(opt) {
        if (!opt) {
            opt = {
                select : false
            };
        }
        var headerRows = $('.header-row', this).length;
        if (typeof headerRows == "undefined")
            headerRows = 1;

        var rows = $("tr", this);
        var id = this.attr("id");
        rows
                .each(function(idx, r) {
                    if (idx % 2 == 0) {
                        $(r).addClass("odd");
                    }

                    // add select check-boxes
                    if (opt.select) {
                        if (idx == 0) {
                            var rowspan = '';
                            if (headerRows > 1) {
                                rowspan = 'rowspan="' + headerRows + '"';
                            }
                            if (opt.selectType == "checkbox") {
                                $(r)
                                        .prepend(
                                                '<td '
                                                        + rowspan
                                                        + ' style="width:40px">選択<input id="'
                                                        + id
                                                        + '_chbx_all" type="checkbox" /> </td>');
                            } else if (opt.selectType == "radio") {
                                $(r)
                                        .prepend(
                                                '<td '
                                                        + rowspan
                                                        + ' style="width:40px">選択</td>');
                            }

                        } else {

                            if (headerRows > 1 && idx < headerRows) {
                            } else {
                                if (opt.selectType == "checkbox") {
                                    $(r)
                                            .prepend(
                                                    '<td style="text-align: center;"><input id="'
                                                            + id
                                                            + '_chbx_'
                                                            + idx
                                                            + '" type="checkbox" /> </td>');
                                } else if (opt.selectType == "radio") {
                                    $(r)
                                            .prepend(
                                                    '<td style="text-align: center;"><input id="'
                                                            + id
                                                            + '_rad_'
                                                            + idx
                                                            + '" type="radio" name="'
                                                            + id + '_rad'
                                                            + '"/> </td>');
                                }
                            }
                        }
                    }
                });

        if (opt.select) {
            $('#' + id + '_chbx_all', this).change(function() {
                var checked = $(this).is(':checked');

                rows.each(function(idx, r) {
                    if (headerRows > 1 && idx < headerRows) {

                    } else if (idx != 0) {
                        $('#' + id + '_chbx_' + idx).prop("checked", checked);
                        if (checked) {
                            $(r).addClass("selected");
                        } else {
                            $(r).removeClass("selected");
                        }
                    }
                });
            });
            rows.each(function(idx, r) {
                if (headerRows > 1 && idx < headerRows) {

                } else if (idx != 0) {
                    // Row click
                    $(r).click(
                            function() {

                                if (opt.selectType == "checkbox") {
                                    var checked = $('#' + id + '_chbx_' + idx)
                                            .is(':checked');

                                    $('#' + id + '_chbx_' + idx).prop(
                                            "checked", !checked);
                                    if (!checked) {
                                        $(r).addClass("selected");
                                    } else {
                                        $(r).removeClass("selected");
                                    }
                                } else if (opt.selectType == "radio") {
                                    $('#' + id + '_rad_' + idx).prop("checked",
                                            true);
                                    rows.each(function(idx2, r2) {
                                        $(r2).removeClass("selected");
                                    });
                                    $(r).addClass("selected");
                                }
                            });
                    // Check-box click
                    if (opt.selectType == "checkbox") {
                        $('#' + id + '_chbx_' + idx).click(function(e) {
                            var checked = $(this).is(':checked');

                            if (checked) {
                                $(r).addClass("selected");
                            } else {
                                $(r).removeClass("selected");
                            }
                            e.stopPropagation();
                        });
                    }
                }

            });

        }
        return this;
    };
})(jQuery);
//(C) NTT Communications  2013  All Rights Reserved