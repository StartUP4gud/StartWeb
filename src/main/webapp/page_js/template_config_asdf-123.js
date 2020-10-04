var csrfToken = $.cookie('XSRF-TOKEN');

var template_fields = [];
getData();
var dataArray1 = [{
        "fields": "Source Mac",
        "value": "src_mac"
    },
    {
        "fields": "Destination MAC",
        "value": "dst_mac"
    },
    {
        "fields": "Source IP",
        "value": "src_ip"
    },
    {
        "fields": "Destination IP",
        "value": "dst_ip"
    },
    {
        "fields": "Protocol",
        "value": "proto"
    },
    {
        "fields": "Source Port",
        "value": "src_port"
    },
    {
        "fields": "Destination Port",
        "value": "dst_port"
    },
    {
        "fields": "TCP Flag",
        "value": "tcp_flags"
    },
    {
        "fields": "Bytes",
        "value": "bytes"
    },
    {
        "fields": "Packets",
        "value": "pkts"
    }
];


var settings1 = {
    "dataArray": dataArray1,
    "itemName": "fields",
    "valueName": "value",
    "callable": function (items) {
        // console.dir(items)
        template_fields = (items);
    }
};

$("#transfer1").Transfer(settings1);
$("#transfer2").Transfer(settings1);
$("#transfer3").Transfer(settings1);


function submitv9() {
    var name = $("#v9_name").val();
    if(name && name.length  < 50 && name.match(/^[a-zA-Z0-9_]*$/)){
        let isNotValid = false;
        let items_datas = template_fields.map(data => data.value);
        items_datas.forEach((data)=>{
            if(!data.match(/^[a-zA-Z0-9_]*$/)){
                console.log(`not valid fields ${data}`);
                isNotValid = true;
            }
        });
      
        if(!isNotValid){
            $.ajax({
            	type: 'POST',
        		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
                url: '/rest/template/add',
                data: {
                    name: name,
                    fields: template_fields.map(data => data.value).join(','),
                    apps: "",
                    version: "v9"
                },
                success: function (data) {
                    getData();
                }
            })
        }
       
    }else{
        console.log(`not valid template name`);
    }
    


}

function getData() {
    $("#templatelist").empty();
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url: '/rest/template/list',
        success: function (data) {
            for (let i = 0; i < data.length; i++) {
                let x = '';
                x += '<tr>';
                x += '<td>' + data[i].id + '</td><td>' + data[i].version + '</td><td>' + data[i].name + '</td><td>' + data[i].fields + '</td>';
                x += '<td>' + data[i].apps + '</td>';
                x += '<td><i class="icon-bin2" style="color:red;" onclick="deleteTemplate(\'' + data[i].id + '\')"></i>';
                x += '</td>';
                x += '</tr>';
                $('#templatelist').append(x);
            }

        }
    })
}

function deleteTemplate(id) {
    swal({
            title: "Are you sure?",
            text: "You will not be able to recover this template",
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
                    text: "The template has been deleted.",
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
        url: '/rest/template/delete',
        data: {
            id: id,
        },
        success: function (data) {
            getData();

        }
    })
}

function submitipfix() {
    var name = $("#ipfix_name").val();

    var selected = new Array();

    var exportipfix_data = document.getElementById("exportipfix_data");

    var chks = exportipfix_data.getElementsByTagName("INPUT");

    for (var i = 0; i < chks.length; i++) {
        if (chks[i].checked) {
            selected.push(chks[i].value);
        }
    }
     

      if(name && name.length  < 50 && name.match(/^[a-zA-Z0-9_]*$/)){
        
        let isNotValid = false;
        let items_datas = template_fields.map(data => data.value);

        items_datas.forEach((data)=>{
            if(!data.match(/^[a-zA-Z0-9_]*$/)){
                console.log(`not valid fields ${data}`);
                isNotValid = true;
            }
        });

        selected.forEach((data)=>{
            if(!data.match(/^[a-zA-Z0-9_]*$/)){
                console.log(`not valid fields ${data}`);
                isNotValid = true;
            }
        });

        if(!isNotValid){
            $.ajax({
            	type: 'POST',
        		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
                url: '/rest/template/add',
                data: {
                    name: name,
                    fields: template_fields.map(data => data.value).join(','),
                    apps: selected.join(","),
                    version: "ipfix"
                },
                success: function (data) {
                    getData();
                }
            })
        }
       

      }else{
        console.log(`not valid template name`);
    }  

}

function submitjson() {
    var name = $("#json_name").val();

    var selected = new Array();

    var exportjson_data = document.getElementById("exportjson_data");

    var chks = exportjson_data.getElementsByTagName("INPUT");

    for (var i = 0; i < chks.length; i++) {
        if (chks[i].checked) {
            selected.push(chks[i].value);
        }
    }

    /*  if (selected.length > 0) {
         alert("Selected values: " + selected.join(","));
     } */


     if(name && name.length  < 50 && name.match(/^[a-zA-Z0-9_]*$/)){

        let isNotValid = false;
        let items_datas = template_fields.map(data => data.value);

        items_datas.forEach((data)=>{
            if(!data.match(/^[a-zA-Z0-9_]*$/)){
                console.log(`not valid fields ${data}`);
                isNotValid = true;
            }
        });

        selected.forEach((data)=>{
            if(!data.match(/^[a-zA-Z0-9_]*$/)){
                console.log(`not valid fields ${data}`);
                isNotValid = true;
            }
        });

        if(!isNotValid){
            $.ajax({
            	type: 'POST',
        		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
                url: '/rest/template/add',
                data: {
                    name: name,
                    fields: template_fields.map(data => data.value).join(','),
                    apps: selected.join(","),
                    version: "json"
                },
                success: function (data) {
                    getData();
                }
            })
        }
       
     }else{
        console.log(`not valid template name`);
    }
   
}