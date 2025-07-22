function showFileInput() {
    const fileInputWrapper = document.getElementById('fileInputWrapper');
    fileInputWrapper.style.display = 'block';
    const originFileWrapper = document.getElementById('originFileWrapper');
    originFileWrapper.style.display = 'none';
}

function confirmCategoryDelete(categoryId) {
    showModal({
        body: `<div class="modal-confirmCategoryDelete-body">정말 삭제하시겠습니까?</div>`,
        footer: `
            <div class="modal-confirmCategoryDelete-footer">
                <button class="confirm" onclick="deleteCategory(${categoryId})">예</button>
                <button class="cancel" onclick="hideModal()">아니요</button>
            </div>
        `
    });
}

function deleteCategory(categoryId) {
    fetch(`/admin/category/delete/${categoryId}`, {
        method: "POST"
    })
        .then(res => {
            if (res.ok) {
                alert("삭제 되었습니다.");
                location.reload();
            } else {
                alert("삭제에 실패했습니다.");
            }
            hideModal();
        })
}