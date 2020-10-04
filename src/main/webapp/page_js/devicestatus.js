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

function getData() {

	getTableData();
	getChartData('network');
}

$("#searchButton").click(function () {
	getChartData('cpu')
});


function getChartData(type) {
	var current_time = $('#input-calender-user').val();
	var start_time = current_time.split("-")[0];
	var end_time = current_time.split("-")[1];
	var start = moment(start_time, "MMMM Do YYYY, h:mm:ss a").valueOf();
	var end = moment(end_time, "MMMM Do YYYY, h:mm:ss a").valueOf();

	$('#chart_resource').empty();

	if (type == "network") {
		getNetworkChartData(start, end);
	} else {
		$.ajax({
			type: 'POST',
			beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
			url: '/rest/device/status/get/graph',
			data: {
				from: start,
				to: end,
				type: type,
			},
			success: function (data) {
				if (data.length == 0) {
					$('#chart_resource').append('<div><div role="alert" class="alert"><em class="fa fa-arrow-circle-left text-muted"></em>&nbsp;No data available!!!</div></div>');
					return false;
				} else {
					drawGraph(data, type);

				}
			}
		});
	}
}

function getNetworkChartData(start, end) {

	$.ajax({
		type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
		url: '/rest/device/status/get/graph',
		data: {
			from: start,
			to: end,
			type: 'Interface_IN',
		},
		success: function (data) {
			if (data.length == 0) {
				$('#chart_resource').append('<div><div role="alert" class="alert"><em class="fa fa-arrow-circle-left text-muted"></em>&nbsp;No data available!!!</div></div>');
				return false;
			} else {
				var dataLength = data.length;
				var seriesData = data;

				$.ajax({
					url: '/rest/device/status/get/graph',
					type: 'POST',
					beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
					data: {
						from: start,
						to: end,
						type: 'Interface_OUT',
					},
					success: function (data1) {

						for (i = 0; i < dataLength; i++) {
							entry = data1[i];
							for (j = 0; j < data1[i].data.length; j++) {
								if (data1[i].data[j][1] > 0) {
									entry.data[j][1] = (data1[i].data[j][1] * 1);
								}
							}
							seriesData[i].name = seriesData[i].name + "-in";
							seriesData[i].id = seriesData[i].id + "-in";

							entry.name = entry.name + "-out";
							entry.id = entry.id + "-out";

							seriesData.push(entry);
						}

						drawGraph(seriesData, 'network');
					}
				});

			}
		}
	});
}

function getTableData() {
	$('#table_network').empty();
	$('#table_cpu').empty();
	$('#table_memory').empty();
	$('#table_storage').empty();

	$.ajax({
		type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
		url: '/rest/device/cpu',
		data: {

		},
		success: function (data) {
			var total = 0;
			for (i = 0; i < data.length; i++) {
				row = "<tr><td>" + data[i].core + "</td><td>" + data[i].load + " %</td></tr>";
				$('#table_cpu').append(row);
				total = total + data[i].load;
			}
			var percent = Math.round(total / data.length);
			$('#cpu_percent').html(percent + " %");
			$(".progress-bar-danger").css("width", percent + "%")
		}
	});

	$.ajax({
		type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
		url: '/rest/device/storage',
		data: {},
		success: function (data) {
			storage = data[0];
			memory = data[1];

			for (i = 0; i < storage.length; i++) {
				var percent = Math.round((storage[i].usedStorage / storage[i].totalStorage) * 100);

				row = "<tr><td>" + storage[i].path + "</td><td>" + bytesToSTD(storage[i].totalStorage, true) +
					"</td><td>" + bytesToSTD(storage[i].usedStorage, true) + "</td><td>" + bytesToSTD(storage[i].freeStorage, true) +
					"</td><td>" + percent + " %</td></tr>";

				$('#table_storage').append(row);

				if (storage[i].path == "/") {
					$('#storage_percent').html(percent + " %");
					$('.progress-bar-primary').css("width", percent + "%")
				}
			}

			var percent = Math.round(((memory[0].totalphysicalMemory - memory[0].freePhysicalMemory) / memory[0].totalphysicalMemory) * 100);
			row = "<tr><td>" + bytesToSTD(memory[0].totalphysicalMemory, true) + "</td><td>" +
				bytesToSTD(memory[0].usedPhysicalMemory, true) + "</td><td>" +
				bytesToSTD(memory[0].usedCacheMemory, true) + "</td><td>" +
				bytesToSTD(memory[0].usedBufferMemory, true) + "</td><td>" +
				bytesToSTD(memory[0].usedSharedMemory, true) + "</td><td>" +
				bytesToSTD(memory[0].freePhysicalMemory, true) + "</td><td>" + percent + " % </td></tr>";

			
			$('#table_memory').append(row);
			$('#memory_percent').html(percent + " %");
			$('.progress-bar-info').css("width", percent + "%")
		}
	});

	$.ajax({
		type: 'POST',
		beforeSend: function(xhr) { xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken); },
		url: '/rest/device/interfaces',
		data: {

		},
		success: function (data) {
			for (i = 0; i < data.length; i++) {
				row = "<tr><td>" + data[i].name + "</td><td>" + data[i].ip + "</td><td>" + data[i].macAddress + "</td><td>" + data[i].status + "</td></tr>";
				$('#table_network').append(row);
			}
		}
	});
}

