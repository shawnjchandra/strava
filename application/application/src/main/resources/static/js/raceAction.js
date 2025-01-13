

$(document).ready(function(){
    $(".join-btn").on("click",function(){

        const id = $(this).data("id");

        if(!id){
            alert("id is not available");
        }

        $.ajax({
            url: `/race/join/${id}`,
            type: "POST",
            success: function(response){
                // alert("Response from server: "+response);
                location.reload();
            },
            error: function(xhr, status, error){
                alert("An error occured: "+error);

            }
        });
    });

   

});