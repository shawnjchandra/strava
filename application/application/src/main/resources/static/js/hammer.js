$(document).ready(function(){
    function handleBanStatus(action) {
        return function() {
            const id = $(this).data("id");
            
            if(!id){
                alert("id is not available");
                return;
            }

            $.ajax({
                url: `/admin/${action}/${id}`,
                type: "POST",
                success: function(response){
                    location.reload();
                },
                error: function(xhr, status, error){
                    alert("An error occurred: " + error);
                }
            });
        };
    }

    $(".btn-active").on("click", handleBanStatus("ban"));
    $(".btn-inactive").on("click", handleBanStatus("unban"));
});