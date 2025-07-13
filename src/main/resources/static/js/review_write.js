
window.onload = () => {
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

//***남은 일 : 등록하기 버튼 클릭 시 후기, 별점 필수 확인 => 등록하시겠습니까 컨펌 ->true->후기상세페이지 이동
//***고민 : 별점이+후기작성form이랑  등록하기 버튼이 있는영역이 다른데 ..한꺼번에 묶어서 전송하는 방법?? */


    //후기작성 : 10자이상 작성 필수 및 별점 선택 필수
    function submitReview() {
        const reviewText = document.getElementById('reviewText').value.trim();
        if (reviewText.length < 10) {
            alert('후기를 10자 이상 작성해주세요.');
            return false; 
        }

        if(!rating){
            alert('별점을 선택해주세요.');
            return false;
        }

        if(confirm("후기를 등록하시겠습니까?")){
            document.getElementById('reviewForm').submit();
        }
    }

    // 별점채우기 : 별 클릭 시 아이콘 자체변경
    const stars = document.querySelectorAll('.star');
    let selectedRating = 0;
    let clickedRating = 0; // 클릭된 별의 값 저장

    stars.forEach(star => {
        star.addEventListener('click', () => {
            selectedRating = parseInt(star.dataset.value);
            
            if(clickedRating === selectedRating) {
                selectedRating = 0; // 이미 선택된 별을 다시 클릭하면 선택 해제
                clickedRating = 0;
            } else {
                clickedRating = selectedRating; // 새로운 별을 클릭하면 선택된 별로 업데이트
            }
            
            updateStars(selectedRating);

            //클릭할때마다 hidden input에도 값 업데이트 시키기
            const raingInput = document.getElementById('raringInput');
            if(raingInput){
              raingInput.value = clickedRating;
            }
        });

        star.addEventListener('mouseover',() => {
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




};