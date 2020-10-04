var csrfToken = $.cookie('XSRF-TOKEN');
$(function() {


    
    

    
    $('.daterange-basic').daterangepicker({
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default'
    });


    
    $('.daterange-weeknumbers').daterangepicker({
        showWeekNumbers: true,
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default'
    });


    
    $('.daterange-buttons').daterangepicker({
        applyClass: 'btn-success',
        cancelClass: 'btn-danger'
    });


    
    $('.daterange-time').daterangepicker({
        timePicker: true,
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        locale: {
            format: 'MM/DD/YYYY h:mm a'
        }
    });


    
    $('.daterange-left').daterangepicker({
        opens: 'left',
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default'
    });


    
    $('.daterange-single').daterangepicker({ 
        singleDatePicker: true
    });


    
    $('.daterange-datemenu').daterangepicker({
        showDropdowns: true,
        opens: "left",
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default'
    });


    
    $('.daterange-increments').daterangepicker({
        timePicker: true,
        opens: "left",
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        timePickerIncrement: 10,
        locale: {
            format: 'MM/DD/YYYY h:mm a'
        }
    });


    
    $('.daterange-locale').daterangepicker({
        applyClass: 'bg-slate-600',
        cancelClass: 'btn-default',
        opens: "left",
        ranges: {
            'Сегодня': [moment(), moment()],
            'Вчера': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            'Последние 7 дней': [moment().subtract(6, 'days'), moment()],
            'Последние 30 дней': [moment().subtract(29, 'days'), moment()],
            'Этот месяц': [moment().startOf('month'), moment().endOf('month')],
            'Прошедший месяц': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        },
        locale: {
            applyLabel: 'Вперед',
            cancelLabel: 'Отмена',
            startLabel: 'Начальная дата',
            endLabel: 'Конечная дата',
            customRangeLabel: 'Выбрать дату',
            daysOfWeek: ['Вс', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт','Сб'],
            monthNames: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
            firstDay: 1
        }
    });


    
    
    

    
    $('.daterange-predefined').daterangepicker(
        {
            startDate: moment().subtract(29, 'days'),
            endDate: moment(),
            minDate: '01/01/2014',
            maxDate: '12/31/2016',
            dateLimit: { days: 60 },
            ranges: {
                'Today': [moment(), moment()],
                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            },
            opens: 'left',
            applyClass: 'btn-small bg-slate',
            cancelClass: 'btn-small btn-default'
        },
        function(start, end) {
            $('.daterange-predefined span').html(start.format('MMMM D, YYYY') + ' &nbsp; - &nbsp; ' + end.format('MMMM D, YYYY'));
            $.jGrowl('Date range has been changed', { header: 'Update', theme: 'bg-primary', position: 'center', life: 1500 });
        }
    );

    
    $('.daterange-predefined span').html(moment().subtract(29, 'days').format('MMMM D, YYYY') + ' &nbsp; - &nbsp; ' + moment().format('MMMM D, YYYY'));


    
    
    

    
    $('.daterange-ranges').daterangepicker(
        {
            startDate: moment().subtract(29, 'days'),
            endDate: moment(),
            minDate: '01/01/2012',
            maxDate: '12/31/2016',
            dateLimit: { days: 60 },
            ranges: {
                'Today': [moment(), moment()],
                'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
                'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                'This Month': [moment().startOf('month'), moment().endOf('month')],
                'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
            },
            opens: 'left',
            applyClass: 'btn-small bg-slate-600',
            cancelClass: 'btn-small btn-default'
        },
        function(start, end) {
            $('.daterange-ranges span').html(start.format('MMMM D, YYYY') + ' &nbsp; - &nbsp; ' + end.format('MMMM D, YYYY'));
        }
    );

    
    $('.daterange-ranges span').html(moment().subtract(29, 'days').format('MMMM D, YYYY') + ' &nbsp; - &nbsp; ' + moment().format('MMMM D, YYYY'));


    
    
    


    
    $('.pickadate').pickadate();


    
    $('.pickadate-strings').pickadate({
        weekdaysShort: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
        showMonthsShort: true
    });


    
    $('.pickadate-buttons').pickadate({
        today: '',
        close: '',
        clear: 'Clear selection'
    });


    
    $('.pickadate-accessibility').pickadate({
        labelMonthNext: 'Go to the next month',
        labelMonthPrev: 'Go to the previous month',
        labelMonthSelect: 'Pick a month from the dropdown',
        labelYearSelect: 'Pick a year from the dropdown',
        selectMonths: true,
        selectYears: true
    });


    
    $('.pickadate-translated').pickadate({
        monthsFull: ['Janvier', 'Février', 'Mars', 'Avril', 'Mai', 'Juin', 'Juillet', 'Août', 'Septembre', 'Octobre', 'Novembre', 'Décembre'],
        weekdaysShort: ['Dim', 'Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam'],
        today: 'aujourd\'hui',
        clear: 'effacer',
        formatSubmit: 'yyyy/mm/dd'
    });


    
    $('.pickadate-format').pickadate({

        
        format: 'You selecte!d: dddd, dd mmm, yyyy',
        formatSubmit: 'yyyy/mm/dd',
        hiddenPrefix: 'prefix__',
        hiddenSuffix: '__suffix'
    });


    
    var $input_date = $('.pickadate-editable').pickadate({
        editable: true,
        onClose: function() {
            $('.datepicker').focus();
        }
    });

    var picker_date = $input_date.pickadate('picker');
    $input_date.on('click', function(event) { 
        if (picker_date.get('open')) {
            picker_date.close();
        } else {
            picker_date.open();
        }                        
        event.stopPropagation();
    });


    
    $('.pickadate-selectors').pickadate({
        selectYears: true,
        selectMonths: true
    });


    
    $('.pickadate-year').pickadate({
        selectYears: 4
    });


    
    $('.pickadate-weekday').pickadate({
        firstDay: 1
    });


    
    $('.pickadate-limits').pickadate({
        min: [2014,3,20],
        max: [2014,7,14]
    });


    
    $('.pickadate-disable').pickadate({
        disable: [
            [2015,8,3],
            [2015,8,12],
            [2015,8,20]
        ]
    });


    
    $('.pickadate-disable-range').pickadate({
        disable: [
            5,
            [2013, 10, 21, 'inverted'],
            { from: [2014, 3, 15], to: [2014, 3, 25] },
            [2014, 3, 20, 'inverted'],
            { from: [2014, 3, 17], to: [2014, 3, 18], inverted: true }
        ]
    });


    
    $('.pickadate-events').pickadate({
        onStart: function() {
            console.log('Hello there :)')
        },
        onRender: function() {
            console.log('Whoa.. rendered anew')
        },
        onOpen: function() {
            console.log('Opened up')
        },
        onClose: function() {
            console.log('Closed now')
        },
        onStop: function() {
            console.log('See ya.')
        },
        onSet: function(context) {
            console.log('Just set stuff:', context)
        }
    });


    
    

    
    $('.pickatime').pickatime();


    
    $('.pickatime-clear').pickatime({
        clear: ''
    });


    
    $('.pickatime-format').pickatime({

        
        format: 'T!ime selected: h:i a',
        formatLabel: '<b>h</b>:i <!i>a</!i>',
        formatSubmit: 'HH:i',
        hiddenPrefix: 'prefix__',
        hiddenSuffix: '__suffix'
    });


    
    $('.pickatime-hidden').pickatime({
        formatSubmit: 'HH:i',
        hiddenName: true
    });


    
    var $input_time = $('.pickatime-editable').pickatime({
        editable: true,
        onClose: function() {
            $('.datepicker').focus();
        }
    });

    var picker_time = $input_time.pickatime('picker');
    $input_time.on('click', function(event) { 
        if (picker_time.get('open')) {
            picker_time.close();
        } else {
            picker_time.open();
        }                        
        event.stopPropagation();
    });


    
    $('.pickatime-intervals').pickatime({
        interval: 150
    });


    
    $('.pickatime-limits').pickatime({
        min: [7,30],
        max: [14,0]
    });


    
    $('.pickatime-limits-integers').pickatime({
        disable: [
            3, 5, 7
        ]
    })


    
    $('.pickatime-disabled').pickatime({
        disable: [
            [0,30],
            [2,0],
            [8,30],
            [9,0]
        ]
    });


    
    $('.pickatime-range').pickatime({
        disable: [
            1,
            [1, 30, 'inverted'],
            { from: [4, 30], to: [10, 30] },
            [6, 30, 'inverted'],
            { from: [8, 0], to: [9, 0], inverted: true }
        ]
    });


    
    $('.pickatime-disableall').pickatime({
        disable: [
            true,
            3, 5, 7,
            [0,30],
            [2,0],
            [8,30],
            [9,0]
        ]
    });


    
    $('.pickatime-events').pickatime({
        onStart: function() {
            console.log('Hello there :)')
        },
        onRender: function() {
            console.log('Whoa.. rendered anew')
        },
        onOpen: function() {
            console.log('Opened up')
        },
        onClose: function() {
            console.log('Closed now')
        },
        onStop: function() {
            console.log('See ya.')
        },
        onSet: function(context) {
            console.log('Just set stuff:', context)
        }
    });



    
    

    
    $("#anytime-date").AnyTime_picker({
        format: "%W, %M %D in the Year %z %E",
        firstDOW: 1
    });


    
    $("#anytime-time").AnyTime_picker({
        format: "%H:%i"
    });


    
    $("#anytime-time-hours").AnyTime_picker({
        format: "%l %p"
    });


    
    $("#anytime-both").AnyTime_picker({
        format: "%M %D %H:%i",
    });


    
    $("#anytime-weekday").AnyTime_picker({
        format: "%W, %D of %M, %Z"
    });


    
    $("#anytime-month-numeric").AnyTime_picker({
        format: "%d/%m/%Z"
    });


    
    $("#anytime-month-day").AnyTime_picker({
        format: "%D of %M"
    });


    
    $('#ButtonCreationDemoButton').click(function (e) {
        $('#ButtonCreationDemoInput').AnyTime_noPicker().AnyTime_picker().focus();
        e.preventDefault();
    });


    
    
    

    
    var oneDay = 24*60*60*1000;
    var rangeDemoFormat = "%e-%b-%Y";
    var rangeDemoConv = new AnyTime.Converter({format:rangeDemoFormat});

    
    $("#rangeDemoToday").click( function (e) {
        $("#rangeDemoStart").val(rangeDemoConv.format(new Date())).change();
    });

    
    $("#rangeDemoClear").click( function (e) {
        $("#rangeDemoStart").val("").change();
    });

    
    $("#rangeDemoStart").AnyTime_picker({
        format: rangeDemoFormat
    });

    
    $("#rangeDemoStart").change(function(e) {
        try {
            var fromDay = rangeDemoConv.parse($("#rangeDemoStart").val()).getTime();

            var dayLater = new Date(fromDay+oneDay);
                dayLater.setHours(0,0,0,0);

            var ninetyDaysLater = new Date(fromDay+(90*oneDay));
                ninetyDaysLater.setHours(23,59,59,999);

            
            $("#rangeDemoFinish")
            .AnyTime_noPicker()
            .removeAttr("disabled")
            .val(rangeDemoConv.format(dayLater))
            .AnyTime_picker({
                earliest: dayLater,
                format: rangeDemoFormat,
                latest: ninetyDaysLater
            });
        }

        catch(e) {

            
            $("#rangeDemoFinish").val("").attr("disabled","disabled");
        }
    });
    
});
