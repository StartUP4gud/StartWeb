var csrfToken = $.cookie('XSRF-TOKEN');

clearFields();
getData();

function clearFields() {
    $("#u_name").val('');
    $("#username").val('');
    $("#userpassword").val('');
}

function getData() {
    $("#userlist").empty();
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url: "/rest/user/list",
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                let x = '';
                x += '<tr>';
                x += '<td>' + data[i].id + '</td><td>' + data[i].name + '</td><td>' + data[i].username + '</td><td>' + (data[i].role).toUpperCase() + '</td>';
                x += '<td><i class="icon-bin2" style="color:red;" onclick="deleteUser(\'' + data[i].id + '\')"></i>';
                x += '</td>';
                x += '</tr>';
                $('#userlist').append(x);
            }
        }
    });
}
function checkInputData(element){
    if(element.id == 'u_name'){
        const name = /^[a-zA-Z]+[\-'\s]?[a-zA-Z ]+$/;
        if(element.value.match(name)){
            return !true;
        }else{
            return !false;
        }
    }
    if(element.id == 'username'){
        const username = /^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){0,18}[a-zA-Z0-9]$/;
        if(element.value.match(username)){
            return !true;
        }else{
            return !false;
        }
    }
}
function validateForm() {
    $('#user_form').find('input').each(function () {
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

function signUp() {
    validateForm();
    let u_name = $("#u_name").val();
    let username = $("#username").val();
    let userpassword = $("#userpassword").val();
    let userrole = $("#userrole").val();
 
    if($('.form_input_error').length > 0){
        return;
    }
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url: '/rest/user/add',
        data: {
            name: u_name,
            username: username,
            password: userpassword,
            role: userrole,
        },
        success: function (data) {
            getData();
            clearFields();
        }
    })
}

function deleteConfirm(id) {
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url: '/rest/user/delete',
        data: {
            id: id
        },
        success: function (data) {
            getData();
            clearFields();
        }
    })
}

function deleteUser(id) {
    swal({
            title: "Are you sure?",
            text: "You will not be able to recover this user",
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
                    text: "The user has been deleted.",
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