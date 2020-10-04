var csrfToken = $.cookie('XSRF-TOKEN');

getData();
templateData();

function templateData() {
    var e = document.getElementById("version");
    var version = e.options[e.selectedIndex].value;
    $.ajax({
        url: '/rest/template/list',
        type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        data: {
            version: version
        },
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                $('#template').append(`<option value=${data[i].id}>${data[i].name}</option>`)
            }
        }
    })
}

function getData() {
    $('#collector_data').empty();
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url: '/rest/collector/list',
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                $('#collector_data').append(`<tr><td>${data[i].id}</td><td>${data[i].hostname}</td>
                                            <td>${data[i].ip}</td>
                                            <td>${data[i].port}</td>
                                            <td>${data[i].sampling_rate}</td>
                                            <td>${data[i].version.toUpperCase()}</td>
                                            <td>${data[i].protocol.toUpperCase()}</td>
                                            <td>${data[i].template_id}</td>                                           
                                            <td><i class="icon-bin2" style="color:red;" onclick="deleteCollector(${data[i].id})"></i></td></tr>`);
            }
        }
    })
}

function deleteCollector(id) {
    swal({
            title: "Are you sure?",
            text: "You will not be able to recover this entry",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#EF5350",
            confirmButtonText: "Yes, delete it!",
            cancelButtonText: "No, cancel pls!",
            closeOnConfirm: false,
            closeOnCancel: false
        },
        function (isConfirm) {
            if (isConfirm) {
                deleteConfirm(id)
                swal({
                    title: "Deleted!",
                    text: "The collector has been deleted.",
                    confirmButtonColor: "#66BB6A",
                    type: "success"
                });
            } else {
                swal({
                    title: "Cancelled",
                    text: "",
                    confirmButtonColor: "#2196F3",
                    type: "error"
                });
            }
        });
}

function deleteConfirm(id) {
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url: '/rest/collector/delete',
        data: {
            id: id
        },
        success: function (data) {
            getData();
            clearFields();
        }
    })
}

function clearFields(){
    $("#host").val('');
    $("#host_ip").val('');
    $("#host_port").val('');
    $("#sampling_rate").val('');
    $("#timeout").val('');
}

function addCollector() {
    validateForm();
    let host = $("#host").val();
    let host_ip = $("#host_ip").val();
    let host_port = $("#host_port").val();
    let sampling_rate = $("#sampling_rate").val();
    let version = $("#version").val();

    let protocol = $("#protocol").val();
    let template = $("#template").val();
    
    if($('.form_input_error').length > 0){
        return;
    } 
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url: '/rest/collector/add',
        data: {
            hostname: host,
            ip: host_ip,
            version: version,
            protocol: protocol,
            port: host_port,
            sampling_rate: sampling_rate,
            template_id: template,
        },
        success: function (data) {
            getData();
            clearFields();
        }
    })
}
function checkInputData(element){
  
    if(element.name === 'host'){

    }

    if(element.name === 'host_ip'){
        return ! ipValidator(element.value);
    }
   
    if(element.name === 'host_port'){
        return ! validatePortNumber(element.value);
    }

    if(element.name === 'sampling_rate'){
        const integernumber = /^[0-9]+$/;
        if(element.value.match(integernumber)){
            return !true;
        }else {
            return !false;
        }
    }


}
function validateForm() {
    $('#collector_form').find('input').each(function () {
        if ($(this).val() == '') {
            $(this).addClass('form_input_error');
        }else if($(this).val().length > 50){
            $(this).addClass('form_input_error');
        }else if(checkInputData(this)){
            $(this).addClass('form_input_error');
        } else {
            $(this).removeClass('form_input_error');
        }
    });
}

function versionchange(){
    var changed_ver = $( "#version option:selected" ).val();
    $("#template").val('');
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url: '/rest/template/list',
        data: {
            version: changed_ver
        },
        success: function (data) {
            if(data.length == 0){
                $('#template').html('');
            }else{
                for (let i = 0; i < data.length; i++) {
                    $('#template').html(`<option value=${data[i].id}>${data[i].name}</option>`)
                }
            }          
        }
    })
}