$(document).ready(function () {

    document.getElementById('avatar').addEventListener('change', function(event) {
        const [file] = event.target.files;
        if (file) {
            document.getElementById('avatarPreview').src = URL.createObjectURL(file);
        }
    });
});