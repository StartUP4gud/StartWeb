var csrfToken = $.cookie('XSRF-TOKEN');

$('#input-calender-user').daterangepicker({
		     timePicker: true,
		     startDate: moment().startOf('day'),
		     endDate: moment(),
		     locale: {
		         format: 'MMMM Do YYYY, h:mm:ss a'
		     }
});


getData();
	
$("#searchButton").click(function (){
	  getData();
});


function getData() {
	 var current_time = $('#input-calender-user').val();
	  var start_time = current_time.split("-")[0];
	  var end_time = current_time.split("-")[1];
	  var start = moment(start_time, "MMMM Do YYYY, h:mm:ss a").valueOf();
	  var end = moment(end_time, "MMMM Do YYYY, h:mm:ss a").valueOf();
	  
	  getReports(start, end, 'OUT_FLOWS');
	  getTableReports(start, end, 'OUT_FLOWS');
}

function getReports(startTime, endTime, type) {

	$('#chart_egress_fps').empty();
	$.ajax({
		url: '/rest/report/get/graph',
		type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
		data:{from:startTime,to:endTime,type:type},
		success: function(data){
			if(data.length==0){
				$('#chart_egress_fps').append('<div><div role="alert" class="alert"><em class="fa fa-arrow-circle-left text-muted"></em>&nbsp;No data available!!!</div></div>');
				return false;
			}
			else
			{
				drawGraph(data);

			}	
		}
	});
}

function getTableReports(startTime, endTime, type) {

	$('#tablebody').empty();
	$.ajax({
		url: '/rest/report/get/table',
		type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
		data:{from:startTime,to:endTime,type:type},
		success: function(data){
			
				showTable(data);
		}
	});
}

function showTable(data) {
	
	if(data.length==0){
		$('#tablebody').append('<tr><td  colspan="2" align="center">No data available!!!</td></tr>');
		return false;
	}

	for(i=0;i<data.length;i++){	

		var avg = data[i].total/data[i].count;
		val = convertToStdFlows(avg);
		var row = '<tr><td>'+ data[i].name +'</td><td>'+ val +'</td></tr>';

		$('#tablebody').append(row);	
	}
}

function drawGraph(seriesData) {

	$('#chart_egress_fps').highcharts({
		chart: {
			type: 'area',
			zoomType: 'x',
			events: {

			}
		},
		title: {
			text:''
		},
		subtitle: {
		},
		credits: {
			enabled: false
		},
		xAxis: {

			type: 'datetime'
		},
		yAxis: {
			gridLineWidth: .4,
			title: {
				style: {

				},
				text:"Exported Flows in fps"
			},
			labels: {

				formatter: function() { 
					return convertToStdPkts(this.value);

				}
			}
		},
		tooltip: {
			shared: true,
			formatter: function() { 
				var added="";
				for(var i=0;i<this.points.length;i++){
					added=added+this.points[i].series.name + ": "+convertToStdPkts(this.points[i].y)+'<br/>';
				}

				return added;
			}
		},
		plotOptions: {
			series: {
				marker: {
					enabled: false
				}
			},
			area: {
				stacking: 'normal'
			}
		},
		series:seriesData 
	});	
}