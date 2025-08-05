
	//파일 한 개만 올리기 -> 새 파일 선택 시, 기존 파일 덮어쓰기
	const fileInput = document.getElementById('imageUpload');
	if (fileInput) {
        fileInput.addEventListener('change', () => {
		const newFile = fileInput.files[0]; //크기 1개짜리 배열생성
		if(!newFile) return;
		
		let selectedFiles = [newFile]; //배열을 초기화시키고 새 파일 추가하기
		
	});
}
	


    // 파일명 표시 + 삭제 버튼 추가
	const fileListDiv = document.getElementById('fileId');
	function addFileTag(fileName) {
	    const fileItem = document.createElement('div');
	    fileItem.className = 'file-tag';

	    const nameSpan = document.createElement('span');
	    nameSpan.textContent = fileName;

	    const deleteSpan = document.createElement('span');
	    deleteSpan.className = 'image-delete-icon';
	    deleteSpan.style.display = 'inline-block';

	    const deleteIcon = document.createElement('i');
	    deleteIcon.className = 'ti ti-trash delete-btn';
	    deleteIcon.addEventListener('click', () => {
	        selectedFiles = selectedFiles.filter(f => f.name !== fileName);
	        fileListDiv.removeChild(fileItem);
	    });

	    deleteSpan.appendChild(deleteIcon);

	    fileItem.appendChild(nameSpan);
	    fileItem.appendChild(deleteSpan);
	    fileListDiv.appendChild(fileItem);
	}


    //후기작성 : 10자이상 작성 필수 및 별점 선택 필수
    function submitReview() {
        const rText = document.getElementById('reviewWriteText').value.trim();
        const rating = document.getElementById('ratingInput').value;

		
        if (rText.length < 10) {
            alert('후기를 10자 이상 작성해주세요.');
            return false;
        }

        if (!rating || rating === "0") {
            alert('별점을 선택해주세요.');
            return false;
        }
		
		
        // 조건 충족 시 1차 모달창 띄우기
        document.getElementById('confirmModal').style.display = "flex";
    }
    

    //등록버튼, 취소버튼 클릭 시 리뷰 등록 여부 질의 모달창
    function closeModal(id) {
        document.getElementById(id).style.display = "none";
    }

    function confirmAndSubmit() {
    document.getElementById('confirmModal').style.display = "none";
    alert('리뷰가 등록되었습니다.');
    document.getElementById('reviewForm').submit();
}


    // 별점채우기 : 별 클릭 시 아이콘 자체변경
    const stars = document.querySelectorAll('.star');
	let selectedRating = 0;
	let clickedRating = 0; // 클릭된 별의 값 저장

stars.forEach(star => {
    star.addEventListener('click', () => {
        selectedRating = parseInt(star.dataset.value);

        if (clickedRating === selectedRating) {
            // 이미 선택된 별을 다시 클릭하면 선택 해제
            selectedRating = 0;
            clickedRating = 0;
        } else {
            clickedRating = selectedRating; // 새로운 별 클릭 시 업데이트
        }

        updateStars(selectedRating);

        // 클릭할 때마다 hidden input에도 값 업데이트
        const ratingInput = document.getElementById('ratingInput');
        if (ratingInput) {
            ratingInput.value = clickedRating;
        }
    });

    star.addEventListener('mouseover', () => {
        const hoverVal = parseInt(star.dataset.value);
        highlightHoveredStars(hoverVal);
    });

    star.addEventListener('mouseout', () => {
        removeHoveredStars();
    });
});

function updateStars(rating) {
    stars.forEach(star => {
        const val = parseInt(star.dataset.value);
        const icon = star.querySelector('i');
        if (val <= rating) {
            icon.classList.remove('ti-carambola');
            icon.classList.add('ti-carambola-filled');
        } else {
            icon.classList.remove('ti-carambola-filled');
            icon.classList.add('ti-carambola');
        }
    });
}

function highlightHoveredStars(hoverValue) {
    stars.forEach(star => {
        const val = parseInt(star.dataset.value);
        if (val <= hoverValue) {
            star.classList.add('hovered');
        } else {
            star.classList.remove('hovered');
        }
    });
}

function removeHoveredStars() {
    stars.forEach(star => {
        star.classList.remove('hovered');
    });
}

// 리뷰 폼 제출 시 별점 1점 이상 필수 체크
const reviewForm = document.getElementById('reviewForm');
if (reviewForm) {
    reviewForm.addEventListener('submit', (e) => {
        if (clickedRating < 1) {
            e.preventDefault(); // 폼 제출 방지
            alert('별점을 1점 이상 선택해주세요.');
        } else {
            const ratingInput = document.getElementById('ratingInput');
            if (ratingInput) {
                ratingInput.value = clickedRating; // hidden input에 값 저장
            }
        }
    });
}




const reviewModalBody = `
    <p>후기를 등록하시겠습니까?</p>
`;

const reviewModalFooter = `
    <button onclick="showSuccessModal()">네</button>
    <button id="closeModal" onclick="closeModal('confirmModal')">아니오</button>
`;

function showReviewConfirmModal() {
    showModal({body: reviewModalBody, footer: reviewModalFooter});
}