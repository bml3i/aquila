(function($) {
    function extractor(query) {
        var result = /([^,]+)$/.exec(query);
        if(result && result[1])
            return result[1].trim();
        return '';
    }
	
	$.fn.bindCustomTypeAhead = function(source) {
		$(this).typeahead({
	        source: source == null ? $(this).data("source") : source,
	        updater: function(item) {
	            return this.$element.val().replace(/[^,]*$/,'')+item+',';
	        },
	        matcher: function (item) {
	          var tquery = extractor(this.query);
	          if(!tquery) return false;
	          return ~item.toLowerCase().indexOf(tquery.toLowerCase())
	        },
	        highlighter: function (item) {
	          var query = extractor(this.query).replace(/[\-\[\]{}()*+?.,\\\^$|#\s]/g, '\\$&')
	          return item.replace(new RegExp('(' + query + ')', 'ig'), function ($1, match) {
	            return '<strong>' + match + '</strong>'
	          })
	        }
	    });
	};
})(jQuery);