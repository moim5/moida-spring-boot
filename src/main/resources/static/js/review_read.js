const img = document.querySelector('.review-img');

img.addEventListener('click', () => {
  img.classList.toggle('zoomed');
});

//포토 컨테이너 버튼 show
const photoContainer = document.getElementById("review-photo");
const moreBtn = document.getElementById("review-photo-btn");

moreBtn.addEventListener("click", () => {
  photoContainer.scrollBy({ left: 150, behavior: 'smooth' });
});

photoContainer.addEventListener("scroll", () => {
  const maxScroll = photoContainer.scrollWidth - photoContainer.clientWidth;
  const currentScroll = photoContainer.scrollLeft;

  if (currentScroll >= maxScroll - 5) {
    moreBtn.style.display = "none";
  } else {
    moreBtn.style.display = "block";
  }
});

//사진 클릭 시 해당 사진 확대
const moveReview = document.querySelector('.review-photo');
moveReview.addEventListener('click', (e) => {
  moveReview.classList.toggle('zoomed');
});



function deleteReviewl() {
	if (!confirm("정말 리뷰를 삭제하시겠습니까?")){
		event.preventDefault();
	} else{
		alert("리뷰가 삭제되었습니다.");
	}
}
