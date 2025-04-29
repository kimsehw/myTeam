let isDeleteMode = false;
let pendingDelete = { alarmId: null, liEl: null };

document.addEventListener('DOMContentLoaded', () => {
    const deleteToggleButton = document.getElementById("alarms_popup_delete");
    const confirmDeleteBtn = document.getElementById('confirm_delete_btn');
    const cancelDeleteBtn = document.getElementById('cancel_delete_btn');

    if (deleteToggleButton) {
        deleteToggleButton.addEventListener('click', toggleDeleteMode);
    }
    if (confirmDeleteBtn && cancelDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', deleteAlarmConfirmed);
        cancelDeleteBtn.addEventListener('click', closeDeleteModal);
    }
});

function toggleDeleteMode() {
    isDeleteMode = !isDeleteMode;
    toggleDeleteButtons();

    const deleteBtn = document.querySelector('#alarms_popup_delete button');
    if (deleteBtn) {
        deleteBtn.innerText = isDeleteMode ? '삭제 종료' : '삭제 하기';
    }
}

function toggleDeleteButtons() {
    const listItems = document.querySelectorAll('#alarmList li');

    listItems.forEach(li => {
        let deleteBtn = li.querySelector('.alarm-delete-btn');

        if (isDeleteMode) {
            if (!deleteBtn) {
                deleteBtn = document.createElement('button');
                deleteBtn.className = 'alarm-delete-btn text-xs text-red-500 hover:text-red-700 ml-2';
                deleteBtn.innerText = '삭제';
                deleteBtn.onclick = function (e) {
                    e.stopPropagation(); // li 클릭 방지
                    const alarmId = li.dataset.alarmId;
                    confirmDelete(alarmId, li);
                };
                li.appendChild(deleteBtn);
            }
        } else {
            if (deleteBtn) {
                deleteBtn.remove();
            }
        }
    });
}

function confirmDelete(alarmId, liEl) {
    pendingDelete = { alarmId, liEl };
    document.getElementById('delete_confirm_modal').classList.remove('hidden');
}

function deleteAlarmConfirmed() {
    const { alarmId, liEl } = pendingDelete;

    fetch(`/alarms/${alarmId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken // 필요한 경우
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.json()
        }
        liEl.remove();
        alert('삭제 완료되었습니다.');
    })
    .then(errors => {
        if (errors) {
            alert(errors.errorMessage)
        }
    })
    .catch(error => {
        console.error("삭제 실패:", error);
        alert('삭제 요청 중 오류가 발생했습니다.');
    })
    .finally(() => {
        closeDeleteModal();
    });
}

function closeDeleteModal() {
    document.getElementById('delete_confirm_modal').classList.add('hidden');
    pendingDelete = { alarmId: null, liEl: null };
}

