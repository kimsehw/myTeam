document.addEventListener("DOMContentLoaded", function () {
    const editButton = document.getElementById("editButton");
    const saveButton = document.getElementById("saveButton");
    const cancelButton = document.getElementById("cancelButton");
    const deleteButtons = document.querySelectorAll("#deleteModalButton");

    editButton.addEventListener("click", function () {
        editButton.classList.add("hidden");

        saveButton.classList.remove("hidden");
        cancelButton.classList.remove("hidden");

        deleteButtons.forEach(button => {
            button.classList.remove("hidden");
        });
    });

    cancelButton.addEventListener("click", function () {
        editButton.classList.remove("hidden");

        saveButton.classList.add("hidden");
        cancelButton.classList.add("hidden");
        deleteButtons.forEach(button => {
            button.classList.add("hidden");
        });
    });

});