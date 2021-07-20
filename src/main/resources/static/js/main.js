/* <!--mostra a senha quando passa o mouse no icone do olho--> */
$(".show-password").hover(
    function () {
        $("#password").attr("type", "text").focus();
    },
    function () {
        $("#password").attr("type", "password");
    }
);

//remain scroll position after redirect
$(window).scroll(function () {
    sessionStorage.scrollTop = $(this).scrollTop();
});

$(document).ready(function () {
    if (sessionStorage.scrollTop !== "undefined") {
        $(window).scrollTop(sessionStorage.scrollTop);
    }
});

/*DataTable plug-in
$(document).ready(function () {
    $('#sortableTable').DataTable(
        {
            columnDefs: [
                {
                    ordering: false,
                    targets: [6, 7]
                }
            ],
            pageLength: 25
        });
    $('.dataTables_length').addClass('bs-select');
}); */

/*Contas para testes*/
$(document).ready(function () {
    $("#demo-admin-btn").click(function () {
        $("#email").val("manager@mail.com");
        $("#password").val("112233");
    });
});
$(document).ready(function () {
    $("#demo-user-btn").click(function () {
        $("#email").val("user@mail.com");
        $("#password").val("112233");
    });
});
$(document).ready(function () {
    $("#demo-gamemaster-btn").click(function () {
        $("#email").val("gamemaster@mail.com");
        $("#password").val("112233");
    });
});