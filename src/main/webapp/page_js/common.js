var csrfToken = $.cookie('XSRF-TOKEN');
function validateIP(ips) {
	if(!ips) {
		return false;
	}
	ipList = ips.split(',');
	for(i in ipList) {

		ip = ipList[i];
		if(ip.indexOf("/") >= 0) {
			prefix = ip.substr(ip.indexOf('/')+1);

			if(!prefix || parseInt(prefix)>32 || parseInt(prefix)<0) {
				return false;
			}
			ip = ip.substr(0,ip.indexOf('/'));
		}
		octets = ip.split(".");
		if(octets.length != 4) {
			return false;
		}
		for(j in octets){
			if(!octets[j] || parseInt(octets[j]) < 0 || parseInt(octets[j]) > 255) {
				return false;
			}
		}
	}   
	return true;
}

function setDate(){

	var start = moment().startOf('day');
	var end = moment();
	
	function cb(start, end) {
		$('#timestamp span').html(start.format('YYYY MMM DD HH:mm') + ' - ' + end.format('YYYY MMM DD HH:mm'));
	}
	$('#timestamp').daterangepicker({
		startDate: start,
		endDate: end,
		ranges: {
			'Last 1 Hour': [moment().subtract(1, 'hour'), moment()],
			'Last 3 Hour': [moment().subtract(3, 'hour'), moment()],
			'Last 6 Hour': [moment().subtract(6, 'hour'), moment()],
			'Last 12 Hour':[moment().subtract(12, 'hour'), moment()],
			'Today'       :[moment().startOf('day'), moment()],
			'Yesterday'   :[moment().subtract(1, 'days').startOf('day'),moment().startOf('day').subtract(10, 'minutes')],
			'Last 1 Day': [moment().subtract(24, 'hour'), moment()],
			'Last 7 Days': [moment().subtract(6, 'days'), moment()],
			'Last 30 Days': [moment().subtract(29, 'days'), moment()],
			'Last 3 Months': [moment().subtract(3, 'months'), moment()],
			'Last 6 Months': [moment().subtract(6, 'months'), moment()]
		},
		timePicker: true,
		timePickerIncrement: 10,
		timePicker24Hour: true,
		locale: {
			format: 'YYYY-MM-DD HH:mm'
		}
	}, cb);
	cb(start, end);
}


function getUrlParameter(sParam) {
	var sPageURL = decodeURIComponent(window.location.search.substring(1)),
	sURLVariables = sPageURL.split('&'),
	sParameterName,
	i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : sParameterName[1];
		}
	}
}

function getTime(timestamp) {
	var dateTime=new Date(timestamp * 1000)
	var starttime = dateTime.toLocaleString();
	return starttime;
}

function integerToIP(ipint1) {
	
	ipint1 = parseInt(ipint1);
	var part1 = ipint1 & 255;
	var part2 = ((ipint1 >> 8) & 255);
	var part3 = ((ipint1 >> 16) & 255);
	var part4 = ((ipint1 >> 24) & 255);
	
	return part4 + "." + part3 + "." + part2 + "." + part1;
}


function IPToInteger(ipString){
	var d = ipString.split('.');
	return ((((((+d[0])*256)+(+d[1]))*256)+(+d[2]))*256)+(+d[3]);
}


function currentTime(){
	return  new Date().getTime(); 
}


function convertToStdBytes(bits) {

	var tmp = bits/8;//converting to bytes
	var finalBytes="";

	if(tmp<=1024){
		return finalBytes = tmp.toFixed(1)+" B";
	}

	tmp = tmp/1024;
	if(tmp<=1024){
		return finalBytes = tmp.toFixed(1)+" KB";
	}

	tmp = tmp/1024;
	if(tmp<=1024){
		return finalBytes = tmp.toFixed(1)+" MB";
	}

	tmp = tmp/1024;
	if(tmp<=1024){
		return finalBytes = tmp.toFixed(1)+" GB";
	}

	tmp = tmp/1024;
	return finalBytes = tmp.toFixed(1)+" TB";


}

function convertToStdBits(bits) {

	var tmp = bits, finalBytes="";

	if(tmp<=1000){
		return finalBytes = tmp.toFixed(1)+" bps";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalBytes = tmp.toFixed(1)+" Kbps";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalBytes = tmp.toFixed(1)+" Mbps";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalBytes = tmp.toFixed(1)+" Gbps";
	}

	tmp = tmp/1000;
	return finalBytes = tmp.toFixed(1)+" Tbps";


}


function convertToStdPktsCount(pkts) {

	var tmp = pkts, finalPkts="";

	if(tmp<=1000){
		return finalPkts = tmp.toFixed(0)+"";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalPkts = tmp.toFixed(1)+" K";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalPkts = tmp.toFixed(1)+" M";
	}

	tmp = tmp/1000;
	return finalPkts = tmp.toFixed(1)+" B";


}