function drawGraph(seriesData, type) {
	var yLabel = "";
	var chart_title = "";
	if (type == "memory") {
		yLabel = "Memory Usage in GB";
		chart_title = "Memory Usage";
	} else if (type == "cpu") {
		yLabel = "CPU Usage in Percentage";
		chart_title = "CPU Usage";
	} else if (type == "network") {
		yLabel = "Network Traffic in bps";
		chart_title = "Network Usage";
	} else if (type == "storage") {
		yLabel = "Storage Usage in Percentage";
		chart_title = "Storage Usage";
	}

	$('#chart_resource').highcharts({
		chart: {
			type: 'area',
			zoomType: 'x',
			events: {

			}
		},
		title: {
			text: chart_title,
		},
		subtitle: {},
		

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
				text: yLabel
			},
			labels: {
				formatter: function () {
					if (type == 'network') {
						return convertToStdBits(Math.abs(this.value));
					} else if (type == 'storage' || type == 'cpu') {
						return (this.value + " %");
					} else if (type == "memory") {
						return convertToStdBytes(this.value * 8);
					}
				}
			}
		},
		tooltip: {
			shared: true,
			formatter: function () {
				var added = "";
				if (type == 'network') {
					for (var i = 0; i < this.points.length; i++) {
						added = added + this.points[i].series.name + ": " + convertToStdBits(Math.abs(this.points[i].y)) + '<br/>';
					}
				} else if (type == 'storage' || type == 'cpu') {
					for (var i = 0; i < this.points.length; i++) {
						added = added + this.points[i].series.name + ": " + this.points[i].y + ' %<br/>';
					}
				} else if (type == "memory") {
					for (var i = 0; i < this.points.length; i++) {
						added = added + this.points[i].series.name + ": " + convertToStdBytes(this.points[i].y * 8) + '<br/>';
					}
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
		series: seriesData
	});
	if (type == 'cpu' || type == 'storage') {
		$('#chart_resource').highcharts().yAxis[0].setExtremes(0, 100, true, false);
	}
	if (type == 'memory') {
		total = 1000;
		for (var i = 0; i < seriesData.length; i++) {
			if (seriesData[i].name == "Total_Memory") {
				total = seriesData[i].data[0][1];
				break;
			}
		}
		$('#chart_resource').highcharts().yAxis[0].setExtremes(0, total, true, false);
	}
}


function cpu_usage() {
	getChartData('cpu');
}

function storage_stat() {
	getChartData('storage');
}

function network_usage() {
	getChartData('network');
}

function ram_usage(){
	getChartData('memory');
}