$(document).ready(function(){
    function handleBanStatus(action) {
        return function() {

            console.log(action);
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

    $(document).on("click",".btn-active" ,handleBanStatus("ban"));
    $(document).on("click", ".btn-inactive", handleBanStatus("unban"));
});