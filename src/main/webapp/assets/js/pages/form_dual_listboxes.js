var csrfToken = $.cookie('XSRF-TOKEN');
$(function() {
    $('.listbox').bootstrapDualListbox();


    $('.listbox-no-selection').bootstrapDualListbox({
        preserveSelectionOnMove: 'moved',
        moveOnSelect: false
    });


    $('.listbox-filtered-results').bootstrapDualListbox({
 
        preserveSelectionOnMove: 'moved',
        moveOnSelect: false,
        showFilterInputs: false,
        selectorMinimalHeight: 330

    });


    $('.listbox-filter-disabled').bootstrapDualListbox({
        showFilterInputs: false
    });

    $('.listbox-tall').bootstrapDualListbox({
        selectorMinimalHeight: 300
    });

    $('.listbox-custom-text').bootstrapDualListbox({
        moveOnSelect: false,
        infoText: 'Показать все {0}',
        infoTextFiltered: '<span class="label label-warning">Отфильтровано</span> {0} из {1}',
        infoTextEmpty: 'Пустой лист',
        filterPlaceHolder: 'Фильтр',
        filterTextClear: 'Показать все'
    });

    $('.listbox-dynamic-options').bootstrapDualListbox({
        moveOnSelect: false
    });

    $(".listbox-add").click(function(){
        $('.listbox-dynamic-options').append('<option value="apples">Apples</option><option value="oranges" selected>Oranges</option>');
        $('.listbox-dynamic-options').trigger('bootstrapDualListbox.refresh');
    });

    $(".listbox-add-clear").click(function(){
        $('.listbox-dynamic-options').append('<option value="apples">Apples</option><option value="oranges" selected>Oranges</option>');
        $('.listbox-dynamic-options').trigger('bootstrapDualListbox.refresh', true);
    });
    
});
