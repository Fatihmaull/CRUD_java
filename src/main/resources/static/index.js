$(document).ready(function () {
    loadAllUsers()
    setupHandlers()
})

function setupHandlers() {
    $("#save").click(handleSave)
    $("#reset").click(resetForm)
    $("#research").click(handleSearch)
    $("#delete").click(function () {
        const id = $("#userID").val()
        if (id) funcdelete(id)
    })
}

function handleSave() {
    const id = $("#userID").val()
    const name = $("#name").val()
    const age = parseInt($("#age").val())

    if (!validName(name)) {
        alert("Name cannot be empty.")
        return
    }

    if (!validAge(age)) {
        $("#spanage").text("Invalid age")
        return
    }

    $("#spanage").text("")

    const method = id ? 'PUT' : 'POST'
    const url = id ? "update" : "save"

    $.ajax({
        url: url,
        type: method,
        contentType: 'application/json',
        data: JSON.stringify({ id, name, age }),
        success: function () {
            alert("Saved successfully!")
            resetForm()
            loadAllUsers()
            $('#Modal').modal('hide')
        },
        error: function (xhr) {
            alert("Error saving user: " + xhr.responseText)
        }
    })
}

function validAge(age) {
    return age >= 0 && age <= 110
}

function validName(name) {
    return name && name.trim() !== ""
}

function handleSearch() {
    const name = $("#recipient-name").val()

    if (!validName(name)) {
        alert("Please enter a name to search.")
        return
    }

    $.ajax({
        url: "searchbyname",
        type: 'GET',
        data: { name: name },
        success: renderTable,
        error: function (xhr) {
            alert("Error when searching user: " + xhr.responseText)
        }
    })
}

function loadAllUsers() {
    $.ajax({
        url: "listall",
        type: 'GET',
        success: renderTable,
        error: function () {
            alert("Failed to load user list.")
        }
    })
}

function renderTable(users) {
    const tbody = $("#tableresult > tbody")
    tbody.empty()

    if (users.length === 0) {
        tbody.append("<tr><td colspan='5'>No users found</td></tr>")
        return
    }

    users.forEach(user => {
        const row = `
            <tr id="${user.id}">
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.age}</td>
                <td><button type="button" class="btn btn-sm btn-warning" onclick="funcedit(${user.id})">Edit</button></td>
                <td><button type="button" class="btn btn-sm btn-danger" onclick="funcdelete(${user.id})">Delete</button></td>
            </tr>
        `
        tbody.append(row)
    })
}

function funcedit(id) {
    $.ajax({
        url: "searchuser",
        type: 'GET',
        data: { iduser: id },
        success: function (user) {
            $("#userID").val(user.id)
            $("#name").val(user.name)
            $("#age").val(user.age)
            $('#Modal').modal('show')
        },
        error: function (xhr) {
            alert("Error finding user: " + xhr.responseText)
        }
    })
}

function funcdelete(id) {
    if (!confirm("Do you really want to delete?")) return

    $.ajax({
        url: "delete",
        type: 'DELETE',
        data: { iduser: id },
        success: function () {
            alert("User deleted successfully!")
            resetForm()
            loadAllUsers()
        },
        error: function (xhr) {
            alert("Error deleting user: " + xhr.responseText)
        }
    })
}

function resetForm() {
    $("#userID").val("")
    $("#name").val("")
    $("#age").val("")
    $("#spanage").text("")
}
