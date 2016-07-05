jQuery(function($){
  // pin navigation
  (function(){
    var nav = $('nav');
    nav.pushpin({ top: nav.offset().top });
  })();


  // hide / toggle
  $(".toggle__container").each(function(i, el){
    var container = $(el),
        toggle = container.find(".toggle__toggle"),
        body = container.find(".toggle__body");
    body.hide();
    toggle.click(function(e){
      e.preventDefault();
      toggle.hide();
      body.show()
    });
  });

  $(".hack-form").each(function(i, el){
    var form = $(el),
        iFrame = $("#hack-frame"),
        contentTextarea = form.find("#hack-frame-content"),
        codeTextarea = form.find("#hack-frame-js"),
        initialColor = iFrame.css("border-color"),
        storage = function(){
          var contentKey = "csrf-demo-hack-page-source"
          var codeKey = "csrf-demo-hack-page-js"
          return {
            save: function(content, code) {
              localStorage.setItem(contentKey, content);
              localStorage.setItem(codeKey, code);
            },
            load: function() {
              var content = localStorage.getItem(contentKey),
                  code = localStorage.getItem(codeKey);
              return {
                content: content,
                code: code,
                exists: content && code
              };
            }
          }
        }(),
        saved = storage.load(),
        timer;

    form.on("submit", function(e){
      e.preventDefault();
      var content = contentTextarea.val(),
          code = codeTextarea.val();
      storage.save(content, code);
      clearTimeout(timer);
      timer = setTimeout(function(){
        iFrame.css("border-color", initialColor);
      }, 1000);
      iFrame.css("border-color", "#26a69a");
      iFrame.contents().find("html").html(content);
      iFrame.get(0).contentWindow.eval(code);
    });

    // extra keyboard goodies
    contentTextarea.add(codeTextarea).keydown(function(e) {
      // handle tab key for indentation
      if(e.keyCode === 9) {
        e.preventDefault();
        // get caret position/selection
        var start = this.selectionStart,
            end = this.selectionEnd,
            textarea = $(this),
            value = textarea.val();
        // set textarea value to: text before caret + <2 spaces> + text after caret
        textarea.val(value.substring(0, start)
            + "  "
            + value.substring(end));
        // restore caret position (+2 for new spaces)
        this.selectionStart = this.selectionEnd = start + 2;
      }
      // submit on ctrl + enter
      if(e.keyCode === 13) {
        form.submit();
      }
    });

    // set stored / initial source to iFrame
    if (saved.exists) {
      contentTextarea.val(saved.content);
      codeTextarea.val(saved.code);
    }
    form.submit();
  });
});
