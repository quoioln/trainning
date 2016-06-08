/*!
 * Simple tabs widget for NTT GCV
 * Author: glen.pike@headforwards.com
 *
 * Created from:
 * http://coding.smashingmagazine.com/2011/10/11/essential-jquery-plugin-patterns/
 * jQuery UI Widget-factory plugin boilerplate (for 1.8/9+)
 * Author: @addyosmani
 * Further changes: @peolanha
 * Licensed under the MIT license
 *
 * usage:
 *
 * Create a container with a number of divs for tab content and a list for navigation
 * the navigation is tied to the tabs by matching the href of each link to the id of the
 * content container, e.g. #tabs-1 refers to id="tabs-1"
 *
 * Then call var tabs = $(".tabs").tabs(); to start your widget up.
 *
 * You can listen for events by binding to the "tabClick" event from the widget - the event name will be
 * prepended by the widget name and lowercased so will be "tabstabclick".
 *
 * e.g.
 *
 * tabs.on("tabstabclick", function(event, data) {
 *  alert("hello from tab " + data.tab);
 * });
 *
 * You can also call methods on the tabs:
 * To show a tab by an id -
 * $('.tabs').tabs("showById", "#tab-id");
 *
 * To show a tab based on the li element with a link containing the tab id (uses the 1st li in the Example below):
 * $('.tabs').tabs("showById", $(".tabs-nav li:first));
 *
 * To get the href of an li element:
 *
 * $('.tabs').tabs("tab", $(".tabs-nav li:last));
 *
 * To destroy the tabs widget and clean up (If you remove the tabs container HTML, JQuery should call this automatically):
 *
 * $('.tabs').tabs("destroy");
 *
 * Example HTML:
 *
 * <div class="tabs">
            <ul class="nav tabs-nav">
              <li class="selected"><a href="#tabs-1">Tab1</a></li>
              <li><a href="#tabs-2">Tab2</a></li>
              <li><a href="#tabs-3">Tab3</a></li>
              <li><a href="#tabs-4">Tab4</a></li>
            </ul>
            <div class="tab" id="tabs-1">Tab 1</div>
            <div class="tab" id="tabs-2">Tab 2</div>
            <div class="tab" id="tabs-3">Tab 3</div>
            <div class="tab" id="tabs-4">Tab 4</div>
        </div>
 */
;
(function ($, window, document, undefined) {

    $.widget("ntt.tabs", {

        //Options to be used as defaults
        options: {},

        //Setup widget (eg. element creation, apply theming
        // , bind events etc.)
        _create: function () {

            var el = $(this.element);
            var that = this;
            el.addClass("ntt-tabs").on("click", "li", $.proxy(this.tabClick, this)).find("ul").children("li").each(function () {
                //console.log("each tab, this ", this);
                $(this).removeClass('selected');
                that.tab($(this)).hide();
            });

            //console.log("_create ", el);
            this.showTab(el.find("ul").children("li:first"));
        },

        showById: function (href) {
            var $a = $(this.element).find("ul li a[href='" + href + "']");
            //console.log("showById href " + href + " result length ", $a.length, " a ", $a);
            if ($a && 0 != $a.length) {
                this.showTab($a.parent());
            }
        },

        tab: function (li) {
            return $(li.find("a").attr("href"));
        },

        // deactivate the old active tab, mark the li as active
        showTab: function (li) {
            //console.log("showTab li ", li, " this ", this);
            var href = this._href(li);
            //console.log("showTab, clickedTab: ", href, " from: ", li);
            //Dispatch an event so that any listeners can do extra processing.
            this._trigger('tabClick', null, {
                tab: href
            });
            this.tab(li.siblings('.selected').removeClass('selected')).hide()
            this.tab(li.addClass('selected')).show();
        },

        // activate the tab on click
        tabClick: function (ev) {
            ev.preventDefault();
            this.showTab($(ev.currentTarget));
            /*var href = this._href($(ev.currentTarget));*/
            // console.log("tabClick, clickedTab: ", href, " from: ", $(ev.currentTarget));
            /*//Dispatch an event so that any listeners can do extra processing.
            this._trigger('tabClick', ev, {
                tab: href
            });*/
        },

        // Destroy an instantiated plugin and clean up
        // modifications the widget has made to the DOM
        destroy: function () {
            var el = $(this.element);

            //Unbind from events, remove tabs and selection.
            el.removeClass("ntt-tabs").off("click", "li").find("ul").children("li").each(function () {
                $(this).removeClass('selected');
            });
            // this.element.removeStuff();
            // For UI 1.8, destroy must be invoked from the
            // base widget
            $.Widget.prototype.destroy.call(this);
            // For UI 1.9, define _destroy instead and don't
            // worry about
            // calling the base widget
        },

        _href: function (li) {
            var href = $(li).find("a").attr("href");
            //console.log("tab, href: ", href, " li ", li);
            return href;
        },

        // Respond to any changes the user makes to the
        // option method
        _setOption: function (key, value) {
            switch (key) {
            default:
                //this.options[ key ] = value;
                break;
            }

            // For UI 1.8, _setOption must be manually invoked
            // from the base widget
            $.Widget.prototype._setOption.apply(this, arguments);
            // For UI 1.9 the _super method can be used instead
            // this._super( "_setOption", key, value );
        }
    });

})(jQuery, window, document);
