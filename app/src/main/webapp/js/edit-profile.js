$(document).ready(function () {

    const saveButton = document.getElementById('save-button');
    const editButton = document.getElementById('edit-button');
    
    const readProfile = document.getElementById('read-profile');
    const editProfile = document.getElementById('edit-profile');

    saveButton.classList.add("hidden");
    editProfile.classList.add("hidden");

    editButton.addEventListener('click', function () {
        editButton.classList.add("hidden");
        saveButton.classList.remove("hidden");
        editProfile.classList.remove("hidden");
        readProfile.classList.add("hidden");
    });
});