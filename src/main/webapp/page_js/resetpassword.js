var csrfToken = $.cookie('XSRF-TOKEN');

function resetpassword(){
    let oldpassword = $("#old_pass").val();
    let currentpassword = $ ("#confirm_pass").val();
    console.log(oldpassword)
    console.log(currentpassword)
    $.ajax({
    	type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
        url : '/rest/user/password/change',
        data : {
            old : oldpassword,
            new : currentpassword
        },
        success : function(data){
            console.log(data)
            var percent = 0;
            var notice = new PNotify({
                text: "Please wait",
                addclass: 'bg-primary',
                type: 'info',
                icon: 'icon-spinner4 spinner',
                hide: false,
                buttons: {
                    closer: false,
                    sticker: false
                },
                opacity: .9,
                width: "170px"
            });
    
            setTimeout(function() {
            notice.update({
                title: false
            });
            var interval = setInterval(function() {
                percent += 2;
                var options = {
                    text: percent + "% complete."
                };
                if (percent == 80) options.title = "Almost There";
                if (percent >= 100) {
                    window.clearInterval(interval);
                    options.title = "Done!";
                    options.addclass = "bg-success";
                    options.type = "success";
                    options.hide = true;
                    options.buttons = {
                        closer: true,
                        sticker: true
                    };
                    options.icon = 'icon-checkmark3';
                    options.opacity = 1;
                    options.width = PNotify.prototype.options.width;
                }
                notice.update(options);
                }, 120);
            }, 2000);
            $("#old_pass").val('');
            $("#new_pass").val('');
            $("#confirm_pass").val('');

        }
    })
}