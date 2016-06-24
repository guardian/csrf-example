jQuery(function($){
  console.log("test");
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
});
