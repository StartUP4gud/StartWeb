var csrfToken = $.cookie('XSRF-TOKEN');

connect();

function connect() {		
		
	chart_realtime_ingress_bps();
	chart_realtime_ingress_pps();
	chart_realtime_egress_bps();
	chart_realtime_egress_pps();
	chart_realtime_egress_fps();
	chart_realtime_drop_pps();
}


function chart_realtime_drop_pps() {	
	$('#realtime_drop_pps').empty();
	$('#realtime_drop_pps').highcharts({
		chart: {
			type: 'line',
			
			marginRight : 10,
			events: {
				load: function () {
					var series = this.series;
								
				}
			}
		},
		title: {
			text: 'Dropped Packets'
		},
		subtitle: {
			text: ''
		},
		xAxis: {
			type: 'datetime',
			title: {
				text: 'Time'
			}
		},
		yAxis: {       
			min: 0,
			gridLineWidth: 0,
			minorGridLineWidth: 0,
			title: {
				style: {
					

				},
				text: 'pps'
			}
		},
		tooltip: {
			style: {
				
					},
				formatter: function () {
					var s = '<b>'+this.series.name +':'+ convertToStdPkts(this.y) + '</b>';
					return s;
				}
		},
		plotOptions: {
			area: {
				pointStart: 1940,
				marker: {
					enabled: false,
					symbol: 'circle',
					radius: 2,
					states: {
						hover: {
							enabled: true
						}
					}
				}
			}
		},
		legend: {
			enabled: true
		},
		exporting: {
			enabled: true
		},
		series: [ {
			name: 'NIC',
			data: (function() {
				var data = [],
				time = currentTime(),
				i;
				for (i = -120; i <= 0; i++) {
					data.push({
						x: time + i * 1000,
						y: null
					});
				}
				return data;
			})()
		}/*, {
			name: 'NIC-2',
			data: (function() {
				var data = [],
				time = currentTime(),
				i;

				for (i = -120; i <= 0; i++) {
					data.push({
						x: time + i * 1000,
						y: null
					});
				}
				return data;
			})()
		}*/ ],
		credits : {
			enabled : false
		}
	});
}

function chart_realtime_egress_fps() {	
	$('#realtime_egress_fps').empty();
	$('#realtime_egress_fps').highcharts({
		chart: {
			type: 'line',
			
			marginRight : 10,
			events: {
				load: function () {
					var series = this.series;
								
				}
			}
		},
		title: {
			text: 'Exported Flows'
		},
		subtitle: {
			text: ''
		},
		xAxis: {
			type: 'datetime',
			title: {
				text: 'Time'
			}
		},
		yAxis: {       
			min: 0,
			gridLineWidth: 0,
			minorGridLineWidth: 0,
			title: {
				style: {
					
				},
				text: 'fps'
			}
		},
		tooltip: {
			style: {
		
			},
				formatter: function () {
					var s = '<b>'+this.series.name +':'+ convertToStdFlows(this.y) + '</b>';
					return s;
				}
		},
		plotOptions: {
			area: {
				pointStart: 1940,
				marker: {
					enabled: false,
					symbol: 'circle',
					radius: 2,
					states: {
						hover: {
							enabled: true
						}
					}
				}
			}
		},
		legend: {
			enabled: true
		},
		exporting: {
			enabled: true
		},
		series: [ {
			name: 'Collector',
			data: (function() {
				var data = [],
				time = currentTime(),
				i;

				for (i = -120; i <= 0; i++) {
					data.push({
						x: time + i * 1000,
						y: null
					});
				}
				return data;
			})()
		} ],
		credits : {
			enabled : false
		}
	});
}

function chart_realtime_egress_pps() {	
	$('#realtime_egress_pps').empty();
	$('#realtime_egress_pps').highcharts({
		chart: {
			type: 'line',
			
			marginRight : 10,
			events: {
				load: function () {
					var series = this.series;
								
				}
			}
		},
		title: {
			text: 'Egress Packets'
		},
		subtitle: {
			text: ''
		},
		xAxis: {
			type: 'datetime',
			title: {
				text: 'Time'
			}
		},
		yAxis: {       
			min: 0,
			gridLineWidth: 0,
			minorGridLineWidth: 0,
			title: {
				style: {
					

				},
				text: 'pps'
			}
		},
		tooltip: {
			style: {
				
					},
				formatter: function () {
					var s = '<b>'+this.series.name +':'+ convertToStdPkts(this.y) + '</b>';
					return s;
				}
		},
		plotOptions: {
			area: {
				pointStart: 1940,
				marker: {
					enabled: false,
					symbol: 'circle',
					radius: 2,
					states: {
						hover: {
							enabled: true
						}
					}
				}
			}
		},
		legend: {
			enabled: true
		},
		exporting: {
			enabled: true
		},
		series: [ {
			name: 'Collector',
			data: (function() {
				var data = [],
				time = currentTime(),
				i;

				for (i = -120; i <= 0; i++) {
					data.push({
						x: time + i * 1000,
						y: null
					});
				}
				return data;
			})()
		}],
		credits : {
			enabled : false
		}
	});
}

