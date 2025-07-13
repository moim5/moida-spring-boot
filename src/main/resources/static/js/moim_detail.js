
    //모임상세설명 부분 클릭 시 오버플로우 -> display 구현
    const moreInfo = document.getElementById('moreInfo');
    const content = document.getElementById('content');
    const fullText = content.innerHTML.trim();
    const maxLength = 1000;

    if (fullText.length > maxLength) {
    const shortText = fullText.slice(0, maxLength) + '...';
    content.innerHTML = shortText;

    moreInfo.style.display = "block"; 
    } else {
    moreInfo.style.display = "none"; 
    }

    let isExpanded = false;

    moreInfo.addEventListener('click', () => {
    if (!isExpanded) {
        content.innerHTML = fullText;
        moreInfo.textContent = "접기";
        isExpanded = true;
    } else {
        content.innerHTML = fullText.slice(0, maxLength)+'...';
        moreInfo.textContent = "더보기";
        isExpanded = false;
    }
});

// 리뷰 클릭 : 모달창 열기+닫기 (모달 제외 배경 클릭 시 close)
function openModal(button) {
  // 리뷰 내용 가져오기
  const span = button.closest('.review').querySelector('span');
  const content = span.getAttribute('data-fulltext');


  // 모달 텍스트 넣기(모달의 p태그 id가 reviewText)
  document.getElementById('reviewText').textContent = content;

  // 모달 표시
  document.querySelector('.modal').style.display = 'block';
  document.querySelector('.modal-backdrop').style.display = 'block';
}

function closeModal() {
  document.querySelector('.modal').style.display = 'none';
  document.querySelector('.modal-backdrop').style.display = 'none';
}

function backDropClick(event){
    const modalContent = document.querySelector('.modal-content');
    if(!modalContent.contains(e.target)){
        closeModal();
    }
}

    

//리뷰칸에 들어가는 글자 수 자르기
const truncateReviewText = () => {
  const spans = document.querySelectorAll(".review span");

  spans.forEach(span => {
    const fullText = span.textContent.replace("더보기", "").trim();
    const maxLength = 28;

    span.setAttribute("data-fulltext", fullText);
    if (fullText.length > maxLength) {
      span.childNodes[0].textContent = fullText.slice(0, maxLength - 3) + "... ";
    }
  });
};


window.onload = () => {
    truncateReviewText();
}