function convertToStdFlows(pkts) {

	var tmp = pkts, finalPkts="";

	if(tmp<=1000){
		return finalPkts = tmp.toFixed(1)+" fps";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalPkts = tmp.toFixed(1)+" Kfps";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalPkts = tmp.toFixed(1)+" Mfps";
	}

	tmp = tmp/1000;
	return finalPkts = tmp.toFixed(1)+" Bfps";


}

function convertToStdPkts(pkts) {

	var tmp = pkts, finalPkts="";

	if(tmp<=1000){
		return finalPkts = tmp.toFixed(1)+" pps";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalPkts = tmp.toFixed(1)+" Kpps";
	}

	tmp = tmp/1000;
	if(tmp<=1000){
		return finalPkts = tmp.toFixed(1)+" Mpps";
	}

	tmp = tmp/1000;
	return finalPkts = tmp.toFixed(1)+" Bpps";


}

function bytesToSTD(bytes, label) {
	if (bytes <= 0) return '0';
	var s = ['bytes', 'KB', 'MB', 'GB', 'TB', 'PB'];
	var e = Math.floor(Math.log(bytes)/Math.log(1024));
	//var value = ((bytes/Math.pow(1024, Math.floor(e))).toFixed(0));
	var value = ((bytes/Math.pow(1024, e)).toFixed(1));
	e = (e<0) ? (-e) : e;
	if (label) value += ' ' + s[e];
	return value;
}

function bitesToSTD(bites, label) {
	if (bites <= 0) return 0;
	
	var s = ['bps', 'Kbps', 'Mbps', 'Gbps', 'Tbps'];
	var e = Math.floor(Math.log(bites)/Math.log(1000));
	//var value = ((bites/Math.pow(1000, Math.floor(e))).toFixed(0));
	var value = ((bites/Math.pow(1000, e)).toFixed(1));
	e = (e<0) ? (-e) : e;
	if (label) value += ' ' + s[e];	
	return value;
}
function bitesToSTDconvert(bites, label) {
	if (bites <= 0) return '';
	var s = ['bps', 'Kbps', 'Mbps', 'Gbps', 'Tbps'];
	var e = Math.floor(Math.log(bites)/Math.log(1024));
	//var value = ((bites/Math.pow(1024, Math.floor(e))).toFixed(1));
	var value = ((bites/Math.pow(1024, e)).toFixed(1));
	e = (e<0) ? (-e) : e;
	if (label) value += ' ' + s[e];
	return value;
}

function hexToString(name){
	name = name.replace(/\\x([\d\w]{2})/gi, function (match, grp) {
		//return String.fromCharCode(parseInt(grp, 16));
		return String.fromCharCode(parseInt(grp, 16));
	});
	return name;
}

function unicodeToString(name){
	name = name.replace(/\\u([\d\w]{4})/gi, function (match, grp) {
		//return String.fromCharCode(parseInt(grp, 16));
		return String.fromCharCode(parseInt(grp, 16));
	});
	return name;
}

function nonAsciiToString(text){
	text = hexToString(text);
	text = unicodeToString(text);
	text = text.replace(/\\/g,"");
	return text;
}

function checkIpv4(ip){
	if(ip.includes(":")){
		return false;
	} else {
		var pattern=/^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$|^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\-]*[A-Za-z0-9])$|^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$/
			if(ip.match(pattern)){
				return true;
			}else{
				return false;
			}
	}
}

function checkIpv4Subnet(ip){
	if(ip.match("^([0-9]{1,3}\.){3}[0-9]{1,3}(\/([0-9]|[1-2][0-9]|3[0-2]))?$")){
		return true;
	}else
		return false;
}

function ipValidator(ip){	
	if(checkIpv4Subnet(ip) || ipv6Validator(ip)){
		return true;
	}else{
		return false;
	}
}

function ipv6Validator(ip){
	var ipv6_regex = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
	if(ip.match(ipv6_regex)){
		return true;
	}else{
		return false;
	}
}

function checkIpv6(ip){
	if(ip.includes(".")){
		console.log(ip)
		return false;
	}else{
		var pattern=/^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$|^(([a-zA-Z]|[a-zA-Z][a-zA-Z0-9\-]*[a-zA-Z0-9])\.)*([A-Za-z]|[A-Za-z][A-Za-z0-9\-]*[A-Za-z0-9])$|^\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)(\.(25[0-5]|2[0-4]\d|1\d\d|[1-9]?\d)){3}))|:)))(%.+)?\s*$/
			if(ip.match(pattern)){
				return true;
			}else{
				return false;
			}
	}	
}

function capitalize(s) {
    return s.charAt(0).toUpperCase() + s.slice(1);
}

function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function validatePortNumber(port){
	const pattern = /^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$/;
	if(port.match(pattern)){
		return true;
	}else{
		return false;
	}
}