function chart_realtime_egress_bps() {	
	$('#realtime_egress_bps').empty();
	$('#realtime_egress_bps').highcharts({
		chart: {
			type: 'line',
			
			marginRight : 10,
			events: {
				load: function () {
					var series = this.series;
							
				}
			}
		},
		title: {
			text: 'Egress Bandwidth'
		},
		subtitle: {
			text: ''
		},
		xAxis: {
			type: 'datetime',
			title: {
				text: 'Time'
			}
		},
		yAxis: {       
			min: 0,
			gridLineWidth: 0,
			minorGridLineWidth: 0,
			title: {
				style: {
					

				},
				text: 'bps'
			},
			labels: {
				formatter: function() { return bitesToSTD(this.value, true); }
			}
		},
		tooltip: {
			style: {
				
					},
				formatter: function () {
					var s = '<b>'+this.series.name +':'+ bitesToSTD(this.y,true) + '</b>';
					return s;
				}
		},
		plotOptions: {
			area: {
				pointStart: 1940,
				marker: {
					enabled: false,
					symbol: 'circle',
					radius: 2,
					states: {
						hover: {
							enabled: true
						}
					}
				}
			}
		},
		legend: {
			enabled: true
		},
		exporting: {
			enabled: true
		},
		series: [ {
			name: 'Collector',
			data: (function() {
				var data = [],
				time = currentTime(),
				i;

				for (i = -120; i <= 0; i++) {
					data.push({
						x: time + i * 1000,
						y: null
					});
				}
				return data;
			})()
		}],
		credits : {
			enabled : false
		}
	});
}

function chart_realtime_ingress_pps() {	
	$('#realtime_ingress_pps').empty();
	$('#realtime_ingress_pps').highcharts({
		chart: {
			type: 'line',
			
			marginRight : 10,
			events: {
				load: function () {
					var series = this.series;
								
				}
			}
		},
		title: {
			text: 'Ingress Packets'
		},
		subtitle: {
			text: ''
		},
		xAxis: {
			type: 'datetime',
			title: {
				text: 'Time'
			}
		},
		yAxis: {       
			min: 0,
			gridLineWidth: 0,
			minorGridLineWidth: 0,
			title: {
				style: {
					

				},
				text: 'pps'
			}
		},
		tooltip: {
			style: {
			
					},
				formatter: function () {
					var s = '<b>'+this.series.name +':'+ convertToStdPkts(this.y) + '</b>';
					return s;
				}
		},
		plotOptions: {
			area: {
				pointStart: 1940,
				marker: {
					enabled: false,
					symbol: 'circle',
					radius: 2,
					states: {
						hover: {
							enabled: true
						}
					}
				}
			}
		},
		legend: {
			enabled: true
		},
		exporting: {
			enabled: true
		},
		series: [ {
			name: 'NIC',
			data: (function() {
				var data = [],
				time = currentTime(),
				i;

				for (i = -120; i <= 0; i++) {
					data.push({
						x: time + i * 1000,
						y: null
					});
				}
				return data;
			})()
		} ],
		credits : {
			enabled : false
		}
	});
}

function chart_realtime_ingress_bps() {	
	$('#realtime_ingress_bps').empty();
	$('#realtime_ingress_bps').highcharts({
		chart: {
			type: 'line',
			
			marginRight : 10,
			events: {
				load: function () {
					var series = this.series;
								
				}
			}
		},
		title: {
			text: 'Ingress Bandwidth'
		},
		subtitle: {
			text: ''
		},
		xAxis: {
			type: 'datetime',
			title: {
				text: 'Time'
			}
		},
		yAxis: {       
			min: 0,
			gridLineWidth: 0,
			minorGridLineWidth: 0,
			title: {
				style: {
					

				},
				text: 'bps'
			},
			labels: {
				formatter: function() { return bitesToSTD(this.value, true); }
			}
		},
		tooltip: {
			style: {
				
					},
				formatter: function () {
					var s = '<b>'+this.series.name +':'+ bitesToSTD(this.y,true) + '</b>';
					return s;
				}
		},
		plotOptions: {
			area: {
				pointStart: 1940,
				marker: {
					enabled: false,
					symbol: 'circle',
					radius: 2,
					states: {
						hover: {
							enabled: true
						}
					}
				}
			}
		},
		legend: {
			enabled: true
		},
		exporting: {
			enabled: true
		},
		series: [ {
			name: 'NIC',
			data: (function() {
				var data = [],
				time = currentTime(),
				i;
				for (i = -120; i <= 0; i++) {
					data.push({
						x: time + i * 1000,
						y: null
					});
				}
				return data;
			})()
		} ],
		credits : {
			enabled : false
		}
	});
}