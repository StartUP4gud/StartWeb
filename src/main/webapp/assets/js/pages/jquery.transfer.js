var csrfToken = $.cookie('XSRF-TOKEN');
(function($) {

    var Transfer = function(element, options) {
        this.$element = element;
    
        this.defaults = {
           
            itemName: "item",
          
            groupItemName: "groupItem",
         
            groupArrayName: "groupArray",
           
            valueName: "value",
          
            dataArray: [],
          
            groupDataArray: []
        };
       
        this.settings = $.extend(this.defaults, options);

        this.selected_total_num = 0;
   
        this.tabNameText = "items";
        
        this.groupTabNameText = "group items";

        this.rightTabNameText = "selected items";
       
        this.searchPlaceholderText = "search";
  
        this.default_total_num_text_template = "total: {total_num}";
      
        this.default_right_item_total_num_text = get_total_num_text(this.default_total_num_text_template, 0);

        this.item_total_num = this.settings.dataArray.length;
     
        this.group_item_total_num = get_group_items_num(this.settings.groupDataArray, this.settings.groupArrayName);
      
        this.group_item_total_num_text = get_total_num_text(this.default_total_num_text_template, this.group_item_total_num);
    
        this.isGroup = this.group_item_total_num > 0;
      
        this._data = new InnerMap();

        this.id = (getId())();
       
        this.transferId = "#transfer_double_" + this.id;
  
        this.itemSearcherId = "#listSearch_" + this.id;
     
        this.groupItemSearcherId = "#groupListSearch_" + this.id;
 
        this.selectedItemSearcherId = "#selectedListSearch_" + this.id;

        this.transferDoubleListUlClass = ".transfer-double-list-ul-" + this.id;
      
        this.transferDoubleListLiClass = ".transfer-double-list-li-" + this.id;
       
        this.checkboxItemClass = ".checkbox-item-" + this.id;
        
        this.checkboxItemLabelClass = ".checkbox-name-" + this.id;
        
        this.totalNumLabelClass = ".total_num_" + this.id;
      
        this.leftItemSelectAllId = "#leftItemSelectAll_" + this.id;

        this.transferDoubleGroupListUlClass = ".transfer-double-group-list-ul-" + this.id;
    
        this.transferDoubleGroupListLiClass = ".transfer-double-group-list-li-" + this.id;
   
        this.groupSelectAllClass = ".group-select-all-" + this.id;
     
        this.transferDoubleGroupListLiUlLiClass = ".transfer-double-group-list-li-ul-li-" + this.id;
   
        this.groupCheckboxItemClass = ".group-checkbox-item-" + this.id;
    
        this.groupCheckboxNameLabelClass = ".group-checkbox-name-" + this.id;

        this.groupTotalNumLabelClass = ".group_total_num_" + this.id;

        this.groupItemSelectAllId = "#groupItemSelectAll_" + this.id;

        this.transferDoubleSelectedListUlClass = ".transfer-double-selected-list-ul-" + this.id;

        this.transferDoubleSelectedListLiClass = ".transfer-double-selected-list-li-" + this.id;
    
        this.checkboxSelectedItemClass = ".checkbox-selected-item-" + this.id;
  
        this.rightItemSelectAllId = "#rightItemSelectAll_" + this.id;
    
        this.selectedTotalNumLabelClass = ".selected_total_num_" + this.id;
    
        this.addSelectedButtonId = "#add_selected_" + this.id;
      
        this.deleteSelectedButtonId = "#delete_selected_" + this.id;
    }
    
    $.fn.Transfer = function(options) {
        
        var transfer = new Transfer(this, options);
        
        transfer.init();

        return {
            
            getSelectedItems: transfer.get_selected_items
        }
    }


    Transfer.prototype.init = function() {
        
        this.$element.append(this.generate_transfer());

        if (this.isGroup) {
            
            this.fill_group_data();

            
            this.left_group_checkbox_item_click_handler();
            
            this.group_select_all_handler();
            
            this.group_item_select_all_handler();
            
            this.left_group_items_search_handler();

        } else {
            
            this.fill_data();

            
            this.left_checkbox_item_click_handler();
            
            this.left_item_select_all_handler();
            
            this.left_items_search_handler();
        }

        
        this.right_checkbox_item_click_handler();
        
        this.move_pre_selection_items_handler();
        this.move_selected_items_handler();

        this.right_items_search_handler();
    }


    Transfer.prototype.generate_transfer = function() {
        var html =
            '<div class="transfer-double" id="transfer_double_' + this.id + '">'
            + '<div class="transfer-double-header"></div>'
            + '<div class="transfer-double-content clearfix">'
            + this.generate_left_part()
            + '<div class="transfer-double-content-middle">'
            + '<div class="btn-select-arrow" id="add_selected_' + this.id + '"><i class="iconfont icon-forward"></i></div>'
            + '<div class="btn-select-arrow" id="delete_selected_' + this.id + '"><i class="iconfont icon-back"></i></div>'
            + '</div>'
            + this.generate_right_part()
            + '</div>'
            + '<div class="transfer-double-footer"></div>'
            + '</div>';
        return html;
    }

    Transfer.prototype.generate_left_part = function() {
        return '<div class="transfer-double-content-left">'
        + '<div class="transfer-double-content-param">'
        + '<div class="param-item">' + (this.isGroup ? this.groupTabNameText : this.tabNameText) + '</div>'
        + '</div>'
        + (this.isGroup ? this.generate_group_items_container() : this.generate_items_container())
        + '</div>'
    }

    
    Transfer.prototype.generate_group_items_container = function() {
        return '<div class="transfer-double-list transfer-double-list-' + this.id + '">'
        + '<div class="transfer-double-list-header">'
        + '<div class="transfer-double-list-search">'
        + '<input class="transfer-double-list-search-input" type="text" id="groupListSearch_' + this.id + '" placeholder="' + this.searchPlaceholderText + '" value="" />'
        + '</div>'
        + '</div>'
        + '<div class="transfer-double-list-content">'
        + '<div class="transfer-double-list-main">'
        + '<ul class="transfer-double-group-list-ul transfer-double-group-list-ul-' + this.id + '">'
        + '</ul>'
        + '</div>'
        + '</div>'
        + '<div class="transfer-double-list-footer">'
        + '<div class="checkbox-group">'
        + '<input type="checkbox" class="checkbox-normal" id="groupItemSelectAll_' + this.id + '"><label for="groupItemSelectAll_' + this.id + '" class="group_total_num_' + this.id + '"></label>'
        + '</div>'
        + '</div>'
        + '</div>'
    }

  
    Transfer.prototype.generate_items_container = function() {
        return '<div class="transfer-double-list transfer-double-list-' + this.id + '">'
        + '<div class="transfer-double-list-header">'
        + '<div class="transfer-double-list-search">'
        + '<input class="transfer-double-list-search-input" type="text" id="listSearch_' + this.id + '" placeholder="' + this.searchPlaceholderText + '" value="" />'
        + '</div>'
        + '</div>'
        + '<div class="transfer-double-list-content">'
        + '<div class="transfer-double-list-main">'
        + '<ul class="transfer-double-list-ul transfer-double-list-ul-' + this.id + '">'
        + '</ul>'
        + '</div>'
        + '</div>'
        + '<div class="transfer-double-list-footer">'
        + '<div class="checkbox-group">'
        + '<input type="checkbox" class="checkbox-normal" id="leftItemSelectAll_' + this.id + '"><label for="leftItemSelectAll_' + this.id + '" class="total_num_' + this.id + '"></label>'
        + '</div>'
        + '</div>'
        + '</div>'
    }

    Transfer.prototype.generate_right_part = function() {
        return '<div class="transfer-double-content-right">'
        + '<div class="transfer-double-content-param">'
        + '<div class="param-item">' + this.rightTabNameText + '</div>'
        + '</div>'
        + '<div class="transfer-double-selected-list">'
        + '<div class="transfer-double-selected-list-header">'
        + '<div class="transfer-double-selected-list-search">'
        + '<input class="transfer-double-selected-list-search-input" type="text" id="selectedListSearch_' + this.id + '" placeholder="' + this.searchPlaceholderText + '" value="" />'
        + '</div>'
        + '</div>'
        + '<div class="transfer-double-selected-list-content">'
        + '<div class="transfer-double-selected-list-main">'
        + '<ul class="transfer-double-selected-list-ul transfer-double-selected-list-ul-' + this.id + '">'
        + '</ul>'
        + '</div>'
        + '</div>'
        + '<div class="transfer-double-list-footer">'
        + '<label class="selected_total_num_' + this.id + '">' + this.default_right_item_total_num_text + '</label>'
        + '</div>'
        + '</div>'
        + '</div>'
    }

    Transfer.prototype.fill_data = function() {
        $(this.transferId).find(this.transferDoubleListUlClass).empty();
        $(this.transferId).find(this.transferDoubleListUlClass).append(this.generate_left_items());

        $(this.transferId).find(this.transferDoubleSelectedListUlClass).empty();
        $(this.transferId).find(this.transferDoubleSelectedListUlClass).append(this.generate_right_items());

        
        $(this.transferId).find(this.totalNumLabelClass).empty();
        $(this.transferId).find(this.totalNumLabelClass).append(get_total_num_text(this.default_total_num_text_template, this._data.get("total_count")));

        
        $(this.transferId).find(this.selectedTotalNumLabelClass).empty();
        $(this.transferId).find(this.selectedTotalNumLabelClass).append(get_total_num_text(this.default_total_num_text_template, this.selected_total_num));
    }

   
    Transfer.prototype.fill_group_data = function() {
        $(this.transferId).find(this.transferDoubleGroupListUlClass).empty();
        $(this.transferId).find(this.transferDoubleGroupListUlClass).append(this.generate_left_group_items());

        
        $(this.transferId).find(this.groupTotalNumLabelClass).empty();
        $(this.transferId).find(this.groupTotalNumLabelClass).append(this.group_item_total_num_text);
    }

   
    Transfer.prototype.generate_left_items = function() {
        var html = "";
        var dataArray = this.settings.dataArray;
        var itemName = this.settings.itemName;
        var valueName = this.settings.valueName;

        for (var i = 0; i < dataArray.length; i++) {

            var selected = dataArray[i].selected || false;
            selected ? this.selected_total_num++ : void(0)

            html +=
            '<li class="transfer-double-list-li transfer-double-list-li-' + this.id + ' ' + (selected ? 'selected-hidden' : '') + '">' +
            '<div class="checkbox-group">' +
            '<input type="checkbox" value="' + dataArray[i][valueName] + '" class="checkbox-normal checkbox-item-' 
            + this.id + '" id="itemCheckbox_' + i + '_' + this.id + '">' +
            '<label class="checkbox-name-' + this.id + '" for="itemCheckbox_' + i + '_' + this.id + '">' + dataArray[i][itemName] + '</label>' +
            '</div>' +
            '</li>'
        }

        this._data.put("pre_selection_count", 0);
        this._data.put("total_count", dataArray.length - this.selected_total_num);

        return html;
    }

  
    Transfer.prototype.generate_left_group_items = function() {
        var html = "";
        var id = this.id;
        var groupDataArray = this.settings.groupDataArray;
        var groupItemName = this.settings.groupItemName;
        var groupArrayName = this.settings.groupArrayName;
        var itemName = this.settings.itemName;
        var valueName = this.settings.valueName;

        for (var i = 0; i < groupDataArray.length; i++) {
            html +=
                '<li class="transfer-double-group-list-li transfer-double-group-list-li-' + id + '">'
                + '<div class="checkbox-group">' +
                '<input type="checkbox" class="checkbox-normal group-select-all-' + id + '" id="group_' + i + '_' + id + '">' +
                '<label for="group_' + i + '_' + id + '" class="group-name-' + id + '">' + groupDataArray[i][groupItemName] + '</label>' +
                '</div>';
            if (groupDataArray[i][groupArrayName].length > 0) {

                var _value = {};
                _value["pre_selection_count"] = 0
                _value["total_count"] = groupDataArray[i][groupArrayName].length
                this._data.put('group_' + i + '_' + this.id, _value);

                html += '<ul class="transfer-double-group-list-li-ul transfer-double-group-list-li-ul-' + id + '">'
                for (var j = 0; j < groupDataArray[i][groupArrayName].length; j++) {
                    html += '<li class="transfer-double-group-list-li-ul-li transfer-double-group-list-li-ul-li-' + id + '">' +
                        '<div class="checkbox-group">' +
                        '<input type="checkbox" value="' + groupDataArray[i][groupArrayName][j][valueName] + '" class="checkbox-normal group-checkbox-item-' + id + ' belongs-group-' + i + '-' + id + '" id="group_' + i + '_checkbox_' + j + '_' + id + '">' +
                        '<label for="group_' + i + '_checkbox_' + j + '_' + id + '" class="group-checkbox-name-' + id + '">' + groupDataArray[i][groupArrayName][j][itemName] + '</label>' +
                        '</div>' +
                        '</li>';
                }
                html += '</ul>'
            } else {
                html += '</li>';
            }
            html += '</li>';
        }

        return html;
    }

    Transfer.prototype.generate_right_items = function() {
        var html = "";
        var dataArray = this.settings.dataArray;
        var itemName = this.settings.itemName;
        var valueName = this.settings.valueName;

        for (var i = 0; i < dataArray.length; i++) {
            if (dataArray[i].selected) {
                html += this.generate_item(this.id, i, dataArray[i][valueName], dataArray[i][itemName]);
            }
        }
        return html;
    }

  
    Transfer.prototype.generate_right_group_items = function() {

    }

    Transfer.prototype.left_checkbox_item_click_handler = function() {
        var self = this;
        $(self.transferId).on("click", self.checkboxItemClass, function () {
            var pre_selection_num = 0;
            $(this).is(":checked") ? pre_selection_num++ : pre_selection_num--

            var pre_selection_count = self._data.get("pre_selection_count");
            self._data.put("pre_selection_count", pre_selection_count + pre_selection_num);

            if (self._data.get("pre_selection_count") > 0) {
                $(self.addSelectedButtonId).addClass("btn-arrow-active");
            } else {
                $(self.addSelectedButtonId).removeClass("btn-arrow-active");
            }

            if (self._data.get("pre_selection_count") < self._data.get("total_count")) {
                $(self.leftItemSelectAllId).prop("checked", false);
            } else if (self._data.get("pre_selection_count") == self._data.get("total_count")) {
                $(self.leftItemSelectAllId).prop("checked", true);
            }
        });
    }

 
    Transfer.prototype.left_group_checkbox_item_click_handler_old = function() {
        var self = this;
        $(self.transferId).on("click", self.groupCheckboxItemClass, function () {
            var pre_selection_num = 0;
            var pre_selection_count_map = new InnerMap();
            self._data.forEach(function(key, value) {
                pre_selection_count_map.put(key, value['pre_selection_count'])
            })

            for (var i = 0; i < $(self.transferId).find(self.groupCheckboxItemClass).length; i++) {
                var groupCheckboxItems = $(self.transferId).find(self.groupCheckboxItemClass);
                if (groupCheckboxItems.parent("div").parent("li").eq(i).css('display') != "none" && groupCheckboxItems.eq(i).is(':checked')) {
                    var id = groupCheckboxItems.eq(i).prop("id");
                    var groupIndex = id.split("_")[1];
                    
                    var groupItem = self._data.get('group_' + groupIndex + '_' + self.id);
                    groupItem["pre_selection_count"] = 

                    pre_selection_num++;
                }
            }
            if (pre_selection_num > 0) {
                $(self.addSelectedButtonId).addClass("btn-arrow-active");
            } else {
                $(self.addSelectedButtonId).removeClass("btn-arrow-active");
            }
        });
    }

   
    Transfer.prototype.left_group_checkbox_item_click_handler = function() {
        var self = this;
        $(self.transferId).on("click", self.groupCheckboxItemClass, function () {
            var pre_selection_num = 0;
            var total_pre_selection_num = 0;
            var remain_total_count = 0

            $(this).is(":checked") ? pre_selection_num++ : pre_selection_num--

            var groupIndex = $(this).prop("id").split("_")[1];
            var groupItem =  self._data.get('group_' + groupIndex + '_' + self.id);
            var pre_selection_count = groupItem["pre_selection_count"];
            groupItem["pre_selection_count"] = pre_selection_count + pre_selection_num

            self._data.forEach(function(key, value) {
                total_pre_selection_num += value["pre_selection_count"]
                remain_total_count += value["total_count"]
            });

            if (total_pre_selection_num > 0) {
                $(self.addSelectedButtonId).addClass("btn-arrow-active");
            } else {
                $(self.addSelectedButtonId).removeClass("btn-arrow-active");
            }

            if (groupItem["pre_selection_count"] < groupItem["total_count"]) {
                $(self.transferId).find("#group_" + groupIndex + "_" + self.id).prop("checked", false);
            } else if (groupItem["pre_selection_count"] == groupItem["total_count"]) {
                $(self.transferId).find("#group_" + groupIndex + "_" + self.id).prop("checked", true);
            }

            if (total_pre_selection_num == remain_total_count) {
                $(self.groupItemSelectAllId).prop("checked", true);
            } else {
                $(self.groupItemSelectAllId).prop("checked", false);
            }
        });
    }

    Transfer.prototype.group_select_all_handler = function() {
        var self = this;
        $(self.groupSelectAllClass).on("click", function () {
            
            var groupIndex = ($(this).attr("id")).split("_")[1];
            var groups =  $(self.transferId).find(".belongs-group-" + groupIndex + "-" + self.id);
            var groupSelectAllArray = $(self.transferId).find(self.groupSelectAllClass);
            
            if ($(this).is(':checked')) {
                
                $(self.addSelectedButtonId).addClass("btn-arrow-active");
                for (var i = 0; i < groups.length; i++) {
                    if (!groups.eq(i).is(':checked') && groups.eq(i).parent("div").parent("li").css("display") != "none") {
                        groups.eq(i).prop("checked", true);
                    }
                }

                var groupItem = self._data.get($(this).prop("id"));
                groupItem["pre_selection_count"] = groupItem["total_count"];

                var groupCheckedNum = 0;
                groupSelectAllArray.each(function () {
                    if ($(this).is(":checked")) {
                        groupCheckedNum = groupCheckedNum + 1;
                    }
                });
                if (groupCheckedNum == groupSelectAllArray.length) {
                    $(self.groupItemSelectAllId).prop("checked", true);
                }
            } else {
                for (var j = 0; j < groups.length; j++) {
                    if (groups.eq(j).is(':checked') && groups.eq(i).parent("div").parent("li").css("display") != "none") {
                        groups.eq(j).prop("checked", false);
                    }
                }

                var groupItem = self._data.get($(this).prop("id"))["pre_selection_count"] = 0;

                var groupCheckedNum = 0;
                groupSelectAllArray.each(function () {
                    if ($(this).is(":checked")) {
                        groupCheckedNum = groupCheckedNum + 1;
                    }
                });
                if (groupCheckedNum != groupSelectAllArray.length) {
                    $(self.groupItemSelectAllId).prop("checked", false);
                }
                if (groupCheckedNum == 0) {
                    $(self.addSelectedButtonId).removeClass("btn-arrow-active");
                }
            }
        });
    }

 
    Transfer.prototype.group_item_select_all_handler = function() {
        var self = this;
        $(self.groupItemSelectAllId).on("click", function () {
            var groupCheckboxItems = $(self.transferId).find(self.groupCheckboxItemClass);
            if ($(this).is(':checked')) {
                for (var i = 0; i < groupCheckboxItems.length; i++) {
                    if (groupCheckboxItems.parent("div").parent("li").eq(i).css('display') != "none" && !groupCheckboxItems.eq(i).is(':checked')) {
                        groupCheckboxItems.eq(i).prop("checked", true);
                    }
                    if (!$(self.transferId).find(self.groupSelectAllClass).eq(i).is(':checked')) {
                        $(self.transferId).find(self.groupSelectAllClass).eq(i).prop("checked", true);
                    }
                }

                self._data.forEach(function (key, value) {
                    value["pre_selection_count"] = value["total_count"];
                })

                $(self.addSelectedButtonId).addClass("btn-arrow-active");
            } else {
                for (var i = 0; i < groupCheckboxItems.length; i++) {
                    if (groupCheckboxItems.parent("div").parent("li").eq(i).css('display') != "none" && groupCheckboxItems.eq(i).is(':checked')) {
                        groupCheckboxItems.eq(i).prop("checked", false);
                    }
                    if ($(self.transferId).find(self.groupSelectAllClass).eq(i).is(':checked')) {
                        $(self.transferId).find(self.groupSelectAllClass).eq(i).prop("checked", false);
                    }
                }

                self._data.forEach(function (key, value) {
                    value["pre_selection_count"] = 0;
                })

                $(self.addSelectedButtonId).removeClass("btn-arrow-active");
            }
        });
    }


    Transfer.prototype.left_group_items_search_handler = function() {
        var self = this;
        $(self.groupItemSearcherId).on("keyup", function () {
            $(self.transferId).find(self.transferDoubleGroupListUlClass).css('display', 'block');
            var transferDoubleGroupListLiUlLis = $(self.transferId).find(self.transferDoubleGroupListLiUlLiClass);
            if ($(self.groupItemSearcherId).val() == "") {
                for (var i = 0; i < transferDoubleGroupListLiUlLis.length; i++) {
                    if (!transferDoubleGroupListLiUlLis.eq(i).hasClass("selected-hidden")) {
                        transferDoubleGroupListLiUlLis.eq(i).parent("ul").parent("li").css('display', 'block');
                        transferDoubleGroupListLiUlLis.eq(i).css('display', 'block');
                    } else {
                        transferDoubleGroupListLiUlLis.eq(i).parent("ul").parent("li").css('display', 'block');
                    }
                }
                return;
            }

            
            $(self.transferId).find(self.transferDoubleGroupListLiClass).css('display', 'none');
            transferDoubleGroupListLiUlLis.css('display', 'none');

            for (var j = 0; j < transferDoubleGroupListLiUlLis.length; j++) {
                if (!transferDoubleGroupListLiUlLis.eq(j).hasClass("selected-hidden")
                    && transferDoubleGroupListLiUlLis.eq(j).text()
                        .substr(0, $(self.groupItemSearcherId).val().length).toLowerCase() == $(self.groupItemSearcherId).val().toLowerCase()) {
                            transferDoubleGroupListLiUlLis.eq(j).parent("ul").parent("li").css('display', 'block');
                            transferDoubleGroupListLiUlLis.eq(j).css('display', 'block');
                }
            }
        });
    }

  
    Transfer.prototype.left_item_select_all_handler = function() {
        var self = this;
        $(self.leftItemSelectAllId).on("click", function () {
            var checkboxItems = $(self.transferId).find(self.checkboxItemClass);
            if ($(this).is(':checked')) {
                for (var i = 0; i < checkboxItems.length; i++) {
                    if (checkboxItems.eq(i).parent("div").parent("li").css('display') != "none" && !checkboxItems.eq(i).is(':checked')) {
                        checkboxItems.eq(i).prop("checked", true);
                    }
                }
                self._data.put("pre_selection_count", self._data.get("total_count"));
                $(self.addSelectedButtonId).addClass("btn-arrow-active");
            } else {
                for (var i = 0; i < checkboxItems.length; i++) {
                    if (checkboxItems.eq(i).parent("div").parent("li").css('display') != "none" && checkboxItems.eq(i).is(':checked')) {
                        checkboxItems.eq(i).prop("checked", false);
                    }
                }
                $(self.addSelectedButtonId).removeClass("btn-arrow-active");
                self._data.put("pre_selection_count", 0);
            }
        });
    }

    Transfer.prototype.left_items_search_handler = function() {
        var self = this;
        $(self.itemSearcherId).on("keyup", function () {
            var transferDoubleListLis = $(self.transferId).find(self.transferDoubleListLiClass);
            $(self.transferId).find(self.transferDoubleListUlClass).css('display', 'block');
            if ($(self.itemSearcherId).val() == "") {
                for (var i = 0; i < transferDoubleListLis.length; i++) {
                    if (!transferDoubleListLis.eq(i).hasClass("selected-hidden")) {
                        $(self.transferId).find(self.transferDoubleListLiClass).eq(i).css('display', 'block');
                    }
                }
                return;
            }

            transferDoubleListLis.css('display', 'none');

            for (var j = 0; j < transferDoubleListLis.length; j++) {
                if (!transferDoubleListLis.eq(j).hasClass("selected-hidden")
                    && transferDoubleListLis.eq(j).text()
                        .substr(0, $(self.itemSearcherId).val().length).toLowerCase() == $(self.itemSearcherId).val().toLowerCase()) {
                            transferDoubleListLis.eq(j).css('display', 'block');
                }
            }
        });
    }

   
    Transfer.prototype.right_checkbox_item_click_handler = function() {
        var self = this;
        $(self.transferId).on("click", self.checkboxSelectedItemClass, function () {
            var pre_selection_num = 0;
            for (var i = 0; i < $(self.transferId).find(self.checkboxSelectedItemClass).length; i++) {
                if ($(self.transferId).find(self.checkboxSelectedItemClass).eq(i).is(':checked')) {
                    pre_selection_num++;
                }
            }
            if (pre_selection_num > 0) {
                $(self.deleteSelectedButtonId).addClass("btn-arrow-active");
            } else {
                $(self.deleteSelectedButtonId).removeClass("btn-arrow-active");
            }
        });
    }

   
    Transfer.prototype.move_pre_selection_items_handler = function() {
        var self = this;
        $(self.addSelectedButtonId).on("click", function () {
            self.isGroup ? self.move_pre_selection_group_items() : self.move_pre_selection_items()
            
            if (Object.prototype.toString.call(self.settings.callable) === "[object Function]") {
                self.settings.callable.call(self, self.get_selected_items());
            }
        });
    }

  
    Transfer.prototype.move_pre_selection_group_items = function() {
        var pre_selection_num = 0;
        var html = "";
        var groupCheckboxItems = $(this.transferId).find(this.groupCheckboxItemClass);
        for (var i = 0; i < groupCheckboxItems.length; i++) {
            if (!groupCheckboxItems.eq(i).parent("div").parent("li").hasClass("selected-hidden") && groupCheckboxItems.eq(i).is(':checked')) {
                var checkboxItemId = groupCheckboxItems.eq(i).attr("id");
                var groupIndex = checkboxItemId.split("_")[1];
                var itemIndex = checkboxItemId.split("_")[3];
                var labelText = $(this.transferId).find(this.groupCheckboxNameLabelClass).eq(i).text();
                var value = groupCheckboxItems.eq(i).val();
                
                html += this.generate_group_item(this.id, groupIndex, itemIndex, value, labelText);
                groupCheckboxItems.parent("div").parent("li").eq(i).css("display", "").addClass("selected-hidden");
                pre_selection_num++;

                var groupItem = this._data.get('group_' + groupIndex + '_' + this.id);
                var total_count = groupItem["total_count"];
                var pre_selection_count = groupItem["pre_selection_count"];
                groupItem["total_count"] = --total_count;
                groupItem["pre_selection_count"] = --pre_selection_count;
            }
        }

        if (pre_selection_num > 0) {
            var groupSelectAllArray = $(this.transferId).find(this.groupSelectAllClass);
            for (var j = 0; j < groupSelectAllArray.length; j++) {
                if (groupSelectAllArray.eq(j).is(":checked")) {
                    groupSelectAllArray.eq(j).prop("disabled", "disabled");
                }
            }

            var remain_total_count = 0;
            this._data.forEach(function(key, value) {
                remain_total_count += value["total_count"];
            })
            selected_total_num = this.group_item_total_num - remain_total_count;

            var groupTotalNumLabel = $(this.transferId).find(this.groupTotalNumLabelClass);
            groupTotalNumLabel.empty();
            groupTotalNumLabel.append(get_total_num_text(this.default_total_num_text_template, remain_total_count));
            $(this.transferId).find(this.selectedTotalNumLabelClass).text(get_total_num_text(this.default_total_num_text_template, selected_total_num));

            if (remain_total_count == 0) {
                $(this.groupItemSelectAllId).prop("checked", true).prop("disabled", "disabled");
            }
    
            $(this.addSelectedButtonId).removeClass("btn-arrow-active");
            var transferDoubleSelectedListUl = $(this.transferId).find(this.transferDoubleSelectedListUlClass);
            transferDoubleSelectedListUl.append(html);
        }
    }

 
    Transfer.prototype.move_pre_selection_items = function() {
        var pre_selection_num = 0;
        var html = "";
        var self = this;
        var checkboxItems = $(self.transferId).find(self.checkboxItemClass);
        for (var i = 0; i < checkboxItems.length; i++) {
            if (checkboxItems.eq(i).parent("div").parent("li").css("display") != "none" && checkboxItems.eq(i).is(':checked')) {
                var checkboxItemId = checkboxItems.eq(i).attr("id");
                
                var index = checkboxItemId.split("_")[1];
                var labelText = $(self.transferId).find(self.checkboxItemLabelClass).eq(i).text();
                var value = checkboxItems.eq(i).val();
                $(self.transferId).find(self.transferDoubleListLiClass).eq(i).css("display", "").addClass("selected-hidden");
                html += self.generate_item(self.id, index, value, labelText);
                pre_selection_num++;

                var pre_selection_count = self._data.get("pre_selection_count");
                var total_count = self._data.get("total_count");
                self._data.put("pre_selection_count", --pre_selection_count);
                self._data.put("total_count", --total_count);
            }
        }
        if (pre_selection_num > 0) {
            var totalNumLabel = $(self.transferId).find(self.totalNumLabelClass);
            totalNumLabel.empty();

            self.selected_total_num += pre_selection_num
            totalNumLabel.append(get_total_num_text(self.default_total_num_text_template, self._data.get("total_count")));
            $(self.transferId).find(self.selectedTotalNumLabelClass).text(get_total_num_text(self.default_total_num_text_template, self.selected_total_num));
            if (self._data.get("total_count") == 0) {
                $(self.leftItemSelectAllId).prop("checked", true).prop("disabled", "disabled");
            }

            $(self.addSelectedButtonId).removeClass("btn-arrow-active");
            $(self.transferId).find(self.transferDoubleSelectedListUlClass).append(html);
        }
    }

 
    Transfer.prototype.move_selected_items_handler = function() {
        var self = this;
        $(self.deleteSelectedButtonId).on("click", function () {
            self.isGroup ? self.move_selected_group_items() : self.move_selected_items()
            $(self.deleteSelectedButtonId).removeClass("btn-arrow-active");
            
            if (Object.prototype.toString.call(self.settings.callable) === "[object Function]") {
                self.settings.callable.call(this, self.get_selected_items());
            }
        });
    }

    Transfer.prototype.move_selected_group_items = function() {
        var pre_selection_num = 0;
        var checkboxSelectedItems = $(this.transferId).find(this.checkboxSelectedItemClass);
        for (var i = 0; i < checkboxSelectedItems.length;) {
            var another_checkboxSelectedItems = $(this.transferId).find(this.checkboxSelectedItemClass);
            if (another_checkboxSelectedItems.eq(i).is(':checked')) {
                var checkboxSelectedItemId = another_checkboxSelectedItems.eq(i).attr("id");
                var groupIndex = checkboxSelectedItemId.split("_")[1];
                var index = checkboxSelectedItemId.split("_")[3];

                another_checkboxSelectedItems.parent("div").parent("li").eq(i).remove();
                $(this.transferId).find("#group_" + groupIndex + "_" + this.id).prop("checked", false).removeAttr("disabled");
                $(this.transferId).find("#group_" + groupIndex + "_checkbox_" + index + "_" + this.id)
                    .prop("checked", false).parent("div").parent("li").css("display", "").removeClass("selected-hidden");

                pre_selection_num++;

                var groupItem = this._data.get('group_' + groupIndex + '_' + this.id);
                var total_count = groupItem["total_count"];
                groupItem["total_count"] = ++total_count;

            } else {
                i++;
            }
        }
        if (pre_selection_num > 0) {
            $(this.transferId).find(this.groupTotalNumLabelClass).empty();

            var remain_total_count = 0;
            this._data.forEach(function(key, value) {
                remain_total_count += value["total_count"];
            })

            selected_total_num -= pre_selection_num;

            $(this.transferId).find(this.groupTotalNumLabelClass).append(get_total_num_text(this.default_total_num_text_template, remain_total_count));
            $(this.transferId).find(this.selectedTotalNumLabelClass).text(get_total_num_text(this.default_total_num_text_template, selected_total_num));
            if ($(this.groupItemSelectAllId).is(':checked')) {
                $(this.groupItemSelectAllId).prop("checked", false).removeAttr("disabled");
            }
        }
    }

 
    Transfer.prototype.move_selected_items = function() {
        var pre_selection_num = 0;
        var self = this;
        for (var i = 0; i < $(self.transferId).find(self.checkboxSelectedItemClass).length;) {
            var checkboxSelectedItems = $(self.transferId).find(self.checkboxSelectedItemClass);
            if (checkboxSelectedItems.eq(i).is(':checked')) {
                var index = checkboxSelectedItems.eq(i).attr("id").split("_")[1];
                checkboxSelectedItems.parent("div").parent("li").eq(i).remove();
                $(self.transferId).find(self.checkboxItemClass).eq(index).prop("checked", false);
                $(self.transferId).find(self.transferDoubleListLiClass).eq(index).css("display", "").removeClass("selected-hidden");

                pre_selection_num++;

                var total_count = self._data.get("total_count");
                self._data.put("total_count", ++total_count);

            } else {
                i++;
            }
        }

        if (pre_selection_num > 0) {
            $(self.transferId).find(self.totalNumLabelClass).empty();
            self.selected_total_num -= pre_selection_num;
            $(self.transferId).find(self.totalNumLabelClass).append(get_total_num_text(self.default_total_num_text_template, self._data.get("total_count")));
            $(self.transferId).find(self.selectedTotalNumLabelClass).text(get_total_num_text(self.default_total_num_text_template, self.selected_total_num));
            if ($(self.leftItemSelectAllId).is(':checked')) {
                $(self.leftItemSelectAllId).prop("checked", false).removeAttr("disabled");
            }
        }
    }

   
    Transfer.prototype.right_items_search_handler = function() {
        var self = this;
        $(self.selectedItemSearcherId).keyup(function () {
            var transferDoubleSelectedListLis = $(self.transferId).find(self.transferDoubleSelectedListLiClass);
            $(self.transferId).find(self.transferDoubleSelectedListUlClass).css('display', 'block');

            if ($(self.selectedItemSearcherId).val() == "") {
                transferDoubleSelectedListLis.css('display', 'block');
                return;
            }

            transferDoubleSelectedListLis.css('display', 'none');

            for (var i = 0; i < transferDoubleSelectedListLis.length; i++) {
                if (transferDoubleSelectedListLis.eq(i).text()
                        .substr(0, $(self.selectedItemSearcherId).val().length).toLowerCase() == $(self.selectedItemSearcherId).val().toLowerCase()) {
                            transferDoubleSelectedListLis.eq(i).css('display', 'block');
                }
            }
        });
    }

 
    Transfer.prototype.generate_item = function(id, index, value, labelText) {
        return '<li class="transfer-double-selected-list-li  transfer-double-selected-list-li-' + id + ' .clearfix">' +
        '<div class="checkbox-group">' +
        '<input type="checkbox" value="' + value + '" class="checkbox-normal checkbox-selected-item-' + id + '" id="selectedCheckbox_' + index + '_' + id + '">' +
        '<label class="checkbox-selected-name-' + id + '" for="selectedCheckbox_' + index + '_' + id + '">' + labelText + '</label>' +
        '</div>' +
        '</li>';
    }

 
    Transfer.prototype.generate_group_item = function(id, groupIndex, itemIndex, value, labelText) {
        return '<li class="transfer-double-selected-list-li transfer-double-selected-list-li-' + id + ' .clearfix">' +
        '<div class="checkbox-group">' +
        '<input type="checkbox" value="' + value + '" class="checkbox-normal checkbox-selected-item-' + id + '" id="group_' + groupIndex + '_selectedCheckbox_' + itemIndex + '_' + id + '">' +
        '<label class="checkbox-selected-name-' + id + '" for="group_' + groupIndex + '_selectedCheckbox_' + itemIndex + '_' + id + '">' + labelText + '</label>' +
        '</div>' +
        '</li>'
    }

   
    Transfer.prototype.get_selected_items = function() {
        var selected = [];
        var self = this;
        var transferDoubleSelectedListLiArray = $(self.transferId).find(self.transferDoubleSelectedListLiClass);
        for (var i = 0; i < transferDoubleSelectedListLiArray.length; i++) {
            var checkboxGroup = transferDoubleSelectedListLiArray.eq(i).find(".checkbox-group");

            var item = {};
            item[this.settings.itemName] = checkboxGroup.find("label").text();
            item[this.settings.valueName] = checkboxGroup.find("input").val();
            selected.push(item);
        }
        return selected;
    }

   
    function get_group_items_num(groupDataArray, groupArrayName) {
        var group_item_total_num = 0;
        for (var i = 0; i < groupDataArray.length; i++) {
            var groupItemData = groupDataArray[i][groupArrayName];
            if (groupItemData.length > 0) {
                group_item_total_num = group_item_total_num + groupItemData.length;
            }
        }
        return group_item_total_num;
    }

   
    function get_total_num_text(template, total_num) {
        var _template = template;
        return _template.replace(/{total_num}/g, total_num);
    }

    function InnerMap() {
        this.keys = new Array();
        this.values = new Object();

        this.put = function(key, value) {
            if (this.values[key] == null) {
                this.keys.push(key);
            }
            this.values[key] = value;
        }
        this.get = function(key) {
            return this.values[key];
        }
        this.remove = function(key) {
            for (var i = 0; i < this.keys.length; i++) {
                if (this.keys[i] === key) {
                    this.keys.splice(i, 1);
                }
            }
            delete this.values[key];
        }
        this.forEach = function(fn) {
            for (var i = 0; i < this.keys.length; i++) {
                var key = this.keys[i];
                var value = this.values[key];
                fn(key, value);
            }
        }
        this.isEmpty = function() {
            return this.keys.length == 0;
        }
        this.size = function() {
            return this.keys.length;
        }
    }

    function getId() {
        var counter = 0;
        return function(prefix) {
            var id = (+new Date()).toString(32), i = 0;
            for (; i < 5; i++) {
                id += Math.floor(Math.random() * 65535).toString(32);
            }
            return (prefix || '') + id + (counter++).toString(32);
        }
    }

}(jQuery));