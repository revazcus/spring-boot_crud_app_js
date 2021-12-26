document.addEventListener("DOMContentLoaded", function(event) {
    let api = {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Referer': null
        },

        getUsers: async () => await fetch("/admin/api/users",{method:"GET"}).then(res => res.json()),
        getUser: async id => await fetch(`/admin/api/users/${id}`, {method:"GET"}).then(res => res.json()),
        addUser: async obj => await fetch("/admin/api/users", {method:"POST", headers: api.headers, body: JSON.stringify(obj)}),
        editUser: async obj => await fetch(`/admin/api/users`, {method:"PUT", headers: api.headers, body: JSON.stringify(obj)}),
        deleteUser: async id => await fetch(`/admin/api/users/${id}`, {method:"DELETE"}),
        showUsers: items => {
            let usersTable = document.querySelector("#usersTable")
            items.forEach(item => {
                usersTable.innerHTML += `<tr id="${item.id}">
                <td class="tdId">${item.id}</td>
                <td class="tdFirstName">${item.firstName}</td>
                <td class="tdLastName">${item.lastName}</td>
                <td class="tdAge">${item.age}</td>
                <td class="tdEmail">${item.email}</td>
                <td class="tdRole">${item.role}</td>
                <td>
                <button class="btn btn-info editUser">Edit</button>
                </td>
                <td>
                <button class="btn btn-danger deleteUser">Delete</button>
                </td></tr>`
            })
        }
    };

    api.getUsers().then(res => api.showUsers(res))


   $(document).on('click', 'button.editUser',function (event){ // modal edit
        event.preventDefault();
        let uId = $(this).parents("tr").attr("id");
        $('.modal-title').text("Edit User")
        $('button.updateUser').css('display','inline-block')
        $('button.confirmDelete').css('display','none')
        api.getUser(uId).then(user => {
            $('#modalEditForm #editId').val(uId);
            $('#modalEditForm #firstName').val(user.firstName);
            $('#modalEditForm #lastName').val(user.lastName);
            $('#modalEditForm #age').val(user.age);
            $('#modalEditForm #email').val(user.email);
            $('#modalEditForm #password').val(user.password);
            $('.userEditForm').modal();
        })
    })

    $('.btn.updateUser').click(async function () {
        let form = document.querySelector("#modalEditForm");
        let formData = new FormData(form);
        let obj = Object.fromEntries(formData);
        obj.roleSetTemp = $("#editRole").val();
        console.log(obj)
        if (obj.roleSetTemp.length === 0) {
            alert("Выберите роль!")
        } else {
            api.editUser(obj).then(res => res.json()).then(res => {
                let tr = $('tr#' + obj.id)
                tr.find('.tdFirstName').text(res.firstName)
                tr.find('.tdLastName').text(res.lastName)
                tr.find('.tdAge').text(res.age)
                tr.find('.tdEmail').text(res.email)
                tr.find('.tdRole').text(res.role)
            })
            $('.btn.editModalClose').click()
        }
    })

    $(document).on('click', 'button.deleteUser',function (event){ //modal delete
        event.preventDefault();
        let uid = $(this).parents("tr").attr("id");
        $('.modal-title').text("Delete User")
        $('button.updateUser').css('display','none')
        $('button.confirmDelete').css('display','inline-block')
        api.getUser(uid).then(user => {
            $('#modalDeleteForm #editId1').val(uid);
            $('#modalDeleteForm #firstName1').val(user.name).attr('readonly', true);
            $('#modalDeleteForm #lastName1').val(user.lastName).attr('readonly', true);
            $('#modalDeleteForm #age1').val(user.age).attr('readonly', true);
            $('#modalDeleteForm #email1').val(user.email).attr('readonly', true);
            $('#modalDeleteForm #password1').val(user.password).attr('readonly', true);
            $('#modalDeleteForm #editRole1').attr('disabled', true);
            $('.userDeleteForm').modal();
        })
    })



    $('.btn.confirmDelete').click(async function () { //Удаление Юзера
        let form = document.querySelector("#modalDeleteForm");
        let formData = new FormData(form);
        let obj = Object.fromEntries(formData);
        api.deleteUser(obj.id).then(res => {
            let tr = $('tr#' + obj.id)
            tr.remove();
        })
        $('.btn.editModalClose').click()
    })


    document.querySelector("button.showUserForm").addEventListener("click", function (){ //Смена вкладки на таблицу Юзеров
        document.querySelector(".topButtons.onFocus").classList.remove("onFocus");
        this.classList.add("onFocus");
        document.querySelector(".cardUserList").style.display = "none";
        document.querySelector(".cardAddUserForm").style.display = "flex";
    })


    document.querySelector("button.showUsers").addEventListener("click", function (){ //Смена вкладки на форму нового Юзера
        document.querySelector(".topButtons.onFocus").classList.remove("onFocus");
        this.classList.add("onFocus");
        document.querySelector(".cardUserList").style.display = "flex";
        document.querySelector(".cardAddUserForm").style.display = "none";
    })

    $('.btn.addUser').click(async function () { //Добавление Юзера
        let form = document.querySelector("#addUserForm");
        let formData = new FormData(form);
        let obj = Object.fromEntries(formData);
        obj.roleSetTemp = $(".roleSelect").val();
        if (obj.roleSetTemp.length === 0){
            alert("Выберите роль!")
        } else {
            api.addUser(obj).then(res => res.json()).then(res => {
                api.showUsers([res])
                alert("Пользователь успешно добавлен!")
            })
        }
    })


})