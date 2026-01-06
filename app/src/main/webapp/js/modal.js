function openDeleteModal() {
    const modal = document.getElementById("deleteModal");
    const modalContent = document.getElementById("modalContent");

    modal.classList.remove("hidden");
    document.body.classList.add("overflow-hidden"); // désactive scroll

    setTimeout(() => {
        modalContent.classList.remove("scale-95", "opacity-0");
        modalContent.classList.add("scale-100", "opacity-100");
    }, 10);
}

function closeDeleteModal() {
    const modal = document.getElementById("deleteModal");
    const modalContent = document.getElementById("modalContent");

    modalContent.classList.remove("scale-100", "opacity-100");
    modalContent.classList.add("scale-95", "opacity-0");

    setTimeout(() => {
        modal.classList.add("hidden");
        document.body.classList.remove("overflow-hidden"); // réactive scroll
    }, 300);
}