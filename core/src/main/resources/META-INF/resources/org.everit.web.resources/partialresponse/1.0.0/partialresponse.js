/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

if (typeof everit === 'undefined' || everit === null) {
	everit = {};
}

if (typeof everit.partialresponse === 'undefined'
		|| everit.partialresponse === null) {
	everit.partialresponse = {
		"version" : "1.1.0"
	};

	everit.partialresponse.process = function(responseContent) {
		var responseObj = $($.parseHTML(responseContent));

		if (responseObj.length < 1) {
			return;
		}

		if (responseObj.length > 1 || responseObj[0].nodeName.toUpperCase() != 'PARTIAL-RESPONSE') {
			throw ("A partial response may have one partial-response root element: " + responseContent);
		}
		
		responseObj.children().each(function() {
			var elementName = this.nodeName.toUpperCase();
			if (elementName == 'PARTIAL-REPLACE') {
				var replaceObj = $(this);
				var selector = replaceObj.attr('selector');
				if (typeof selector !== typeof undefined && selector !== false) {
					$(selector).replaceWith($.parseHTML(this.innerText));
				} else {
					$($.parseHTML(this.innerText)).each(function() {
						var newContentObj = $(this);
						var elementId = newContentObj.attr('id');
						var newContentOuterHTML = this.outerHTML;
						$('#' + elementId).replaceWith(newContentOuterHTML);
					});
				}
			} else if (elementName == 'PARTIAL-APPEND') {
				var appendObj = $(this);
				var selector = appendObj.attr('selector');
				$(selector).append($.parseHTML(this.innerText));
			} else if (elementName == 'PARTIAL-PREPEND') {
				var prependObj = $(this);
				var selector = prependObj.attr('selector');
				$(selector).prepend($.parseHTML(this.innerText));
			} else {
				console.log('Unknown partial-response element: ' + elementName);
			}
		});
	}
}
