window.onload = () => {
	//파일 한 개만 올리기 -> 새 파일 선택 시, 기존 파일 덮어쓰기
	const fileInput = document.getElementById('imageUpload');
	fileInput.addEventListener('change', () => {
		const newFile = fileInput.files[0]; //크기 1개짜리 배열생성
		if(!newFile) return;
		
		selectedFiles = [newFile]; //배열을 초기화시키고 새 파일 추가하기
		
		
	});
	
	
	
	
	
    const imageUpload = document.getElementsByClassName('review-image-upload')[0];
    const fileInput = document.getElementById('imageUpload');
    const fileListDiv = document.getElementById('imageFileList');

    let selectedFiles = [];

    imageUpload.addEventListener('click', () => {
        fileInput.click();
    });

    fileInput.addEventListener('change', () => {
        const newFiles = Array.from(fileInput.files);

        newFiles.forEach(file => {
            if (!selectedFiles.some(f => f.name === file.name)) {
                selectedFiles.push(file);
                addFileTag(file.name);
            }
        });

        fileInput.value = '';
        console.log('파일 목록:', selectedFiles.map(f => f.name));
    });

    // 파일명 표시 + 삭제 버튼 추가
    function addFileTag(fileName) {
        const fileItem = document.createElement('div');
        fileItem.className = 'file-tag';

        const nameSpan = document.createElement('span');
        nameSpan.textContent = fileName;

        const deleteTemplate = document.getElementById('imageDeleteIcon');
        const deleteBtn = deleteTemplate.cloneNode(true);
        deleteBtn.className = 'delete-btn';
        deleteBtn.style.display = 'inline-block';
        deleteBtn.removeAttribute('id');

        deleteBtn.addEventListener('click', () => {
            selectedFiles = selectedFiles.filter(f => f.name !== fileName);
            fileListDiv.removeChild(fileItem);
        });

        fileItem.appendChild(nameSpan);
        fileItem.appendChild(deleteBtn);
        fileListDiv.appendChild(fileItem);
    }


    //후기작성 : 10자이상 작성 필수 및 별점 선택 필수
    function submitReview() {
        const reviewText = document.getElementById('reviewText').value.trim();
        if (reviewText.length < 10) {
            alert('후기를 10자 이상 작성해주세요.');
            return false;
        }

        if (!rating) {
            alert('별점을 선택해주세요.');
            return false;
        }

        if (reviewText.length >= 10 && rating) { //1차 모달 열기
            document.getElementById('confirmModal').style.display = "flex";
        }
    }

    //등록버튼, 취소버튼 클릭 시 리뷰 등록 여부 질의 모달창
    function closeModal(id) {
        document.getElementById(id).style.display = "none";
    }

    //2차 모달 띄우기
    function showSuccessModal() {
        closeModal('confirmModal'); //1차 모달 닫기
        document.getElementById('successModal').style.display = "flex";
    }

    function enrollReview() { //리뷰 다쓰면 review/read로 이동
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
                selectedRating = 0; // 이미 선택된 별을 다시 클릭하면 선택 해제
                clickedRating = 0;
            } else {
                clickedRating = selectedRating; // 새로운 별을 클릭하면 선택된 별로 업데이트
            }

            updateStars(selectedRating);

            //클릭할때마다 hidden input에도 값 업데이트 시키기
            const raingInput = document.getElementById('raringInput');
            if (raingInput) {
                raingInput.value = clickedRating;
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

    // 별점 1점 미만 제출 차단 (별점1점이상 필수선택)
    const reviewForm = document.getElementById('reviewForm');

    reviewForm.addEventListener('submit', (e) => {
        const ratingInput = document.getElementById('ratingInput');
        if (clickedRating < 1) {
            e.preventDefault(); // 폼 제출 방지
            alert('별점을 1점 이상 선택해주세요.');
        } else {
            ratingInput.value = clickedRating; // 선택된 별점 값을 hidden input에 저장
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