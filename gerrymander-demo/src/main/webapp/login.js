$(function () {
    $(".sign-up-btn").click(function() {
        $(".lightbox").delay(500).fadeIn(500);
    });
    $(".btn-sm").click(function() {
        $(this).stop().fadeOut();
    });

});