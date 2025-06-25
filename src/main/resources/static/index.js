$(document).ready(function () {
    saveButton()
    reset()
    research()
    resetcampus()
    clickdelete()
})

function saveButton() {
    $("#save").click(function () {

        var id = $("#userID").val()
        var name = $("#name").val()
        var age = $("#age").val()

        if (validAge(parseInt(age) && validName(name))) {
            $("#spanage").text("")
            ajaxSave(id, name, parseInt(age))
        } else {
            $("#spanage").text("Invalid age")
        }
    })
}

function validAge(Age) {

    if (Age >= 0 && Age <= 110) {
        return true
    }

    return false
}

function validName(Name) {

    if (Name != null) {
        return true
    }
    return false
}

function ajaxSave(id, name, age) {

    $.ajax({
        url: "save",
        type: 'POST',
        async: true,
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            name: name,
            age: age
        }),
        success: function (response) {
            $("#userID").val(response.id)
            alert("Sucess")
        }

    }).fail(function (xhr, status, errorThrow) {
        alert("error saving user " + xhr.responseText);
    })
}

function reset() {

    $("#reset").click(function () {

        resetcampus()

    })

}

function research() {

    $("#research").click(function () {
        var name = $("#recipient-name").val()


        if (name != null && name.trim() != "") {

            $.ajax({
                url: "searchbyname",
                type: 'GET',
                async: true,
                contentType: 'application/json',
                data: "name=" + name,
                success: table

            }).fail(function (xhr, status, errorThrow) {
                alert("error when searching user " + xhr.responseText);
            })
        }

    })

}

function table(response) {

    $("#tableresult > tbody > tr").remove();

    for (var i = 0; i < response.length; i++) {

        $("#tableresult > tbody").append("<tr id= '" + response[i].id + "'><td>" + response[i].id + "</td><td>" + response[i].name +
            "</td><td>" + response[i].age +
            "</td><td> <button type='button' onclick=funcedit(" + response[i].id + ") class='buttons' id = 'edit'>edit</button>" +
            "</td><td> <button type='button' onclick=funcdelete(" + response[i].id + ") id='button' class='buttons' id ='delete'>Delete</button> </td></tr>");

    }
}

function funcedit(id) {

    $.ajax({
        url: "searchuser",
        type: 'GET',
        async: true,
        contentType: 'application/json',
        data: "iduser=" + id,
        success: function (response) {
            $("#userID").val(response.id)
            $("#name").val(response.name)
            $("#age").val(response.age)
            $('#Modal').modal('toggle');
        }

    }).fail(function (xhr, status, errorThrow) {
        alert("error when searching user " + xhr.responseText);
    })

}

function funcdelete(id) {


    if (confirm("Do you really want to delete? ")) {

        $.ajax({
            url: "delete",
            type: 'DELETE',
            data: "iduser=" + id,
            success: function (response) {

                alert(response);
                resetcampus();
                $("#" + id).remove();

            }

        }).fail(function (xhr, status, errorThrow) {
            alert("error when delete user " + xhr.responseText);
        })
    }

}

function resetcampus() {
    $("#userID").val("")
    $("#name").val("")
    $("#age").val("")
}

function clickdelete(){

    var id = $("#userID").val()
    
    $("#delete").click(function(){
        var id = $("#userID").val()
        if(id != null || id != ""){
            funcdelete(id)
        }
    })
}